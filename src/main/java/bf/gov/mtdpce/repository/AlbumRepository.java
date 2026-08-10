package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    
    Page<Album> findByIsPublicTrueOrderByDisplayOrderAscCreatedAtDesc(Pageable pageable);
    
    List<Album> findByCategoryAndIsPublicTrueOrderByDisplayOrderAsc(String category);
    
    long countByIsPublicTrue();
}
