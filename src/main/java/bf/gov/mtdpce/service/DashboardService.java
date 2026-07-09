package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.response.DashboardStats;
import bf.gov.mtdpce.entity.AgendaStatus;
import bf.gov.mtdpce.entity.ArticleStatus;
import bf.gov.mtdpce.entity.ProjectStatus;
import bf.gov.mtdpce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private static final String CODE_ACTUALITE = "ACTUALITE";
    private static final String CODE_COMMUNIQUE = "COMMUNIQUE";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    public DashboardStats getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();

        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countActiveUsers())
                .totalArticles(articleRepository.count())
                .publishedArticles(articleRepository.countByStatus(ArticleStatus.PUBLISHED))
                .totalProjects(projectRepository.count())
                .activeProjects(projectRepository.countByStatus(ProjectStatus.EN_COURS))
                .completedProjects(projectRepository.countByStatus(ProjectStatus.TERMINE))
                .totalDocuments(documentRepository.count())
                .publicDocuments(documentRepository.countPublicDocuments())
                .totalContacts(contactRepository.count())
                .pendingContacts(contactRepository.countPendingContacts())
                .averageProjectProgress(projectRepository.getAverageProgress())

                // ---- Pilotage éditorial (DCRP) ----
                .totalActualites(articleRepository.countByCategoryCode(CODE_ACTUALITE))
                .publishedActualites(articleRepository.countByStatusAndCategoryCode(ArticleStatus.PUBLISHED, CODE_ACTUALITE))
                .totalCommuniques(articleRepository.countByCategoryCode(CODE_COMMUNIQUE))
                .publishedCommuniques(articleRepository.countByStatusAndCategoryCode(ArticleStatus.PUBLISHED, CODE_COMMUNIQUE))
                .draftArticles(articleRepository.countByStatus(ArticleStatus.DRAFT))
                .publicationsLast7Days(articleRepository.countByStatusAndPublishedAtAfter(ArticleStatus.PUBLISHED, now.minusDays(7)))
                .publicationsLast30Days(articleRepository.countByStatusAndPublishedAtAfter(ArticleStatus.PUBLISHED, now.minusDays(30)))
                .totalAgendas(agendaRepository.count())
                .publishedAgendas(agendaRepository.countByStatus(AgendaStatus.PUBLISHED))
                .totalEvents(eventRepository.count())
                .totalMedia(mediaRepository.count())

                // ---- Newsletter ----
                .newsletterSubscribers(newsletterSubscriptionRepository.countByIsActiveTrueAndIsVerifiedTrue())

                // ---- Contenus les plus lus ----
                .topReadArticles(topReadArticles())

                .build();
    }

    /** Les 5 contenus publiés les plus consultés (impact éditorial). */
    private List<DashboardStats.TopContent> topReadArticles() {
        return articleRepository.findTop5ByStatusOrderByViewCountDesc(ArticleStatus.PUBLISHED)
                .stream()
                .map(a -> DashboardStats.TopContent.builder()
                        .title(a.getTitle())
                        .category(a.getCategory() != null ? a.getCategory().getLabel() : null)
                        .viewCount(a.getViewCount() != null ? a.getViewCount().longValue() : 0L)
                        .build())
                .toList();
    }
}
