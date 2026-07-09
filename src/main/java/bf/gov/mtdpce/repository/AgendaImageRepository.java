package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.AgendaImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaImageRepository extends JpaRepository<AgendaImage, UUID> {

    List<AgendaImage> findByAgendaIdOrderByDisplayOrderAsc(UUID agendaId);

    void deleteByAgendaId(UUID agendaId);
}
