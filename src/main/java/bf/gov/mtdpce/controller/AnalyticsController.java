package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.response.AnalyticsOverview;
import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.request.TrackRequest;
import bf.gov.mtdpce.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Suivi de fréquentation du site (DCRP)")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /** Endpoint public appelé par le site à chaque navigation/clic. */
    @PostMapping("/track")
    @Operation(summary = "Enregistrer une visite ou un clic")
    public ResponseEntity<Void> track(@RequestBody TrackRequest request) {
        analyticsService.track(request);
        return ResponseEntity.noContent().build();
    }

    /** Vue d'ensemble des statistiques (réservé admin/modérateur). */
    @GetMapping("/overview")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Statistiques de fréquentation")
    public ResponseEntity<ApiResponse<AnalyticsOverview>> getOverview() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getOverview()));
    }
}
