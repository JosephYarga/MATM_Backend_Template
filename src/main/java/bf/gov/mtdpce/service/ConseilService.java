package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.ConseilRequest;
import bf.gov.mtdpce.dto.response.ConseilResponse;
import bf.gov.mtdpce.entity.Conseil;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ConseilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConseilService {

    @Autowired
    private ConseilRepository conseilRepository;

    /** Conseils actifs à faire défiler sur la page d'accueil. */
    public List<ConseilResponse> getActiveConseils() {
        return conseilRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** Tous les conseils (administration). */
    public List<ConseilResponse> getAllConseils() {
        return conseilRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ConseilResponse getConseilById(UUID id) {
        Conseil conseil = conseilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conseil non trouvé avec l'id: " + id));
        return mapToResponse(conseil);
    }

    public ConseilResponse createConseil(ConseilRequest request) {
        Conseil conseil = new Conseil();
        mapRequestToEntity(request, conseil);
        return mapToResponse(conseilRepository.save(conseil));
    }

    public ConseilResponse updateConseil(UUID id, ConseilRequest request) {
        Conseil conseil = conseilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conseil non trouvé avec l'id: " + id));
        mapRequestToEntity(request, conseil);
        return mapToResponse(conseilRepository.save(conseil));
    }

    public void deleteConseil(UUID id) {
        if (!conseilRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conseil non trouvé avec l'id: " + id);
        }
        conseilRepository.deleteById(id);
    }

    /** Réordonne les conseils selon la liste d'ids fournie (index = nouvel ordre). */
    public void reorder(List<UUID> orderedIds) {
        if (orderedIds == null) return;
        int order = 1;
        for (UUID id : orderedIds) {
            Conseil conseil = conseilRepository.findById(id).orElse(null);
            if (conseil != null) {
                conseil.setDisplayOrder(order);
                conseilRepository.save(conseil);
            }
            order++;
        }
    }

    private void mapRequestToEntity(ConseilRequest request, Conseil conseil) {
        conseil.setTitle(request.getTitle());
        conseil.setDescription(request.getDescription());
        // L'image n'est mise à jour que si une nouvelle est fournie (upload ou chemin).
        if (request.getImage() != null && !request.getImage().isBlank()) {
            conseil.setImage(request.getImage());
        }
        conseil.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        conseil.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }

    private ConseilResponse mapToResponse(Conseil conseil) {
        ConseilResponse response = new ConseilResponse();
        response.setId(conseil.getId());
        response.setTitle(conseil.getTitle());
        response.setDescription(conseil.getDescription());
        response.setImage(conseil.getImage());
        response.setDisplayOrder(conseil.getDisplayOrder());
        response.setIsActive(conseil.getIsActive());
        response.setCreatedAt(conseil.getCreatedAt());
        response.setUpdatedAt(conseil.getUpdatedAt());
        return response;
    }
}
