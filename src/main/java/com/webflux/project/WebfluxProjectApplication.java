package com.webflux.project;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@ComponentScan(basePackages = {"com.webflux.project.controllers",
        "com.webflux.project.services","org.springframework.web.reactive.function.client.*","com.webflux.project.configurations", "com.webflux.project.repositories"})

public class WebfluxProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebfluxProjectApplication.class, args);
    }

}
