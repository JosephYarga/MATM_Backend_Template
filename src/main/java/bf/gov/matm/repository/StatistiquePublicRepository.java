package bf.gov.matm.repository;

import bf.gov.matm.entity.StatistiquePublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiquePublicRepository extends JpaRepository<StatistiquePublic, Long> {

    boolean existsByNom(String nom);
}
