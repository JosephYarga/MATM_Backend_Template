package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.DashboardStats;
import bf.gov.mtdpce.entity.ArticleStatus;
import bf.gov.mtdpce.entity.ProjectStatus;
import bf.gov.mtdpce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

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

    public DashboardStats getDashboardStats() {
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
                .build();
    }
}
