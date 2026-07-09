package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {

    private UUID id;
    private String title;
    private String summary;
    private String content;
    private String featuredImage;
    /** Code de la catégorie (ex. "ACTUALITE") — contrat d'API stable. */
    private String category;
    private String categoryLabel;
    private UUID categoryId;
    private ArticleStatus status;
    private Integer viewCount;
    private Boolean featured;
    private Boolean publishToFacebook;
    private String facebookContent;
    private String authorName;
    private UUID authorId;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ArticleImageResponse> images;
    private List<FacebookImageResponse> imagesFacebook;
}
