package com.phoenix.adapter.controller;

import com.phoenix.common.lang.Strings;
import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.bussiness.auth.SignInByQrCode;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.CreateAccountPayload;
import com.phoenix.domain.payload.CreateQrForSignInPayload;
import com.phoenix.domain.payload.SignInByPasswordPayload;
import com.phoenix.domain.payload.SignInByQrCodePayload;
import com.phoenix.domain.response.ApiResponse;
import com.phoenix.domain.response.HttpStatus;
import com.phoenix.domain.response.ResponseType;

public class AuthControllerAdapter extends DefaultControllerAdapter {

    private final UseCase createAccount;
    private final UseCase signInByPassword;
    private final UseCase createQrCodeForSignIn;
    private final UseCase signInByQrCode;

    public AuthControllerAdapter(UseCase createAccount,
                                 UseCase signInByPassword,
                                 UseCase createQrCodeForSignIn,
                                 UseCase signInByQrCode
                                 ) {
        this.createAccount = createAccount;
        this.signInByPassword = signInByPassword;
        this.createQrCodeForSignIn = createQrCodeForSignIn;
        this.signInByQrCode = signInByQrCode;
    }

    public ApiResponse createAccount(CreateAccountPayload payload) {

        UseCaseResponse response = createAccount.execute(payload);

        return response(response);

    }

    public ApiResponse signInByPassword(SignInByPasswordPayload payload) {

        UseCaseResponse<AccessToken> token = signInByPassword.execute(payload);

        return response(token);
    }

    public ApiResponse createQrCodeForSignIn(CreateQrForSignInPayload payload) {
        UseCaseResponse<String> qrCode = createQrCodeForSignIn.execute(payload);
        return response(qrCode);
    }

    public ApiResponse signInByQrCode(SignInByQrCodePayload payload) {
        UseCaseResponse<AccessToken> response = signInByQrCode.execute(payload);
        return response(response);
    }
}
