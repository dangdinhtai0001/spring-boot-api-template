package com.phoenix.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * basePackages: we use this field to set the base package of our repository. For instance, for the primary data source,
 * It must point to the package com.phoenix.infrastructure.repositories
 *
 * entityManagerFactoryRef: We use this field to reference the entity manager factory bean defined in the data source
 * configuration file. It is important to take note of the fact that the entityManagerFactoryRef value must match the
 * bean name (if specified via the name field of the @Bean annotation else will default to method name) of the entity
 * manager factory defined in the configuration file.
 *
 * transactionManagerRef: This field references the transaction manager defined in the data source configuration file.
 * Again it is important to ensure that the transactionManagerRef  value matches with the bean name of the transaction
 * manager factory.
 *
 * Important Points to note: ===============
 *
 * entity manager factory bean: Please make sure that you are referencing the correct data source when creating the
 * entity manager factory bean otherwise you will get unexpected results.
 *
 * transaction manager bean: To ensure that you have provided the correct entity manager factory reference for the
 * transaction manager, you may use the @Qualifier annotation.
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.phoenix.infrastructure.repositories.*"},
        entityManagerFactoryRef = "PrimaryEntityManagerFactory",
        transactionManagerRef = "PrimaryTransactionManager")
@EnableTransactionManagement
public class PrimaryPersistenceConfig {
    private final com.phoenix.infrastructure.config.PrimaryPersistenceConfig configuration;

    public PrimaryPersistenceConfig() throws IOException {
        configuration = new com.phoenix.infrastructure.config.PrimaryPersistenceConfig();
    }

    @Primary
    @Bean(name = "PrimaryDataSource")
    public DataSource DataSource() {
        return configuration.createDataSource();
    }

    @Primary
    @Bean(name = "PrimaryLocalContainerEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean LocalContainerEntityManagerFactoryBean() {
        return configuration.createLocalContainerEntityManagerFactory(this.DataSource());
    }

    @Bean(name = "PrimaryEntityManagerFactory")
    public EntityManagerFactory EntityManagerFactory() {
        return configuration.createEntityManagerFactory(this.LocalContainerEntityManagerFactoryBean());
    }

    @Primary
    @Bean(name = "PrimaryTransactionManager")
    public PlatformTransactionManager TransactionManagerBean() {
        return configuration.createTransactionManagerBean(this.EntityManagerFactory());
    }

    @Bean(name = "PrimaryEntityManager")
    public EntityManager EntityManager() {
        return configuration.createEntityManager(this.EntityManagerFactory());
    }

    @Bean(name = "AuditorAware")
    public AuditorAware<String> AuditorAware() {
        return configuration.createAuditorAware();
    }
}
