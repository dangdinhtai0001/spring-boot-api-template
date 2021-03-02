package com.phoenix.infrastructure.repositories.primary;

import com.phoenix.domain.entity.DomainUser;
import com.phoenix.infrastructure.config.PrimaryPersistenceConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository(value = "UserRepositoryImp")
public class UserRepositoryImp {

    @PersistenceContext(unitName = PrimaryPersistenceConfig.PERSISTENCE_UNIT_NAME)
    private final EntityManager entityManager;

    public UserRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public  DomainUser findUserByEmailOrUsername(String name){

        return null;
    }
}
