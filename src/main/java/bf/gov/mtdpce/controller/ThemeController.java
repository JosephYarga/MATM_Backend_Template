package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ThemeDTO;
import bf.gov.mtdpce.service.ThemeService;
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
@Tag(name = "Themes", description = "Gestion des themes")
public class ThemeController {
    private final ThemeService themeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ThemeDTO> create(@Valid @RequestBody ThemeDTO dto) {
        return new ResponseEntity<>(themeService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{title}")
    public ResponseEntity<ThemeDTO> getByTitle(@PathVariable String title) {
        return ResponseEntity.ok(themeService.getThemeByTitile(title));
    }

    @GetMapping
    public ResponseEntity<List<ThemeDTO>> getAll() {
        return ResponseEntity.ok(themeService.getAll());
    }
}
