package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.AgendaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponse {

    private UUID id;
    private String title;
    private String summary;
    private String content;
    private AgendaStatus status;
    private UUID authorId;
    private String authorName;
    private String lieux;
    private LocalDate datePublication;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AgendaImageResponse> images;
}
