package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.StructureRattacheDTO;
import bf.gov.mtdpce.entity.Domaine;
import bf.gov.mtdpce.entity.Ministere;
import bf.gov.mtdpce.entity.StructureRattache;
import bf.gov.mtdpce.repository.DomaineRepository;
import bf.gov.mtdpce.repository.MinistereRepository;
import bf.gov.mtdpce.repository.StructureRattacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StructureRattacheService {

    @Autowired
    private StructureRattacheRepository repository;
    @Autowired
    private DomaineRepository domaineRepository;
    @Autowired
    private MinistereRepository ministereRepository;

    public Page<StructureRattacheDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public StructureRattacheDTO getById(Long id) {

        StructureRattache entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure rattachée non trouvée"));

        return mapToDTO(entity);
    }

    public StructureRattacheDTO create(StructureRattacheDTO dto) {

        Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                .orElseThrow(() -> new RuntimeException("Ministere introuvable"));

        Set<Domaine> domaines = new HashSet<>();
        if (dto.getDomaineIds() != null) {
            domaines = new HashSet<>(
                    domaineRepository.findAllById(dto.getDomaineIds())
            );
        }

        StructureRattache structureRattache = new StructureRattache();
        structureRattache.setName(dto.getName());
        structureRattache.setAcronym(dto.getAcronym());
        structureRattache.setType(dto.getType());
        structureRattache.setDescription(dto.getDescription());
        structureRattache.setAddress(dto.getAddress());
        structureRattache.setPhone(dto.getPhone());
        structureRattache.setEmail(dto.getEmail());
        structureRattache.setWebsite(dto.getWebsite());
        structureRattache.setLogourl(dto.getLogourl());
        structureRattache.setMinistere(ministere);
        structureRattache.setDomaines(domaines);

        return mapToDTO(repository.save(structureRattache));
    }

    public StructureRattacheDTO update(Long id, StructureRattacheDTO dto) {

        StructureRattache structureRattache = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure introuvable"));

        if (dto.getMinistereId() != null) {
            Ministere ministere = ministereRepository.findById(dto.getMinistereId())
                    .orElseThrow(() -> new RuntimeException("Ministere introuvable"));
            structureRattache.setMinistere(ministere);
        }

        if (dto.getDomaineIds() != null) {
            Set<Domaine> domaines = new HashSet<>(
                    domaineRepository.findAllById(dto.getDomaineIds())
            );
            structureRattache.setDomaines(domaines);
        }

        structureRattache.setName(dto.getName());
        structureRattache.setAcronym(dto.getAcronym());
        structureRattache.setType(dto.getType());
        structureRattache.setDescription(dto.getDescription());
        structureRattache.setAddress(dto.getAddress());
        structureRattache.setPhone(dto.getPhone());
        structureRattache.setEmail(dto.getEmail());
        structureRattache.setWebsite(dto.getWebsite());

        if (dto.getLogourl() != null) {
            structureRattache.setLogourl(dto.getLogourl());
        }

        return mapToDTO(repository.save(structureRattache));
    }

    public void delete(Long id) {

        StructureRattache entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Structure rattachée non trouvée"));

        repository.delete(entity);
    }

    private StructureRattacheDTO mapToDTO(StructureRattache structureRattache) {

        StructureRattacheDTO dto = new StructureRattacheDTO();

        dto.setId(structureRattache.getId());
        dto.setName(structureRattache.getName());
        dto.setAcronym(structureRattache.getAcronym());
        dto.setType(structureRattache.getType());
        dto.setDescription(structureRattache.getDescription());
        dto.setAddress(structureRattache.getAddress());
        dto.setPhone(structureRattache.getPhone());
        dto.setEmail(structureRattache.getEmail());
        dto.setWebsite(structureRattache.getWebsite());
        dto.setLogourl(structureRattache.getLogourl());
        dto.setMinistereId(structureRattache.getMinistere().getId());

        dto.setDomaineIds(
                structureRattache.getDomaines()
                        .stream()
                        .map(Domaine::getId)
                        .collect(Collectors.toSet())
        );

        return dto;
    }

    private void mapToEntity(StructureRattacheDTO dto, StructureRattache entity) {

        entity.setName(dto.getName());
        entity.setAcronym(dto.getAcronym());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setWebsite(dto.getWebsite());
        entity.setLogourl(dto.getLogourl());

        if (dto.getDomaineIds() != null) {
            Set<Domaine> domaines = dto.getDomaineIds()
                    .stream()
                    .map(id -> domaineRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + id)))
                    .collect(Collectors.toSet());

            entity.setDomaines(domaines);
        }
    }
}
