package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.ContactStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private ContactStatus status;
    private String response;
    private String respondedByName;
    private UUID respondedById;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;
}
