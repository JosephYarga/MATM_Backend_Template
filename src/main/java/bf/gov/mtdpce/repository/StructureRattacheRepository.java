package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.StructureRattache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRattacheRepository extends JpaRepository<StructureRattache, UUID> {
    boolean existsByAcronym(String acronym);
}
