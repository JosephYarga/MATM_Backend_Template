package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ProjetCategorieRequest;
import bf.gov.mtdpce.dto.response.ProjetCategorieResponse;
import bf.gov.mtdpce.service.ProjetCategorieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projetcategories")
@RequiredArgsConstructor
@Tag(name = "Projet Categorie", description = "Gestion des catégories de projet")
public class ProjetCategorieController {
    private final ProjetCategorieService projetCategorieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjetCategorieResponse> create(@Valid @RequestBody ProjetCategorieRequest dto) {
        return new ResponseEntity<>(projetCategorieService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjetCategorieResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProjetCategorieRequest dto) {
        return ResponseEntity.ok(projetCategorieService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetCategorieResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(projetCategorieService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjetCategorieResponse>> getAll() {
        return ResponseEntity.ok(projetCategorieService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projetCategorieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
