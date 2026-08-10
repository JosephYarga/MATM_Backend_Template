package bf.gov.mtdpce.dto;

import bf.gov.mtdpce.entity.StructureType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StructureDTO {
    private Long id;
    private String title;
    private String name;
    private String phone;
    private String email;
    private String acronym;
    private String niveau;
    private String photo;
    private Long ministereId;
    private String ministereName;
    private StructureType structureType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
