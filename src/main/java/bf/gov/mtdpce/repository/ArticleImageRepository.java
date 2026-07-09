package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, UUID> {
}
