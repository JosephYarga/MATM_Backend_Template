package bf.gov.mtdpce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Base des tests d'intégration / fonctionnels.
 *
 * - @SpringBootTest : charge le contexte complet (contre le PostgreSQL local, comme l'app réelle).
 * - @AutoConfigureMockMvc : permet d'appeler les endpoints via MockMvc (sans ouvrir de port).
 * - @Transactional : chaque test est annulé (rollback) à la fin -> la base n'est pas polluée.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    /** Identifiants seedés par DataInitializer. */
    protected static final String SUPERADMIN_USER = "superadmin";
    protected static final String SUPERADMIN_PASS = "SuperAdmin2024";

    /** Se connecte et renvoie un JWT valide pour les tests d'endpoints protégés. */
    protected String loginAndGetToken(String username, String password) throws Exception {
        String body = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("token").asText();
    }

    protected String superAdminToken() throws Exception {
        return loginAndGetToken(SUPERADMIN_USER, SUPERADMIN_PASS);
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }
}
