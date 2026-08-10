package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.DomaineDTO;
import bf.gov.mtdpce.service.DomaineService;
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
@RequestMapping("/api/v1/domaines")
@Tag(name = "Domaines", description = "API de gestion des domaines")
public class DomaineController {

    @Autowired
    private DomaineService domaineService;

    @GetMapping
    @Operation(summary = "Liste des domaines")
    public ResponseEntity<ApiResponse<Page<DomaineDTO>>> getAllDomaines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(domaineService.getAll(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un domaine")
    public ResponseEntity<ApiResponse<DomaineDTO>> getDomaineById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(domaineService.getById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un domaine")
    public ResponseEntity<DomaineDTO> createDomaine(@RequestBody DomaineDTO domaineDTO) {

        return ResponseEntity.ok(domaineService.create(domaineDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un domaine")
    public ResponseEntity<DomaineDTO> updateDomaine(
            @PathVariable Long id,
            @RequestBody DomaineDTO domaineDTO) {

        return ResponseEntity.ok(domaineService.update(id, domaineDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un domaine")
    public ResponseEntity<ApiResponse<Void>> deleteDomaine(@PathVariable Long id) {

        domaineService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Domaine supprimé", null)
        );
    }
}
