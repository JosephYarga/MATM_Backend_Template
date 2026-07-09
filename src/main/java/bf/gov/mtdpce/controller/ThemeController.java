package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.ThemeRequest;
import bf.gov.mtdpce.dto.response.ThemeResponse;
import bf.gov.mtdpce.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/themes")
@RequiredArgsConstructor
@Tag(name = "Themes", description = "Gestion et validation du thème (couleurs) du site")
public class ThemeController {

    private final ThemeService themeService;

    /** Thème actif appliqué sur tout le site — public (consommé au démarrage du front). */
    @GetMapping("/active")
    @Operation(summary = "Thème actif du site")
    public ResponseEntity<ApiResponse<ThemeResponse>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(themeService.getActiveTheme()));
    }

    @GetMapping
    @Operation(summary = "Liste des thèmes")
    public ResponseEntity<ApiResponse<List<ThemeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(themeService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un thème")
    public ResponseEntity<ApiResponse<ThemeResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(themeService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un thème")
    public ResponseEntity<ApiResponse<ThemeResponse>> create(@Valid @RequestBody ThemeRequest dto) {
        return new ResponseEntity<>(ApiResponse.success("Thème créé", themeService.create(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un thème")
    public ResponseEntity<ApiResponse<ThemeResponse>> update(@PathVariable String id, @Valid @RequestBody ThemeRequest dto) {
        return ResponseEntity.ok(ApiResponse.success("Thème mis à jour", themeService.update(id, dto)));
    }

    /** Valide (active) un thème : il devient le thème appliqué sur tout le site. */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Valider (activer) un thème")
    public ResponseEntity<ApiResponse<ThemeResponse>> activate(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Thème validé et appliqué sur le site", themeService.activate(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un thème")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        themeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Thème supprimé", null));
    }
}
