package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.ArticleDTO;
import bf.gov.mtdpce.dto.DocumentDTO;
import bf.gov.mtdpce.entity.DocumentCategory;
import bf.gov.mtdpce.security.UserDetailsImpl;
import bf.gov.mtdpce.service.DocumentService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents", description = "API de gestion des documents et ressources")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    // Public endpoints
    @GetMapping("/public")
    @Operation(summary = "Documents publics", description = "Récupère la liste des documents publics")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getPublicDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(documentService.getPublicDocuments(pageable)));
    }

    @GetMapping("/public/latest")
    @Operation(summary = "Derniers documents", description = "Récupère les 10 derniers documents publics")
    public ResponseEntity<ApiResponse<List<DocumentDTO>>> getLatestPublicDocuments() {
        return ResponseEntity.ok(ApiResponse.success(documentService.getLatestPublicDocuments()));
    }

    @GetMapping("/public/category/{category}")
    @Operation(summary = "Documents par catégorie", description = "Récupère les documents d'une catégorie")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getDocumentsByCategory(
            @PathVariable DocumentCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(documentService.getDocumentsByCategory(category, pageable)));
    }

    @GetMapping("/public/search")
    @Operation(summary = "Recherche de documents", description = "Recherche dans les documents publics")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> searchPublicDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(documentService.searchPublicDocuments(query, pageable)));
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Détail document", description = "Récupère un document par son ID")
    public ResponseEntity<ApiResponse<DocumentDTO>> getPublicDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(documentService.getDocumentById(id)));
    }

    @PostMapping("/public/{id}/download")
    @Operation(summary = "Télécharger document", description = "Incrémente le compteur de téléchargement")
    public ResponseEntity<ApiResponse<Void>> downloadDocument(@PathVariable Long id) {
        documentService.incrementDownloadCount(id);
        return ResponseEntity.ok(ApiResponse.success("Téléchargement enregistré", null));
    }

    // Protected endpoints
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Tous les documents", description = "Récupère tous les documents (admin)")
    public ResponseEntity<ApiResponse<Page<DocumentDTO>>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(documentService.getAllDocuments(pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Détail document (admin)", description = "Récupère un document par son ID (admin)")
    public ResponseEntity<ApiResponse<DocumentDTO>> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(documentService.getDocumentById(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un document", description = "Crée un nouveau document")
    public ResponseEntity<ApiResponse<DocumentDTO>> createDocument(
            @RequestPart("document") DocumentDTO documentDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file);
                documentDTO.setFileName(file.getOriginalFilename());
                documentDTO.setFilePath(filePath);
                documentDTO.setFileType(file.getContentType());
                documentDTO.setFileSize(file.getSize());
            }
            return ResponseEntity.ok(ApiResponse.success("Document créé", documentService.createDocument(documentDTO, userDetails.getId())));

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }

    }


    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un document", description = "Met à jour un document existant")
    public ResponseEntity<ApiResponse<DocumentDTO>> updateDocument(
            @PathVariable Long id,
            @RequestPart("document") DocumentDTO documentDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file);
                documentDTO.setFileName(file.getOriginalFilename());
                documentDTO.setFilePath(filePath);
                documentDTO.setFileType(file.getContentType());
                documentDTO.setFileSize(file.getSize());
            }
            return ResponseEntity.ok(ApiResponse.success("Document mis à jour", documentService.updateDocument(id, documentDTO)));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un document", description = "Supprime un document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Document supprimé", null));
    }

    private String saveFile(MultipartFile file) throws IOException {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new RuntimeException("Type de fichier inconnu");
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
            throw new RuntimeException("Type de fichier non supporté : " + contentType);
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
