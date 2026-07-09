package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.UserRequest;
import bf.gov.mtdpce.dto.response.UserResponse;
import bf.gov.mtdpce.dto.request.UserUpdateRequest;
import bf.gov.mtdpce.entity.ERole;
import bf.gov.mtdpce.entity.Role;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.RoleRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<UserResponse> searchUsers(String search, Pageable pageable) {
        return userRepository.searchUsers(search, pageable).map(this::convertToResponse);
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        return convertToResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "username", username));
        return convertToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new BadRequestException("Cet email est déjà utilisé");
            }
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : userDTO.getRoles()) {
                ERole eRole;
                try {
                    eRole = ERole.valueOf(roleName);
                } catch (IllegalArgumentException ex) {
                    throw new BadRequestException("Rôle invalide : " + roleName);
                }
                Role role = roleRepository.findByName(eRole)
                        .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable : " + roleName));
                roles.add(role);
            }
            if (roles.isEmpty()) {
                throw new BadRequestException("Un utilisateur doit avoir au moins un rôle");
            }
            user.setRoles(roles);
        }

        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getPhone() != null) user.setPhone(userDTO.getPhone());
        if (userDTO.getPosition() != null) user.setPosition(userDTO.getPosition());
        if (userDTO.getDepartment() != null) user.setDepartment(userDTO.getDepartment());
        if (userDTO.getProfileImage() != null) user.setProfileImage(userDTO.getProfileImage());

        return convertToResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("L'ancien mot de passe est incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /** Réinitialisation par un administrateur (sans ancien mot de passe). */
    @Transactional
    public void adminResetPassword(UUID id, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("Le mot de passe doit contenir au moins 6 caractères");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /** Désactivation du compte (par l'utilisateur lui-même ou un admin). */
    @Transactional
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void toggleUserStatus(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        if(user.getEnabled())
        {
            user.setEnabled(false);
        }else
        {
            user.setEnabled(true);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur", "id", id);
        }
        userRepository.deleteById(id);
    }

    public Long countActiveUsers() {
        return userRepository.countActiveUsers();
    }

    @Transactional
    public UserResponse createUser(UserRequest dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Ce nom d'utilisateur est déjà utilisé");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Cet email est déjà utilisé");
        }

        Set<Role> roles = new HashSet<>();

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Rôle ROLE_USER introuvable"));
            roles.add(userRole);
        } else {
            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByName(ERole.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable : " + roleName));
                roles.add(role);
            }
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .enabled(true)
                .accountNonLocked(true)
                .roles(roles)
                .build();

        return convertToResponse(userRepository.save(user));
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .position(user.getPosition())
                .department(user.getDepartment())
                .profileImage(user.getProfileImage())
                .enabled(user.getEnabled())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
