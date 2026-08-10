package bf.gov.mtdpce.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleImageDTO {

    private Long id;
    private String imageUrl;
    private Boolean isFeatured;
}