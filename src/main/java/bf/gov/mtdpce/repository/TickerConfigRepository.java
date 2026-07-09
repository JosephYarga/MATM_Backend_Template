package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.TickerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TickerConfigRepository extends JpaRepository<TickerConfig, UUID> {
}
