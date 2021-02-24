package com.phoenix.core.common;

public class UseCaseResponse <T>{
    private UseCaseStatus status;
    private String message;
    private T payload;

    public UseCaseResponse(UseCaseStatus status, String message, T payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    public UseCaseResponse() {
    }

    public UseCaseStatus getStatus() {
        return status;
    }

    public void setStatus(UseCaseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "UseCaseResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}
