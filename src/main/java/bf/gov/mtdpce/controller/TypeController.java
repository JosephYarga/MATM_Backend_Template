package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.TypeRequest;
import bf.gov.mtdpce.dto.response.TypeResponse;
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
    public ResponseEntity<TypeResponse> create(@Valid @RequestBody TypeRequest dto) {
        return new ResponseEntity<>(typeService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TypeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TypeRequest dto) {
        return ResponseEntity.ok(typeService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(typeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TypeResponse>> getAll() {
        return ResponseEntity.ok(typeService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        typeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
