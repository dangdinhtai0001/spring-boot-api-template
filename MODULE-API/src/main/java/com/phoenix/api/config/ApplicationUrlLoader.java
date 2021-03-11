package com.phoenix.api.config;

import com.phoenix.domain.model.UrlAntMatcherModel;
import com.phoenix.infrastructure.repositories.primary.ApiUrlRepositoryImp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "ApplicationUrlLoader")
public class ApplicationUrlLoader {

    private final List<UrlAntMatcherModel> needAuthAntMatchers;
    private final List<UrlAntMatcherModel> permitAllAntMatchers;
    private Map allAntMatchers;

    public ApplicationUrlLoader(@Qualifier("ApiUrlRepositoryImp") ApiUrlRepositoryImp apiUrlRepositoryImp) {
        this.needAuthAntMatchers = apiUrlRepositoryImp.findAllNeedAuthAntMatchers();
        this.permitAllAntMatchers = apiUrlRepositoryImp.findAllPermitAllAntMatchers();
        this.allAntMatchers = createAllAntMatchersMap();
    }

    public List<UrlAntMatcherModel> getNeedAuthAntMatchers() {
        return needAuthAntMatchers;
    }

    public List<UrlAntMatcherModel> getPermitAllAntMatchers() {
        return permitAllAntMatchers;
    }

    public Map getAllAntMatchers() {
        return allAntMatchers;
    }

    private Map createAllAntMatchersMap() {
        Map map = new HashMap();
        for (UrlAntMatcherModel model : this.needAuthAntMatchers) {
            map.put(model.getName(), model.getUrl());
        }

        for (UrlAntMatcherModel model : this.permitAllAntMatchers) {
            map.put(model.getName(), model.getUrl());
        }

        return map;
    }
}
