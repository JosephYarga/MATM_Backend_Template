package bf.gov.mtdpce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limitation de débit (anti-spam) sur les formulaires publics sensibles :
 * contact et abonnement newsletter. Fenêtre glissante en mémoire, par IP + chemin.
 *
 * Implémentation volontairement légère (aucune dépendance externe), adaptée au
 * faible volume de ces endpoints. Au-delà du quota → 429 avec un message explicite.
 */
public class RateLimitingFilter extends OncePerRequestFilter {

    /** Quota : nombre max de requêtes autorisées sur la fenêtre (ms). */
    private record Rule(int max, long windowMs) {}

    private static final long WINDOW = 5 * 60_000L; // 5 minutes

    private final Map<String, Rule> rules = Map.of(
            "/api/v1/contacts/submit", new Rule(5, WINDOW),
            "/api/v1/public/contacts", new Rule(5, WINDOW),
            "/api/v1/newsletter/public/subscribe", new Rule(5, WINDOW)
    );

    private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Rule rule = "POST".equalsIgnoreCase(request.getMethod())
                ? rules.get(request.getRequestURI())
                : null;

        if (rule != null && !allow(clientIp(request) + "|" + request.getRequestURI(), rule)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Trop de tentatives. Veuillez réessayer dans quelques minutes.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private synchronized boolean allow(String key, Rule rule) {
        long now = System.currentTimeMillis();
        Deque<Long> window = hits.computeIfAbsent(key, k -> new ArrayDeque<>());
        while (!window.isEmpty() && now - window.peekFirst() > rule.windowMs()) {
            window.pollFirst();
        }
        if (window.size() >= rule.max()) {
            return false;
        }
        window.addLast(now);
        return true;
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
