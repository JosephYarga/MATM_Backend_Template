package bf.gov.mtdpce.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LegalTextResponse {
    
    private Long id;
    private String title;
    private String textType;
    private String referenceNumber;
    private LocalDate publicationDate;
    private LocalDate effectiveDate;
    private String summary;
    private String content;
    private String fileUrl;
    private String category;
    private List<String> keywords;
    private String signatoryAuthority;
    private String journalOfficielReference;
    private Boolean isActive;
    private Long viewCount;
    private Long downloadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getTextType() { return textType; }
    public void setTextType(String textType) { this.textType = textType; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    
    public String getSignatoryAuthority() { return signatoryAuthority; }
    public void setSignatoryAuthority(String signatoryAuthority) { this.signatoryAuthority = signatoryAuthority; }
    
    public String getJournalOfficielReference() { return journalOfficielReference; }
    public void setJournalOfficielReference(String journalOfficielReference) { this.journalOfficielReference = journalOfficielReference; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
