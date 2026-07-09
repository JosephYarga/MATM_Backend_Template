package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.response.SearchResponse;
import bf.gov.mtdpce.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Recherche unifiée publique sur tout le contenu du portail.
 * Endpoint public (couvert par /api/v1/public/**).
 */
@RestController
@RequestMapping("/api/v1/public/search")
@Tag(name = "Recherche", description = "Recherche unifiée sur tout le contenu public")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    @Operation(summary = "Recherche globale",
            description = "Recherche transversale (actualités, communiqués, projets, documents, agenda, "
                    + "événements, services, FAQ) avec pertinence, facettes, surbrillance et pagination.")
    public ResponseEntity<ApiResponse<SearchResponse>> search(
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "types", required = false) List<String> types,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false) String sort) {

        SearchResponse result = searchService.search(query, types, page, size, sort);
        return ResponseEntity.ok(ApiResponse.success("Recherche effectuée", result));
    }
}
