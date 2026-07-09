package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.FacebookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacebookImageRepository extends JpaRepository<FacebookImage, UUID> {
}
