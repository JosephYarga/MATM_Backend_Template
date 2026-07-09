package bf.gov.mtdpce.dto.request;

import bf.gov.mtdpce.entity.AgendaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequest {

    private String title;
    private String summary;
    private String content;
    private AgendaStatus status;
    private String lieux;
    private LocalDate datePublication;
}
