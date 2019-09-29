package fun.vyse.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
public class VyseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VyseApplication.class,args);
    }
}
