package com.phoenix.core.bussiness.auth;

import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;

public class CreateQrCodeForAuthenticator implements UseCase<String, String> {
    @Override
    public void validate(String s) {

    }

    @Override
    public UseCaseResponse<String> execute(String s) {
        return null;
    }
}
