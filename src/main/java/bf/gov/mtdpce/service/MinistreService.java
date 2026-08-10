package bf.gov.mtdpce.service;

import bf.gov.mtdpce.repository.MinistreRepository;
import bf.gov.mtdpce.dto.MinistreDTO;
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

    public Page<MinistreDTO> getAll(Pageable pageable) {
        return ministreRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<MinistreDTO> getByMinistere(Long ministereId, Pageable pageable) {
        return ministreRepository.findByMinistereId(ministereId, pageable)
                .map(this::convertToDTO);
    }

    public MinistreDTO getById(Long id) {
        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministre non trouvé"));

        return convertToDTO(ministre);
    }

    public MinistreDTO create(MinistreDTO dto) {

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new RuntimeException("Ministère non trouvé"));

        if (Boolean.TRUE.equals(dto.getIsActif())) {
            desactiverMinistreActuel();
        }

        Ministre ministre = new Ministre();
        ministre.setNom(dto.getNom());
        ministre.setPrenom(dto.getPrenom());
        ministre.setProfession(dto.getProfession());
        ministre.setBiographie(dto.getBiographie());
        ministre.setContent(dto.getContent());
        ministre.setPhoto(dto.getPhoto());
        ministre.setIsActif(dto.getIsActif());
        ministre.setMinistere(ministere);
        ministre.setDateDebut(dto.getDateDebut());
        ministre.setDateFin(dto.getDateFin());

        return convertToDTO(ministreRepository.save(ministre));
    }

    public MinistreDTO update(Long id, MinistreDTO dto) {

        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministre non trouvé"));

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new RuntimeException("Ministère non trouvé"));

        if (Boolean.TRUE.equals(dto.getIsActif())) {
            desactiverMinistreActuel();
        }

        ministre.setNom(dto.getNom());
        ministre.setPrenom(dto.getPrenom());
        ministre.setProfession(dto.getProfession());
        ministre.setBiographie(dto.getBiographie());
        ministre.setContent(dto.getContent());
        ministre.setPhoto(dto.getPhoto());
        ministre.setDateDebut(dto.getDateDebut());
        ministre.setDateFin(dto.getDateFin());
        ministre.setIsActif(dto.getIsActif());
        ministre.setMinistere(ministere);

        return convertToDTO(ministreRepository.save(ministre));
    }

    public void delete(Long id) {

        Ministre ministre = ministreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministre non trouvé"));

        ministreRepository.delete(ministre);
    }

    private void desactiverMinistreActuel() {
        ministreRepository.findByIsActifTrue().ifPresent(ministre -> {
            ministre.setIsActif(false);
            ministreRepository.save(ministre);
        });
    }

    private MinistreDTO convertToDTO(Ministre ministre) {

        MinistreDTO dto = new MinistreDTO();
        dto.setId(ministre.getId());
        dto.setNom(ministre.getNom());
        dto.setPrenom(ministre.getPrenom());
        dto.setProfession(ministre.getProfession());
        dto.setBiographie(ministre.getBiographie());
        dto.setContent(ministre.getContent());
        dto.setPhoto(ministre.getPhoto());
        dto.setIsActif(ministre.getIsActif());
        dto.setMinistereId(ministre.getMinistere().getId());
        dto.setDateDebut(ministre.getDateDebut());
        dto.setDateFin(ministre.getDateFin());
        dto.setCreatedAt(ministre.getCreatedAt());
        dto.setUpdatedAt(ministre.getUpdatedAt());

        return dto;
    }
}