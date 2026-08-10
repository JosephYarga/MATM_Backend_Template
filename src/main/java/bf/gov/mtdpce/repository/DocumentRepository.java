package bf.gov.mtdpce.repository;

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
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findByCategory(DocumentCategory category, Pageable pageable);

    Page<Document> findByIsPublicTrue(Pageable pageable);

    List<Document> findTop10ByIsPublicTrueOrderByCreatedAtDesc();

    @Query("SELECT d FROM Document d WHERE d.isPublic = true AND " +
            "(LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Document> searchPublicDocuments(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.isPublic = true")
    Long countPublicDocuments();
}
