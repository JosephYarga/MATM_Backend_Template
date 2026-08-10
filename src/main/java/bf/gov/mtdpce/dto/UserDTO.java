package bf.gov.mtdpce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String position;
    private String department;
    private String profileImage;
    private Boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
