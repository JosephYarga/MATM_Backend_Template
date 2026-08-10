package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.FAQRequest;
import bf.gov.mtdpce.dto.response.FAQResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.entity.FAQ;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FAQService {
    
    @Autowired
    private FAQRepository faqRepository;
    
    public PaginatedResponse<FAQResponse> getAllFAQs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FAQ> faqPage = faqRepository.findByIsPublishedTrueOrderByDisplayOrderAsc(pageable);
        
        List<FAQResponse> faqs = faqPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(faqs, page, size, faqPage.getTotalElements());
    }
    
    public List<FAQResponse> getFAQsByCategory(String category) {
        return faqRepository.findByCategoryAndIsPublishedTrueOrderByDisplayOrderAsc(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<String> getAllCategories() {
        return faqRepository.findAllCategories();
    }
    
    public FAQResponse getFAQById(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ non trouvée avec l'id: " + id));
        faqRepository.incrementViewCount(id);
        return mapToResponse(faq);
    }
    
    public FAQResponse createFAQ(FAQRequest request) {
        FAQ faq = new FAQ();
        mapRequestToEntity(request, faq);
        FAQ saved = faqRepository.save(faq);
        return mapToResponse(saved);
    }
    
    public FAQResponse updateFAQ(Long id, FAQRequest request) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ non trouvée avec l'id: " + id));
        mapRequestToEntity(request, faq);
        FAQ updated = faqRepository.save(faq);
        return mapToResponse(updated);
    }
    
    public void deleteFAQ(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("FAQ non trouvée avec l'id: " + id);
        }
        faqRepository.deleteById(id);
    }
    
    public void markHelpful(Long id, boolean helpful) {
        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("FAQ non trouvée avec l'id: " + id);
        }
        if (helpful) {
            faqRepository.incrementHelpfulCount(id);
        } else {
            faqRepository.incrementNotHelpfulCount(id);
        }
    }
    
    public PaginatedResponse<FAQResponse> searchFAQs(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FAQ> faqPage = faqRepository.searchFAQs(query, pageable);
        
        List<FAQResponse> faqs = faqPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(faqs, page, size, faqPage.getTotalElements());
    }
    
    private void mapRequestToEntity(FAQRequest request, FAQ faq) {
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setCategory(request.getCategory());
        if (request.getTags() != null) {
            faq.setTags(String.join(",", request.getTags()));
        }
        faq.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        faq.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : true);
    }
    
    private FAQResponse mapToResponse(FAQ faq) {
        FAQResponse response = new FAQResponse();
        response.setId(faq.getId());
        response.setQuestion(faq.getQuestion());
        response.setAnswer(faq.getAnswer());
        response.setCategory(faq.getCategory());
        if (faq.getTags() != null && !faq.getTags().isEmpty()) {
            response.setTags(Arrays.asList(faq.getTags().split(",")));
        }
        response.setDisplayOrder(faq.getDisplayOrder());
        response.setIsPublished(faq.getIsPublished());
        response.setViewCount(faq.getViewCount());
        response.setHelpfulCount(faq.getHelpfulCount());
        response.setNotHelpfulCount(faq.getNotHelpfulCount());
        response.setCreatedAt(faq.getCreatedAt());
        response.setUpdatedAt(faq.getUpdatedAt());
        return response;
    }
}
