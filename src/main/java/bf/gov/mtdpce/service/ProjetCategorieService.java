package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.ProjetCategorieDTO;
import bf.gov.mtdpce.entity.ProjetCategorie;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ProjetCategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetCategorieService {

    private final ProjetCategorieRepository projetCategorieRepository;

    public ProjetCategorieService(ProjetCategorieRepository projetCategorieRepository) {
        this.projetCategorieRepository = projetCategorieRepository;
    }


    public ProjetCategorieDTO create(ProjetCategorieDTO dto) {

        if (projetCategorieRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Cette catégorie existe déjà");
        }

        ProjetCategorie projetCategorie = ProjetCategorie.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return mapToDTO(projetCategorieRepository.save(projetCategorie));
    }

    public ProjetCategorieDTO update(Long id, ProjetCategorieDTO dto) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        if (!projetCategorie.getName().equals(dto.getName())
                && projetCategorieRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Cette catégorie existe déjà");
        }

        projetCategorie.setName(dto.getName());
        projetCategorie.setDescription(dto.getDescription());

        return mapToDTO(projetCategorieRepository.save(projetCategorie));
    }

    public ProjetCategorieDTO getById(Long id) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        return mapToDTO(projetCategorie);
    }

    public List<ProjetCategorieDTO> getAll() {
        return projetCategorieRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public void delete(Long id) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        projetCategorieRepository.delete(projetCategorie);
    }

    private ProjetCategorieDTO mapToDTO(ProjetCategorie projetCategorie) {
        return ProjetCategorieDTO.builder()
                .id(projetCategorie.getId())
                .name(projetCategorie.getName())
                .description(projetCategorie.getDescription())
                .createdAt(projetCategorie.getCreatedAt())
                .build();
    }
}
