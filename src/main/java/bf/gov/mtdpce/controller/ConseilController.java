package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.ConseilRequest;
import bf.gov.mtdpce.dto.response.ConseilResponse;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.service.ConseilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/conseils")
@Tag(name = "Conseils", description = "Gestion des conseils défilants de la page d'accueil")
public class ConseilController {

    @Autowired
    private ConseilService conseilService;

    @org.springframework.beans.factory.annotation.Value("${app.upload.base-path:/opt/mtdpce/uploads}")
    private String UPLOAD_BASE_PATH;

    @GetMapping("/public")
    @Operation(summary = "Conseils actifs (défilement page d'accueil)")
    public ResponseEntity<List<ConseilResponse>> getActiveConseils() {
        return ResponseEntity.ok(conseilService.getActiveConseils());
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'un conseil")
    public ResponseEntity<ConseilResponse> getConseilById(@PathVariable UUID id) {
        return ResponseEntity.ok(conseilService.getConseilById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Liste de tous les conseils (admin)")
    public ResponseEntity<List<ConseilResponse>> getAllConseils() {
        return ResponseEntity.ok(conseilService.getAllConseils());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Créer un conseil", description = "Crée un conseil avec image optionnelle")
    public ResponseEntity<ConseilResponse> createConseil(
            @RequestPart("conseil") @Valid ConseilRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                request.setImage(saveImage(image));
            }
            return ResponseEntity.ok(conseilService.createConseil(request));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Modifier un conseil")
    public ResponseEntity<ConseilResponse> updateConseil(
            @PathVariable UUID id,
            @RequestPart("conseil") @Valid ConseilRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                request.setImage(saveImage(image));
            }
            return ResponseEntity.ok(conseilService.updateConseil(id, request));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }

    @PutMapping("/reorder")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Réordonner les conseils", description = "Reçoit la liste des ids dans le nouvel ordre")
    public ResponseEntity<ApiResponse<Void>> reorderConseils(@RequestBody List<UUID> orderedIds) {
        conseilService.reorder(orderedIds);
        return ResponseEntity.ok(ApiResponse.success("Ordre des conseils mis à jour", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un conseil")
    public ResponseEntity<ApiResponse<Void>> deleteConseil(@PathVariable UUID id) {
        conseilService.deleteConseil(id);
        return ResponseEntity.ok(ApiResponse.success("Conseil supprimé avec succès", null));
    }

    private String saveImage(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Type de fichier non supporté : " + contentType);
        }

        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.format("%02d", LocalDate.now().getMonthValue());

        Path uploadDir = Paths.get(UPLOAD_BASE_PATH, "images", year, month);
        Files.createDirectories(uploadDir);

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images/" + year + "/" + month + "/" + fileName;
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot).toLowerCase();
    }
}
