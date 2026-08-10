package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "newsletter_subscriptions")
public class NewsletterSubscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;
    
    @Column(columnDefinition = "TEXT")
    private String preferences;
    
    @Column(length = 20)
    private String frequency = "WEEKLY";
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "verification_token", length = 100)
    private String verificationToken;
    
    @Column(name = "unsubscribe_token", nullable = false, length = 100)
    private String unsubscribeToken;
    
    @Column(name = "subscribed_at")
    private LocalDateTime subscribedAt;
    
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
    
    @Column(name = "last_email_sent_at")
    private LocalDateTime lastEmailSentAt;
    
    @Column(name = "emails_sent")
    private Integer emailsSent = 0;
    
    @PrePersist
    protected void onCreate() {
        subscribedAt = LocalDateTime.now();
        if (unsubscribeToken == null) {
            unsubscribeToken = UUID.randomUUID().toString();
        }
        if (verificationToken == null) {
            verificationToken = UUID.randomUUID().toString();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }
    
    public String getUnsubscribeToken() { return unsubscribeToken; }
    public void setUnsubscribeToken(String unsubscribeToken) { this.unsubscribeToken = unsubscribeToken; }
    
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
    
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
    
    public LocalDateTime getLastEmailSentAt() { return lastEmailSentAt; }
    public void setLastEmailSentAt(LocalDateTime lastEmailSentAt) { this.lastEmailSentAt = lastEmailSentAt; }
    
    public Integer getEmailsSent() { return emailsSent; }
    public void setEmailsSent(Integer emailsSent) { this.emailsSent = emailsSent; }
}
