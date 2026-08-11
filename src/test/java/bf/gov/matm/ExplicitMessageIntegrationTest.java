package bf.gov.matm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie que les endpoints renvoient un MESSAGE EXPLICITE du résultat
 * (champ `message` de l'ApiResponse), aussi bien en erreur qu'en succès.
 *
 * C'est ce message que le frontend affiche à l'utilisateur authentifié
 * (et qu'il masque pour les visiteurs des pages publiques).
 */
class ExplicitMessageIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Erreur 404 -> success=false + message explicite non vide")
    void notFound_hasExplicitMessage() throws Exception {
        mockMvc.perform(get("/api/v1/domaines/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Erreur de validation (@Valid) -> message explicite 'Erreurs de validation'")
    void validationError_hasExplicitMessage() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(post("/api/v1/types")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Erreur 415 (media type) -> message explicite non vide")
    void unsupportedMediaType_hasExplicitMessage() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(post("/api/v1/services")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Succès sur endpoint public enveloppé -> success=true + message explicite non vide")
    void publicSuccess_hasExplicitMessage() throws Exception {
        mockMvc.perform(get("/api/v1/articles/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }
}
