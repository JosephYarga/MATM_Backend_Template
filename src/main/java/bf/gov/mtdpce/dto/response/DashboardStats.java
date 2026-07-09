package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    private Long totalUsers;
    private Long activeUsers;
    private Long totalArticles;
    private Long publishedArticles;
    private Long totalProjects;
    private Long activeProjects;
    private Long completedProjects;
    private Long totalDocuments;
    private Long publicDocuments;
    private Long totalContacts;
    private Long pendingContacts;
    private Double averageProjectProgress;

    // ---- Pilotage éditorial (DCRP) ----
    private Long totalActualites;
    private Long publishedActualites;
    private Long totalCommuniques;
    private Long publishedCommuniques;
    private Long draftArticles;
    private Long publicationsLast7Days;
    private Long publicationsLast30Days;
    private Long totalAgendas;
    private Long publishedAgendas;
    private Long totalEvents;
    private Long totalMedia;

    // ---- Newsletter (relation public) ----
    private Long newsletterSubscribers;

    // ---- Contenus les plus lus ----
    private List<TopContent> topReadArticles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopContent {
        private String title;
        private String category;
        private Long viewCount;
    }
}
