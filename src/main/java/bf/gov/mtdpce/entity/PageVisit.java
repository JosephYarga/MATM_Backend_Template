package bf.gov.mtdpce.entity;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Trace une visite de page ou un clic sur le site public.
 * Sert de base aux statistiques de fréquentation (analytics DCRP).
 */
@Entity
@Table(name = "page_visits", indexes = {
        @Index(name = "idx_page_visits_visited_at", columnList = "visited_at"),
        @Index(name = "idx_page_visits_type", columnList = "type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Chemin visité (ex: /actualites, /projets/3) */
    @Column(nullable = false, length = 500)
    private String path;

    /** Identifiant de visiteur (uuid stocké côté navigateur) — sert à compter les visiteurs uniques */
    @Column(name = "session_id", length = 100)
    private String sessionId;

    /** PAGE_VIEW ou CLICK */
    @Column(nullable = false, length = 20)
    private String type;

    /** Libellé optionnel (texte/href du lien cliqué) */
    @Column(length = 500)
    private String label;

    /** Provenance (document.referrer) */
    @Column(length = 500)
    private String referrer;

    @Column(name = "visited_at", nullable = false)
    private LocalDateTime visitedAt;
}
