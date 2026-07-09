package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.BadRequestException;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.StructureRequest;
import bf.gov.mtdpce.dto.response.StructureResponse;
import bf.gov.mtdpce.entity.*;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.MinistereRepository;
import bf.gov.mtdpce.repository.StructureRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StructureService {

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private MinistereRepository ministereRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<StructureResponse> getAll(Pageable pageable) {
        return structureRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public StructureResponse getById(UUID id) {
        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Structure non trouvée"));

        return convertToResponse(structure);
    }

    public StructureResponse create(StructureRequest dto,UUID authorId) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministere", "id", dto.getMinistereId()));

        if (dto.getAcronym() != null &&
                structureRepository.existsByAcronym(dto.getAcronym())) {
            throw new BadRequestException("Une structure avec cet acronyme existe déjà.");
        }

        Structure structure = Structure.builder()
                  .email(dto.getEmail())
                  .phone(dto.getPhone())
                  .name(dto.getName())
                  .structureType(dto.getStructureType() != null ? dto.getStructureType() : StructureType.SERVICE)
                  .title(dto.getTitle())
                  .ministere(ministere)
                  .photo(dto.getPhoto())
                  .acronym(dto.getAcronym())
                  .niveau(dto.getNiveau())
                  .description(dto.getDescription())
                  .parentId(dto.getParentId())
                  .build();

        return convertToResponse(structureRepository.save(structure));
    }

    public StructureResponse update(UUID id, StructureRequest dto) {

        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Structure non trouvée"));

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministere", "id", dto.getMinistereId()));

        structure.setMinistere(ministere);

        if (dto.getAcronym() != null &&
                !dto.getAcronym().equals(structure.getAcronym()) &&
                structureRepository.existsByAcronym(dto.getAcronym())) {

            throw new BadRequestException("Une structure avec cet acronyme existe déjà.");
        }

        if (dto.getEmail() != null) structure.setEmail(dto.getEmail());
        if (dto.getPhone() != null) structure.setPhone(dto.getPhone());
        if (dto.getPhone() != null) structure.setPhone(dto.getPhone());
        if (dto.getStructureType() != null) structure.setStructureType(dto.getStructureType());
        if (dto.getTitle() != null) structure.setTitle(dto.getTitle());
        if (dto.getPhone() != null) structure.setPhone(dto.getPhone());
        if (dto.getName() != null) structure.setName(dto.getName());
        if (dto.getAcronym()!= null) structure.setAcronym(dto.getAcronym());
        if (dto.getNiveau() != null) structure.setNiveau(dto.getNiveau());
        if (dto.getDescription() != null) structure.setDescription(dto.getDescription());

        // Parent : réglé de façon inconditionnelle (permet aussi de repasser en racine),
        // avec garde contre l'auto-référence.
        if (dto.getParentId() != null && dto.getParentId().equals(id)) {
            throw new BadRequestException("Une structure ne peut pas être sa propre parente.");
        }
        structure.setParentId(dto.getParentId());

        return convertToResponse(structureRepository.save(structure));
    }

    public void delete(UUID id) {

        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Structure non trouvée"));

        structureRepository.delete(structure);
    }

    private StructureResponse convertToResponse(Structure structure) {

        StructureResponse dto = new StructureResponse();
        dto.setId(structure.getId());
        dto.setTitle(structure.getTitle());
        dto.setName(structure.getName());
        dto.setMinistereId(structure.getMinistere().getId());
        dto.setMinistereName(structure.getMinistere().getNomReel());
        dto.setPhone(structure.getPhone());
        dto.setEmail(structure.getEmail());
        dto.setAcronym(structure.getAcronym());
        dto.setNiveau(structure.getNiveau());
        dto.setDescription(structure.getDescription());
        dto.setParentId(structure.getParentId());
        dto.setPhoto(structure.getPhoto());
        dto.setStructureType(structure.getStructureType());
        dto.setCreatedAt(structure.getCreatedAt());
        dto.setUpdatedAt(structure.getUpdatedAt());

        return dto;
    }
}

