package com.phoenix.infrastructure.config;

import com.phoenix.infrastructure.constant.DataSourceConstant;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.exception.FlywayValidateException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FlywayConfig {
//    private final String CONFIG_FILE = "-flyway.properties";

    public FlywayConfig() {
    }

    /**
     * @param name : datasource's type name like: primary, secondary,...
     * @return
     * @throws IOException
     */
    @Deprecated
    private Properties getProperties(String name) throws IOException {
        String configFileName = name + DataSourceConstant.FLYWAY_CONFIG_FILE_POSTFIX;
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(configFileName).getFile());
        FileInputStream fileInputStream = new FileInputStream(configFile);

        Properties properties = new Properties();
        properties.load(fileInputStream);

        return properties;
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

        FluentConfiguration configuration = Flyway.configure()
                .locations("classpath:/migration")
                .baselineOnMigrate(Boolean.parseBoolean(properties.getProperty("baselineOnMigrate")))
                .baselineVersion(properties.getProperty("baselineVersion"))
                .sqlMigrationPrefix(properties.getProperty("sqlMigrationPrefix"))
                .sqlMigrationSeparator(properties.getProperty("sqlMigrationSeparator"))
                .validateOnMigrate(Boolean.parseBoolean(properties.getProperty("validateOnMigrate")));

        return configuration;
    }

    private FluentConfiguration loadDataSourceConfiguration(String name, FluentConfiguration fluentConfiguration)
            throws IOException {
        Properties properties = getProperties(name, DataSourceConstant.HIKARICP_CONFIG_FILE_POSTFIX);

        String url = properties.getProperty("jdbcUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        fluentConfiguration.dataSource(url, username, password);

        return fluentConfiguration;
    }

    public Flyway initializeFlyway(String name) throws IOException {
//        Flyway flyway = loadConfiguration(name).load();
        FluentConfiguration configuration = loadConfiguration(name);
        configuration = loadDataSourceConfiguration(name, configuration);

        Flyway flyway = configuration.load();

        return flyway;
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
