package bf.gov.mtdpce.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agenda_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;
}
