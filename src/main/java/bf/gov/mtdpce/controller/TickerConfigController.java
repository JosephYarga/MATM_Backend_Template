package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.entity.TickerConfig;
import bf.gov.mtdpce.service.TickerConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticker-config")
@Tag(name = "Config bandeau Flash Info", description = "Réglage global du défilement du bandeau")
public class TickerConfigController {

    @Autowired
    private TickerConfigService tickerConfigService;

    @GetMapping("/public")
    @Operation(summary = "Configuration du bandeau (public)")
    public ResponseEntity<TickerConfig> getPublicConfig() {
        return ResponseEntity.ok(tickerConfigService.getConfig());
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @Operation(summary = "Modifier la durée de défilement du bandeau")
    public ResponseEntity<TickerConfig> updateConfig(@RequestBody TickerConfig request) {
        return ResponseEntity.ok(tickerConfigService.updateScrollDuration(request.getScrollDuration()));
    }
}
