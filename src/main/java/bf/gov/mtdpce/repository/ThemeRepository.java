package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, String> {
    Optional<Theme> findByTitle(String title);
    Optional<Theme> findTopByOrderByCreatedAtDesc();
}
