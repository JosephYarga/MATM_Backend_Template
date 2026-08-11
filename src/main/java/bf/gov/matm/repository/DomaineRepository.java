package bf.gov.matm.repository;

import bf.gov.matm.entity.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineRepository extends JpaRepository <Domaine, Long> {
    boolean existsByNom(String nom);
}
