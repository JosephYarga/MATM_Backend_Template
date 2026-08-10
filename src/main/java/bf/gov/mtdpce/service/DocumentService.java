package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.DocumentDTO;
import bf.gov.mtdpce.entity.Document;
import bf.gov.mtdpce.entity.DocumentCategory;
import bf.gov.mtdpce.entity.Type;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.DocumentRepository;
import bf.gov.mtdpce.repository.TypeRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRepository typeRepository;

    public Page<DocumentDTO> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<DocumentDTO> getPublicDocuments(Pageable pageable) {
        return documentRepository.findByIsPublicTrue(pageable).map(this::convertToDTO);
    }

    public Page<DocumentDTO> getDocumentsByCategory(DocumentCategory category, Pageable pageable) {
        return documentRepository.findByCategory(category, pageable).map(this::convertToDTO);
    }

    public Page<DocumentDTO> searchPublicDocuments(String search, Pageable pageable) {
        return documentRepository.searchPublicDocuments(search, pageable).map(this::convertToDTO);
    }

    public List<DocumentDTO> getLatestPublicDocuments() {
        return documentRepository.findTop10ByIsPublicTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DocumentDTO getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        return convertToDTO(document);
    }

    @Transactional
    public DocumentDTO createDocument(DocumentDTO documentDTO, Long uploadedById) {
        User uploadedBy = userRepository.findById(uploadedById)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", uploadedById));
        Type type = typeRepository.findById(documentDTO.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", documentDTO.getTypeId()));
        Document document = Document.builder()
                .title(documentDTO.getTitle())
                .description(documentDTO.getDescription())
                .fileName(documentDTO.getFileName())
                .filePath(documentDTO.getFilePath())
                .fileType(documentDTO.getFileType())
                .typeDocument(documentDTO.getTypeDocument())
                .fileSize(documentDTO.getFileSize())
                .category(documentDTO.getCategory() != null ? documentDTO.getCategory() : DocumentCategory.AUTRE)
                .isPublic(documentDTO.getIsPublic() != null ? documentDTO.getIsPublic() : true)
                .downloadCount(0)
                .uploadedBy(uploadedBy)
                .type(type)
                .build();

        return convertToDTO(documentRepository.save(document));
    }

    @Transactional
    public DocumentDTO updateDocument(Long id, DocumentDTO documentDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));

        Type type = typeRepository.findById(documentDTO.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", documentDTO.getTypeId()));
        document.setType(type);

        if (documentDTO.getTitle() != null) document.setTitle(documentDTO.getTitle());
        if (documentDTO.getFileName() != null) document.setFileName(documentDTO.getFileName());
        if (documentDTO.getFilePath() != null) document.setFilePath(documentDTO.getFilePath());
        if (documentDTO.getFileType() != null) document.setFileType(documentDTO.getFileType());
        if (documentDTO.getTypeDocument() != null) document.setTypeDocument(documentDTO.getTypeDocument());
        if (documentDTO.getFileSize() != null) document.setFileSize(documentDTO.getFileSize());
        if (documentDTO.getDescription() != null) document.setDescription(documentDTO.getDescription());
        if (documentDTO.getCategory() != null) document.setCategory(documentDTO.getCategory());
        if (documentDTO.getIsPublic() != null) document.setIsPublic(documentDTO.getIsPublic());

        return convertToDTO(documentRepository.save(document));
    }

    @Transactional
    public void incrementDownloadCount(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        document.setDownloadCount(document.getDownloadCount() + 1);
        documentRepository.save(document);
    }

    @Transactional
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document", "id", id);
        }
        documentRepository.deleteById(id);
    }

    public Long countPublicDocuments() {
        return documentRepository.countPublicDocuments();
    }

    private DocumentDTO convertToDTO(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .title(document.getTitle())
                .description(document.getDescription())
                .fileName(document.getFileName())
                .filePath(document.getFilePath())
                .fileType(document.getFileType())
                .typeDocument(document.getTypeDocument())
                .fileSize(document.getFileSize())
                .category(document.getCategory())
                .typeId(document.getType().getId())
                .typeName(document.getType().getName())
                .downloadCount(document.getDownloadCount())
                .isPublic(document.getIsPublic())
                .uploadedByName(document.getUploadedBy().getFirstName() + " " + document.getUploadedBy().getLastName())
                .uploadedById(document.getUploadedBy().getId())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
