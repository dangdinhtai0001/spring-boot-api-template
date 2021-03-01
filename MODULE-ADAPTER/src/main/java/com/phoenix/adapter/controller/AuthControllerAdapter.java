package com.phoenix.adapter.controller;

import com.phoenix.core.bussiness.UseCase;
import com.phoenix.domain.payload.RegisterUser;
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

//    public ApiResponse createAccount(RegisterUser registerUser) {
//        try {
//            RegisterUserMapUser mapping = new RegisterUserMapUser();
//
//            User domainUser = signUpUseCase.execute(mapping.convert(registerUser));
//
//
//            ApiResponse<User> userApiResponse = new ApiResponse<User>(
//                    String.valueOf(HttpStatus.CREATED.value()),
//                    ResponseType.INFO, domainUser,
//                    "Enums.Message.RESOURCE_CREATED.value()");
//            return userApiResponse;
//        } catch (Exception e) {
//            //e.printStackTrace();
//            ApiResponse<Exception> exceptionApiResponse = new ApiResponse<>(
//                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
//                    ResponseType.EXCEPTION, null,
//                    e.getMessage());
//            return exceptionApiResponse;
//        }
//    }
}
