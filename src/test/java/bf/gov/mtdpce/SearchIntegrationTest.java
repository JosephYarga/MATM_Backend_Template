package bf.gov.mtdpce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests fonctionnels de la recherche unifiée publique (/api/v1/public/search).
 */
class SearchIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Recherche publique : 200 + enveloppe complète (résultats, facettes, pagination)")
    void search_returnsEnvelopeWithFacets() throws Exception {
        mockMvc.perform(get("/api/v1/public/search").param("query", "le").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())))
                .andExpect(jsonPath("$.data.query").value("le"))
                .andExpect(jsonPath("$.data.results", isA(java.util.List.class)))
                .andExpect(jsonPath("$.data.facets").exists())
                .andExpect(jsonPath("$.data.searchDurationMs").exists());
    }

    @Test
    @DisplayName("Recherche sans authentification autorisée (endpoint public)")
    void search_isPublic() throws Exception {
        mockMvc.perform(get("/api/v1/public/search").param("query", "numerique"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Suggestions : le champ 'suggestions' est toujours un tableau")
    void search_hasSuggestionsArray() throws Exception {
        mockMvc.perform(get("/api/v1/public/search").param("query", "trensformation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.suggestions").isArray());
    }

    @Test
    @DisplayName("Requête trop courte : 200 avec 0 résultat (pas d'erreur)")
    void search_tooShort_returnsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/public/search").param("query", "a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalResults").value(0));
    }
}
