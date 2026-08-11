package bf.gov.matm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test de fumée : vérifie que le contexte Spring complet démarre
 * (toutes les beans, la sécurité, la JPA et la connexion PostgreSQL).
 */
@SpringBootTest
class MatmApplicationTests {

    @Test
    void contextLoads() {
        // Si le contexte ne se charge pas, ce test échoue.
    }
}
