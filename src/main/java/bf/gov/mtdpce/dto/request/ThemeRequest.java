package bf.gov.mtdpce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRequest {

    @NotBlank(message = "Le nom du theme est requis")
    private String title;

    private String primaryColor;
    private String accentColor;
    private String secondaryColor;
    private String tertiaryColor;
}
