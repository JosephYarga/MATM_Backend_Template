package bf.gov.mtdpce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDTO {

    private String id;
    @NotBlank(message = "Le nom du theme est requis")
    private String title;

    private String primaryColor;

    private String accentColor;

    private String secondaryColor;

    private String tertiaryColor;
}
