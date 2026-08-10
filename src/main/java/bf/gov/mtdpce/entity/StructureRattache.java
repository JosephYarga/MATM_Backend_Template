package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "structure_rattaches")
@Data
public class StructureRattache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String acronym;

    @Column(length = 255)
    private String type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(columnDefinition = "TEXT")
    private String website;

    @Column(columnDefinition = "TEXT")
    private String logourl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ministere_id", nullable = false)
    private Ministere ministere;

    @ManyToMany
    @JoinTable(
            name = "structure_rattache_domaines",
            joinColumns = @JoinColumn(name = "structure_rattache_id"),
            inverseJoinColumns = @JoinColumn(name = "domaine_id")
    )
    private Set<Domaine> domaines = new HashSet<>();
}
