package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.StructureRattacheDTO;
import bf.gov.mtdpce.service.StructureRattacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1/structures-rattachees")
@Tag(name = "Structures rattachées", description = "API de gestion des structures rattachées")
public class StructureRattacheController {

    @Autowired
    private StructureRattacheService service;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    @GetMapping
    @Operation(summary = "Liste des structures rattachees")
    public ResponseEntity<ApiResponse<Page<StructureRattacheDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.success(service.getAll(pageable))
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Creer une structure rattache")
    public ResponseEntity<StructureRattacheDTO> create(
            @RequestPart("structure") StructureRattacheDTO dto,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        try {

            if (logo != null && !logo.isEmpty()) {
                dto.setLogourl(saveFile(logo));
            }

            return ResponseEntity.ok(service.create(dto));

        } catch (IOException e) {
            throw new RuntimeException("Erreur upload logo", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "mettre à jour une structure rattache")
    public ResponseEntity<StructureRattacheDTO> update(
            @PathVariable Long id,
            @RequestPart("structure") StructureRattacheDTO dto,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {

        try {

            if (logo != null && !logo.isEmpty()) {
                dto.setLogourl(saveFile(logo));
            }

            return ResponseEntity.ok(service.update(id, dto));

        } catch (IOException e) {
            throw new RuntimeException("Erreur upload logo", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Structure rattachée supprimée", null)
        );
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
