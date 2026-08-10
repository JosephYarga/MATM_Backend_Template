package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "structures")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Structure {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 50)
    private String phone;

    @Column(length = 255)
    private String email;
    
    @Column(length = 100)
    private String acronym;

    @Column(length = 100)
    private String niveau;

    @Column(name = "photo")
    private String photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "structure_type", nullable = false, length = 30)
    private StructureType structureType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministere_id", nullable = false)
    private Ministere ministere;

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
