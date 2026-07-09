package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.BannerRequest;
import bf.gov.mtdpce.dto.response.BannerResponse;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.service.BannerService;
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
@RequestMapping("/api/v1/banners")
@Tag(name = "Bannières", description = "Gestion des bannières défilantes de la page d'accueil")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @org.springframework.beans.factory.annotation.Value("${app.upload.base-path:/opt/mtdpce/uploads}")
    private String UPLOAD_BASE_PATH;

    @GetMapping("/public")
    @Operation(summary = "Bannières actives (défilement page d'accueil)")
    public ResponseEntity<List<BannerResponse>> getActiveBanners() {
        return ResponseEntity.ok(bannerService.getActiveBanners());
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'une bannière")
    public ResponseEntity<BannerResponse> getBannerById(@PathVariable UUID id) {
        return ResponseEntity.ok(bannerService.getBannerById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Liste de toutes les bannières (admin)")
    public ResponseEntity<List<BannerResponse>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Créer une bannière", description = "Crée une bannière avec image de fond optionnelle")
    public ResponseEntity<BannerResponse> createBanner(
            @RequestPart("banner") @Valid BannerRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                request.setImage(saveImage(image));
            }
            return ResponseEntity.ok(bannerService.createBanner(request));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Modifier une bannière")
    public ResponseEntity<BannerResponse> updateBanner(
            @PathVariable UUID id,
            @RequestPart("banner") @Valid BannerRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                request.setImage(saveImage(image));
            }
            return ResponseEntity.ok(bannerService.updateBanner(id, request));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }

    @PutMapping("/reorder")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Réordonner les bannières", description = "Reçoit la liste des ids dans le nouvel ordre")
    public ResponseEntity<ApiResponse<Void>> reorderBanners(@RequestBody List<UUID> orderedIds) {
        bannerService.reorder(orderedIds);
        return ResponseEntity.ok(ApiResponse.success("Ordre des bannières mis à jour", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une bannière")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable UUID id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success("Bannière supprimée avec succès", null));
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
