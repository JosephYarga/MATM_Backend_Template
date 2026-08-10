package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.EService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EServiceRepository extends JpaRepository<EService, Long> {
    
    Page<EService> findByIsActiveTrueOrderByDisplayOrderAsc(Pageable pageable);
    
    List<EService> findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(String category);
    
    Page<EService> findByIsOnlineTrueAndIsActiveTrueOrderByDisplayOrderAsc(Pageable pageable);
    
    @Query("SELECT e FROM EService e WHERE e.isActive = true AND " +
           "(LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<EService> searchServices(String query, Pageable pageable);
    
    @Query("SELECT DISTINCT e.category FROM EService e WHERE e.isActive = true ORDER BY e.category")
    List<String> findAllCategories();
    
    @Modifying
    @Query("UPDATE EService e SET e.viewCount = e.viewCount + 1 WHERE e.id = :id")
    void incrementViewCount(Long id);
    
    @Modifying
    @Query("UPDATE EService e SET e.requestCount = e.requestCount + 1 WHERE e.id = :id")
    void incrementRequestCount(Long id);
    
    long countByIsActiveTrue();
    
    long countByIsOnlineTrueAndIsActiveTrue();
}
