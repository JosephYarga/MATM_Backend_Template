package bf.gov.mtdpce.dto.request;

import bf.gov.mtdpce.entity.StructureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructureRequest {

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
    private StructureType structureType;
}
