package com.phoenix.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLoadBean {
    @Qualifier("PrimaryLocalContainerEntityManagerFactoryBean")
    @Autowired
    private org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    @Test
    public void testLoadLocalContainerEntityManagerFactoryBean(){
        Assert.assertNotNull(localContainerEntityManagerFactoryBean);
        Map<String, Object> map =localContainerEntityManagerFactoryBean.getJpaPropertyMap();

        System.out.println(map);
    }
}
