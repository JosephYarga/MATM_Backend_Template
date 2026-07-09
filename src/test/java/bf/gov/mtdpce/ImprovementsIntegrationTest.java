package bf.gov.mtdpce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests des améliorations transverses : health-check, sitemap et limitation de débit.
 */
class ImprovementsIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Health-check public -> 200 status UP")
    void health_isUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("UP"));
    }

    @Test
    @DisplayName("Sitemap public -> XML avec pages statiques et contenus")
    void sitemap_returnsXml() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("<urlset")))
                .andExpect(content().string(containsString("/actualites")));
    }

    @Test
    @DisplayName("Documents : navigation paginée par type + facettes (pagination serveur)")
    void documentsBrowse_paginatedWithFacets() throws Exception {
        mockMvc.perform(get("/api/v1/documents/public/browse")
                        .param("typeDocument", "DOCUMENT SIMPLE").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.facets").exists())
                .andExpect(jsonPath("$.data.totalElements").exists());
    }

    @Test
    @DisplayName("Anciens ministres : endpoint paginé serveur")
    void formerMinisters_paginated() throws Exception {
        mockMvc.perform(get("/api/v1/ministres/anciens").param("size", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalElements").exists());
    }

    @Test
    @DisplayName("Rate-limiting : la 6e soumission de contact en rafale est bloquée (429)")
    void rateLimiting_blocksBurst() throws Exception {
        String body = "{\"name\":\"Test\",\"email\":\"rl@example.com\",\"subject\":\"Sujet\",\"message\":\"Message de test\"}";

        // 5 requêtes autorisées dans la fenêtre
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/public/contacts")
                            .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isOk());
        }
        // la 6e dépasse le quota
        mockMvc.perform(post("/api/v1/public/contacts")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.success").value(false));
    }
}
