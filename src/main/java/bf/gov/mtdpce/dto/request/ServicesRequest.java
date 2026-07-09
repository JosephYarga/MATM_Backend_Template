package bf.gov.mtdpce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesRequest {

    private String name;
    private String description;
    private String url;
    private String logo;
}
