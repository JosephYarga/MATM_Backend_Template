package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineRepository extends JpaRepository <Domaine, Long> {
    boolean existsByNom(String nom);
}
