package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.StructureRattache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRattacheRepository extends JpaRepository<StructureRattache, Long> {
    boolean existsByAcronym(String acronym);
}
