package bf.gov.mtdpce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class NewsletterResponse {
    
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> preferences;
    private String frequency;
    private Boolean isActive;
    private Boolean isVerified;
    private String unsubscribeToken;
    private LocalDateTime subscribedAt;
    private LocalDateTime lastEmailSentAt;
    private Integer emailsSent;
    private String message;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public String getUnsubscribeToken() { return unsubscribeToken; }
    public void setUnsubscribeToken(String unsubscribeToken) { this.unsubscribeToken = unsubscribeToken; }
    
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
    
    public LocalDateTime getLastEmailSentAt() { return lastEmailSentAt; }
    public void setLastEmailSentAt(LocalDateTime lastEmailSentAt) { this.lastEmailSentAt = lastEmailSentAt; }
    
    public Integer getEmailsSent() { return emailsSent; }
    public void setEmailsSent(Integer emailsSent) { this.emailsSent = emailsSent; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
