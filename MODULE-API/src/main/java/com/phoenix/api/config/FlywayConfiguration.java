package com.phoenix.api.config;

import com.phoenix.infrastructure.config.FlywayConfig;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;

/**
 * Flyway Configuration
 */
@Configuration
public class FlywayConfiguration {
    private final FlywayConfig flywayConfig;

    public FlywayConfiguration() throws IOException {
        flywayConfig = new FlywayConfig();
    }

    @Bean(name = "InitializePrimaryFlyway")
    public Flyway flyway() throws IOException {
        return flywayConfig.initializeFlyway("primary");
    }

    @Bean(name = "InitializePrimaryFlywayMigrate")
    @DependsOn("InitializePrimaryFlyway")
    public void migrateFlyway() throws IOException {
        if (this.flywayConfig.isMigrate()) {
            this.flywayConfig.migrate(this.flyway());
        }
    }

}
