package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Données d'analytics de fréquentation pour le tableau de bord (DCRP).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsOverview {

    // Visiteurs uniques (sessions distinctes)
    private long visitorsToday;
    private long visitorsWeek;
    private long visitorsMonth;
    private long visitors3Months;

    // Pages vues
    private long pageViewsToday;
    private long pageViewsWeek;
    private long pageViewsMonth;
    private long pageViews3Months;

    private long totalClicks;

    /** Évolution journalière sur 90 jours */
    private List<DailyPoint> dailySeries;

    /** Répartition par heure (0-23) sur 30 jours */
    private List<HourPoint> hourly;

    /** Pages les plus visitées (30 jours) */
    private List<LabelCount> topPages;

    /** Liens les plus cliqués (30 jours) */
    private List<LabelCount> topClicks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyPoint {
        private String date;      // yyyy-MM-dd
        private long visitors;
        private long pageViews;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HourPoint {
        private int hour;         // 0-23
        private long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabelCount {
        private String label;
        private long count;
    }
}
