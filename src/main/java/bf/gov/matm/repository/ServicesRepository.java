package bf.gov.matm.repository;

import bf.gov.matm.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    boolean existsByName(String name);
}
