package bf.gov.mtdpce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinistreRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String profession;
    private String biographie;
    private String content;

    /** Chemin de la photo (renseigné automatiquement lors d'un upload). */
    private String photo;

    @Builder.Default
    private Boolean isActif = false;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private UUID ministereId;
}
