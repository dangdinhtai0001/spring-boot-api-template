package com.phoenix.infrastructure.repositories.primary;

import com.phoenix.domain.entity.DomainUser;
import com.phoenix.infrastructure.config.PrimaryPersistenceConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository(value = "UserRepositoryImp")
public class UserRepositoryImp {

    @PersistenceContext(unitName = PrimaryPersistenceConfig.PERSISTENCE_UNIT_NAME)
    private final EntityManager entityManager;

    public UserRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * @param name
     * @return
     * @Query select u.id           id,
     * u.USERNAME     username,
     * u.EMAIL        email,
     * u.PASSWORD     password,
     * u.LOCKED       isLocked,
     * u.ENABLED      isEnabled,
     * up.roles       roles,
     * up.permissions permissions
     * from user u
     * join view_user_permissions up on u.ID = up.user_id where u.USERNAME = ? or u.EMAIL = ?;
     */
    public DomainUser findUserByEmailOrUsername(String name) {
        String sql = "select u.id           id,\n" +
                "       u.USERNAME     username,\n" +
                "       u.EMAIL        email,\n" +
                "       u.PASSWORD     password,\n" +
                "       u.LOCKED       isLocked,\n" +
                "       u.ENABLED      isEnabled,\n" +
                "       up.roles       roles,\n" +
                "       up.permissions permissions\n" +
                "from user u\n" +
                "         join view_user_permissions up on u.ID = up.user_id where u.USERNAME = ? or u.EMAIL = ?";
        Query query = this.entityManager.createNativeQuery(sql);


        query.setParameter(1, name);
        query.setParameter(2, name);

        List<Object[]> result = query.getResultList();

        long id;
        String  username, email, password;
        boolean isLocked, isEnabled;
        List<String> roles, permissions;

        for (Object[] record : result) {
            id = Long.parseLong(String.valueOf(record[0]));
            username = String.valueOf(record[1]);
            email = String.valueOf(record[2]);
            password = String.valueOf(record[3]);
            isLocked = Boolean.parseBoolean(String.valueOf(record[4]));
            isEnabled = Boolean.parseBoolean(String.valueOf(record[5]));

            roles = Arrays.asList(record[6].toString().split(","));
            permissions = Arrays.asList(record[7].toString().split(","));

            break;
        }

        DomainUser domainUser = new DomainUser();

        domainUser.setId(id);
        domainUser.setUsername(username);

        return null;
    }
}
