package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.exception.BadRequestException;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.MissionRequest;
import bf.gov.mtdpce.dto.response.MissionResponse;
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

    public Page<MissionResponse> getAll(Pageable pageable) {
        return missionRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public Page<MissionResponse> getByMinistere(UUID ministereId, Pageable pageable) {
        return missionRepository.findByMinistereId(ministereId, pageable)
                .map(this::convertToResponse);
    }

    public MissionResponse getById(UUID id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission non trouvée"));

        return convertToResponse(mission);
    }

    public MissionResponse create(MissionRequest dto) {

        if (missionRepository.existsByCategorie(dto.getCategorie())) {
            throw new BadRequestException("Une mission avec cette catégorie existe déjà.");
        }

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministère non trouvé"));

        Mission mission = new Mission();
        mission.setCategorie(dto.getCategorie());
        mission.setDescription(dto.getDescription());
        mission.setMinistere(ministere);

        Mission saved = missionRepository.save(mission);

        return convertToResponse(saved);
    }

    public MissionResponse update(UUID id, MissionRequest dto) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission non trouvée"));

        if (!mission.getCategorie().equals(dto.getCategorie())
                && missionRepository.existsByCategorie(dto.getCategorie())) {

            throw new BadRequestException("Une mission avec cette catégorie existe déjà.");
        }

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministère non trouvé"));

        mission.setCategorie(dto.getCategorie());
        mission.setDescription(dto.getDescription());
        mission.setMinistere(ministere);

        Mission updated = missionRepository.save(mission);

        return convertToResponse(updated);
    }

    public void delete(UUID id) {

        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission non trouvée"));

        missionRepository.delete(mission);
    }

    private MissionResponse convertToResponse(Mission mission) {

        MissionResponse dto = new MissionResponse();
        dto.setId(mission.getId());
        dto.setCategorie(mission.getCategorie());
        dto.setDescription(mission.getDescription());
        dto.setMinistereId(mission.getMinistere().getId());

        return dto;
    }
}