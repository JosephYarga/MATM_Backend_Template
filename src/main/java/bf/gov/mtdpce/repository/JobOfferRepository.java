package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.JobOffer;
import bf.gov.mtdpce.entity.JobOfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    
    Page<JobOffer> findByIsPublishedTrueAndStatusOrderByCreatedAtDesc(JobOfferStatus status, Pageable pageable);
    
    Page<JobOffer> findByIsPublishedTrueAndDeadlineAfterOrderByDeadlineAsc(LocalDate date, Pageable pageable);
    
    List<JobOffer> findByIsPublishedTrueAndStatusAndDeadlineAfterOrderByDeadlineAsc(JobOfferStatus status, LocalDate date);
    
    @Query("SELECT j FROM JobOffer j WHERE j.isPublished = true AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<JobOffer> searchJobOffers(String query, Pageable pageable);
    
    @Modifying
    @Query("UPDATE JobOffer j SET j.viewCount = j.viewCount + 1 WHERE j.id = :id")
    void incrementViewCount(Long id);
    
    @Modifying
    @Query("UPDATE JobOffer j SET j.applicationCount = j.applicationCount + 1 WHERE j.id = :id")
    void incrementApplicationCount(Long id);
    
    long countByIsPublishedTrueAndStatus(JobOfferStatus status);
    
    long countByIsPublishedTrueAndDeadlineAfter(LocalDate date);
}
