package bf.gov.matm.service;

import bf.gov.matm.dto.ThemeDTO;
import bf.gov.matm.entity.Theme;
import bf.gov.matm.entity.User;
import bf.gov.matm.exception.ResourceNotFoundException;
import bf.gov.matm.repository.ThemeRepository;
import bf.gov.matm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserRepository userRepository;

    public ThemeDTO getThemeByTitile(String title) {
        Theme theme = themeRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Theme", "title", title));
        return convertToDTO(theme);
    }

    public ThemeDTO create(ThemeDTO dto) {

        Theme theme = themeRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Theme", "title", dto.getTitle()));
        theme.setTitle(dto.getTitle());
        theme.setAccentColor(dto.getAccentColor());
        theme.setPrimaryColor(dto.getPrimaryColor());
        theme.setSecondaryColor(dto.getSecondaryColor());
        theme.setAccentColor(dto.getAccentColor());
        theme.setTertiaryColor(dto.getTertiaryColor());
        return convertToDTO(themeRepository.save(theme));
    }

    public List<ThemeDTO> getAll() {
        return themeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ThemeDTO convertToDTO(Theme theme) {
        return ThemeDTO.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .accentColor(theme.getAccentColor())
                .primaryColor(theme.getPrimaryColor())
                .secondaryColor(theme.getSecondaryColor())
                .tertiaryColor(theme.getTertiaryColor())
                .tertiaryColor(theme.getTertiaryColor())
                .build();
    }

}
