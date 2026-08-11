package bf.gov.matm.repository;

import bf.gov.matm.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    boolean existsByCategorie(String categorie);
    Page<Mission> findByMinistereId(Long ministereId, Pageable pageable);
}
