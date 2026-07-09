package bf.gov.mtdpce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Réponse paginée pour la navigation dans les documents publics (Règlementation /
 * Stratégie & Politiques) avec les facettes (compteurs par catégorie), calculées
 * côté serveur pour éviter tout chargement complet côté client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentBrowseResponse {

    private List<DocumentResponse> content;
    private long totalElements;
    private int totalPages;
    /** Page courante (0-based). */
    private int number;
    private int size;
    /** Catégorie -> nombre de documents (sur l'ensemble du type, indépendamment de la page). */
    private Map<String, Long> facets;
}
