package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.TypeDTO;
import bf.gov.mtdpce.service.TypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
@Tag(name = "Types", description = "Gestion des types de documents")
public class TypeController {

    private final TypeService typeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TypeDTO> create(@Valid @RequestBody TypeDTO dto) {
        return new ResponseEntity<>(typeService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TypeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeDTO dto) {
        return ResponseEntity.ok(typeService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(typeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TypeDTO>> getAll() {
        return ResponseEntity.ok(typeService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
