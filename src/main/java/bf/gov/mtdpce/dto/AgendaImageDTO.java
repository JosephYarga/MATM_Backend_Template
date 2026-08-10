package bf.gov.mtdpce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaImageDTO {

    private Long id;

    private String imageUrl;

    private Integer displayOrder;
}
