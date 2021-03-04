package com.phoenix.infrastructure.repositories.primary;

import com.phoenix.domain.model.UrlAntMatcherModel;
import com.phoenix.infrastructure.config.PrimaryPersistenceConfig;
import com.phoenix.infrastructure.repositories.DefaultRepositoryImp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Repository(value = "ApiUrlRepositoryImp")
public class ApiUrlRepositoryImp extends DefaultRepositoryImp {

    @PersistenceContext(unitName = PrimaryPersistenceConfig.PERSISTENCE_UNIT_NAME)
    private final EntityManager entityManager;

    public ApiUrlRepositoryImp(@Qualifier("PrimaryEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * select au.NAME name, au.URL url, '' permissions
     * from api_url au
     * where au.id not in (select aup.URL_ID from api_url_permission aup)
     * <p>
     * sample:
     * <p>
     * ====================================================
     * SIGN_IN_BY_PASSWORD | /auth/sign-in/password | ""
     * ====================================================
     */
    public List<UrlAntMatcherModel> findAllPermitAllAntMatchers() {
        String sql = "select au.NAME name, au.URL url, '' permissions\n" +
                "from api_url au\n" +
                "where au.id not in (select aup.URL_ID from api_url_permission aup)";
        List<Object[]> result = getResultList(this.entityManager, sql);

        return mapResultList2UrlAntMatcherModel(result);
    }

    /**
     * select au.NAME name, au.URL url, group_concat(p.NAME) permissions
     * from api_url au
     * join api_url_permission aup on au.ID = aup.URL_ID
     * join permission p on aup.PERMISSION_ID = p.ID
     * group by au.id;
     * <p>
     * sample:
     * <p>
     * ====================================================
     * CREATE_ACCOUNT,/auth/create-account,USER__CREATE
     * CREATE_QR_CODE,/auth/create-qr-code,PROFILE__READ
     * ====================================================
     */
    public List<UrlAntMatcherModel> findAllNeedAuthAntMatchers() {
        String sql = "select au.NAME name, au.URL url, group_concat(p.NAME) permissions\n" +
                "from api_url au\n" +
                "         join api_url_permission aup on au.ID = aup.URL_ID\n" +
                "         join permission p on aup.PERMISSION_ID = p.ID\n" +
                "group by au.id;";
        List<Object[]> result = getResultList(this.entityManager, sql);

        return mapResultList2UrlAntMatcherModel(result);
    }

    private List<UrlAntMatcherModel> mapResultList2UrlAntMatcherModel(List<Object[]> result) {
        List<UrlAntMatcherModel> urlAntMatcherModelList = new LinkedList<>();

        for (Object[] record : result) {
            String name, url;
            String[] permissions;

            name = String.valueOf(record[0]);
            url = String.valueOf(record[1]);
            permissions = record[2].toString().split(",");

            urlAntMatcherModelList.add(new UrlAntMatcherModel(name, url, permissions));

        }

        return urlAntMatcherModelList;
    }
}
