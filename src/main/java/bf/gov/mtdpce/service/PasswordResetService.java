package bf.gov.mtdpce.service;

import bf.gov.mtdpce.entity.PasswordResetToken;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.repository.PasswordResetTokenRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${app.password-reset.token-validity-minutes:60}")
    private long tokenValidityMinutes;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                MailService mailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    /**
     * Crée un jeton et envoie l'email de réinitialisation.
     * Ne révèle jamais si l'email existe (réponse identique dans tous les cas).
     */
    @Transactional
    public void requestReset(String email) {
        if (email == null || email.isBlank()) return;

        userRepository.findByEmail(email.trim()).ifPresentOrElse(user -> {
            if (Boolean.FALSE.equals(user.getEnabled())) {
                log.info("Demande de réinitialisation ignorée : compte désactivé ({})", email);
                return;
            }
            tokenRepository.invalidateAllForUser(user);

            PasswordResetToken token = PasswordResetToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusMinutes(tokenValidityMinutes))
                    .used(false)
                    .build();
            tokenRepository.save(token);

            String link = frontendUrl + "/auth/reset-password?token=" + token.getToken();
            mailService.sendPasswordReset(user.getEmail(), link);
        }, () -> log.info("Demande de réinitialisation pour un email inconnu : {}", email));
    }

    @Transactional(readOnly = true)
    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .map(PasswordResetToken::isValid)
                .orElse(false);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("Le mot de passe doit contenir au moins 6 caractères.");
        }
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Lien de réinitialisation invalide."));

        if (prt.isUsed()) {
            throw new BadRequestException("Ce lien a déjà été utilisé.");
        }
        if (prt.isExpired()) {
            throw new BadRequestException("Ce lien a expiré. Veuillez en demander un nouveau.");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        prt.setUsed(true);
        tokenRepository.save(prt);
    }
}
