package bf.gov.matm.controller;

import bf.gov.matm.dto.request.NewsletterSubscriptionRequest;
import bf.gov.matm.dto.response.NewsletterResponse;
import bf.gov.matm.service.NewsletterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/newsletter")
@Tag(name = "Newsletter", description = "Gestion des inscriptions à la newsletter")
public class NewsletterController {
    
    @Autowired
    private NewsletterService newsletterService;
    
    @PostMapping("/public/subscribe")
    @Operation(summary = "S'inscrire à la newsletter")
    public ResponseEntity<NewsletterResponse> subscribe(
            @Valid @RequestBody NewsletterSubscriptionRequest request) {
        return ResponseEntity.ok(newsletterService.subscribe(request));
    }
    
    @GetMapping("/public/verify")
    @Operation(summary = "Vérifier l'email")
    public ResponseEntity<NewsletterResponse> verify(@RequestParam String token) {
        return ResponseEntity.ok(newsletterService.verify(token));
    }
    
    @GetMapping("/public/unsubscribe")
    @Operation(summary = "Se désinscrire de la newsletter")
    public ResponseEntity<NewsletterResponse> unsubscribe(@RequestParam String token) {
        return ResponseEntity.ok(newsletterService.unsubscribe(token));
    }
    
    @PutMapping("/public/preferences")
    @Operation(summary = "Mettre à jour les préférences")
    public ResponseEntity<NewsletterResponse> updatePreferences(
            @RequestParam String email,
            @Valid @RequestBody NewsletterSubscriptionRequest request) {
        return ResponseEntity.ok(newsletterService.updatePreferences(email, request));
    }
    
    @GetMapping("/public/count")
    @Operation(summary = "Nombre d'abonnés")
    public ResponseEntity<Map<String, Long>> getSubscriberCount() {
        return ResponseEntity.ok(Map.of("count", newsletterService.getSubscriberCount()));
    }
}
