package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructureRattacheResponse {

    private UUID id;
    private String name;
    private String acronym;
    private String type;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String logourl;
    private UUID ministereId;
    private Set<UUID> domaineIds;
}
