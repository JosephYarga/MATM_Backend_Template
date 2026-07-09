package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Conseil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConseilRepository extends JpaRepository<Conseil, UUID> {

    /** Conseils actifs, ordonnés pour le défilement (uniquement ceux qui s'affichent). */
    List<Conseil> findByIsActiveTrueOrderByDisplayOrderAsc();
}
