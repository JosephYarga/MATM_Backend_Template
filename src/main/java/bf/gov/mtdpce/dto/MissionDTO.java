package bf.gov.mtdpce.dto;

import lombok.Data;

@Data
public class MissionDTO {
    private Long id;
    private String categorie;
    private String description;
    private Long ministereId;
}
