package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ProjetCategorieRequest;
import bf.gov.mtdpce.dto.response.ProjetCategorieResponse;
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


    public ProjetCategorieResponse create(ProjetCategorieRequest dto) {

        if (projetCategorieRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Cette catégorie existe déjà");
        }

        ProjetCategorie projetCategorie = ProjetCategorie.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return mapToResponse(projetCategorieRepository.save(projetCategorie));
    }

    public ProjetCategorieResponse update(UUID id, ProjetCategorieRequest dto) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        if (!projetCategorie.getName().equals(dto.getName())
                && projetCategorieRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Cette catégorie existe déjà");
        }

        projetCategorie.setName(dto.getName());
        projetCategorie.setDescription(dto.getDescription());

        return mapToResponse(projetCategorieRepository.save(projetCategorie));
    }

    public ProjetCategorieResponse getById(UUID id) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        return mapToResponse(projetCategorie);
    }

    public List<ProjetCategorieResponse> getAll() {
        return projetCategorieRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(UUID id) {

        ProjetCategorie projetCategorie = projetCategorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));

        projetCategorieRepository.delete(projetCategorie);
    }

    private ProjetCategorieResponse mapToResponse(ProjetCategorie projetCategorie) {
        return ProjetCategorieResponse.builder()
                .id(projetCategorie.getId())
                .name(projetCategorie.getName())
                .description(projetCategorie.getDescription())
                .createdAt(projetCategorie.getCreatedAt())
                .build();
    }
}
