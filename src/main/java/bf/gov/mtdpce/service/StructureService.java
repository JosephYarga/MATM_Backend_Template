package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.StructureDTO;
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

    public Page<StructureDTO> getAll(Pageable pageable) {
        return structureRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public StructureDTO getById(Long id) {
        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        return convertToDTO(structure);
    }

    public StructureDTO create(StructureDTO dto,Long authorId) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministere", "id", dto.getMinistereId()));

        if (dto.getAcronym() != null &&
                structureRepository.existsByAcronym(dto.getAcronym())) {
            throw new RuntimeException("Une structure avec cet acronyme existe déjà.");
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
                  .createdAt(dto.getCreatedAt())
                  .updatedAt(dto.getUpdatedAt())
                  .build();

        return convertToDTO(structureRepository.save(structure));
    }

    public StructureDTO update(Long id, StructureDTO dto) {

        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministere", "id", dto.getMinistereId()));

        structure.setMinistere(ministere);

        if (dto.getAcronym() != null &&
                !dto.getAcronym().equals(structure.getAcronym()) &&
                structureRepository.existsByAcronym(dto.getAcronym())) {

            throw new RuntimeException("Une structure avec cet acronyme existe déjà.");
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

        return convertToDTO(structureRepository.save(structure));
    }

    public void delete(Long id) {

        Structure structure = structureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure non trouvée"));

        structureRepository.delete(structure);
    }

    private StructureDTO convertToDTO(Structure structure) {

        StructureDTO dto = new StructureDTO();
        dto.setId(structure.getId());
        dto.setTitle(structure.getTitle());
        dto.setName(structure.getName());
        dto.setMinistereId(structure.getMinistere().getId());
        dto.setMinistereName(structure.getMinistere().getNomReel());
        dto.setPhone(structure.getPhone());
        dto.setEmail(structure.getEmail());
        dto.setAcronym(structure.getAcronym());
        dto.setNiveau(structure.getNiveau());
        dto.setPhoto(structure.getPhoto());
        dto.setStructureType(structure.getStructureType());
        dto.setCreatedAt(structure.getCreatedAt());
        dto.setUpdatedAt(structure.getUpdatedAt());

        return dto;
    }
}

