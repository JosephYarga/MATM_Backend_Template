package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.BannerRequest;
import bf.gov.mtdpce.dto.response.BannerResponse;
import bf.gov.mtdpce.entity.Banner;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    /** Bannières actives à faire défiler sur la page d'accueil. */
    public List<BannerResponse> getActiveBanners() {
        return bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /** Toutes les bannières (administration). */
    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BannerResponse getBannerById(UUID id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bannière non trouvée avec l'id: " + id));
        return mapToResponse(banner);
    }

    public BannerResponse createBanner(BannerRequest request) {
        Banner banner = new Banner();
        mapRequestToEntity(request, banner);
        return mapToResponse(bannerRepository.save(banner));
    }

    public BannerResponse updateBanner(UUID id, BannerRequest request) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bannière non trouvée avec l'id: " + id));
        mapRequestToEntity(request, banner);
        return mapToResponse(bannerRepository.save(banner));
    }

    public void deleteBanner(UUID id) {
        if (!bannerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bannière non trouvée avec l'id: " + id);
        }
        bannerRepository.deleteById(id);
    }

    /** Réordonne les bannières selon la liste d'ids fournie (index = nouvel ordre). */
    public void reorder(List<UUID> orderedIds) {
        if (orderedIds == null) return;
        int order = 1;
        for (UUID id : orderedIds) {
            Banner banner = bannerRepository.findById(id).orElse(null);
            if (banner != null) {
                banner.setDisplayOrder(order);
                bannerRepository.save(banner);
            }
            order++;
        }
    }

    private void mapRequestToEntity(BannerRequest request, Banner banner) {
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        // L'image n'est mise à jour que si une nouvelle est fournie (upload ou chemin).
        if (request.getImage() != null && !request.getImage().isBlank()) {
            banner.setImage(request.getImage());
        }
        banner.setLinkUrl(request.getLinkUrl());
        banner.setLinkText(request.getLinkText());
        banner.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        banner.setDisplayDuration(request.getDisplayDuration() != null ? request.getDisplayDuration() : 5);
        banner.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
    }

    private BannerResponse mapToResponse(Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setTitle(banner.getTitle());
        response.setDescription(banner.getDescription());
        response.setImage(banner.getImage());
        response.setLinkUrl(banner.getLinkUrl());
        response.setLinkText(banner.getLinkText());
        response.setDisplayOrder(banner.getDisplayOrder());
        response.setDisplayDuration(banner.getDisplayDuration());
        response.setIsActive(banner.getIsActive());
        response.setCreatedAt(banner.getCreatedAt());
        response.setUpdatedAt(banner.getUpdatedAt());
        return response;
    }
}
