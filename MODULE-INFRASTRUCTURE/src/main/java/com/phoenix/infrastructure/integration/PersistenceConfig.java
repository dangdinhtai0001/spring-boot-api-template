package com.phoenix.infrastructure.integration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public interface PersistenceConfig {
    public DataSource createDataSource();

    public LocalContainerEntityManagerFactoryBean createLocalContainerEntityManagerFactory(DataSource dataSource);

    public EntityManagerFactory createEntityManagerFactory(
            LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean);

    public PlatformTransactionManager createTransactionManagerBean(EntityManagerFactory entityManagerFactory);

    public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory);

    public AuditorAware<String> createAuditorAware();
}
