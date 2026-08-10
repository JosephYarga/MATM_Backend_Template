package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.ServicesDTO;
import bf.gov.mtdpce.service.ServicesService;
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
@RequestMapping("/api/v1/services")
@Tag(name = "Services", description = "API de gestion des services")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";

    @GetMapping
    @Operation(summary = "Liste des services", description = "Récupère la liste des services")
    public ResponseEntity<ApiResponse<Page<ServicesDTO>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(servicesService.getAll(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un service", description = "Récupère un service par son ID")
    public ResponseEntity<ApiResponse<ServicesDTO>> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(servicesService.getById(id))
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un service", description = "Crée un service avec logo optionnel")
    public ResponseEntity<ServicesDTO> createService(
            @RequestPart("service") ServicesDTO servicesDTO,
            @RequestParam Long authorId,
            @RequestPart(value = "logo", required = false) MultipartFile logo
    ) {
        try {

            if (logo != null && !logo.isEmpty()) {
                String filePath = saveFile(logo);
                servicesDTO.setLogo(filePath);
            }

            ServicesDTO savedService = servicesService.create(servicesDTO,authorId);

            return ResponseEntity.ok(savedService);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du logo", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un service", description = "Modifie un service existant avec ou sans nouveau logo")
    public ResponseEntity<ServicesDTO> updateService(
            @PathVariable Long id,
            @RequestPart("service") ServicesDTO servicesDTO,
            @RequestPart(value = "logo", required = false) MultipartFile logo
    ) {
        try {

            if (logo != null && !logo.isEmpty()) {
                String filePath = saveFile(logo);
                servicesDTO.setLogo(filePath);
            }

            ServicesDTO updatedService = servicesService.update(id, servicesDTO);

            return ResponseEntity.ok(updatedService);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du logo", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un service", description = "Supprime un service")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {

        servicesService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Service supprimé", null)
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
