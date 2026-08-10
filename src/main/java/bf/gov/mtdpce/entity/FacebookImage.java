package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "facebook_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacebookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}
