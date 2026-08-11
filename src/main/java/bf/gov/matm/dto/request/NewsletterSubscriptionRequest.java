package bf.gov.matm.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class NewsletterSubscriptionRequest {
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
    
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    private String firstName;
    
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String lastName;
    
    private List<String> preferences; // ACTUALITES, PROJETS, EVENEMENTS, OFFRES_EMPLOI
    
    private String frequency; // DAILY, WEEKLY, MONTHLY
    
    private Boolean acceptTerms = false;

    // Getters and Setters
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
    
    public Boolean getAcceptTerms() { return acceptTerms; }
    public void setAcceptTerms(Boolean acceptTerms) { this.acceptTerms = acceptTerms; }
}
