package bf.gov.mtdpce.dto.request;

import lombok.Data;

/**
 * Charge utile envoyée par le site public pour tracer une visite/clic.
 */
@Data
public class TrackRequest {
    private String path;
    private String type;      // PAGE_VIEW (défaut) ou CLICK
    private String label;
    private String referrer;
    private String sessionId;
}
