package bf.gov.mtdpce.controller;
import bf.gov.mtdpce.exception.BadRequestException;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.ArticleRequest;
import bf.gov.mtdpce.dto.response.ArticleResponse;
import bf.gov.mtdpce.security.UserDetailsImpl;
import bf.gov.mtdpce.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "Articles", description = "API de gestion des articles et actualités")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @org.springframework.beans.factory.annotation.Value("${app.upload.base-path:/opt/mtdpce/uploads}")
    private String UPLOAD_BASE_PATH;

    // Public endpoints
    @GetMapping("/published")
    @Operation(summary = "Articles publiés", description = "Récupère la liste des articles publiés")
    public ResponseEntity<ApiResponse<Page<ArticleResponse>>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return ResponseEntity.ok(ApiResponse.success(articleService.getPublishedArticles(pageable)));
    }

    @GetMapping("/published/latest")
    @Operation(summary = "Derniers articles", description = "Récupère les 5 derniers articles publiés")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getLatestArticles() {
        return ResponseEntity.ok(ApiResponse.success(articleService.getLatestArticles()));
    }

    @GetMapping("/published/featured")
    @Operation(summary = "Articles à la une", description = "Récupère les articles mis en avant")
    public ResponseEntity<ApiResponse<List<ArticleResponse>>> getFeaturedArticles() {
        return ResponseEntity.ok(ApiResponse.success(articleService.getFeaturedArticles()));
    }

    @GetMapping("/published/category/{category}")
    @Operation(summary = "Articles par catégorie", description = "Récupère les articles publiés d'une catégorie")
    public ResponseEntity<ApiResponse<Page<ArticleResponse>>> getArticlesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return ResponseEntity.ok(ApiResponse.success(articleService.getArticlesByCategory(category, pageable)));
    }

    @GetMapping("/published/search")
    @Operation(summary = "Recherche d'articles", description = "Recherche dans les articles publiés")
    public ResponseEntity<ApiResponse<Page<ArticleResponse>>> searchPublishedArticles(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(articleService.searchPublishedArticles(query, pageable)));
    }

    @GetMapping("/published/{id}")
    @Operation(summary = "Détail article publié", description = "Récupère un article publié par son ID")
    public ResponseEntity<ApiResponse<ArticleResponse>> getPublishedArticleById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getPublishedArticleById(id)));
    }

    // Protected endpoints
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Tous les articles", description = "Récupère tous les articles (admin)")
    public ResponseEntity<ApiResponse<Page<ArticleResponse>>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(articleService.getAllArticles(pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Détail article", description = "Récupère un article par son ID (admin)")
    public ResponseEntity<ApiResponse<ArticleResponse>> getArticleById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(articleService.getArticleById(id)));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Modifier un article",
            description = "Modifie un article existant avec ou sans nouveau fichier"
    )
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable UUID id,
            @RequestPart("article") @Valid ArticleRequest articleDTO,
            @RequestPart(value = "file", required = false) List<MultipartFile> file,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {

            List<String> imagePaths = uploadMultipleFiles(file);
            List<String> imagePathsFacebook = uploadMultipleFiles(files);
            ArticleResponse updatedArticle = articleService.updateArticle(id, articleDTO,imagePaths,imagePathsFacebook);

            return ResponseEntity.ok(updatedArticle);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }
    }

    @PostMapping("/{id}/facebook")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Publier un article sur Facebook",
            description = "Envoie l'article sur la page Facebook (contenu Facebook spécifique si renseigné), quel que soit son statut")
    public ResponseEntity<ApiResponse<ArticleResponse>> publishOnFacebook(@PathVariable UUID id) {
        ArticleResponse article = articleService.publishOnFacebook(id);
        return ResponseEntity.ok(ApiResponse.success("Publication Facebook déclenchée", article));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un article", description = "Supprime un article")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success("Article supprimé", null));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un article", description = "Crée un nouvel article avec image ou document")
    public ResponseEntity<ArticleResponse> createArticle(
            @RequestPart("article") @Valid ArticleRequest articleDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "file", required = false) List<MultipartFile> file,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            List<String> imagePaths = uploadMultipleFiles(file);
            List<String> imagePathsFacebook = uploadMultipleFiles(files);
            ArticleResponse savedArticle = articleService.createArticle(articleDTO, userDetails.getId(),imagePaths,imagePathsFacebook);
            return ResponseEntity.ok(savedArticle);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }
    }

    /*
     * ============================================
     * FILE UPLOAD LOGIC
     * ============================================
     */

    private List<String> uploadMultipleFiles(List<MultipartFile> files) throws IOException {

        if (files == null || files.isEmpty()) return null;

        List<String> paths = new ArrayList<>();

        for (MultipartFile file : files) {

            if (!file.isEmpty()) {
                paths.add(saveFile(file));
            }
        }

        return paths;
    }

    private String saveFile(MultipartFile file) throws IOException {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new BadRequestException("Type de fichier inconnu");
        }

        boolean isImage = contentType.startsWith("image/");
        boolean isDocument = List.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ).contains(contentType);

        if (!isImage && !isDocument) {
            throw new BadRequestException("Type de fichier non supporté : " + contentType);
        }

        String folder = isImage ? "images" : "documents";

        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.format("%02d", LocalDate.now().getMonthValue());

        Path uploadDir = Paths.get(UPLOAD_BASE_PATH, folder, year, month);
        Files.createDirectories(uploadDir);

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + folder + "/" + year + "/" + month + "/" + fileName;
    }

    private String getExtension(String filename) {

        if (filename == null) {
            return "";
        }

        int lastDot = filename.lastIndexOf('.');

        if (lastDot == -1) {
            return "";
        }

        return filename.substring(lastDot).toLowerCase();
    }
}
