package bf.gov.mtdpce.service;

import bf.gov.mtdpce.entity.TickerConfig;
import bf.gov.mtdpce.repository.TickerConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TickerConfigService {

    @Autowired
    private TickerConfigRepository tickerConfigRepository;

    /** Retourne la configuration (crée la ligne par défaut si elle n'existe pas encore). */
    public TickerConfig getConfig() {
        return tickerConfigRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> tickerConfigRepository.save(
                        TickerConfig.builder().scrollDuration(30).build()));
    }

    public TickerConfig updateScrollDuration(Integer scrollDuration) {
        TickerConfig config = getConfig();
        if (scrollDuration != null && scrollDuration > 0) {
            config.setScrollDuration(scrollDuration);
        }
        return tickerConfigRepository.save(config);
    }
}
