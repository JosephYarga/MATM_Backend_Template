package bf.gov.matm.repository;


import bf.gov.matm.entity.Ministere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MinistereRepository extends JpaRepository <Ministere, Long> {
    Optional<Ministere> findByAcronyme(String acronyme);

    boolean existsByAcronyme(String acronyme);
}
