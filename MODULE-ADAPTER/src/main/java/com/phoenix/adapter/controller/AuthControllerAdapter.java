package com.phoenix.adapter.controller;

import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.CreateAccountPayload;
import com.phoenix.domain.payload.SignInByPasswordPayload;
import com.phoenix.domain.response.ApiResponse;
import com.phoenix.domain.response.HttpStatus;
import com.phoenix.domain.response.ResponseType;

public class AuthControllerAdapter extends DefaultControllerAdapter {

    private final UseCase createAccount;
    private final UseCase signInByPassword;

    public AuthControllerAdapter(UseCase createAccount,
                                 UseCase signInByPassword) {
        this.createAccount = createAccount;
        this.signInByPassword = signInByPassword;
    }

    public ApiResponse createAccount(CreateAccountPayload payload) {

        UseCaseResponse response = createAccount.execute(payload);

        return response(response);

    }

    public ApiResponse signInByPassword(SignInByPasswordPayload payload) {

        UseCaseResponse<AccessToken> token = signInByPassword.execute(payload);

        return response(token);
    }
}
