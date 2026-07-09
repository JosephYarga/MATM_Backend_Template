package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinistreResponse {

    private UUID id;
    private String nom;
    private String prenom;
    private String profession;
    private String biographie;
    private String content;
    private String photo;
    private Boolean isActif;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private UUID ministereId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
