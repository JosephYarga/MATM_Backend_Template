package bf.gov.mtdpce.dto;

import lombok.Data;

import java.util.Set;

@Data
public class StructureRattacheDTO {

    private Long id;
    private String name;
    private String acronym;
    private String type;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String logourl;
    private Long ministereId;
    private Set<Long> domaineIds;
}
