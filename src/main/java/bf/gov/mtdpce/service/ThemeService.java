package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.ThemeRequest;
import bf.gov.mtdpce.dto.response.ThemeResponse;
import bf.gov.mtdpce.entity.Theme;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    // Couleurs de la charte par défaut (Burkina Faso) si aucun thème n'est validé.
    private static final String DEFAULT_PRIMARY = "#00843B";
    private static final String DEFAULT_ACCENT = "#FCD116";
    private static final String DEFAULT_SECONDARY = "#EF3340";
    private static final String DEFAULT_TERTIARY = "#B8860B";

    @Autowired
    private ThemeRepository themeRepository;

    /** Thème actif appliqué sur le site (repli sur la charte par défaut si aucun n'est validé). */
    public ThemeResponse getActiveTheme() {
        return themeRepository.findFirstByIsActiveTrueOrderByUpdatedAtDesc()
                .map(this::convertToResponse)
                .orElseGet(this::defaultTheme);
    }

    public List<ThemeResponse> getAll() {
        return themeRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public ThemeResponse getById(String id) {
        return convertToResponse(findOrThrow(id));
    }

    /** Crée un nouveau thème. Le tout premier thème créé devient automatiquement actif. */
    public ThemeResponse create(ThemeRequest dto) {
        boolean firstEver = themeRepository.count() == 0;
        Theme theme = Theme.builder()
                .title(dto.getTitle())
                .primaryColor(dto.getPrimaryColor())
                .accentColor(dto.getAccentColor())
                .secondaryColor(dto.getSecondaryColor())
                .tertiaryColor(dto.getTertiaryColor())
                .isActive(firstEver)
                .build();
        return convertToResponse(themeRepository.save(theme));
    }

    public ThemeResponse update(String id, ThemeRequest dto) {
        Theme theme = findOrThrow(id);
        theme.setTitle(dto.getTitle());
        theme.setPrimaryColor(dto.getPrimaryColor());
        theme.setAccentColor(dto.getAccentColor());
        theme.setSecondaryColor(dto.getSecondaryColor());
        theme.setTertiaryColor(dto.getTertiaryColor());
        return convertToResponse(themeRepository.save(theme));
    }

    /** Valide (active) un thème : il devient le seul actif et s'applique sur tout le site. */
    public ThemeResponse activate(String id) {
        Theme theme = findOrThrow(id);
        // Un seul thème actif à la fois.
        for (Theme other : themeRepository.findAllByIsActiveTrue()) {
            if (!other.getId().equals(id)) {
                other.setIsActive(false);
                themeRepository.save(other);
            }
        }
        theme.setIsActive(true);
        return convertToResponse(themeRepository.save(theme));
    }

    public void delete(String id) {
        Theme theme = findOrThrow(id);
        themeRepository.delete(theme);
    }

    private Theme findOrThrow(String id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theme", "id", id));
    }

    private ThemeResponse defaultTheme() {
        return ThemeResponse.builder()
                .id(null)
                .title("Charte par défaut")
                .primaryColor(DEFAULT_PRIMARY)
                .accentColor(DEFAULT_ACCENT)
                .secondaryColor(DEFAULT_SECONDARY)
                .tertiaryColor(DEFAULT_TERTIARY)
                .isActive(true)
                .build();
    }

    private ThemeResponse convertToResponse(Theme theme) {
        return ThemeResponse.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .primaryColor(theme.getPrimaryColor())
                .accentColor(theme.getAccentColor())
                .secondaryColor(theme.getSecondaryColor())
                .tertiaryColor(theme.getTertiaryColor())
                .isActive(theme.getIsActive())
                .build();
    }
}
