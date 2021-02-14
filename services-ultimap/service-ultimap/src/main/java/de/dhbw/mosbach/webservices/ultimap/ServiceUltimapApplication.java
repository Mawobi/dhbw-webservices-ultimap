package de.dhbw.mosbach.webservices.ultimap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceUltimapApplication {

    public static void main (String[] args) {
        SpringApplication.run(ServiceUltimapApplication.class, args);
    }

    @Bean
    public RestTemplate getDefaultRestTemplate() {
        return new RestTemplate();
    }
}
