package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.*;
import bf.gov.mtdpce.entity.ArticleCategory;
import bf.gov.mtdpce.entity.ContactStatus;
import bf.gov.mtdpce.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@Tag(name = "Public", description = "API publique - Accès sans authentification")
public class PublicController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private DashboardService dashboardService;

    // ==================== ARTICLES ====================
    
    @GetMapping("/articles")
    @Operation(summary = "Articles publiés", description = "Récupère la liste des articles publiés")
    public ResponseEntity<ApiResponse<Page<ArticleDTO>>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return ResponseEntity.ok(ApiResponse.success(articleService.getPublishedArticles(pageable)));
    }

    @GetMapping("/articles/latest")
    @Operation(summary = "Derniers articles", description = "Récupère les 5 derniers articles publiés")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getLatestArticles() {
        return ResponseEntity.ok(ApiResponse.success(articleService.getLatestArticles()));
    }

    @GetMapping("/articles/featured")
    @Operation(summary = "Articles à la une", description = "Récupère les articles mis en avant")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> getFeaturedArticles() {
        return ResponseEntity.ok(ApiResponse.success(articleService.getFeaturedArticles()));
    }

    @GetMapping("/articles/category/{category}")
    @Operation(summary = "Articles par catégorie", description = "Récupère les articles publiés d'une catégorie")
    public ResponseEntity<ApiResponse<Page<ArticleDTO>>> getArticlesByCategory(
            @PathVariable ArticleCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return ResponseEntity.ok(ApiResponse.success(articleService.getArticlesByCategory(category, pageable)));
    }

    @GetMapping("/articles/search")
    @Operation(summary = "Recherche d'articles", description = "Recherche dans les articles publiés")
    public ResponseEntity<ApiResponse<Page<ArticleDTO>>> searchPublishedArticles(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(articleService.searchPublishedArticles(query, pageable)));
    }

    @GetMapping("/articles/{id}")
    @Operation(summary = "Détail article publié", description = "Récupère un article publié par son ID")
    public ResponseEntity<ApiResponse<ArticleDTO>> getPublishedArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getPublishedArticleById(id)));
    }

    // ==================== PROJECTS ====================
    
    @GetMapping("/projects")
    @Operation(summary = "Projets publics", description = "Récupère la liste des projets publics")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> getPublicProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(projectService.getPublicProjects(pageable)));
    }

    @GetMapping("/projects/latest")
    @Operation(summary = "Derniers projets", description = "Récupère les derniers projets")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getLatestProjects() {
        return ResponseEntity.ok(ApiResponse.success(projectService.getLatestProjects()));
    }

    @GetMapping("/projects/search")
    @Operation(summary = "Recherche de projets", description = "Recherche dans les projets")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> searchProjects(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(projectService.searchProjects(query, pageable)));
    }

    @GetMapping("/projects/{id}")
    @Operation(summary = "Détail projet", description = "Récupère un projet par son ID")
    public ResponseEntity<ApiResponse<ProjectDTO>> getPublicProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectById(id)));
    }

    // ==================== DOCUMENTS ====================
    
    @GetMapping("/documents")
    @Operation(summary = "Documents publics", description = "Récupère la liste des documents publics")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getPublicDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(documentService.getPublicDocuments(pageable)));
    }

    @GetMapping("/documents/search")
    @Operation(summary = "Recherche de documents", description = "Recherche dans les documents publics")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> searchDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(documentService.searchPublicDocuments(query, pageable)));
    }

    // ==================== CONTACT ====================
    
    @PostMapping("/contacts")
    @Operation(summary = "Soumettre un message", description = "Envoie un message de contact")
    public ResponseEntity<ApiResponse<ContactDTO>> submitContact(@Valid @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(ApiResponse.success("Message envoyé avec succès", contactService.submitContact(contactDTO)));
    }
}
