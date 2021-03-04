package com.phoenix.api.config;

import com.phoenix.domain.model.UrlAntMatcherModel;
import com.phoenix.infrastructure.repositories.primary.ApiUrlRepositoryImp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "ApplicationUrlLoader")
public class ApplicationUrlLoader {

    private final ApiUrlRepositoryImp apiUrlRepositoryImp;

    public ApplicationUrlLoader(@Qualifier("ApiUrlRepositoryImp") ApiUrlRepositoryImp apiUrlRepositoryImp) {
        this.apiUrlRepositoryImp = apiUrlRepositoryImp;
    }

    public List<UrlAntMatcherModel> getNeedAuthAntMatchers() {
        return apiUrlRepositoryImp.findAllNeedAuthAntMatchers();
    }

    public List<UrlAntMatcherModel> getPermitAllAntMatchers() {
        return apiUrlRepositoryImp.findAllPermitAllAntMatchers();
    }
}
