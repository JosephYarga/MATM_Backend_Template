package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.response.AnalyticsOverview;
import bf.gov.mtdpce.dto.request.TrackRequest;
import bf.gov.mtdpce.entity.PageVisit;
import bf.gov.mtdpce.repository.PageVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AnalyticsService {

    private static final String PAGE_VIEW = "PAGE_VIEW";
    private static final String CLICK = "CLICK";

    @Autowired
    private PageVisitRepository pageVisitRepository;

    /** Enregistre une visite/clic envoyé par le site public. */
    @Transactional
    public void track(TrackRequest req) {
        if (req == null || req.getPath() == null || req.getPath().isBlank()) {
            return;
        }
        String type = CLICK.equalsIgnoreCase(req.getType()) ? CLICK : PAGE_VIEW;

        PageVisit visit = PageVisit.builder()
                .path(truncate(req.getPath(), 500))
                .sessionId(truncate(req.getSessionId(), 100))
                .type(type)
                .label(truncate(req.getLabel(), 500))
                .referrer(truncate(req.getReferrer(), 500))
                .visitedAt(LocalDateTime.now())
                .build();

        pageVisitRepository.save(visit);
    }

    @Transactional(readOnly = true)
    public AnalyticsOverview getOverview() {
        LocalDateTime startToday = LocalDate.now().atStartOfDay();
        LocalDateTime startWeek = LocalDate.now().minusDays(6).atStartOfDay();
        LocalDateTime startMonth = LocalDate.now().minusDays(29).atStartOfDay();
        LocalDateTime start3Months = LocalDate.now().minusDays(89).atStartOfDay();

        // Séries journalières (90 jours) à partir des couples (session, date)
        List<Object[]> rows = pageVisitRepository.findSessionAndDateSince(PAGE_VIEW, start3Months);
        List<AnalyticsOverview.DailyPoint> dailySeries = buildDailySeries(rows, 90);
        List<AnalyticsOverview.HourPoint> hourly = buildHourly(rows, startMonth);

        return AnalyticsOverview.builder()
                .visitorsToday(pageVisitRepository.countDistinctSessionsSince(PAGE_VIEW, startToday))
                .visitorsWeek(pageVisitRepository.countDistinctSessionsSince(PAGE_VIEW, startWeek))
                .visitorsMonth(pageVisitRepository.countDistinctSessionsSince(PAGE_VIEW, startMonth))
                .visitors3Months(pageVisitRepository.countDistinctSessionsSince(PAGE_VIEW, start3Months))
                .pageViewsToday(pageVisitRepository.countByTypeAndVisitedAtGreaterThanEqual(PAGE_VIEW, startToday))
                .pageViewsWeek(pageVisitRepository.countByTypeAndVisitedAtGreaterThanEqual(PAGE_VIEW, startWeek))
                .pageViewsMonth(pageVisitRepository.countByTypeAndVisitedAtGreaterThanEqual(PAGE_VIEW, startMonth))
                .pageViews3Months(pageVisitRepository.countByTypeAndVisitedAtGreaterThanEqual(PAGE_VIEW, start3Months))
                .totalClicks(pageVisitRepository.countByTypeAndVisitedAtGreaterThanEqual(CLICK, startMonth))
                .dailySeries(dailySeries)
                .hourly(hourly)
                .topPages(toLabelCounts(pageVisitRepository.topPathsSince(PAGE_VIEW, startMonth), 10))
                .topClicks(toLabelCounts(pageVisitRepository.topLabelsSince(CLICK, startMonth), 10))
                .build();
    }

    private List<AnalyticsOverview.DailyPoint> buildDailySeries(List<Object[]> rows, int days) {
        // date -> [pageViews, set de sessions]
        Map<LocalDate, long[]> pageViewsByDay = new TreeMap<>();
        Map<LocalDate, java.util.Set<String>> sessionsByDay = new LinkedHashMap<>();

        for (Object[] row : rows) {
            String session = (String) row[0];
            LocalDateTime dt = (LocalDateTime) row[1];
            LocalDate d = dt.toLocalDate();
            pageViewsByDay.computeIfAbsent(d, k -> new long[1])[0]++;
            sessionsByDay.computeIfAbsent(d, k -> new java.util.HashSet<>()).add(session);
        }

        List<AnalyticsOverview.DailyPoint> series = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            long pv = pageViewsByDay.containsKey(d) ? pageViewsByDay.get(d)[0] : 0;
            long vis = sessionsByDay.containsKey(d) ? sessionsByDay.get(d).size() : 0;
            series.add(AnalyticsOverview.DailyPoint.builder()
                    .date(d.toString())
                    .visitors(vis)
                    .pageViews(pv)
                    .build());
        }
        return series;
    }

    private List<AnalyticsOverview.HourPoint> buildHourly(List<Object[]> rows, LocalDateTime since) {
        long[] buckets = new long[24];
        for (Object[] row : rows) {
            LocalDateTime dt = (LocalDateTime) row[1];
            if (dt.isBefore(since)) continue;
            buckets[dt.getHour()]++;
        }
        List<AnalyticsOverview.HourPoint> hourly = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            hourly.add(AnalyticsOverview.HourPoint.builder().hour(h).count(buckets[h]).build());
        }
        return hourly;
    }

    private List<AnalyticsOverview.LabelCount> toLabelCounts(List<Object[]> rows, int limit) {
        List<AnalyticsOverview.LabelCount> list = new ArrayList<>();
        for (Object[] row : rows) {
            if (list.size() >= limit) break;
            String label = row[0] != null ? row[0].toString() : "(inconnu)";
            long count = ((Number) row[1]).longValue();
            list.add(AnalyticsOverview.LabelCount.builder().label(label).count(count).build());
        }
        return list;
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() > max ? s.substring(0, max) : s;
    }
}
