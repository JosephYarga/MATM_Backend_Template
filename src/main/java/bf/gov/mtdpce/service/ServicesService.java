package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.BadRequestException;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ServicesRequest;
import bf.gov.mtdpce.dto.response.ServicesResponse;
import bf.gov.mtdpce.entity.Services;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ServicesRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicesService {

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<ServicesResponse> getAll(Pageable pageable) {
        return servicesRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public ServicesResponse getById(UUID id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec id : " + id));

        return convertToResponse(service);
    }

    @Transactional
    public ServicesResponse create(ServicesRequest servicesDTO,UUID authorId) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        if (servicesRepository.existsByName(servicesDTO.getName())) {
            throw new BadRequestException("Un service avec ce nom existe déjà");
        }

        Services services = Services.builder()
                .name(servicesDTO.getName())
                .url(servicesDTO.getUrl())
                .description(servicesDTO.getDescription())
                .logo(servicesDTO.getLogo())
                .build();

        return convertToResponse(servicesRepository.save(services));
    }

    @Transactional
    public ServicesResponse update(UUID id, ServicesRequest servicesDTO) {

        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec id : " + id));

        if(servicesDTO.getName() != null) services.setName(servicesDTO.getName());
        if(servicesDTO.getDescription() != null) services.setDescription(servicesDTO.getDescription());
        if(servicesDTO.getUrl() != null) services.setUrl(servicesDTO.getUrl());
        if(servicesDTO.getLogo()!= null) services.setLogo(servicesDTO.getLogo());

        return convertToResponse(servicesRepository.save(services));
    }

    public void delete(UUID id) {
        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec id : " + id));

        servicesRepository.delete(services);
    }


    private ServicesResponse convertToResponse(Services service) {
        ServicesResponse dto = new ServicesResponse();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setUrl(service.getUrl());
        dto.setLogo(service.getLogo());
        return dto;
    }
}
