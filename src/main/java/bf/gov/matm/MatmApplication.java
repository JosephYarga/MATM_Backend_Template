package bf.gov.matm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MatmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatmApplication.class, args);
    }
}
