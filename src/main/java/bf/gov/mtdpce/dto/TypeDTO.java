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
public class TypeDTO {
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String name;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
