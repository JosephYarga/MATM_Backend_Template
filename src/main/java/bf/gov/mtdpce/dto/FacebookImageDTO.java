package bf.gov.mtdpce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacebookImageDTO {
    private Long id;
    private String imageUrl;
    private Boolean isFeatured;
}
