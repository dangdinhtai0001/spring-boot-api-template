package com.phoenix.api.controller;

import com.phoenix.domain.payload.CreateAccountPayload;
import com.phoenix.domain.payload.SignInByPasswordPayload;
import com.phoenix.domain.response.ApiResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAuthController {
    @Autowired
    private AuthController authController;

    @Test
    public void testCreateAccount(){
        CreateAccountPayload payload = new CreateAccountPayload();

        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        payload.setUsername("dangdinhtai");
        payload.setPassword("123456");
        payload.setEmail("dangdinhtai0001@gmail.com");
        payload.setRoles(roles);

        ApiResponse apiResponse = this.authController.createAccount(payload);

        System.out.println(apiResponse);
    }

    @Test
    public void testSignInByPassword(){
        SignInByPasswordPayload payload = new SignInByPasswordPayload();

        payload.setUsername("dangdinhtai");
        payload.setPassword("123456");

        ApiResponse apiResponse = this.authController.signInByPassword(payload);

        System.out.println(apiResponse);
    }
}
