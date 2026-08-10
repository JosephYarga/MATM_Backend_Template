package bf.gov.mtdpce.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MinistreDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String profession;
    private String biographie;
    private String content;
    private String photo;
    private Boolean isActif;
    private Long ministereId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
