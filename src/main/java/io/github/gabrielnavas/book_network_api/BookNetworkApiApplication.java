package io.github.gabrielnavas.book_network_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
@EnableAsync
public class BookNetworkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApiApplication.class, args);
    }

}
