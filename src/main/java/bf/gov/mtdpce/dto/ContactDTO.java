package bf.gov.mtdpce.dto;

import bf.gov.mtdpce.entity.ContactStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String name;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String phone;

    @NotBlank(message = "Le sujet est requis")
    @Size(max = 200, message = "Le sujet ne doit pas dépasser 200 caractères")
    private String subject;

    @NotBlank(message = "Le message est requis")
    private String message;

    private ContactStatus status;
    private String response;
    private String respondedByName;
    private Long respondedById;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
