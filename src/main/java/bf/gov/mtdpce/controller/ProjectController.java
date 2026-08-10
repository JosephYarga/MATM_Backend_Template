package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.ProjectDTO;
import bf.gov.mtdpce.entity.ProjectStatus;
import bf.gov.mtdpce.security.UserDetailsImpl;
import bf.gov.mtdpce.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projets", description = "API de gestion des projets et programmes")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Public endpoints
    @GetMapping("/public")
    @Operation(summary = "Projets publics", description = "Récupère la liste des projets")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> getPublicProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects(pageable)));
    }

    @GetMapping("/public/latest")
    @Operation(summary = "Derniers projets", description = "Récupère les 5 derniers projets")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getLatestProjects() {
        return ResponseEntity.ok(ApiResponse.success(projectService.getLatestProjects()));
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "Détail projet", description = "Récupère un projet par son ID")
    public ResponseEntity<ApiResponse<ProjectDTO>> getPublicProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectById(id)));
    }

    @GetMapping("/public/search")
    @Operation(summary = "Recherche de projets", description = "Recherche dans les projets")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> searchProjects(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(projectService.searchProjects(query, pageable)));
    }

    // Protected endpoints
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Tous les projets", description = "Récupère tous les projets (admin)")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects(pageable)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Projets par statut", description = "Récupère les projets par statut")
    public ResponseEntity<ApiResponse<Page<ProjectDTO>>> getProjectsByStatus(
            @PathVariable ProjectStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectsByStatus(status, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Détail projet (admin)", description = "Récupère un projet par son ID (admin)")
    public ResponseEntity<ApiResponse<ProjectDTO>> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Créer un projet", description = "Crée un nouveau projet")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(
            @Valid @RequestBody ProjectDTO projectDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(ApiResponse.success("Projet créé", projectService.createProject(projectDTO, userDetails.getId())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Modifier un projet", description = "Met à jour un projet existant")
    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(ApiResponse.success("Projet mis à jour", projectService.updateProject(id, projectDTO)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Supprimer un projet", description = "Supprime un projet")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Projet supprimé", null));
    }
}
