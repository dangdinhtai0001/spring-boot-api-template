package com.phoenix.adapter.config;

import com.phoenix.adapter.controller.AuthControllerAdapter;
import com.phoenix.adapter.repository.UserRepositoryAdapter;
import com.phoenix.adapter.security.AuthenticationManagerAdapter;
import com.phoenix.adapter.security.PasswordEncoderAdapter;
import com.phoenix.common.security.*;
import com.phoenix.core.bussiness.auth.CreateAccount;
import com.phoenix.core.bussiness.auth.CreateQrCodeForSignIn;
import com.phoenix.core.bussiness.auth.SignInByPassword;
import com.phoenix.core.bussiness.auth.SignInByQrCode;
import com.phoenix.core.port.PasswordEncoderPort;
import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import com.phoenix.infrastructure.repositories.primary.UserRepositoryImp;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.File;
import java.io.IOException;

public class SpringBootConfig implements AdapterConfig {
    private final KeyProvider base64KeyProvider;
    private final KeyProvider base32KeyProvider;
    private final AuthenticationManagerAdapter authenticationManager;
    private final PasswordEncoderPort passwordEncoderPort;
    private final UserRepositoryPort userRepositoryPort;

    public SpringBootConfig(UserRepository userRepository,
                            UserRepositoryImp userRepositoryImp,
                            AuthenticationManager authenticationManager,
                            File keyFile,
                            File shortKeyFile
    ) throws IOException, ClassNotFoundException {
        this.passwordEncoderPort = new PasswordEncoderAdapter();
        this.base64KeyProvider = new Base64KeyProvider(keyFile);
        this.base32KeyProvider = new Base32KeyProvider(shortKeyFile);

        this.authenticationManager = new AuthenticationManagerAdapter(authenticationManager);
        this.userRepositoryPort = new UserRepositoryAdapter(userRepository, userRepositoryImp);
    }

    //=======================================================
    //                   CONTROLLER
    //=======================================================

    public AuthControllerAdapter authControllerAdapter() {
        return new AuthControllerAdapter(
                this.createAccountUseCase(),
                this.signInByPasswordUseCase(),
                this.createQrCodeForSignIn(),
                this.signInByQrCode()
        );
    }

    //=======================================================
    //                   USE CASE
    //=======================================================

    public CreateAccount createAccountUseCase() {
        return new CreateAccount(this.passwordEncoderPort, this.userRepositoryPort);
    }

    public SignInByPassword signInByPasswordUseCase() {
        return new SignInByPassword(this.authenticationManager, this.userRepositoryPort, this.createTokenProvider());
    }

    public CreateQrCodeForSignIn createQrCodeForSignIn() {
        return new CreateQrCodeForSignIn(this.createTokenProvider(), this.userRepositoryPort);
    }

    public SignInByQrCode signInByQrCode() {
        return new SignInByQrCode(this.userRepositoryPort, this.createTokenProvider());
    }

    //=======================================================
    //                   DEPENDENCY
    //=======================================================

    public KeyProvider createBase64KeyProvider() {
        return this.base64KeyProvider;
    }

    public KeyProvider createBase32KeyProvider() {
        return this.base32KeyProvider;
    }

    public TokenProvider createTokenProvider() {
        return new DefaultTokenProvider(this.base64KeyProvider);
    }

    public AuthenticationManagerAdapter createAuthenticationManagerAdapter() {
        return this.authenticationManager;
    }
}
