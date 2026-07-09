package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.Document;
import bf.gov.mtdpce.entity.DocumentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    Page<Document> findByCategory(DocumentCategory category, Pageable pageable);

    Page<Document> findByIsPublicTrue(Pageable pageable);

    /** Documents publics d'un type (Règlementation / Stratégie), filtrés par catégorie et/ou recherche. */
    @Query("SELECT d FROM Document d WHERE d.isPublic = true AND d.typeDocument = :typeDocument " +
            "AND (:category IS NULL OR d.category = :category) " +
            "AND (:query = '' OR (LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :query, '%'))))")
    Page<Document> browsePublic(@Param("typeDocument") String typeDocument,
                                @Param("category") DocumentCategory category,
                                @Param("query") String query,
                                Pageable pageable);

    /** Compteurs par catégorie (facettes) pour un type public, en tenant compte de la recherche. */
    @Query("SELECT d.category, COUNT(d) FROM Document d WHERE d.isPublic = true AND d.typeDocument = :typeDocument " +
            "AND (:query IS NULL OR (LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :query, '%')))) GROUP BY d.category")
    List<Object[]> countByCategoryForType(@Param("typeDocument") String typeDocument, @Param("query") String query);

    List<Document> findTop10ByIsPublicTrueOrderByCreatedAtDesc();

    @Query("SELECT d FROM Document d WHERE d.isPublic = true AND " +
            "(LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Document> searchPublicDocuments(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.isPublic = true")
    Long countPublicDocuments();
}
