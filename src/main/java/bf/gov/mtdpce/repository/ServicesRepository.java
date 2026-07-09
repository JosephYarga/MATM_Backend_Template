package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {
    boolean existsByName(String name);
}
