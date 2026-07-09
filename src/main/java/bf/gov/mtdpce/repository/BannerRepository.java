package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BannerRepository extends JpaRepository<Banner, UUID> {

    /** Bannières actives, ordonnées pour le défilement (uniquement celles qui défilent). */
    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
}
