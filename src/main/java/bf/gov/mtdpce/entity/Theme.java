package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "themes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 500)
    private String title;

    private String primaryColor;

    private String accentColor;

    private String secondaryColor;

    private String tertiaryColor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
