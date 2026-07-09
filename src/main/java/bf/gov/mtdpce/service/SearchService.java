package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.response.SearchResponse;
import bf.gov.mtdpce.dto.response.SearchResponse.SearchResultItem;
import bf.gov.mtdpce.entity.*;
import bf.gov.mtdpce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Recherche unifiée sur tout le contenu public du portail.
 *
 * Interroge chaque source (actualités, communiqués, projets, documents, agenda,
 * événements, services, FAQ), calcule un score de pertinence, met en surbrillance
 * le terme recherché dans les extraits, agrège les facettes (compteurs par type)
 * puis pagine le résultat global.
 */
@Service
public class SearchService {

    private static final int MIN_LENGTH = 2;
    private static final int PER_TYPE_CAP = 50;

    // Types normalisés (alignés sur le frontend)
    private static final String ACTUALITE = "actualite";
    private static final String COMMUNIQUE = "communique";
    private static final String PROJET = "projet";
    private static final String DOCUMENT = "document";
    private static final String AGENDA = "agenda";
    private static final String EVENEMENT = "evenement";
    private static final String SERVICE = "service";
    private static final String FAQ = "faq";

    @Autowired private ArticleRepository articleRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private AgendaRepository agendaRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private ServicesRepository servicesRepository;
    @Autowired private FAQRepository faqRepository;

    public SearchResponse search(String query, List<String> types, int page, int size, String sort) {
        long start = System.currentTimeMillis();

        String q = query == null ? "" : query.trim();
        int p = Math.max(0, page);
        int s = size <= 0 ? 10 : Math.min(size, 50);

        List<SearchResultItem> all = new ArrayList<>();
        Set<String> enabled = normalizeTypes(types);

        if (q.length() >= MIN_LENGTH) {
            collectArticles(all, q, enabled);
            if (on(enabled, PROJET))    collectProjects(all, q);
            if (on(enabled, DOCUMENT))  collectDocuments(all, q);
            if (on(enabled, AGENDA))    collectAgendas(all, q);
            if (on(enabled, EVENEMENT)) collectEvents(all, q);
            if (on(enabled, SERVICE))   collectServices(all, q);
            if (on(enabled, FAQ))       collectFaqs(all, q);
        }

        // Facettes (compteurs par type) sur l'ensemble des correspondances
        Map<String, Long> facets = all.stream()
                .collect(Collectors.groupingBy(SearchResultItem::getType, Collectors.counting()));

        sortItems(all, sort);

        long total = all.size();
        int from = Math.min(p * s, all.size());
        int to = Math.min(from + s, all.size());
        List<SearchResultItem> pageItems = new ArrayList<>(all.subList(from, to));
        int totalPages = (int) Math.ceil((double) total / s);

        SearchResponse resp = new SearchResponse();
        resp.setQuery(q);
        resp.setResults(pageItems);
        resp.setTotalResults(total);
        resp.setPage(p);
        resp.setSize(s);
        resp.setTotalPages(totalPages);
        resp.setFacets(facets);
        resp.setSuggestions(buildSuggestions(q, total));
        resp.setSearchDurationMs(System.currentTimeMillis() - start);
        return resp;
    }

    // ---------------- Suggestions (« Vouliez-vous dire… ») ----------------

    /**
     * Propose des contenus proches (correspondance floue) quand la recherche donne
     * peu ou pas de résultats — utile en cas de faute de frappe ou d'accents.
     */
    private List<String> buildSuggestions(String query, long total) {
        String q = deaccent(query.trim().toLowerCase());
        if (q.length() < 3 || total >= 5) return List.of();

        return candidatePhrases().stream()
                .distinct()
                .map(p -> Map.entry(p, fuzzyScore(q, deaccent(p.toLowerCase()))))
                .filter(e -> e.getValue() >= 0.62)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(4)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /** Vocabulaire de phrases candidates (titres et questions du contenu public). */
    private List<String> candidatePhrases() {
        List<String> phrases = new ArrayList<>();
        articleRepository.findByStatus(ArticleStatus.PUBLISHED, PageRequest.of(0, 120)).getContent()
                .forEach(a -> { if (a.getTitle() != null) phrases.add(a.getTitle()); });
        projectRepository.findAll(PageRequest.of(0, 80)).getContent()
                .forEach(p -> { if (p.getName() != null) phrases.add(p.getName()); });
        eventRepository.findByIsPublicTrueOrderByStartDateDesc(PageRequest.of(0, 80)).getContent()
                .forEach(e -> { if (e.getTitle() != null) phrases.add(e.getTitle()); });
        faqRepository.findByIsPublishedTrueOrderByDisplayOrderAsc(PageRequest.of(0, 80)).getContent()
                .forEach(f -> { if (f.getQuestion() != null) phrases.add(f.getQuestion()); });
        return phrases;
    }

    /** Similarité floue [0..1] entre la requête et une phrase (par mots, distance d'édition). */
    private double fuzzyScore(String q, String phrase) {
        if (phrase.contains(q)) return 1.0;
        String[] qTokens = q.split("\\s+");
        String[] pTokens = phrase.split("\\s+");
        double sum = 0;
        int n = 0;
        for (String qt : qTokens) {
            if (qt.length() < 3) continue;
            double best = 0;
            for (String pt : pTokens) {
                if (pt.length() < 3) continue;
                int d = levenshtein(qt, pt);
                double sim = 1.0 - (double) d / Math.max(qt.length(), pt.length());
                if (sim > best) best = sim;
            }
            sum += best;
            n++;
        }
        return n == 0 ? 0 : sum / n;
    }

    private int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] cur = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            cur[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[b.length()];
    }

