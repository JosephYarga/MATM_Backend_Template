package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.StructureType;
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
public class StructureResponse {

    private UUID id;
    private String title;
    private String name;
    private String phone;
    private String email;
    private String acronym;
    private String niveau;
    private String description;
    private UUID parentId;
    private String photo;
    private UUID ministereId;
    private String ministereName;
    private StructureType structureType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
