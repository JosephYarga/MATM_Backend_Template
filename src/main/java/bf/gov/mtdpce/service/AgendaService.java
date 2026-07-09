package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.AgendaRequest;
import bf.gov.mtdpce.dto.response.AgendaResponse;
import bf.gov.mtdpce.dto.response.AgendaImageResponse;
import bf.gov.mtdpce.entity.Agenda;
import bf.gov.mtdpce.entity.AgendaImage;
import bf.gov.mtdpce.entity.AgendaStatus;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.AgendaRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<AgendaResponse> getAllAgenda(Pageable pageable) {
        return agendaRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<AgendaResponse> getPublishedArticles(Pageable pageable) {
        return agendaRepository.findByStatus(AgendaStatus.PUBLISHED, pageable).map(this::convertToResponse);
    }

    public Page<AgendaResponse> searchPublishedAgenda(String search, Pageable pageable) {
        return agendaRepository.searchPublishedAgenda(search, AgendaStatus.PUBLISHED, pageable)
                .map(this::convertToResponse);
    }

    public List<AgendaResponse> getLatestAgenda() {
        return agendaRepository.findTop5ByStatusOrderByPublishedAtDesc(AgendaStatus.PUBLISHED)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AgendaResponse getAgendaById(UUID id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda", "id", id));
        return convertToResponse(agenda);
    }

    @Transactional
    public AgendaResponse getPublishedAgendaById(UUID id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda", "id", id));

        if (agenda.getStatus() != AgendaStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Agenda", "id", id);
        }
        agendaRepository.save(agenda);
        return convertToResponse(agenda);
    }

    @Transactional
    public AgendaResponse createAgenda(AgendaRequest agendaDTO,
                                  UUID authorId,
                                  List<String> imagePaths) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", authorId));

        Agenda agenda = Agenda.builder()
                .title(agendaDTO.getTitle())
                .summary(agendaDTO.getSummary())
                .content(agendaDTO.getContent())
                .datePublication(agendaDTO.getDatePublication())
                .lieux(agendaDTO.getLieux())
                .status(agendaDTO.getStatus() != null ? agendaDTO.getStatus() : AgendaStatus.DRAFT)
                .author(author)
                .build();

        if (agenda.getStatus() == AgendaStatus.PUBLISHED) {
            agenda.setPublishedAt(LocalDateTime.now());
        }

        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String path : imagePaths) {
                AgendaImage image = AgendaImage.builder()
                        .imageUrl(path)
                        .agenda(agenda)
                        .build();
                agenda.getImages().add(image);
            }
        }

        return convertToResponse(agendaRepository.save(agenda));
    }

    @Transactional
    public AgendaResponse updateAgenda(UUID id,
                                  AgendaRequest agendaDTO,
                                  List<String> imagePaths) {

        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda", "id", id));

        if (agendaDTO.getTitle() != null)
            agenda.setTitle(agendaDTO.getTitle());

        if (agendaDTO.getSummary() != null)
            agenda.setSummary(agendaDTO.getSummary());

        if (agendaDTO.getContent() != null)
            agenda.setContent(agendaDTO.getContent());

        if (agendaDTO.getLieux() != null)
            agenda.setLieux(agendaDTO.getLieux());

        if (agendaDTO.getDatePublication() != null)
            agenda.setDatePublication(agendaDTO.getDatePublication());

        if (agendaDTO.getStatus() != null) {
            if (agendaDTO.getStatus() == AgendaStatus.PUBLISHED
                    && agenda.getStatus() != AgendaStatus.PUBLISHED) {

                agenda.setPublishedAt(LocalDateTime.now());
            }

            agenda.setStatus(agendaDTO.getStatus());
        }

        if (imagePaths != null && !imagePaths.isEmpty()) {

            agenda.getImages().clear();
            for (String path : imagePaths) {
                AgendaImage image = AgendaImage.builder()
                        .imageUrl(path)
                        .agenda(agenda)
                        .build();

                agenda.getImages().add(image);
            }
        }

        return convertToResponse(agendaRepository.save(agenda));
    }

    @Transactional
    public void deleteArticle(UUID id) {
        if (!agendaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article", "id", id);
        }
        agendaRepository.deleteById(id);
    }

    public Long countPublishedArticles() {
        return agendaRepository.countByStatus(AgendaStatus.PUBLISHED);
    }

    private AgendaResponse convertToResponse(Agenda agenda) {

        List<AgendaImageResponse> images = agenda.getImages()
                .stream()
                .map(img -> AgendaImageResponse.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();
        return AgendaResponse.builder()
                .id(agenda.getId())
                .title(agenda.getTitle())
                .summary(agenda.getSummary())
                .content(agenda.getContent())
                .status(agenda.getStatus())
                .authorId(agenda.getAuthor().getId())
                .datePublication(agenda.getDatePublication())
                .lieux(agenda.getLieux())
                .authorName(agenda.getAuthor().getFirstName() + " " + agenda.getAuthor().getLastName())
                .publishedAt(agenda.getPublishedAt())
                .createdAt(agenda.getCreatedAt())
                .updatedAt(agenda.getUpdatedAt())
                .images(images)
                .build();
    }
}
