package bf.gov.matm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class LegalTextRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 500, message = "Le titre ne doit pas dépasser 500 caractères")
    private String title;
    
    @NotBlank(message = "Le type de texte est obligatoire")
    private String textType; // LOI, DECRET, ARRETE, CIRCULAIRE, NOTE, ORDONNANCE
    
    @Size(max = 100, message = "Le numéro de référence ne doit pas dépasser 100 caractères")
    private String referenceNumber;
    
    private LocalDate publicationDate;
    
    private LocalDate effectiveDate;
    
    @Size(max = 10000, message = "Le résumé ne doit pas dépasser 10000 caractères")
    private String summary;
    
    private String content;
    
    private String fileUrl;
    
    private String category;
    
    private String[] keywords;
    
    private String signatoryAuthority;
    
    private String journalOfficielReference;
    
    private Boolean isActive = true;

    // Getters and Setters
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
    
    public String[] getKeywords() { return keywords; }
    public void setKeywords(String[] keywords) { this.keywords = keywords; }
    
    public String getSignatoryAuthority() { return signatoryAuthority; }
    public void setSignatoryAuthority(String signatoryAuthority) { this.signatoryAuthority = signatoryAuthority; }
    
    public String getJournalOfficielReference() { return journalOfficielReference; }
    public void setJournalOfficielReference(String journalOfficielReference) { this.journalOfficielReference = journalOfficielReference; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
