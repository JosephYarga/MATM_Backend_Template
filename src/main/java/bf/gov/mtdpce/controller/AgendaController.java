package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.AgendaDTO;
import bf.gov.mtdpce.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/agendas")
@Tag(name = "Agendas", description = "API de gestion des agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    /*
     * ============================================
     * PUBLIC ENDPOINTS
     * ============================================
     */

    @GetMapping("/published")
    @Operation(summary = "Agenda publiés", description = "Récupère la liste des agendas publiés")
    public ResponseEntity<ApiResponse<Page<AgendaDTO>>> getPublishedAgenda(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return ResponseEntity.ok(ApiResponse.success(
                agendaService.getPublishedArticles(pageable)
        ));
    }

    @GetMapping("/published/latest")
    @Operation(summary = "Derniers agendas", description = "Récupère les 5 derniers agendas publiés")
    public ResponseEntity<ApiResponse<List<AgendaDTO>>> getLatestAgenda() {
        return ResponseEntity.ok(ApiResponse.success(
                agendaService.getLatestAgenda()
        ));
    }

    @GetMapping("/published/search")
    @Operation(summary = "Recherche d'agenda", description = "Recherche dans les agendas publiés")
    public ResponseEntity<ApiResponse<Page<AgendaDTO>>> searchPublishedAgenda(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(
                agendaService.searchPublishedAgenda(query, pageable)
        ));
    }

    @GetMapping("/published/{id}")
    @Operation(summary = "Détail agenda publié", description = "Récupère un agenda publié par son ID")
    public ResponseEntity<ApiResponse<AgendaDTO>> getPublishedAgendaById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                agendaService.getPublishedAgendaById(id)
        ));
    }

    /*
     * ============================================
     * ADMIN ENDPOINTS
     * ============================================
     */

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Tous les agendas", description = "Récupère tous les agendas (admin)")
    public ResponseEntity<ApiResponse<Page<AgendaDTO>>> getAllAgenda(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(ApiResponse.success(
                agendaService.getAllAgenda(pageable)
        ));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Détail agenda", description = "Récupère un agenda par son ID (admin)")
    public ResponseEntity<ApiResponse<AgendaDTO>> getAgendaById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                agendaService.getAgendaById(id)
        ));
    }

    /*
     * ============================================
     * CREATE
     * ============================================
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un agenda", description = "Crée un nouvel agenda avec image ou document")
    public ResponseEntity<AgendaDTO> createAgenda(
            @RequestPart("agenda") AgendaDTO agendaDTO,
            @RequestParam Long authorId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {

            List<String> imagePaths = uploadMultipleFiles(files);

            AgendaDTO saved = agendaService.createAgenda(
                    agendaDTO,
                    authorId,
                    imagePaths
            );

            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            throw new RuntimeException("Erreur upload images", e);
        }
    }

    /*
     * ============================================
     * UPDATE
     * ============================================
     */

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Modifier un agenda",
            description = "Modifie un agenda existant avec ou sans nouveau fichier"
    )
    public ResponseEntity<AgendaDTO> updateAgenda(
            @PathVariable Long id,
            @RequestPart("agenda") AgendaDTO agendaDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {

            List<String> imagePaths = uploadMultipleFiles(files);

            AgendaDTO updated = agendaService.updateAgenda(
                    id,
                    agendaDTO,
                    imagePaths
            );

            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            throw new RuntimeException("Erreur upload images", e);
        }
    }

    /*
     * ============================================
     * DELETE
     * ============================================
     */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un agenda", description = "Supprime un agenda")
    public ResponseEntity<ApiResponse<Void>> deleteAgenda(@PathVariable Long id) {

        agendaService.deleteArticle(id);

        return ResponseEntity.ok(
                ApiResponse.success("Agenda supprimé", null)
        );
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

