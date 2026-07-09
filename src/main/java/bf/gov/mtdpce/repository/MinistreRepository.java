package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.Ministre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MinistreRepository extends JpaRepository<Ministre, UUID> {

    Page<Ministre> findByMinistereId(UUID ministereId, Pageable pageable);
    Optional<Ministre> findByIsActifTrue();
    /** Tous les ministres actifs (pour garantir qu'un seul reste actif). */
    List<Ministre> findAllByIsActifTrue();
    boolean existsByIsActifTrue();

    /** Anciens ministres (non en fonction), du mandat le plus récent au plus ancien. */
    @Query("SELECT m FROM Ministre m WHERE m.isActif = false ORDER BY m.dateDebut DESC NULLS LAST")
    Page<Ministre> findFormerMinisters(Pageable pageable);
}
