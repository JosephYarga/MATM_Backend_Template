package bf.gov.matm.service;

import bf.gov.matm.dto.request.NewsletterSubscriptionRequest;
import bf.gov.matm.dto.response.NewsletterResponse;
import bf.gov.matm.entity.NewsletterSubscription;
import bf.gov.matm.exception.BadRequestException;
import bf.gov.matm.exception.ResourceNotFoundException;
import bf.gov.matm.repository.NewsletterSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
public class NewsletterService {
    
    @Autowired
    private NewsletterSubscriptionRepository subscriptionRepository;
    
    public NewsletterResponse subscribe(NewsletterSubscriptionRequest request) {
        if (subscriptionRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Cette adresse email est déjà inscrite à la newsletter");
        }
        
        NewsletterSubscription subscription = new NewsletterSubscription();
        subscription.setEmail(request.getEmail());
        subscription.setFirstName(request.getFirstName());
        subscription.setLastName(request.getLastName());
        if (request.getPreferences() != null) {
            subscription.setPreferences(String.join(",", request.getPreferences()));
        }
        subscription.setFrequency(request.getFrequency() != null ? request.getFrequency() : "WEEKLY");
        
        NewsletterSubscription saved = subscriptionRepository.save(subscription);
        
        return mapToResponse(saved, "Inscription réussie. Veuillez vérifier votre email pour confirmer votre inscription.");
    }
    
    public NewsletterResponse verify(String token) {
        NewsletterSubscription subscription = subscriptionRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de vérification invalide"));
        
        if (subscription.getIsVerified()) {
            return mapToResponse(subscription, "Votre email est déjà vérifié");
        }
        
        subscription.setIsVerified(true);
        subscription.setVerifiedAt(LocalDateTime.now());
        subscriptionRepository.save(subscription);
        
        return mapToResponse(subscription, "Email vérifié avec succès. Vous recevrez désormais nos newsletters.");
    }
    
    public NewsletterResponse unsubscribe(String token) {
        NewsletterSubscription subscription = subscriptionRepository.findByUnsubscribeToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de désinscription invalide"));
        
        subscription.setIsActive(false);
        subscriptionRepository.save(subscription);
        
        return mapToResponse(subscription, "Vous avez été désinscrit de notre newsletter avec succès.");
    }
    
    public NewsletterResponse updatePreferences(String email, NewsletterSubscriptionRequest request) {
        NewsletterSubscription subscription = subscriptionRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune inscription trouvée pour cet email"));
        
        if (request.getFirstName() != null) {
            subscription.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            subscription.setLastName(request.getLastName());
        }
        if (request.getPreferences() != null) {
            subscription.setPreferences(String.join(",", request.getPreferences()));
        }
        if (request.getFrequency() != null) {
            subscription.setFrequency(request.getFrequency());
        }
        
        subscriptionRepository.save(subscription);
        
        return mapToResponse(subscription, "Préférences mises à jour avec succès.");
    }
    
    public long getSubscriberCount() {
        return subscriptionRepository.countByIsActiveTrueAndIsVerifiedTrue();
    }
    
    private NewsletterResponse mapToResponse(NewsletterSubscription subscription, String message) {
        NewsletterResponse response = new NewsletterResponse();
        response.setEmail(subscription.getEmail());
        response.setFirstName(subscription.getFirstName());
        response.setLastName(subscription.getLastName());
        if (subscription.getPreferences() != null && !subscription.getPreferences().isEmpty()) {
            response.setPreferences(Arrays.asList(subscription.getPreferences().split(",")));
        }
        response.setFrequency(subscription.getFrequency());
        response.setIsActive(subscription.getIsActive());
        response.setIsVerified(subscription.getIsVerified());
        response.setSubscribedAt(subscription.getSubscribedAt());
        response.setMessage(message);
        return response;
    }
}
