package bf.gov.mtdpce.controller;
import bf.gov.mtdpce.exception.BadRequestException;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.MinistereRequest;
import bf.gov.mtdpce.dto.response.MinistereResponse;
import bf.gov.mtdpce.service.MinistereService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import bf.gov.mtdpce.security.UserDetailsImpl;
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
@RequestMapping("/api/v1/ministeres")
@Tag(name = "Ministères", description = "API de gestion des ministères")
public class MinistereController {

    @Autowired
    private MinistereService ministereService;

    @org.springframework.beans.factory.annotation.Value("${app.upload.base-path:/opt/mtdpce/uploads}")
    private String UPLOAD_BASE_PATH;

    @GetMapping
    @Operation(summary = "Liste des ministères")
    public ResponseEntity<ApiResponse<Page<MinistereResponse>>> getAllMinisteres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nomReel") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(ministereService.getAll(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un ministère")
    public ResponseEntity<ApiResponse<MinistereResponse>> getMinistereById(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(ministereService.getById(id))
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un ministère", description = "Crée un ministère avec logo optionnel")
    public ResponseEntity<MinistereResponse> createMinistere(
            @RequestPart("ministere") MinistereRequest ministereDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {

            if (logo != null && !logo.isEmpty()) {
                String filePath = saveFile(logo);
                ministereDTO.setLogo(filePath);
            }

            if (image!= null && !image.isEmpty()) {
                String filePath = saveFile(image);
                ministereDTO.setImage(filePath);
            }

            MinistereResponse saved = ministereService.create(ministereDTO, userDetails.getId());

            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du logo", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un ministère")
    public ResponseEntity<MinistereResponse> updateMinistere(
            @PathVariable UUID id,
            @RequestPart("ministere") MinistereRequest ministereDTO,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {

            if (logo != null && !logo.isEmpty()) {
                String filePath = saveFile(logo);
                ministereDTO.setLogo(filePath);
            }

            if (image!= null && !image.isEmpty()) {
                String filePath = saveFile(image);
                ministereDTO.setImage(filePath);
            }

            MinistereResponse updated = ministereService.update(id, ministereDTO);

            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du logo", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un ministère")
    public ResponseEntity<ApiResponse<Void>> deleteMinistere(@PathVariable UUID id) {

        ministereService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Ministère supprimé", null)
        );
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

