package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.Article;
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
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    Page<Article> findByCategory_Code(String code, Pageable pageable);

    Page<Article> findByStatusAndCategory_Code(ArticleStatus status, String code, Pageable pageable);

    List<Article> findTop5ByStatusOrderByPublishedAtDesc(ArticleStatus status);

    List<Article> findTop5ByStatusOrderByViewCountDesc(ArticleStatus status);

    List<Article> findByFeaturedTrueAndStatusOrderByPublishedAtDesc(ArticleStatus status);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.status = :status AND a.category.code = :code")
    Long countByStatusAndCategoryCode(@Param("status") ArticleStatus status, @Param("code") String code);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.category.code = :code")
    Long countByCategoryCode(@Param("code") String code);

    @Query("SELECT a FROM Article a WHERE a.status = :status AND " +
            "(LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.summary) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Article> searchPublishedArticles(@Param("search") String search, @Param("status") ArticleStatus status, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.status = :status")
    Long countByStatus(@Param("status") ArticleStatus status);

    /** Nombre de contenus publiés depuis une date (cadence de publication DCRP). */
    Long countByStatusAndPublishedAtAfter(ArticleStatus status, java.time.LocalDateTime date);

    Page<Article> findByAuthorId(UUID authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"images", "author"})
    Optional<Article> findWithImagesById(UUID id);

    @EntityGraph(attributePaths = {"imagesFacebook", "author"})
    Optional<Article> findWithFacebookImagesById(UUID id);
}
