package bf.gov.mtdpce.entity;

import java.util.UUID;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Configuration d'intégration à l'API Facebook Graph (page officielle du ministère).
 *
 * Stocke l'identifiant de page et le jeton d'accès « longue durée » utilisés pour
 * publier / récupérer le contenu de la page Facebook.
 *
 * ⚠️ Sécurité : {@code accessToken} est un SECRET. Il ne doit JAMAIS être exposé dans
 * un DTO public ni renvoyé par un endpoint accessible sans authentification admin.
 * Prévoir un masquage (ex. « EAAU…Mgi ») dans les réponses d'administration.
 */
@Entity
@Table(name = "facebook_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacebookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Libellé interne (ex. « Page officielle MTDPCE »). */
    @Column(length = 150)
    private String label;

    /** Identifiant de la page Facebook (Page ID). */
    @Column(name = "page_id", nullable = false, length = 100)
    private String pageId;

    /** Jeton d'accès longue durée de la page — SECRET. */
    @Column(name = "access_token", nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    /** Date d'expiration du jeton, si connue (jetons longue durée ≈ 60 jours). */
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    /** Configuration active (une seule devrait l'être à la fois). */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
