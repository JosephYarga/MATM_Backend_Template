package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Structure;
import bf.gov.mtdpce.entity.StructureType;
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
