package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.ProjectDTO;
import bf.gov.mtdpce.entity.Project;
import bf.gov.mtdpce.entity.ProjectStatus;
import bf.gov.mtdpce.entity.ProjetCategorie;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ProjectRepository;
import bf.gov.mtdpce.repository.ProjetCategorieRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjetCategorieRepository projetCategorieRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<ProjectDTO> getProjectsByStatus(ProjectStatus status, Pageable pageable) {
        return projectRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    public Page<ProjectDTO> searchProjects(String search, Pageable pageable) {
        return projectRepository.searchProjects(search, pageable).map(this::convertToDTO);
    }

    public Page<ProjectDTO> getPublicProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<ProjectDTO> getLatestProjects() {
        return projectRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet", "id", id));
        return convertToDTO(project);
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, Long managerId) {
        User manager = null;
        if (managerId != null) {
            manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", managerId));
        }

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(projectDTO.getCategorieProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", projectDTO.getCategorieProjetId()));

        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .objectives(projectDTO.getObjectives())
                .projetCategorie(projetCategorie)
                .featuredImage(projectDTO.getFeaturedImage())
                .status(projectDTO.getStatus() != null ? projectDTO.getStatus() : ProjectStatus.PLANIFIE)
                .budget(projectDTO.getBudget())
                .progressPercentage(projectDTO.getProgressPercentage() != null ? projectDTO.getProgressPercentage() : 0)
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .partner(projectDTO.getPartner())
                .responsibleDepartment(projectDTO.getResponsibleDepartment())
                .manager(manager)
                .build();

        return convertToDTO(projectRepository.save(project));
    }

    @Transactional
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet", "id", id));

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(projectDTO.getCategorieProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", projectDTO.getCategorieProjetId()));

        project.setProjetCategorie(projetCategorie);
        if (projectDTO.getName() != null) project.setName(projectDTO.getName());
        if (projectDTO.getDescription() != null) project.setDescription(projectDTO.getDescription());
        if (projectDTO.getObjectives() != null) project.setObjectives(projectDTO.getObjectives());
        if (projectDTO.getFeaturedImage() != null) project.setFeaturedImage(projectDTO.getFeaturedImage());
        if (projectDTO.getStatus() != null) project.setStatus(projectDTO.getStatus());
        if (projectDTO.getBudget() != null) project.setBudget(projectDTO.getBudget());
        if (projectDTO.getProgressPercentage() != null) project.setProgressPercentage(projectDTO.getProgressPercentage());
        if (projectDTO.getStartDate() != null) project.setStartDate(projectDTO.getStartDate());
        if (projectDTO.getEndDate() != null) project.setEndDate(projectDTO.getEndDate());
        if (projectDTO.getPartner() != null) project.setPartner(projectDTO.getPartner());
        if (projectDTO.getResponsibleDepartment() != null) project.setResponsibleDepartment(projectDTO.getResponsibleDepartment());

        if (projectDTO.getManagerId() != null) {
            User manager = userRepository.findById(projectDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", projectDTO.getManagerId()));
            project.setManager(manager);
        }

        return convertToDTO(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Projet", "id", id);
        }
        projectRepository.deleteById(id);
    }

    public Long countActiveProjects() {
        return projectRepository.countByStatus(ProjectStatus.EN_COURS);
    }

    public Long countCompletedProjects() {
        return projectRepository.countByStatus(ProjectStatus.TERMINE);
    }

    public Double getAverageProgress() {
        Double avg = projectRepository.getAverageProgress();
        return avg != null ? avg : 0.0;
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO.ProjectDTOBuilder builder = ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .objectives(project.getObjectives())
                .type(project.getType())
                .categorieProjetId(project.getProjetCategorie().getId())
                .categorieProjetName(project.getProjetCategorie().getName())
                .featuredImage(project.getFeaturedImage())
                .status(project.getStatus())
                .budget(project.getBudget())
                .progressPercentage(project.getProgressPercentage())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .partner(project.getPartner())
                .responsibleDepartment(project.getResponsibleDepartment())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt());

        if (project.getManager() != null) {
            builder.managerName(project.getManager().getFirstName() + " " + project.getManager().getLastName())
                    .managerId(project.getManager().getId());
        }

        return builder.build();
    }
}
