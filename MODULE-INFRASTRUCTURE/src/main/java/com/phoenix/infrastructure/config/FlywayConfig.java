package com.phoenix.infrastructure.config;

import com.phoenix.infrastructure.constant.DataSourceConstant;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.exception.FlywayValidateException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class FlywayConfig {
    private boolean migrate;

    public FlywayConfig() {
    }

    /**
     * @param name : datasource's type name like: primary, secondary,...
     * @return
     * @throws IOException
     */
    private Properties getProperties(String name, String postfix) throws IOException {
        String configFileName = name + postfix;
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(configFileName).getFile());
        FileInputStream fileInputStream = new FileInputStream(configFile);

        Properties properties = new Properties();
        properties.load(fileInputStream);

        return properties;
    }

    /**
     * @param name: datasource's type name like: primary, secondary,...
     * @return
     * @throws IOException
     */
    private FluentConfiguration loadConfiguration(String name) throws IOException {
        Properties properties = getProperties(name, DataSourceConstant.FLYWAY_CONFIG_FILE_POSTFIX);

        Properties dsProperties = getProperties(name, DataSourceConstant.HIKARICP_CONFIG_FILE_POSTFIX);

        String url = dsProperties.getProperty("jdbcUrl");
        String username = dsProperties.getProperty("username");
        String password = dsProperties.getProperty("password");

        FluentConfiguration configuration = Flyway.configure()
                .dataSource(url, username, password)
//                .locations("classpath:/migration")
                .locations("filesystem:MODULE-INFRASTRUCTURE/src/main/resources/migration")
                .baselineOnMigrate(Boolean.parseBoolean(properties.getProperty("baselineOnMigrate")))
                .baselineVersion(properties.getProperty("baselineVersion"))
                .sqlMigrationPrefix(properties.getProperty("sqlMigrationPrefix"))
                .sqlMigrationSeparator(properties.getProperty("sqlMigrationSeparator"))
                .validateOnMigrate(Boolean.parseBoolean(properties.getProperty("validateOnMigrate")));

        this.migrate = Boolean.parseBoolean(properties.getProperty("migrate"));

        return configuration;
    }

    public Flyway initializeFlyway(String name) throws IOException {
//        Flyway flyway = loadConfiguration(name).load();
        FluentConfiguration configuration = loadConfiguration(name);

        return configuration.load();
    }

    public void migrate(Flyway flyway) {
        try {
            flyway.migrate();
        } catch (FlywayValidateException e) {
            flyway.repair();
            flyway.migrate();
        }
    }
}
