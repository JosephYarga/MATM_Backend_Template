package bf.gov.matm.repository;

import bf.gov.matm.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {
    Optional<Theme> findByTitle(String title);
    Optional<Theme> findTopByOrderByCreatedAtDesc();
}
