package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    
    Page<FAQ> findByIsPublishedTrueOrderByDisplayOrderAsc(Pageable pageable);
    
    List<FAQ> findByCategoryAndIsPublishedTrueOrderByDisplayOrderAsc(String category);
    
    @Query("SELECT f FROM FAQ f WHERE f.isPublished = true AND " +
           "(LOWER(f.question) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.answer) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<FAQ> searchFAQs(String query, Pageable pageable);
    
    @Query("SELECT DISTINCT f.category FROM FAQ f WHERE f.isPublished = true ORDER BY f.category")
    List<String> findAllCategories();
    
    @Modifying
    @Query("UPDATE FAQ f SET f.viewCount = f.viewCount + 1 WHERE f.id = :id")
    void incrementViewCount(Long id);
    
    @Modifying
    @Query("UPDATE FAQ f SET f.helpfulCount = f.helpfulCount + 1 WHERE f.id = :id")
    void incrementHelpfulCount(Long id);
    
    @Modifying
    @Query("UPDATE FAQ f SET f.notHelpfulCount = f.notHelpfulCount + 1 WHERE f.id = :id")
    void incrementNotHelpfulCount(Long id);
    
    long countByIsPublishedTrue();
}
