package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.auth.JwtResponse;
import bf.gov.mtdpce.dto.auth.LoginRequest;
import bf.gov.mtdpce.dto.auth.RegisterRequest;
import bf.gov.mtdpce.entity.ERole;
import bf.gov.mtdpce.entity.Role;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.repository.RoleRepository;
import bf.gov.mtdpce.repository.UserRepository;
import bf.gov.mtdpce.security.JwtUtils;
import bf.gov.mtdpce.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Update last login
        userRepository.findById(userDetails.getId()).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .profileImage(userDetails.getProfileImage())
                .roles(roles)
                .build();
    }

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Ce nom d'utilisateur est déjà utilisé");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Cet email est déjà utilisé");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phone(registerRequest.getPhone())
                .position(registerRequest.getPosition())
                .department(registerRequest.getDepartment())
                .enabled(true)
                .accountNonLocked(true)
                .build();

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                        roles.add(adminRole);
                        break;
                    case "moderator":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                        roles.add(modRole);
                        break;
                    case "super_admin":
                        Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                        roles.add(superAdminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }
}
