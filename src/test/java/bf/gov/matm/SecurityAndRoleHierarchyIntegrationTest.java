package bf.gov.matm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de sécurité :
 *  - un endpoint protégé sans token -> 401
 *  - la hiérarchie de rôles : SUPER_ADMIN hérite des droits ADMIN/MODERATOR (correction appliquée).
 */
class SecurityAndRoleHierarchyIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("GET /api/v1/faqs sans token -> 401")
    void protectedEndpoint_withoutToken_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/faqs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/v1/users sans token -> 401")
    void usersEndpoint_withoutToken_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("SUPER_ADMIN accède à un endpoint @PreAuthorize(hasRole('ADMIN'/'MODERATOR')) -> 200 (hiérarchie)")
    void superAdmin_inheritsAdminRole_onFaqs() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(get("/api/v1/faqs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SUPER_ADMIN accède à /api/v1/flash-infos (ADMIN/MODERATOR) -> 200 (hiérarchie)")
    void superAdmin_inheritsAdminRole_onFlashInfos() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(get("/api/v1/flash-infos")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SUPER_ADMIN accède à la liste des utilisateurs -> 200")
    void superAdmin_canListUsers() throws Exception {
        String token = superAdminToken();
        mockMvc.perform(get("/api/v1/users")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());
    }
}
