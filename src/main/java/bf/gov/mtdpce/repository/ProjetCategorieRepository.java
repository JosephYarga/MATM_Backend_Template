package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.ProjetCategorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetCategorieRepository extends JpaRepository<ProjetCategorie, Long> {
    boolean existsByName(String name);
    Optional<ProjetCategorie> findByName(String name);
}
