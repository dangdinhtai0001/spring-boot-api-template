package com.phoenix.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring Boot Application Template.
 */

@SpringBootApplication(scanBasePackages = {
        "com.phoenix.api",
        "com.phoenix.infrastructure.*",
        "com.phoenix.UserInterface.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
    }

}
