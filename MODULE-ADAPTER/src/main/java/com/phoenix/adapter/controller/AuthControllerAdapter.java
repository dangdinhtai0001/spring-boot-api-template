package com.phoenix.adapter.controller;

import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.CreateAccountPayload;
import com.phoenix.domain.payload.LoginByPasswordPayload;
import com.phoenix.domain.persistence.primary.UserEntity;
import com.phoenix.domain.response.ApiResponse;
import com.phoenix.domain.response.HttpStatus;
import com.phoenix.domain.response.ResponseType;

public class AuthControllerAdapter {

    private final UseCase createAccount;
    private final UseCase signInByPassword;

    public AuthControllerAdapter(UseCase createAccount,
                                 UseCase signInByPassword) {
        this.createAccount = createAccount;
        this.signInByPassword = signInByPassword;
    }

    public ApiResponse createAccount(CreateAccountPayload payload) {
        try {
            UseCaseResponse<UserEntity> user = createAccount.execute(payload);

            return new ApiResponse<>(
                    String.valueOf(HttpStatus.CREATED.value()),
                    ResponseType.INFO, user.getPayload(),
                    "Enums.Message.RESOURCE_CREATED.value()");
        } catch (Exception e) {
            //e.printStackTrace();
            return new ApiResponse<>(
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    ResponseType.EXCEPTION, null,
                    e.getMessage());
        }
    }

    public ApiResponse signInByPassword(LoginByPasswordPayload payload) {
        try {
            UseCaseResponse<AccessToken> token = signInByPassword.execute(payload);

            return new ApiResponse<>(
                    String.valueOf(HttpStatus.CREATED.value()),
                    ResponseType.INFO, token.getPayload(),
                    "Enums.Message.RESOURCE_CREATED.value()");
        } catch (Exception e) {
            //e.printStackTrace();
            return new ApiResponse<>(
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    ResponseType.EXCEPTION, null,
                    e.getMessage());
        }
    }
}
