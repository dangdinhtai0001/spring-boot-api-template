package com.phoenix.core.port;

public interface AuthenticationManagerPort {
    public void authenticate(String username, String password);
}
