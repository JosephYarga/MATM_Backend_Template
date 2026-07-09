package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.exception.BadRequestException;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.StatistiquePublicRequest;
import bf.gov.mtdpce.dto.response.StatistiquePublicResponse;
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

    public Page<StatistiquePublicResponse> getAll(Pageable pageable) {
        return statistiqueRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public StatistiquePublicResponse getById(UUID id) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Statistique non trouvée"));

        return convertToResponse(statistique);
    }

    public StatistiquePublicResponse create(StatistiquePublicRequest dto) {

        if (statistiqueRepository.existsByNom(dto.getNom())) {
            throw new BadRequestException("Une statistique avec ce nom existe déjà.");
        }

        StatistiquePublic statistique = new StatistiquePublic();
        statistique.setNom(dto.getNom());
        statistique.setValeur(dto.getValeur());

        return convertToResponse(statistiqueRepository.save(statistique));
    }

    public StatistiquePublicResponse update(UUID id, StatistiquePublicRequest dto) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Statistique non trouvée"));

        if (!statistique.getNom().equals(dto.getNom())
                && statistiqueRepository.existsByNom(dto.getNom())) {

            throw new BadRequestException("Une statistique avec ce nom existe déjà.");
        }

        statistique.setNom(dto.getNom());
        statistique.setValeur(dto.getValeur());

        return convertToResponse(statistiqueRepository.save(statistique));
    }

    public void delete(UUID id) {

        StatistiquePublic statistique = statistiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Statistique non trouvée"));

        statistiqueRepository.delete(statistique);
    }

    private StatistiquePublicResponse convertToResponse(StatistiquePublic statistique) {

        StatistiquePublicResponse dto = new StatistiquePublicResponse();
        dto.setId(statistique.getId());
        dto.setNom(statistique.getNom());
        dto.setValeur(statistique.getValeur());

        return dto;
    }
}
