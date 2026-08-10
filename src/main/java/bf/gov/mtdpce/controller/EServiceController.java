package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.ServiceRequest;
import bf.gov.mtdpce.dto.response.ServiceResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.service.EServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service")
@Tag(name = "E-Services", description = "Gestion des services en ligne")
public class EServiceController {
    
    @Autowired
    private EServiceService eServiceService;
    
    @GetMapping("/public")
    @Operation(summary = "Liste des services publics")
    public ResponseEntity<PaginatedResponse<ServiceResponse>> getPublicServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eServiceService.getAllServices(page, size));
    }
    
    @GetMapping("/public/online")
    @Operation(summary = "Services disponibles en ligne")
    public ResponseEntity<PaginatedResponse<ServiceResponse>> getOnlineServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eServiceService.getOnlineServices(page, size));
    }
    
    @GetMapping("/public/category/{category}")
    @Operation(summary = "Services par catégorie")
    public ResponseEntity<List<ServiceResponse>> getServicesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(eServiceService.getServicesByCategory(category));
    }
    
    @GetMapping("/public/categories")
    @Operation(summary = "Liste des catégories de services")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(eServiceService.getAllCategories());
    }
    
    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'un service")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(eServiceService.getServiceById(id));
    }
    
    @GetMapping("/public/search")
    @Operation(summary = "Rechercher des services")
    public ResponseEntity<PaginatedResponse<ServiceResponse>> searchServices(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eServiceService.searchServices(query, page, size));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Liste de tous les services (admin)")
    public ResponseEntity<PaginatedResponse<ServiceResponse>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eServiceService.getAllServices(page, size));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un service")
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(eServiceService.createService(request));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un service")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(eServiceService.updateService(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un service")
    public ResponseEntity<ApiResponse> deleteService(@PathVariable Long id) {
        eServiceService.deleteService(id);
        return ResponseEntity.ok(new ApiResponse(true, "Service supprimé avec succès"));
    }
}
