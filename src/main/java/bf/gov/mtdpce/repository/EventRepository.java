package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Event;
import bf.gov.mtdpce.entity.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Page<Event> findByIsPublicTrueOrderByStartDateDesc(Pageable pageable);
    
    Page<Event> findByStatusOrderByStartDateDesc(EventStatus status, Pageable pageable);
    
    List<Event> findByIsPublicTrueAndStartDateAfterOrderByStartDateAsc(LocalDateTime date);
    
    @Query("SELECT e FROM Event e WHERE e.isPublic = true AND e.startDate BETWEEN :start AND :end ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE e.isPublic = true AND " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Event> searchEvents(String query, Pageable pageable);
    
    long countByStatus(EventStatus status);
}
