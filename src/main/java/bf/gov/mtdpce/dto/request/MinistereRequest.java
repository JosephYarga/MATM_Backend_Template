package bf.gov.mtdpce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinistereRequest {

    private String nomGeneral;
    private String nomReel;
    private String acronyme;
    private String missionGeneral;
    private String presentationSynthetique;
    private String presentationGlobale;
    private String logo;
    private String image;
}
