package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Article;
import bf.gov.mtdpce.entity.ArticleCategory;
import bf.gov.mtdpce.entity.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    Page<Article> findByCategory(ArticleCategory category, Pageable pageable);

    Page<Article> findByStatusAndCategory(ArticleStatus status, ArticleCategory category, Pageable pageable);

    List<Article> findTop5ByStatusOrderByPublishedAtDesc(ArticleStatus status);

    List<Article> findByFeaturedTrueAndStatusOrderByPublishedAtDesc(ArticleStatus status);

    @Query("SELECT a FROM Article a WHERE a.status = :status AND " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.summary) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Article> searchPublishedArticles(@Param("search") String search, @Param("status") ArticleStatus status, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.status = :status")
    Long countByStatus(@Param("status") ArticleStatus status);

    Page<Article> findByAuthorId(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"images", "author"})
    Optional<Article> findWithImagesById(Long id);

    @EntityGraph(attributePaths = {"imagesFacebook", "author"})
    Optional<Article> findWithFacebookImagesById(Long id);
}
