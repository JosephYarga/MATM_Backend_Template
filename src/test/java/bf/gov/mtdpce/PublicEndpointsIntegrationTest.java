package bf.gov.mtdpce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests fonctionnels des endpoints publics : accessibles SANS authentification (-> 200).
 */
class PublicEndpointsIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("GET /api/v1/articles/published -> 200 sans token")
    void publishedArticles_arepublic() throws Exception {
        mockMvc.perform(get("/api/v1/articles/published"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/statistiques -> 200 sans token")
    void statistiques_arePublic() throws Exception {
        mockMvc.perform(get("/api/v1/statistiques"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/agendas/published -> 200 sans token")
    void publishedAgendas_arePublic() throws Exception {
        mockMvc.perform(get("/api/v1/agendas/published"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/domaines -> 200 sans token")
    void domaines_arePublic() throws Exception {
        mockMvc.perform(get("/api/v1/domaines"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api-docs (OpenAPI) -> 200")
    void openApiDocs_areAccessible() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());
    }
}
