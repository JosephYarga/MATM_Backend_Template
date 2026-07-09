package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesResponse {

    private UUID id;
    private String name;
    private String description;
    private String url;
    private String logo;
}
