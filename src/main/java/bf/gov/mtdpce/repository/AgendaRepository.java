package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Agenda;
import bf.gov.mtdpce.entity.AgendaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    Page<Agenda> findByStatus(AgendaStatus status, Pageable pageable);

    List<Agenda> findTop5ByStatusOrderByPublishedAtDesc(AgendaStatus status);

    List<Agenda> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT a FROM Agenda a WHERE a.status = :status AND " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.summary) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Agenda> searchPublishedAgenda(@Param("search") String search, @Param("status") AgendaStatus status, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Agenda a WHERE a.status = :status")
    Long countByStatus(@Param("status") AgendaStatus status);

    Page<Agenda> findByAuthorId(Long authorId, Pageable pageable);
}
