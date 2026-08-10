package bf.gov.mtdpce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class FAQResponse {
    
    private Long id;
    private String question;
    private String answer;
    private String category;
    private List<String> tags;
    private Integer displayOrder;
    private Boolean isPublished;
    private Long viewCount;
    private Long helpfulCount;
    private Long notHelpfulCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; }
    
    public Long getNotHelpfulCount() { return notHelpfulCount; }
    public void setNotHelpfulCount(Long notHelpfulCount) { this.notHelpfulCount = notHelpfulCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
