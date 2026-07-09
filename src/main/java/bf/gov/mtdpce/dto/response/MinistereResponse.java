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
public class MinistereResponse {

    private UUID id;
    private String nomGeneral;
    private String nomReel;
    private String acronyme;
    private String missionGeneral;
    private String presentationSynthetique;
    private String presentationGlobale;
    private String logo;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
