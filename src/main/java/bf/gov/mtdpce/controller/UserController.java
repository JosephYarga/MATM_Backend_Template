package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.UserRequest;
import bf.gov.mtdpce.dto.response.UserResponse;
import bf.gov.mtdpce.dto.request.UserUpdateRequest;
import bf.gov.mtdpce.security.UserDetailsImpl;
import bf.gov.mtdpce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Liste des utilisateurs", description = "Récupère la liste paginée des utilisateurs")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Recherche d'utilisateurs", description = "Recherche des utilisateurs par nom, email, etc.")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(query, pageable)));
    }

    @GetMapping("/me")
    @Operation(summary = "Profil utilisateur", description = "Récupère le profil de l'utilisateur connecté")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(userDetails.getId())));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Détail utilisateur", description = "Récupère les détails d'un utilisateur par son ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Mise à jour utilisateur", description = "Met à jour les informations d'un utilisateur")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest userDTO) {
        return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour", userService.updateUser(id, userDTO)));
    }
    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id")
    @Operation(summary = "Changement de mot de passe", description = "Change le mot de passe de l'utilisateur")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable UUID id,
            @RequestBody Map<String, String> passwords) {
        userService.changePassword(id, passwords.get("oldPassword"), passwords.get("newPassword"));
        return ResponseEntity.ok(ApiResponse.success("Mot de passe changé avec succès", null));
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Réinitialiser le mot de passe (admin)",
            description = "Définit un nouveau mot de passe pour un utilisateur, sans l'ancien")
    public ResponseEntity<ApiResponse<Void>> adminResetPassword(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {
        userService.adminResetPassword(id, body.get("newPassword"));
        return ResponseEntity.ok(ApiResponse.success("Mot de passe réinitialisé avec succès", null));
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Activer/Désactiver utilisateur", description = "Active ou désactive un compte utilisateur")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable UUID id) {
        userService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Statut utilisateur modifié", null));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Désactiver son compte", description = "Désactive le compte (self-service ou admin)")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("Compte désactivé", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Supprimer utilisateur",
            description = "Supprime un utilisateur (Super Admin) ou son propre compte")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur supprimé", null));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un utilisateur", description = "Ajoute un nouvel utilisateur")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest createUserDTO) {

        UserResponse user = userService.createUser(createUserDTO);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur créé avec succès", user));
    }

}
