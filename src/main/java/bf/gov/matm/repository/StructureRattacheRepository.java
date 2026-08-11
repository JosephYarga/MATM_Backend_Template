package bf.gov.matm.repository;

import bf.gov.matm.entity.StructureRattache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRattacheRepository extends JpaRepository<StructureRattache, Long> {
    boolean existsByAcronym(String acronym);
}
