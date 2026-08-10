package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Media;
import bf.gov.mtdpce.entity.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    Page<Media> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Media> findByMediaTypeAndIsPublicTrueOrderByCreatedAtDesc(MediaType mediaType, Pageable pageable);
    
    Page<Media> findByAlbumIdAndIsPublicTrueOrderByDisplayOrderAsc(Long albumId, Pageable pageable);
    
    List<Media> findByCategoryAndIsPublicTrueOrderByDisplayOrderAsc(String category);
    
    @Query("SELECT m FROM Media m WHERE m.isPublic = true AND " +
           "(LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Media> searchMedia(String query, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Media m SET m.viewCount = m.viewCount + 1 WHERE m.id = :id")
    void incrementViewCount(Long id);
    
    @Modifying
    @Query("UPDATE Media m SET m.downloadCount = m.downloadCount + 1 WHERE m.id = :id")
    void incrementDownloadCount(Long id);
    
    long countByMediaType(MediaType mediaType);
}
