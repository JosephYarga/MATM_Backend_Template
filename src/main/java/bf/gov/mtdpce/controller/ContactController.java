package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.ContactRequest;
import bf.gov.mtdpce.dto.response.ContactResponse;
import bf.gov.mtdpce.entity.ContactStatus;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.security.UserDetailsImpl;
import bf.gov.mtdpce.service.ContactService;
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
@RequestMapping("/api/v1/contacts")
@Tag(name = "Contacts", description = "API de gestion des messages de contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Public endpoint
    @PostMapping("/submit")
    @Operation(summary = "Soumettre un message", description = "Soumet un nouveau message de contact")
    public ResponseEntity<ApiResponse<ContactResponse>> submitContact(@Valid @RequestBody ContactRequest contactDTO) {
        return ResponseEntity.ok(ApiResponse.success("Message envoyé avec succès", contactService.submitContact(contactDTO)));
    }

    // Protected endpoints
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Tous les messages", description = "Récupère tous les messages de contact")
    public ResponseEntity<ApiResponse<Page<ContactResponse>>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(contactService.getAllContacts(pageable)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Messages par statut", description = "Récupère les messages par statut")
    public ResponseEntity<ApiResponse<Page<ContactResponse>>> getContactsByStatus(
            @PathVariable ContactStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(contactService.getContactsByStatus(status, pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Recherche de messages", description = "Recherche dans les messages")
    public ResponseEntity<ApiResponse<Page<ContactResponse>>> searchContacts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(contactService.searchContacts(query, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Détail message", description = "Récupère un message par son ID")
    public ResponseEntity<ApiResponse<ContactResponse>> getContactById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(contactService.getContactById(id)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier le statut", description = "Change le statut d'un message")
    public ResponseEntity<ApiResponse<ContactResponse>> updateContactStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body) {
        String rawStatus = body.get("status");
        if (rawStatus == null || rawStatus.isBlank()) {
            throw new BadRequestException("Le champ 'status' est obligatoire.");
        }
        ContactStatus status;
        try {
            status = ContactStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Statut invalide : '" + rawStatus
                    + "'. Valeurs autorisées : " + java.util.Arrays.toString(ContactStatus.values()));
        }
        return ResponseEntity.ok(ApiResponse.success("Statut mis à jour", contactService.updateContactStatus(id, status)));
    }

    @PostMapping("/{id}/respond")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Répondre au message", description = "Envoie une réponse à un message")
    public ResponseEntity<ApiResponse<ContactResponse>> respondToContact(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Réponse envoyée", 
                contactService.respondToContact(id, body.get("response"), userDetails.getId())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un message", description = "Supprime un message de contact")
    public ResponseEntity<ApiResponse<Void>> deleteContact(@PathVariable UUID id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok(ApiResponse.success("Message supprimé", null));
    }
}
