package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "missions")
@Data
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String categorie;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ministere_id", nullable = false)
    private Ministere ministere;
}
