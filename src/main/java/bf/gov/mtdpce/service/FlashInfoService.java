package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.FlashInfoRequest;
import bf.gov.mtdpce.dto.response.FlashInfoResponse;
import bf.gov.mtdpce.dto.response.PaginatedResponse;
import bf.gov.mtdpce.entity.FlashInfo;
import bf.gov.mtdpce.entity.FlashInfoPriority;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.FlashInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlashInfoService {
    
    @Autowired
    private FlashInfoRepository flashInfoRepository;
    
    public List<FlashInfoResponse> getActiveFlashInfos() {
        return flashInfoRepository.findActiveFlashInfos(LocalDateTime.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public PaginatedResponse<FlashInfoResponse> getAllFlashInfos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FlashInfo> flashInfoPage = flashInfoRepository.findByIsActiveTrueOrderByPriorityDescCreatedAtDesc(pageable);
        
        List<FlashInfoResponse> flashInfos = flashInfoPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return new PaginatedResponse<>(flashInfos, page, size, flashInfoPage.getTotalElements());
    }
    
    public FlashInfoResponse getFlashInfoById(Long id) {
        FlashInfo flashInfo = flashInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flash info non trouvé avec l'id: " + id));
        return mapToResponse(flashInfo);
    }
    
    public FlashInfoResponse createFlashInfo(FlashInfoRequest request) {
        FlashInfo flashInfo = new FlashInfo();
        mapRequestToEntity(request, flashInfo);
        FlashInfo saved = flashInfoRepository.save(flashInfo);
        return mapToResponse(saved);
    }
    
    public FlashInfoResponse updateFlashInfo(Long id, FlashInfoRequest request) {
        FlashInfo flashInfo = flashInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flash info non trouvé avec l'id: " + id));
        mapRequestToEntity(request, flashInfo);
        FlashInfo updated = flashInfoRepository.save(flashInfo);
        return mapToResponse(updated);
    }
    
    public void deleteFlashInfo(Long id) {
        if (!flashInfoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flash info non trouvé avec l'id: " + id);
        }
        flashInfoRepository.deleteById(id);
    }
    
    private void mapRequestToEntity(FlashInfoRequest request, FlashInfo flashInfo) {
        flashInfo.setTitle(request.getTitle());
        flashInfo.setContent(request.getContent());
        flashInfo.setLinkUrl(request.getLinkUrl());
        flashInfo.setLinkText(request.getLinkText());
        flashInfo.setPriority(request.getPriority() != null ? 
                FlashInfoPriority.valueOf(request.getPriority()) : FlashInfoPriority.NORMAL);
        flashInfo.setStartDate(request.getStartDate());
        flashInfo.setEndDate(request.getEndDate());
        flashInfo.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }
    
    private FlashInfoResponse mapToResponse(FlashInfo flashInfo) {
        FlashInfoResponse response = new FlashInfoResponse();
        response.setId(flashInfo.getId());
        response.setTitle(flashInfo.getTitle());
        response.setContent(flashInfo.getContent());
        response.setLinkUrl(flashInfo.getLinkUrl());
        response.setLinkText(flashInfo.getLinkText());
        response.setPriority(flashInfo.getPriority().name());
        response.setStartDate(flashInfo.getStartDate());
        response.setEndDate(flashInfo.getEndDate());
        response.setIsActive(flashInfo.getIsActive());
        response.setCreatedAt(flashInfo.getCreatedAt());
        response.setUpdatedAt(flashInfo.getUpdatedAt());
        if (flashInfo.getCreatedBy() != null) {
            response.setCreatedBy(flashInfo.getCreatedBy().getUsername());
        }
        return response;
    }
}
