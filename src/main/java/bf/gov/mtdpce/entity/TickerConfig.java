package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Configuration globale du bandeau « Flash Info » (une seule ligne).
 * scrollDuration : durée (en secondes) d'un cycle complet de défilement.
 * Plus la valeur est petite, plus le défilement est rapide.
 */
@Entity
@Table(name = "ticker_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Durée (en secondes) d'un cycle complet de défilement du bandeau. */
    @Column(name = "scroll_duration")
    @Builder.Default
    private Integer scrollDuration = 30;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }
}
