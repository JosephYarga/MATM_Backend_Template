package bf.gov.mtdpce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String position;
    private String department;
    private String profileImage;
    private Set<String> roles;
}
