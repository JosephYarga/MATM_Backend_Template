package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import java.util.UUID;

import bf.gov.mtdpce.repository.MinistreRepository;
import bf.gov.mtdpce.dto.request.MinistreRequest;
import bf.gov.mtdpce.dto.response.MinistreResponse;
import bf.gov.mtdpce.entity.Ministere;
import bf.gov.mtdpce.entity.Ministre;
import bf.gov.mtdpce.repository.MinistereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MinistreService {

    @Autowired
    private MinistreRepository ministreRepository;

    @Autowired
    private MinistereRepository ministereRepository;

    public Page<MinistreResponse> getAll(Pageable pageable) {
        return ministreRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Page<MinistreResponse> getByMinistere(UUID ministereId, Pageable pageable) {
        return ministreRepository.findByMinistereId(ministereId, pageable)
                .map(this::convertToResponse);
    }

    /** Anciens ministres (non en fonction), paginés côté serveur. */
    public Page<MinistreResponse> getFormerMinisters(Pageable pageable) {
        return ministreRepository.findFormerMinisters(pageable)
                .map(this::convertToResponse);
    }

    public MinistreResponse getById(UUID id) {
        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministre non trouvé"));

        return convertToResponse(ministre);
    }

    public MinistreResponse create(MinistreRequest request) {

        Ministere ministere = ministereRepository.findById(request.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministère non trouvé"));

        if (Boolean.TRUE.equals(request.getIsActif())) {
            desactiverAutresMinistres(null);
        }

        Ministre ministre = new Ministre();
        ministre.setNom(request.getNom());
        ministre.setPrenom(request.getPrenom());
        ministre.setProfession(request.getProfession());
        ministre.setBiographie(request.getBiographie());
        ministre.setContent(request.getContent());
        ministre.setPhoto(request.getPhoto());
        ministre.setIsActif(request.getIsActif());
        ministre.setMinistere(ministere);
        ministre.setDateDebut(request.getDateDebut());
        ministre.setDateFin(request.getDateFin());

        return convertToResponse(ministreRepository.save(ministre));
    }

    public MinistreResponse update(UUID id, MinistreRequest request) {

        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministre non trouvé"));

        Ministere ministere = ministereRepository.findById(request.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministère non trouvé"));

        if (Boolean.TRUE.equals(request.getIsActif())) {
            desactiverAutresMinistres(id);
        }

        ministre.setNom(request.getNom());
        ministre.setPrenom(request.getPrenom());
        ministre.setProfession(request.getProfession());
        ministre.setBiographie(request.getBiographie());
        ministre.setContent(request.getContent());
        ministre.setPhoto(request.getPhoto());
        ministre.setDateDebut(request.getDateDebut());
        ministre.setDateFin(request.getDateFin());
        ministre.setIsActif(request.getIsActif());
        ministre.setMinistere(ministere);

        return convertToResponse(ministreRepository.save(ministre));
    }

    public void delete(UUID id) {

        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministre non trouvé"));

        ministreRepository.delete(ministre);
    }

    /**
     * Garantit qu'un seul ministre est actif : désactive tous les ministres actifs,
     * en conservant éventuellement celui dont l'id est passé (le ministre en cours d'édition).
     */
    private void desactiverAutresMinistres(UUID exceptId) {
        for (Ministre ministre : ministreRepository.findAllByIsActifTrue()) {
            if (exceptId == null || !ministre.getId().equals(exceptId)) {
                ministre.setIsActif(false);
                ministreRepository.save(ministre);
            }
        }
    }

    private MinistreResponse convertToResponse(Ministre ministre) {

        MinistreResponse response = new MinistreResponse();
        response.setId(ministre.getId());
        response.setNom(ministre.getNom());
        response.setPrenom(ministre.getPrenom());
        response.setProfession(ministre.getProfession());
        response.setBiographie(ministre.getBiographie());
        response.setContent(ministre.getContent());
        response.setPhoto(ministre.getPhoto());
        response.setIsActif(ministre.getIsActif());
        response.setMinistereId(ministre.getMinistere().getId());
        response.setDateDebut(ministre.getDateDebut());
        response.setDateFin(ministre.getDateFin());
        response.setCreatedAt(ministre.getCreatedAt());
        response.setUpdatedAt(ministre.getUpdatedAt());

        return response;
    }
}
