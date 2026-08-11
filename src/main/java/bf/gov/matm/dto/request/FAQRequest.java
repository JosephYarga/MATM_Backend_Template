package bf.gov.matm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FAQRequest {
    
    @NotBlank(message = "La question est obligatoire")
    @Size(max = 500, message = "La question ne doit pas dépasser 500 caractères")
    private String question;
    
    @NotBlank(message = "La réponse est obligatoire")
    @Size(max = 5000, message = "La réponse ne doit pas dépasser 5000 caractères")
    private String answer;
    
    @NotBlank(message = "La catégorie est obligatoire")
    private String category;
    
    private String[] tags;
    
    private Integer displayOrder;
    
    private Boolean isPublished = true;

    // Getters and Setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
}
