package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.AgendaImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaImageRepository extends JpaRepository<AgendaImage, Long> {

    List<AgendaImage> findByAgendaIdOrderByDisplayOrderAsc(Long agendaId);

    void deleteByAgendaId(Long agendaId);
}
