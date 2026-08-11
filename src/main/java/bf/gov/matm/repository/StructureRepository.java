package bf.gov.matm.repository;

import bf.gov.matm.entity.Structure;
import bf.gov.matm.entity.StructureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {

    boolean existsByAcronym(String acronym);
    Page<Structure> findByStructureType(StructureType structureType, Pageable pageable);
}
