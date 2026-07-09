package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.TypeRequest;
import bf.gov.mtdpce.dto.response.TypeResponse;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.TypeRepository;
import bf.gov.mtdpce.entity.Type;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public TypeResponse create(TypeRequest dto) {

        if (typeRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ce type existe déjà");
        }

        Type type = Type.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        return mapToResponse(typeRepository.save(type));
    }

    public TypeResponse update(UUID id, TypeRequest dto) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        if (!type.getName().equals(dto.getName())
                && typeRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Ce type existe déjà");
        }

        type.setName(dto.getName());
        type.setDescription(dto.getDescription());

        return mapToResponse(typeRepository.save(type));
    }

    public TypeResponse getById(UUID id) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        return mapToResponse(type);
    }

    public List<TypeResponse> getAll() {
        return typeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(UUID id) {

        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type", "id", id));

        typeRepository.delete(type);
    }

    private TypeResponse mapToResponse(Type type) {
        return TypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .description(type.getDescription())
                .createdAt(type.getCreatedAt())
                .build();
    }
}
