package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.MissionDTO;
import bf.gov.mtdpce.entity.Ministere;
import bf.gov.mtdpce.entity.Mission;
import bf.gov.mtdpce.repository.MinistereRepository;
import bf.gov.mtdpce.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MinistereRepository ministereRepository;

    public Page<MissionDTO> getAll(Pageable pageable) {
        return missionRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<MissionDTO> getByMinistere(Long ministereId, Pageable pageable) {
        return missionRepository.findByMinistereId(ministereId, pageable)
                .map(this::convertToDTO);
    }

    public MissionDTO getById(Long id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée"));

        return convertToDTO(mission);
    }

    public MissionDTO create(MissionDTO dto) {

        if (missionRepository.existsByCategorie(dto.getCategorie())) {
            throw new RuntimeException("Une mission avec cette catégorie existe déjà.");
        }

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new RuntimeException("Ministère non trouvé"));

        Mission mission = new Mission();
        mission.setCategorie(dto.getCategorie());
        mission.setDescription(dto.getDescription());
        mission.setMinistere(ministere);

        Mission saved = missionRepository.save(mission);

        return convertToDTO(saved);
    }

    public MissionDTO update(Long id, MissionDTO dto) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée"));

        if (!mission.getCategorie().equals(dto.getCategorie())
                && missionRepository.existsByCategorie(dto.getCategorie())) {

            throw new RuntimeException("Une mission avec cette catégorie existe déjà.");
        }

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new RuntimeException("Ministère non trouvé"));

        mission.setCategorie(dto.getCategorie());
        mission.setDescription(dto.getDescription());
        mission.setMinistere(ministere);

        Mission updated = missionRepository.save(mission);

        return convertToDTO(updated);
    }

    public void delete(Long id) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée"));

        missionRepository.delete(mission);
    }

    private MissionDTO convertToDTO(Mission mission) {

        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setCategorie(mission.getCategorie());
        dto.setDescription(mission.getDescription());
        dto.setMinistereId(mission.getMinistere().getId());

        return dto;
    }
}