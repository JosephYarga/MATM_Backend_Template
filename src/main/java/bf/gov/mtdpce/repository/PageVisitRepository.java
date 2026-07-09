package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.PageVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PageVisitRepository extends JpaRepository<PageVisit, UUID> {

    /** Nombre de visiteurs uniques (sessions distinctes) sur une période, pour un type donné. */
    @Query("SELECT COUNT(DISTINCT v.sessionId) FROM PageVisit v " +
            "WHERE v.type = :type AND v.visitedAt >= :start")
    long countDistinctSessionsSince(@Param("type") String type,
                                    @Param("start") LocalDateTime start);

    /** Nombre total d'événements (pages vues / clics) sur une période. */
    long countByTypeAndVisitedAtGreaterThanEqual(String type, LocalDateTime start);

    /** Timestamps + session pour agrégation jour/heure côté service. */
    @Query("SELECT v.sessionId, v.visitedAt FROM PageVisit v " +
            "WHERE v.type = :type AND v.visitedAt >= :start")
    List<Object[]> findSessionAndDateSince(@Param("type") String type,
                                           @Param("start") LocalDateTime start);

    /** Top chemins (path, nombre) sur une période. */
    @Query("SELECT v.path, COUNT(v) FROM PageVisit v " +
            "WHERE v.type = :type AND v.visitedAt >= :start " +
            "GROUP BY v.path ORDER BY COUNT(v) DESC")
    List<Object[]> topPathsSince(@Param("type") String type,
                                 @Param("start") LocalDateTime start);

    /** Top libellés de liens cliqués sur une période. */
    @Query("SELECT v.label, COUNT(v) FROM PageVisit v " +
            "WHERE v.type = :type AND v.label IS NOT NULL AND v.visitedAt >= :start " +
            "GROUP BY v.label ORDER BY COUNT(v) DESC")
    List<Object[]> topLabelsSince(@Param("type") String type,
                                  @Param("start") LocalDateTime start);
}
