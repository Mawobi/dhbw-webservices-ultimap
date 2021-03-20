package de.dhbw.mosbach.webservices.carinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceCarinfoApplication {

    public static void main (String[] args) {
        SpringApplication.run(ServiceCarinfoApplication.class, args);
    }

    @Bean
    RestTemplate getDefaultRestTemplate() {
        return new RestTemplate();
    }

}
