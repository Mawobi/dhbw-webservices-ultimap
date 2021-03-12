package de.dhbw.mosbach.webservices.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceWeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceWeatherApplication.class, args);
    }

    @Bean
    public RestTemplate getDefaultRestTemplate() {
        return new RestTemplate();
    }
}
