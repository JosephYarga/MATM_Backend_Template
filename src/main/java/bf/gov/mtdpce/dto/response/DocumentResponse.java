package bf.gov.mtdpce.dto.response;

import bf.gov.mtdpce.entity.DocumentCategory;
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
public class DocumentResponse {

    private UUID id;
    private String title;
    private String description;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private DocumentCategory category;
    private UUID typeId;
    private String typeName;
    private String typeDocument;
    private Integer downloadCount;
    private Boolean isPublic;
    private String uploadedByName;
    private UUID uploadedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
