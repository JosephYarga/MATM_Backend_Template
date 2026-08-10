package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Ministre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MinistreRepository extends JpaRepository<Ministre, Long> {

    Page<Ministre> findByMinistereId(Long ministereId, Pageable pageable);
    Optional<Ministre> findByIsActifTrue();
    boolean existsByIsActifTrue();
}
