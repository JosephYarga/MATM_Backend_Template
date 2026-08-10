package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.StatistiquePublicDTO;
import bf.gov.mtdpce.entity.StatistiquePublic;
import bf.gov.mtdpce.repository.StatistiquePublicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatistiquePublicService {

    @Autowired
    private StatistiquePublicRepository statistiqueRepository;

    public Page<StatistiquePublicDTO> getAll(Pageable pageable) {
        return statistiqueRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public StatistiquePublicDTO getById(Long id) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistique non trouvée"));

        return convertToDTO(statistique);
    }

    public StatistiquePublicDTO create(StatistiquePublicDTO dto) {

        if (statistiqueRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Une statistique avec ce nom existe déjà.");
        }

        StatistiquePublic statistique = new StatistiquePublic();
        statistique.setNom(dto.getNom());
        statistique.setValeur(dto.getValeur());

        return convertToDTO(statistiqueRepository.save(statistique));
    }

    public StatistiquePublicDTO update(Long id, StatistiquePublicDTO dto) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistique non trouvée"));

        if (!statistique.getNom().equals(dto.getNom())
                && statistiqueRepository.existsByNom(dto.getNom())) {

            throw new RuntimeException("Une statistique avec ce nom existe déjà.");
        }

        statistique.setNom(dto.getNom());
        statistique.setValeur(dto.getValeur());

        return convertToDTO(statistiqueRepository.save(statistique));
    }

    public void delete(Long id) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistique non trouvée"));

        statistiqueRepository.delete(statistique);
    }

    private StatistiquePublicDTO convertToDTO(StatistiquePublic statistique) {

        StatistiquePublicDTO dto = new StatistiquePublicDTO();
        dto.setId(statistique.getId());
        dto.setNom(statistique.getNom());
        dto.setValeur(statistique.getValeur());

        return dto;
    }
}
