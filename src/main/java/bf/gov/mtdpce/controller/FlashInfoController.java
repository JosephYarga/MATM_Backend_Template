package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.FlashInfoRequest;
import bf.gov.mtdpce.dto.response.FlashInfoResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.service.FlashInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flash-infos")
@Tag(name = "Flash Infos", description = "Gestion des informations flash")
public class FlashInfoController {
    
    @Autowired
    private FlashInfoService flashInfoService;
    
    @GetMapping("/public")
    @Operation(summary = "Flash infos actifs")
    public ResponseEntity<List<FlashInfoResponse>> getActiveFlashInfos() {
        return ResponseEntity.ok(flashInfoService.getActiveFlashInfos());
    }
    
    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'un flash info")
    public ResponseEntity<FlashInfoResponse> getFlashInfoById(@PathVariable Long id) {
        return ResponseEntity.ok(flashInfoService.getFlashInfoById(id));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Liste de tous les flash infos (admin)")
    public ResponseEntity<PaginatedResponse<FlashInfoResponse>> getAllFlashInfos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(flashInfoService.getAllFlashInfos(page, size));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Créer un flash info")
    public ResponseEntity<FlashInfoResponse> createFlashInfo(@Valid @RequestBody FlashInfoRequest request) {
        return ResponseEntity.ok(flashInfoService.createFlashInfo(request));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Modifier un flash info")
    public ResponseEntity<FlashInfoResponse> updateFlashInfo(
            @PathVariable Long id,
            @Valid @RequestBody FlashInfoRequest request) {
        return ResponseEntity.ok(flashInfoService.updateFlashInfo(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un flash info")
    public ResponseEntity<ApiResponse> deleteFlashInfo(@PathVariable Long id) {
        flashInfoService.deleteFlashInfo(id);
        return ResponseEntity.ok(new ApiResponse(true, "Flash info supprimé avec succès"));
    }
}
