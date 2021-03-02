package com.phoenix.adapter.config;

import com.phoenix.adapter.controller.AuthControllerAdapter;
import com.phoenix.adapter.map.Mapper;
import com.phoenix.adapter.repository.UserRepositoryAdapter;
import com.phoenix.adapter.security.AuthenticationManagerAdapter;
import com.phoenix.adapter.security.PasswordEncoderAdapter;
import com.phoenix.common.security.DefaultTokenProvider;
import com.phoenix.common.security.KeyProvider;
import com.phoenix.common.security.TokenProvider;
import com.phoenix.core.bussiness.auth.CreateAccount;
import com.phoenix.core.bussiness.auth.SignInByPassword;
import com.phoenix.core.port.PasswordEncoderPort;
import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import com.phoenix.infrastructure.repositories.primary.UserRepositoryImp;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.File;
import java.io.IOException;

public class SpringBootConfig implements AdapterConfig {
    private final KeyProvider keyProvider;
    private final AuthenticationManagerAdapter authenticationManager;
    private final PasswordEncoderPort passwordEncoderPort;
    private final UserRepositoryPort userRepositoryPort;

    public SpringBootConfig(UserRepository userRepository,
                            UserRepositoryImp userRepositoryImp,
                            AuthenticationManager authenticationManager,
                            File keyFile
    ) throws IOException, ClassNotFoundException {
        this.passwordEncoderPort = new PasswordEncoderAdapter();
        this.keyProvider = new KeyProvider(keyFile);

        this.authenticationManager = new AuthenticationManagerAdapter(authenticationManager);
        this.userRepositoryPort = new UserRepositoryAdapter(userRepository, userRepositoryImp);
    }

    //=======================================================
    //                   CONTROLLER
    //=======================================================

    public AuthControllerAdapter authControllerAdapter() {
        return new AuthControllerAdapter(
                this.createAccountUseCase(),
                this.signInByPasswordUseCase()
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

    //=======================================================
    //                   DEPENDENCY
    //=======================================================

    public KeyProvider createKeyProvider() {
        return this.keyProvider;
    }

    public TokenProvider createTokenProvider() {
        return new DefaultTokenProvider(this.keyProvider);
    }

    public AuthenticationManagerAdapter createAuthenticationManagerAdapter() {
        return this.authenticationManager;
    }
}
