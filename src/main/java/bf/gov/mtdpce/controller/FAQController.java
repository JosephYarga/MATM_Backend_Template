package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.FAQRequest;
import bf.gov.mtdpce.dto.response.FAQResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.service.FAQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@Tag(name = "FAQ", description = "Gestion des questions fréquentes")
public class FAQController {
    
    @Autowired
    private FAQService faqService;
    
    @GetMapping("/public")
    @Operation(summary = "Liste des FAQs publiques")
    public ResponseEntity<PaginatedResponse<FAQResponse>> getPublicFAQs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(faqService.getAllFAQs(page, size));
    }
    
    @GetMapping("/public/category/{category}")
    @Operation(summary = "FAQs par catégorie")
    public ResponseEntity<List<FAQResponse>> getFAQsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(faqService.getFAQsByCategory(category));
    }
    
    @GetMapping("/public/categories")
    @Operation(summary = "Liste des catégories de FAQ")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(faqService.getAllCategories());
    }
    
    @GetMapping("/public/{id}")
    @Operation(summary = "Détail d'une FAQ")
    public ResponseEntity<FAQResponse> getFAQById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFAQById(id));
    }
    
    @GetMapping("/public/search")
    @Operation(summary = "Rechercher dans les FAQs")
    public ResponseEntity<PaginatedResponse<FAQResponse>> searchFAQs(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(faqService.searchFAQs(query, page, size));
    }
    
    @PostMapping("/public/{id}/feedback")
    @Operation(summary = "Donner un avis sur une FAQ")
    public ResponseEntity<ApiResponse> giveFeedback(
            @PathVariable Long id,
            @RequestParam boolean helpful) {
        faqService.markHelpful(id, helpful);
        return ResponseEntity.ok(new ApiResponse(true, "Merci pour votre retour"));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Liste de toutes les FAQs (admin)")
    public ResponseEntity<PaginatedResponse<FAQResponse>> getAllFAQs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(faqService.getAllFAQs(page, size));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Créer une FAQ")
    public ResponseEntity<FAQResponse> createFAQ(@Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqService.createFAQ(request));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Modifier une FAQ")
    public ResponseEntity<FAQResponse> updateFAQ(
            @PathVariable Long id,
            @Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqService.updateFAQ(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une FAQ")
    public ResponseEntity<ApiResponse> deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.ok(new ApiResponse(true, "FAQ supprimée avec succès"));
    }
}
