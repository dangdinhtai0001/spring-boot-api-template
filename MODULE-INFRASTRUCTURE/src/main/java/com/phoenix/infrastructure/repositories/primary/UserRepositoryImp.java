package com.phoenix.infrastructure.repositories.primary;

import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.persistence.primary.UserEntity;
import com.phoenix.infrastructure.config.PrimaryPersistenceConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
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

        long id = 0;
        String username = null, email = null, password = null;
        boolean isLocked = false, isEnabled = false;
        List<String> roles = null, permissions = null;

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

        if (username == null && email == null) {
            return null;
        }

        DomainUser domainUser = new DomainUser();

        domainUser.setId(id);
        domainUser.setUsername(username);
        domainUser.setEmail(email);
        domainUser.setPassword(password);
        domainUser.setLocked(isLocked);
        domainUser.setEnabled(isEnabled);
        domainUser.setRoles(com.phoenix.common.lang.Collections.listToSet(roles));
        domainUser.setPermissions(com.phoenix.common.lang.Collections.listToSet(permissions));

        return domainUser;
    }

    /**
     * @param user
     * @return
     */
    @Transactional
    public int createUser(DomainUser user) {
        String insertUserSql = "INSERT INTO user (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";

        Query query = this.entityManager.createNativeQuery(insertUserSql);

        query.setParameter(1, user.getUsername());
        query.setParameter(2, user.getEmail());
        query.setParameter(3, user.getPassword());

        int result = query.executeUpdate();

        Set<String> roles = user.getRoles();
        StringBuilder insertUserRoleSqlBuilder = new StringBuilder();

        if (roles == null || roles.isEmpty()) {
            return result;
        }

        insertUserRoleSqlBuilder.append("insert into user_role (user_id, role_id) select user.id, role.id from role, user where (user.email = ? or user.USERNAME = ?) and role.NAME in (");
        List<String> list = new LinkedList<>();

        for (String role : roles) {
            insertUserRoleSqlBuilder.append("?,");
            list.add(role);
        }

        insertUserRoleSqlBuilder.deleteCharAt(insertUserRoleSqlBuilder.length() - 1);

        insertUserRoleSqlBuilder.append(")");

        query = null;
        query = this.entityManager.createNativeQuery(insertUserRoleSqlBuilder.toString());

        query.setParameter(1, user.getEmail());
        query.setParameter(2, user.getUsername());

        for (int i = 0; i < list.size(); i++) {
            query.setParameter(i + 3, list.get(i));
        }

        result = query.executeUpdate();

        return result;
    }
}
