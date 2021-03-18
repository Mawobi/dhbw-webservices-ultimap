package de.dhbw.mosbach.webservices.routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceRoutingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRoutingApplication.class, args);
    }

    @Bean
    public RestTemplate getDefaultRestTemplate() {
        return new RestTemplate();
    }
}
