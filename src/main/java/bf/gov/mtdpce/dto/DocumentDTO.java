package bf.gov.mtdpce.dto;

import bf.gov.mtdpce.entity.DocumentCategory;
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
public class DocumentDTO {

    private Long id;

    @NotBlank(message = "Le titre est requis")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private DocumentCategory category;
    private Long typeId;
    private String typeName;
    private String typeDocument;
    private Integer downloadCount;
    private Boolean isPublic;
    private String uploadedByName;
    private Long uploadedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
