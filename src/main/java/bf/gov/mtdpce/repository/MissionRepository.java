package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, UUID> {
    boolean existsByCategorie(String categorie);
    Page<Mission> findByMinistereId(UUID ministereId, Pageable pageable);
}
