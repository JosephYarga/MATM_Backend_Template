package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.StatistiquePublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiquePublicRepository extends JpaRepository<StatistiquePublic, UUID> {

    boolean existsByNom(String nom);
}
