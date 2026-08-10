package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ProjetCategorieDTO;
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
    public ResponseEntity<ProjetCategorieDTO> create(@Valid @RequestBody ProjetCategorieDTO dto) {
        return new ResponseEntity<>(projetCategorieService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjetCategorieDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProjetCategorieDTO dto) {
        return ResponseEntity.ok(projetCategorieService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetCategorieDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projetCategorieService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjetCategorieDTO>> getAll() {
        return ResponseEntity.ok(projetCategorieService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetCategorieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
