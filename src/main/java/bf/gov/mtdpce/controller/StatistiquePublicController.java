package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.StatistiquePublicRequest;
import bf.gov.mtdpce.dto.response.StatistiquePublicResponse;
import bf.gov.mtdpce.service.StatistiquePublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistiques")
@Tag(name = "Statistiques", description = "API de gestion des statistiques publiques")
public class StatistiquePublicController {

    @Autowired
    private StatistiquePublicService statistiqueService;

    @GetMapping
    @Operation(summary = "Liste des statistiques")
    public ResponseEntity<ApiResponse<Page<StatistiquePublicResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(statistiqueService.getAll(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une statistique")
    public ResponseEntity<ApiResponse<StatistiquePublicResponse>> getById(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(statistiqueService.getById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer une statistique")
    public ResponseEntity<StatistiquePublicResponse> create(
            @RequestBody StatistiquePublicRequest dto) {

        return ResponseEntity.ok(
                statistiqueService.create(dto)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier une statistique")
    public ResponseEntity<StatistiquePublicResponse> update(
            @PathVariable UUID id,
            @RequestBody StatistiquePublicRequest dto) {

        return ResponseEntity.ok(
                statistiqueService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer une statistique")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        statistiqueService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Statistique supprimée", null)
        );
    }
}
