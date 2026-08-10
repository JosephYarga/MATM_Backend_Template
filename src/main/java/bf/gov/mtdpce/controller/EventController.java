package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.ArticleDTO;
import bf.gov.mtdpce.dto.request.EventRequest;
import bf.gov.mtdpce.dto.response.EventResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.service.EventService;
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
@RequestMapping("/api/v1/events")
@Tag(name = "Événements", description = "Gestion des événements")
public class EventController {
    
    @Autowired
    private EventService eventService;

    private static final String UPLOAD_BASE_PATH = "/opt/mtdpce/uploads";
    
    @GetMapping("/public")
    @Operation(summary = "Liste des événements publics")
    public ResponseEntity<PaginatedResponse<EventResponse>> getPublicEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getAllEvents(page, size));
    }
    
    @GetMapping("/public/upcoming")
    @Operation(summary = "Événements à venir")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }
    
    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'un événement")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
    
    @GetMapping("/public/search")
    @Operation(summary = "Rechercher des événements")
    public ResponseEntity<PaginatedResponse<EventResponse>> searchEvents(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.searchEvents(query, page, size));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")//
    @Operation(summary = "Liste de tous les événements (admin)")
    public ResponseEntity<PaginatedResponse<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getAllEvents(page, size));
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un événement")
    public ResponseEntity<EventResponse> createEvent(
            @RequestPart("evenement") EventRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file);
                request.setImageUrl(filePath); // on garde le même champ
            }

            return ResponseEntity.ok(eventService.createEvent(request));

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }

    }
    
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un événement")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @RequestPart("evenement") EventRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file);
                request.setImageUrl(filePath); // on garde le même champ
            }

            return ResponseEntity.ok(eventService.updateEvent(id, request));

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier", e);
        }

    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un événement")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ApiResponse(true, "Événement supprimé avec succès"));
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
