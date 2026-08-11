package bf.gov.matm.service;

import bf.gov.matm.dto.TypeDTO;
import bf.gov.matm.exception.BadRequestException;
import bf.gov.matm.exception.ResourceNotFoundException;
import bf.gov.matm.repository.TypeRepository;
import bf.gov.matm.entity.Type;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public TypeDTO create(TypeDTO dto) {

        if (typeRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ce type existe déjà");
        }

        Type type = Type.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return mapToDTO(typeRepository.save(type));
    }

    public TypeDTO update(Long id, TypeDTO dto) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        if (!type.getName().equals(dto.getName())
                && typeRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ce type existe déjà");
        }

        type.setName(dto.getName());
        type.setDescription(dto.getDescription());

        return mapToDTO(typeRepository.save(type));
    }

    public TypeDTO getById(Long id) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        return mapToDTO(type);
    }

    public List<TypeDTO> getAll() {
        return typeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public void delete(Long id) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        typeRepository.delete(type);
    }

    private TypeDTO mapToDTO(Type type) {
        return TypeDTO.builder()
                .id(type.getId())
                .name(type.getName())
                .description(type.getDescription())
                .createdAt(type.getCreatedAt())
                .build();
    }
}
