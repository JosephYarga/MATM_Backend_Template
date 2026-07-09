package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private String objectives;
    private String type;
    private UUID categorieProjetId;
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
    private UUID managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
