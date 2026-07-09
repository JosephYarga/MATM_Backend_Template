package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.entity.AgendaStatus;
import bf.gov.mtdpce.entity.ArticleStatus;
import bf.gov.mtdpce.repository.AgendaRepository;
import bf.gov.mtdpce.repository.ArticleRepository;
import bf.gov.mtdpce.repository.EventRepository;
import bf.gov.mtdpce.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Génère le plan du site (sitemap.xml) pour l'indexation par les moteurs de recherche :
 * pages statiques + contenus publiés (actualités, projets, événements, agenda).
 */
@RestController
public class SitemapController {

    @Value("${app.public-url:https://site-test.tic.gov.bf}")
    private String baseUrl;

    @Autowired private ArticleRepository articleRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private AgendaRepository agendaRepository;

    private static final List<String> STATIC_PATHS = List.of(
            "/", "/actualites", "/communiques", "/projets", "/evenements", "/agendas",
            "/ressources/documents", "/ressources/politiques", "/services", "/faq", "/contact",
            "/mediatheque", "/ministere/ministre", "/ministere/anciens-ministres",
            "/ministere/missions", "/ministere/organigramme", "/ministere/structures"
    );

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        String base = baseUrl.replaceAll("/+$", "");
        String today = LocalDate.now().toString();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (String path : STATIC_PATHS) {
            url(sb, base + path, today, "/".equals(path) ? "1.0" : "0.8");
        }

        articleRepository.findByStatus(ArticleStatus.PUBLISHED, PageRequest.of(0, 2000)).getContent()
                .forEach(a -> url(sb, base + "/actualites/" + a.getId(), date(a.getUpdatedAt(), a.getPublishedAt()), "0.7"));

        projectRepository.findAll(PageRequest.of(0, 2000)).getContent()
                .forEach(p -> url(sb, base + "/projets/" + p.getId(), date(p.getUpdatedAt(), p.getCreatedAt()), "0.6"));

        eventRepository.findByIsPublicTrueOrderByStartDateDesc(PageRequest.of(0, 2000)).getContent()
                .forEach(e -> url(sb, base + "/evenements/" + e.getId(), date(e.getUpdatedAt(), e.getStartDate()), "0.6"));

        agendaRepository.findByStatus(AgendaStatus.PUBLISHED, PageRequest.of(0, 2000)).getContent()
                .forEach(ag -> url(sb, base + "/agendas/" + ag.getId(), date(ag.getUpdatedAt(), ag.getPublishedAt()), "0.5"));

        sb.append("</urlset>\n");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(sb.toString());
    }

    private void url(StringBuilder sb, String loc, String lastmod, String priority) {
        sb.append("  <url><loc>").append(escape(loc)).append("</loc>")
                .append("<lastmod>").append(lastmod).append("</lastmod>")
                .append("<priority>").append(priority).append("</priority></url>\n");
    }

    private String date(LocalDateTime primary, LocalDateTime fallback) {
        LocalDateTime d = primary != null ? primary : fallback;
        return (d != null ? d.toLocalDate() : LocalDate.now()).toString();
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
