package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Point de contrôle de disponibilité (health-check) pour la supervision / le déploiement.
 * Public et léger : ne dépend d'aucune ressource externe.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Santé", description = "Disponibilité de l'API")
public class HealthController {

    @GetMapping
    @Operation(summary = "État de l'API")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "UP");
        body.put("time", LocalDateTime.now().toString());
        return ResponseEntity.ok(ApiResponse.success("Service disponible", body));
    }
}
