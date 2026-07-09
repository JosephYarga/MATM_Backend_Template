package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, UUID> {

    Optional<ArticleCategory> findByCode(String code);

    boolean existsByCode(String code);

    List<ArticleCategory> findAllByOrderByDisplayOrderAscLabelAsc();
}
