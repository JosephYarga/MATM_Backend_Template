package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategoryResponse {

    private UUID id;
    private String code;
    private String label;
    private String description;
    private Integer displayOrder;
    /** Nombre d'articles rattachés à cette catégorie (lecture seule). */
    private Long articleCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
