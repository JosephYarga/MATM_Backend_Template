package bf.gov.matm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests fonctionnels de la gestion d'erreurs.
 * Vérifient que les situations qui renvoyaient AVANT un 500 renvoient
 * désormais le bon code (404 / 400 / 415), grâce au GlobalExceptionHandler corrigé.
 */
class ExceptionHandlingIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("GET /api/v1/domaines/{uuid inconnu} -> 404 (et non 500)")
    void getUnknownResource_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/domaines/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/missions/{uuid inconnu} -> 404 (et non 500)")
    void getUnknownMission_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/missions/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/articles/published/search sans paramètre 'query' -> 400 (et non 500)")
    void searchWithoutRequiredParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/articles/published/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/contacts/status/{valeur enum invalide} -> 400 (et non 500)")
    void invalidEnumPathVariable_returnsBadRequest() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(get("/api/v1/contacts/status/PAS_UN_STATUT")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/services (multipart) en JSON -> 415 (et non 500)")
    void jsonOnMultipartEndpoint_returnsUnsupportedMediaType() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(post("/api/v1/services")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("PUT /api/v1/contacts/{id}/status sans champ 'status' -> 400 (et non 500 / NPE)")
    void updateStatusWithoutStatusField_returnsBadRequest() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/v1/contacts/" + UUID.randomUUID() + "/status")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
