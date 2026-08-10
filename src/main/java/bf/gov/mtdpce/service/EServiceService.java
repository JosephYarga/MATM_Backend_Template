package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.ServiceRequest;
import bf.gov.mtdpce.dto.response.ServiceResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.entity.EService;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.EServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EServiceService {
    
    @Autowired
    private EServiceRepository eServiceRepository;
    
    public PaginatedResponse<ServiceResponse> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EService> servicePage = eServiceRepository.findByIsActiveTrueOrderByDisplayOrderAsc(pageable);
        
        List<ServiceResponse> services = servicePage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(services, page, size, servicePage.getTotalElements());
    }
    
    public List<ServiceResponse> getServicesByCategory(String category) {
        return eServiceRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public PaginatedResponse<ServiceResponse> getOnlineServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EService> servicePage = eServiceRepository.findByIsOnlineTrueAndIsActiveTrueOrderByDisplayOrderAsc(pageable);
        
        List<ServiceResponse> services = servicePage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(services, page, size, servicePage.getTotalElements());
    }
    
    public List<String> getAllCategories() {
        return eServiceRepository.findAllCategories();
    }
    
    public ServiceResponse getServiceById(Long id) {
        EService service = eServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + id));
        eServiceRepository.incrementViewCount(id);
        return mapToResponse(service);
    }
    
    public ServiceResponse createService(ServiceRequest request) {
        EService service = new EService();
        mapRequestToEntity(request, service);
        EService saved = eServiceRepository.save(service);
        return mapToResponse(saved);
    }
    
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        EService service = eServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + id));
        mapRequestToEntity(request, service);
        EService updated = eServiceRepository.save(service);
        return mapToResponse(updated);
    }
    
    public void deleteService(Long id) {
        if (!eServiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service non trouvé avec l'id: " + id);
        }
        eServiceRepository.deleteById(id);
    }
    
    public PaginatedResponse<ServiceResponse> searchServices(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EService> servicePage = eServiceRepository.searchServices(query, pageable);
        
        List<ServiceResponse> services = servicePage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(services, page, size, servicePage.getTotalElements());
    }
    
    private void mapRequestToEntity(ServiceRequest request, EService service) {
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setSubcategory(request.getSubcategory());
        service.setTargetAudience(request.getTargetAudience());
        service.setCost(request.getCost());
        service.setCostDetails(request.getCostDetails());
        service.setProcessingTime(request.getProcessingTime());
        service.setAccessConditions(request.getAccessConditions());
        if (request.getRequiredDocuments() != null) {
            service.setRequiredDocuments(String.join("|||", request.getRequiredDocuments()));
        }
        service.setProcedureSteps(request.getProcedureSteps());
        service.setOnlineUrl(request.getOnlineUrl());
        service.setPhysicalAddress(request.getPhysicalAddress());
        service.setContactEmail(request.getContactEmail());
        service.setContactPhone(request.getContactPhone());
        service.setExampleFileUrl(request.getExampleFileUrl());
        service.setIconName(request.getIconName());
        service.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        service.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        service.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
    }
    
    private ServiceResponse mapToResponse(EService service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setCategory(service.getCategory());
        response.setSubcategory(service.getSubcategory());
        response.setTargetAudience(service.getTargetAudience());
        response.setCost(service.getCost());
        response.setCostDetails(service.getCostDetails());
        response.setProcessingTime(service.getProcessingTime());
        response.setAccessConditions(service.getAccessConditions());
        if (service.getRequiredDocuments() != null && !service.getRequiredDocuments().isEmpty()) {
            response.setRequiredDocuments(Arrays.asList(service.getRequiredDocuments().split("\\|\\|\\|")));
        }
        response.setProcedureSteps(service.getProcedureSteps());
        response.setOnlineUrl(service.getOnlineUrl());
        response.setPhysicalAddress(service.getPhysicalAddress());
        response.setContactEmail(service.getContactEmail());
        response.setContactPhone(service.getContactPhone());
        response.setExampleFileUrl(service.getExampleFileUrl());
        response.setIsOnline(service.getIsOnline());
        response.setIsActive(service.getIsActive());
        response.setDisplayOrder(service.getDisplayOrder());
        response.setViewCount(service.getViewCount());
        response.setRequestCount(service.getRequestCount());
        response.setCreatedAt(service.getCreatedAt());
        response.setUpdatedAt(service.getUpdatedAt());
        return response;
    }
}
