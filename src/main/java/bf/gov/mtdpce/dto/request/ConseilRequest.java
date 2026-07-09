package bf.gov.mtdpce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConseilRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;

    private String description;

    /** Chemin de l'image (renseigné automatiquement lors d'un upload). */
    private String image;

    private Integer displayOrder;

    @Builder.Default
    private Boolean isActive = true;
}
