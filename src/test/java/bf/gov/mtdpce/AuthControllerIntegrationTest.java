package bf.gov.mtdpce;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests fonctionnels du flux d'authentification (/api/auth/**).
 */
class AuthControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("POST /api/auth/login - identifiants valides -> 200 + token")
    void login_withValidCredentials_returnsToken() throws Exception {
        String body = "{\"username\":\"" + SUPERADMIN_USER + "\",\"password\":\"" + SUPERADMIN_PASS + "\"}";
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.roles[0]").value("ROLE_SUPER_ADMIN"));
    }

    @Test
    @DisplayName("POST /api/auth/login - mauvais mot de passe -> 401")
    void login_withWrongPassword_returnsUnauthorized() throws Exception {
        String body = "{\"username\":\"" + SUPERADMIN_USER + "\",\"password\":\"mauvais\"}";
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/login - corps vide -> 400 (validation)")
    void login_withEmptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - JSON illisible -> 400 (handler HttpMessageNotReadable)")
    void login_withMalformedJson_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ broken json "))
                .andExpect(status().isBadRequest());
    }
}
