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
public class BannerRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;

    @Size(max = 500, message = "Le descriptif ne doit pas dépasser 500 caractères")
    private String description;

    /** Chemin de l'image de fond (renseigné automatiquement lors d'un upload). */
    private String image;

    private String linkUrl;

    private String linkText;

    private Integer displayOrder;

    /** Durée d'affichage (en secondes) avant le défilement automatique. */
    private Integer displayDuration;

    @Builder.Default
    private Boolean isActive = true;
}
