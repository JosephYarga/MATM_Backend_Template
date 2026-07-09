package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Conseil (astuce / bonne pratique) affiché dans la section défilante horizontale
 * de la page d'accueil, sous les dernières actualités.
 * Chaque conseil possède une image, un titre et un texte descriptif.
 *  - displayOrder : ordre d'apparition dans le défilement,
 *  - isActive : seuls les conseils actifs sont affichés.
 */
@Entity
@Table(name = "conseils")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conseil {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Chemin de l'image d'illustration (upload /uploads/... ou asset). */
    @Column(length = 500)
    private String image;

    /** Ordre d'apparition dans le défilement. */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    /** Seuls les conseils actifs sont affichés dans le défilement. */
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
