package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "domaines")
@Data
public class Domaine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nom;
}
