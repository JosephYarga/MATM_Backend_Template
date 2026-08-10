package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.ServicesDTO;
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


    public Page<ServicesDTO> getAll(Pageable pageable) {
        return servicesRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public ServicesDTO getById(Long id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé avec id : " + id));

        return convertToDTO(service);
    }

    @Transactional
    public ServicesDTO create(ServicesDTO servicesDTO,Long authorId) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        if (servicesRepository.existsByName(servicesDTO.getName())) {
            throw new RuntimeException("Un service avec ce nom existe déjà");
        }

        Services services = Services.builder()
                .name(servicesDTO.getName())
                .url(servicesDTO.getUrl())
                .description(servicesDTO.getDescription())
                .logo(servicesDTO.getLogo())
                .build();

        return convertToDTO(servicesRepository.save(services));
    }

    @Transactional
    public ServicesDTO update(Long id, ServicesDTO servicesDTO) {

        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé avec id : " + id));

        if(servicesDTO.getName() != null) services.setName(servicesDTO.getName());
        if(servicesDTO.getDescription() != null) services.setDescription(servicesDTO.getDescription());
        if(servicesDTO.getUrl() != null) services.setUrl(servicesDTO.getUrl());
        if(servicesDTO.getLogo()!= null) services.setLogo(servicesDTO.getLogo());

        return convertToDTO(servicesRepository.save(services));
    }

    public void delete(Long id) {
        Services services = servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé avec id : " + id));

        servicesRepository.delete(services);
    }


    private ServicesDTO convertToDTO(Services service) {
        ServicesDTO dto = new ServicesDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setUrl(service.getUrl());
        dto.setLogo(service.getLogo());
        return dto;
    }

    private Services convertToEntity(ServicesDTO dto) {
        Services service = new Services();
        service.setId(dto.getId());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setUrl(dto.getUrl());
        service.setLogo(dto.getLogo());
        return service;
    }
}
