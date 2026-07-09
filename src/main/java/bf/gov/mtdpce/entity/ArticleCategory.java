package bf.gov.mtdpce.entity;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Catégorie d'article gérée en base (anciennement un enum).
 * Le champ {@code code} conserve les valeurs historiques de l'enum
 * (ACTUALITE, COMMUNIQUE, ...) afin de garder le contrat d'API stable.
 */
@Entity
@Table(name = "article_categories", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Code stable utilisé par l'API et le frontend (ex. "ACTUALITE"). */
    @Column(nullable = false, length = 50, unique = true)
    private String code;

    /** Libellé affiché (ex. "Actualité"). */
    @Column(nullable = false, length = 100)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
