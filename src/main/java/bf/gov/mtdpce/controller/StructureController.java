package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.StructureDTO;
import bf.gov.mtdpce.service.StructureService;
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
@RequestMapping("/api/v1/structures")
@Tag(name = "Structures", description = "API de gestion des structures")
public class StructureController {

    @Autowired
    private StructureService structureService;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    @GetMapping
    @Operation(summary = "Liste des structures")
    public ResponseEntity<ApiResponse<Page<StructureDTO>>> getAllStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(structureService.getAll(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une structure")
    public ResponseEntity<ApiResponse<StructureDTO>> getStructureById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(structureService.getById(id))
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer une structure")
    public ResponseEntity<StructureDTO> createStructure(
            @RequestPart("structure") StructureDTO structureDTO,
            @RequestParam Long authorId,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {

        try {

            if (photo != null && !photo.isEmpty()) {
                String filePath = saveFile(photo);
                structureDTO.setPhoto(filePath);
            }

            return ResponseEntity.ok(structureService.create(structureDTO,authorId));

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de la photo", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<StructureDTO> updateStructure(
            @PathVariable Long id,
            @RequestPart("structure") StructureDTO structureDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {

        try {

            if (photo != null && !photo.isEmpty()) {
                String filePath = saveFile(photo);
                structureDTO.setPhoto(filePath);
            }

            return ResponseEntity.ok(structureService.update(id, structureDTO));

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de la photo", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStructure(@PathVariable Long id) {

        structureService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Structure supprimée", null)
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
