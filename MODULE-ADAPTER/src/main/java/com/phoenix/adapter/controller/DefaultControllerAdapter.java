package com.phoenix.adapter.controller;

import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.core.common.UseCaseStatus;
import com.phoenix.domain.response.ApiResponse;
import com.phoenix.domain.response.HttpStatus;
import com.phoenix.domain.response.ResponseType;

public class DefaultControllerAdapter {
    protected ApiResponse response(UseCaseResponse useCaseResponse){
         String code, message;
         ResponseType type;
        if(useCaseResponse.getStatus() == UseCaseStatus.FAILED){
            code = HttpStatus.INTERNAL_SERVER_ERROR.toString();
            type = ResponseType.EXCEPTION;
        }else {
            code = HttpStatus.OK.toString();
            type = ResponseType.INFO;
        }

        message = useCaseResponse.getMessage();
        return new ApiResponse(code,type,useCaseResponse.getPayload(), message);
    }
}
