package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeResponse {

    private String id;
    private String title;
    private String primaryColor;
    private String accentColor;
    private String secondaryColor;
    private String tertiaryColor;
    private Boolean isActive;
}
