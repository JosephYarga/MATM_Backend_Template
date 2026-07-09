package bf.gov.mtdpce.entity;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "statistiques")
@Data
public class StatistiquePublic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String valeur;
}
