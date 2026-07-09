package bf.gov.mtdpce.dto.request;

import bf.gov.mtdpce.entity.ArticleStatus;
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
public class ArticleRequest {

    @NotBlank(message = "Le titre est requis")
    @Size(max = 500, message = "Le titre ne doit pas dépasser 500 caractères")
    private String title;

    @NotBlank(message = "Le résumé est requis")
    private String summary;

    @NotBlank(message = "Le contenu est requis")
    private String content;

    private String featuredImage;

    /** Code de la catégorie (ex. "ACTUALITE") — contrat d'API stable. */
    private String category;

    private ArticleStatus status;

    private Boolean featured;

    /** Cocher pour publier également sur Facebook. */
    private Boolean publishToFacebook;

    /** Contenu riche spécifique à Facebook (utilisé si publishToFacebook = true). */
    private String facebookContent;
}
