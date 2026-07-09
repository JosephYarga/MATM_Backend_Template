package bf.gov.mtdpce.controller;
import java.util.UUID;


import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.MissionRequest;
import bf.gov.mtdpce.dto.response.MissionResponse;
import bf.gov.mtdpce.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/missions")
@Tag(name = "Missions", description = "API de gestion des missions")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping
    @Operation(summary = "Liste des missions")
    public ResponseEntity<ApiResponse<Page<MissionResponse>>> getAllMissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categorie") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(missionService.getAll(pageable))
        );
    }

    @GetMapping("/ministere/{ministereId}")
    @Operation(summary = "Liste des missions par ministère")
    public ResponseEntity<ApiResponse<Page<MissionResponse>>> getMissionsByMinistere(
            @PathVariable UUID ministereId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.success(missionService.getByMinistere(ministereId, pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une mission")
    public ResponseEntity<ApiResponse<MissionResponse>> getMissionById(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.success(missionService.getById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer une mission")
    public ResponseEntity<MissionResponse> createMission(@RequestBody MissionRequest missionDTO) {

        return ResponseEntity.ok(
                missionService.create(missionDTO)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier une mission")
    public ResponseEntity<MissionResponse> updateMission(
            @PathVariable UUID id,
            @RequestBody MissionRequest missionDTO) {

        return ResponseEntity.ok(
                missionService.update(id, missionDTO)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer une mission")
    public ResponseEntity<ApiResponse<Void>> deleteMission(@PathVariable UUID id) {

        missionService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success("Mission supprimée", null)
        );
    }
}