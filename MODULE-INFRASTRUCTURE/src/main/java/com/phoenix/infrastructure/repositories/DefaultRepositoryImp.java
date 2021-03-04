package com.phoenix.infrastructure.repositories;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class DefaultRepositoryImp {
    protected List<Object[]> getResultList(EntityManager entityManager, String sql){
        Query query = entityManager.createNativeQuery(sql);

        return query.getResultList();
    }
}
