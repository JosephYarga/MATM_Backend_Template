package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Bannière défilante de la première section de la page d'accueil.
 * Chaque bannière possède une image de fond, un titre, un court descriptif
 * et paramètre son propre comportement de défilement :
 *  - displayDuration : durée d'affichage (en secondes) avant de passer à la suivante,
 *  - isActive : seules les bannières actives entrent dans le défilement.
 */
@Entity
@Table(name = "banners")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    /** Chemin de l'image de fond (upload /uploads/... ou asset). */
    @Column(length = 500)
    private String image;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "link_text", length = 100)
    private String linkText;

    /** Ordre d'apparition dans le défilement. */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    /** Durée d'affichage de la bannière (en secondes) avant le défilement automatique. */
    @Column(name = "display_duration")
    @Builder.Default
    private Integer displayDuration = 5;

    /** Seules les bannières actives défilent. */
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
