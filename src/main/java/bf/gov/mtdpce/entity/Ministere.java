package bf.gov.mtdpce.entity;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ministere")
@Data
public class Ministere {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nomGeneral;

    @Column(nullable = false, length = 255)
    private String nomReel;

    @Column(nullable = false, length = 255)
    private String acronyme;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String missionGeneral;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String presentationSynthetique;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String presentationGlobale;

    @Column(name = "logo")
    private String logo;

    @Column(name = "image")
    private String image;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
