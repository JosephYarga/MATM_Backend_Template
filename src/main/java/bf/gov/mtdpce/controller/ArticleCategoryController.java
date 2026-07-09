package bf.gov.mtdpce.controller;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ArticleCategoryRequest;
import bf.gov.mtdpce.dto.response.ArticleCategoryResponse;
import bf.gov.mtdpce.service.ArticleCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article-categories")
@RequiredArgsConstructor
@Tag(name = "Article Categories", description = "Gestion des catégories d'article")
public class ArticleCategoryController {

    private final ArticleCategoryService articleCategoryService;

    @GetMapping
    public ResponseEntity<List<ArticleCategoryResponse>> getAll() {
        return ResponseEntity.ok(articleCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleCategoryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(articleCategoryService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ArticleCategoryResponse> create(@Valid @RequestBody ArticleCategoryRequest dto) {
        return new ResponseEntity<>(articleCategoryService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ArticleCategoryResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody ArticleCategoryRequest dto) {
        return ResponseEntity.ok(articleCategoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        articleCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
