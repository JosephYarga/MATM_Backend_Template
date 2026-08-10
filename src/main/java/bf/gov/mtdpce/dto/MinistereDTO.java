package bf.gov.mtdpce.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MinistereDTO {
    private Long id;
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
