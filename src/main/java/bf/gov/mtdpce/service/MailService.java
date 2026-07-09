package bf.gov.mtdpce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Envoi d'emails. En l'absence de configuration SMTP (app.mail.enabled=false),
 * le contenu est journalisé au lieu d'être envoyé — pratique en développement.
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.from:no-reply@mtdpce.gov.bf}")
    private String from;

    public MailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    public void sendPasswordReset(String to, String resetLink) {
        String subject = "Réinitialisation de votre mot de passe - MTDPCE";
        String body = "Bonjour,\n\n"
                + "Vous avez demandé la réinitialisation de votre mot de passe.\n"
                + "Cliquez sur le lien ci-dessous pour définir un nouveau mot de passe "
                + "(valable 60 minutes) :\n\n"
                + resetLink + "\n\n"
                + "Si vous n'êtes pas à l'origine de cette demande, ignorez simplement cet email.\n\n"
                + "Cordialement,\nMTDPCE Burkina Faso";
        send(to, subject, body, resetLink);
    }

    private void send(String to, String subject, String body, String fallbackInfo) {
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (!mailEnabled || sender == null) {
            log.warn("[MAIL DÉSACTIVÉ] Email non envoyé à {} — sujet « {} ». Lien : {}",
                    to, subject, fallbackInfo);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            sender.send(message);
            log.info("Email envoyé à {}", to);
        } catch (Exception e) {
            // On ne fait pas échouer la requête si l'envoi échoue : on journalise le lien.
            log.error("Échec d'envoi d'email à {} : {}. Lien : {}", to, e.getMessage(), fallbackInfo);
        }
    }
}
