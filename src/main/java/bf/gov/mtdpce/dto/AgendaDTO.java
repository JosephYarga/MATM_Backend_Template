package bf.gov.mtdpce.dto;

import bf.gov.mtdpce.entity.AgendaStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaDTO {

    private Long id;

    private String title;

    private String summary;

    private String content;

    private AgendaStatus status;

    private Long authorId;

    private String authorName;

    private String lieux;

    private LocalDate datePublication;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AgendaImageDTO> images;
}
