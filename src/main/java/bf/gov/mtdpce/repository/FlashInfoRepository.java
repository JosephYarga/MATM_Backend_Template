package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.FlashInfo;
import bf.gov.mtdpce.entity.FlashInfoPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlashInfoRepository extends JpaRepository<FlashInfo, Long> {
    
    @Query("SELECT f FROM FlashInfo f WHERE f.isActive = true AND " +
           "(f.startDate IS NULL OR f.startDate <= :now) AND " +
           "(f.endDate IS NULL OR f.endDate >= :now) " +
           "ORDER BY f.priority DESC, f.createdAt DESC")
    List<FlashInfo> findActiveFlashInfos(LocalDateTime now);
    
    Page<FlashInfo> findByIsActiveTrueOrderByPriorityDescCreatedAtDesc(Pageable pageable);
    
    List<FlashInfo> findByPriorityOrderByCreatedAtDesc(FlashInfoPriority priority);
    
    long countByIsActiveTrue();
}
