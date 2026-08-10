package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.NewsletterSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, Long> {
    
    Optional<NewsletterSubscription> findByEmail(String email);
    
    Optional<NewsletterSubscription> findByVerificationToken(String token);
    
    Optional<NewsletterSubscription> findByUnsubscribeToken(String token);
    
    Page<NewsletterSubscription> findByIsActiveTrueAndIsVerifiedTrue(Pageable pageable);
    
    List<NewsletterSubscription> findByIsActiveTrueAndIsVerifiedTrueAndFrequency(String frequency);
    
    boolean existsByEmail(String email);
    
    long countByIsActiveTrue();
    
    long countByIsActiveTrueAndIsVerifiedTrue();
}
