package bf.gov.mtdpce.dto;

import bf.gov.mtdpce.entity.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Le nom du projet est requis")
    @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
    private String name;

    @NotBlank(message = "La description est requise")
    private String description;

    private String objectives;
    private String type;
    private Long categorieProjetId;
    private String categorieProjetName;
    private String featuredImage;
    private ProjectStatus status;
    private BigDecimal budget;
    private Integer progressPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String partner;
    private String responsibleDepartment;
    private String managerName;
    private Long managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