    /** Retire les accents pour comparer « numerique » et « numérique ». */
    private String deaccent(String s) {
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
    }

    // ---------------- Collecte par source ----------------

    private void collectArticles(List<SearchResultItem> out, String q, Set<String> enabled) {
        boolean wantAct = on(enabled, ACTUALITE);
        boolean wantCom = on(enabled, COMMUNIQUE);
        if (!wantAct && !wantCom) return;

        articleRepository.searchPublishedArticles(q, ArticleStatus.PUBLISHED, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().forEach(a -> {
                    boolean isCom = a.getCategory() != null && "COMMUNIQUE".equalsIgnoreCase(a.getCategory().getCode());
                    String type = isCom ? COMMUNIQUE : ACTUALITE;
                    if (isCom ? !wantCom : !wantAct) return;
                    String body = a.getSummary() != null && !a.getSummary().isBlank() ? a.getSummary() : a.getContent();
                    out.add(item(type, a.getId(), a.getTitle(), body,
                            "/actualites/" + a.getId(), a.getFeaturedImage(),
                            a.getCategory() != null ? a.getCategory().getLabel() : null,
                            a.getPublishedAt() != null ? a.getPublishedAt() : a.getCreatedAt(), q));
                });
    }

    private void collectProjects(List<SearchResultItem> out, String q) {
        projectRepository.searchProjects(q, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().forEach(pr -> out.add(item(PROJET, pr.getId(), pr.getName(), pr.getDescription(),
                        "/projets/" + pr.getId(), pr.getFeaturedImage(), null, pr.getCreatedAt(), q)));
    }

    private void collectDocuments(List<SearchResultItem> out, String q) {
        documentRepository.searchPublicDocuments(q, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().forEach(d -> out.add(item(DOCUMENT, d.getId(), d.getTitle(), d.getDescription(),
                        "/ressources/documents", null,
                        d.getCategory() != null ? d.getCategory().name() : null, d.getCreatedAt(), q)));
    }

    private void collectAgendas(List<SearchResultItem> out, String q) {
        agendaRepository.searchPublishedAgenda(q, AgendaStatus.PUBLISHED, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().forEach(ag -> out.add(item(AGENDA, ag.getId(), ag.getTitle(), ag.getSummary(),
                        "/agendas/" + ag.getId(), null, ag.getLieux(),
                        ag.getPublishedAt() != null ? ag.getPublishedAt() : ag.getCreatedAt(), q)));
    }

    private void collectEvents(List<SearchResultItem> out, String q) {
        eventRepository.searchEvents(q, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().stream()
                .filter(e -> Boolean.TRUE.equals(e.getIsPublic()))
                .forEach(e -> {
                    String body = e.getDescription() != null && !e.getDescription().isBlank() ? e.getDescription() : e.getContent();
                    out.add(item(EVENEMENT, e.getId(), e.getTitle(), body,
                            "/evenements/" + e.getId(), e.getImageUrl(), e.getCategory(), e.getStartDate(), q));
                });
    }

    private void collectServices(List<SearchResultItem> out, String q) {
        String ql = q.toLowerCase();
        servicesRepository.findAll().stream()
                .filter(sv -> contains(sv.getName(), ql) || contains(sv.getDescription(), ql))
                .limit(PER_TYPE_CAP)
                .forEach(sv -> {
                    SearchResultItem it = item(SERVICE, sv.getId(), sv.getName(), sv.getDescription(),
                            sv.getUrl() != null && !sv.getUrl().isBlank() ? sv.getUrl() : "/services",
                            sv.getLogo(), null, sv.getCreatedAt(), q);
                    out.add(it);
                });
    }

    private void collectFaqs(List<SearchResultItem> out, String q) {
        faqRepository.searchFAQs(q, PageRequest.of(0, PER_TYPE_CAP))
                .getContent().stream()
                .filter(f -> Boolean.TRUE.equals(f.getIsPublished()))
                .forEach(f -> out.add(item(FAQ, f.getId(), f.getQuestion(), f.getAnswer(),
                        "/faq", null, f.getCategory(), f.getCreatedAt(), q)));
    }

    // ---------------- Construction d'un résultat ----------------

    private SearchResultItem item(String type, UUID id, String title, String body,
                                  String url, String imageUrl, String category,
                                  LocalDateTime date, String q) {
        SearchResultItem it = new SearchResultItem();
        it.setId(id);
        it.setType(type);
        it.setTitle(title);
        it.setUrl(url);
        it.setImageUrl(imageUrl);
        it.setCategory(category);
        it.setDate(date);
        it.setExcerpt(buildExcerpt(body, q));
        it.setRelevanceScore(score(q, title, body));

        Map<String, String> highlights = new HashMap<>();
        highlights.put("title", highlight(title == null ? "" : title, q));
        it.setHighlights(highlights);
        return it;
    }

    // ---------------- Pertinence / tri ----------------

    private double score(String q, String title, String body) {
        String ql = q.toLowerCase();
        String t = title == null ? "" : title.toLowerCase();
        double s = 0;
        if (t.equals(ql)) s += 10;
        else if (t.startsWith(ql)) s += 6;
        else if (t.contains(ql)) s += 4;
        if (body != null && strip(body).toLowerCase().contains(ql)) s += 1;
        return s;
    }

    private void sortItems(List<SearchResultItem> items, String sort) {
        Comparator<SearchResultItem> byDateDesc =
                Comparator.comparing(SearchResultItem::getDate, Comparator.nullsLast(Comparator.reverseOrder()));
        Comparator<SearchResultItem> cmp;
        if (sort == null) sort = "RELEVANCE";
        switch (sort.toUpperCase()) {
            case "DATE_DESC" -> cmp = byDateDesc;
            case "DATE_ASC" -> cmp = Comparator.comparing(SearchResultItem::getDate,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case "TITLE" -> cmp = Comparator.comparing(i -> i.getTitle() == null ? "" : i.getTitle().toLowerCase());
            default -> cmp = Comparator.comparingDouble(SearchResultItem::getRelevanceScore).reversed()
                    .thenComparing(byDateDesc);
        }
        items.sort(cmp);
    }

    // ---------------- Surbrillance / extraits ----------------

    /** Construit un extrait de ~180 caractères centré sur la 1ère occurrence, avec le terme surligné. */
    private String buildExcerpt(String text, String q) {
        String plain = strip(text);
        if (plain.isEmpty()) return "";
        int idx = plain.toLowerCase().indexOf(q.toLowerCase());
        if (idx < 0) {
            return highlight(truncate(plain, 180), q);
        }
        int from = Math.max(0, idx - 60);
        int to = Math.min(plain.length(), idx + q.length() + 120);
        String window = (from > 0 ? "… " : "") + plain.substring(from, to) + (to < plain.length() ? " …" : "");
        return highlight(window, q);
    }

    /** Entoure chaque occurrence (insensible à la casse) du terme par une balise <mark>. */
    private String highlight(String text, String q) {
        if (text == null || text.isEmpty() || q == null || q.isBlank()) return text == null ? "" : text;
        Matcher m = Pattern.compile(Pattern.quote(q), Pattern.CASE_INSENSITIVE).matcher(text);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement("<mark>" + m.group() + "</mark>"));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    // ---------------- Utilitaires ----------------

    private Set<String> normalizeTypes(List<String> types) {
        if (types == null || types.isEmpty()) return Collections.emptySet();
        return types.stream().filter(Objects::nonNull)
                .map(t -> t.trim().toLowerCase()).filter(t -> !t.isEmpty())
                .collect(Collectors.toSet());
    }

    /** Un type est actif si aucun filtre n'est fourni (ensemble vide = tout) ou s'il est listé. */
    private boolean on(Set<String> enabled, String type) {
        return enabled.isEmpty() || enabled.contains(type);
    }

    private boolean contains(String value, String ql) {
        return value != null && strip(value).toLowerCase().contains(ql);
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max).trim() + " …";
    }

    private String strip(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", " ").replace("&nbsp;", " ").replaceAll("\\s+", " ").trim();
    }
}
