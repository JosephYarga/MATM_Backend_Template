package bf.gov.mtdpce.repository;
import java.util.UUID;


import bf.gov.mtdpce.entity.Ministere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MinistereRepository extends JpaRepository <Ministere, UUID> {
    Optional<Ministere> findByAcronyme(String acronyme);

    boolean existsByAcronyme(String acronyme);
}
