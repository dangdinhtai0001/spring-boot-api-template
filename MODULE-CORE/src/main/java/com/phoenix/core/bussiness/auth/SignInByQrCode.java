package com.phoenix.core.bussiness.auth;

import com.phoenix.common.mfa.generator.CodeGenerator;
import com.phoenix.common.mfa.generator.DefaultCodeGenerator;
import com.phoenix.common.mfa.verifier.CodeVerifier;
import com.phoenix.common.mfa.verifier.DefaultCodeVerifier;
import com.phoenix.common.security.KeyProvider;
import com.phoenix.common.security.TokenProvider;
import com.phoenix.common.util.SystemTimeProvider;
import com.phoenix.common.util.TimeProvider;
import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.bussiness.UseCaseUtils;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.core.common.UseCaseStatus;
import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.SignInByQrCodePayload;

import java.util.NoSuchElementException;
import java.util.Optional;

public class SignInByQrCode implements UseCase<SignInByQrCodePayload, AccessToken> {

    private final CodeVerifier verifier;
    private final UserRepositoryPort userRepository;
    private final TokenProvider tokenProvider;

    public SignInByQrCode(UserRepositoryPort userRepository,
                          TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    }

    @Override
    public void validate(SignInByQrCodePayload payload) {

    }

    @Override
    public UseCaseResponse<AccessToken> execute(SignInByQrCodePayload payload) {
        try {
            //STEP-1: get user secret
            String code = payload.getCode();
            String username = payload.getUsername();
            String key = userRepository.findSecretByUsername(username);

            //STEP-2: verifiy code
            if (verifier.isValidCode(key, code)) {
                Optional<DomainUser> optional = userRepository.findUserByEmailOrUsername(username);

                AccessToken token = UseCaseUtils.generateAccessToken(optional, tokenProvider);

                return new UseCaseResponse<>(UseCaseStatus.SUCCESS, "", token);
            } else {
                return new UseCaseResponse<>(UseCaseStatus.FAILED, "Invalid code...", null);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            return new UseCaseResponse<>(UseCaseStatus.FAILED, "Can't not find user from database", null);
        } catch (Exception e) {
            //e.printStackTrace();
            return new UseCaseResponse<>(UseCaseStatus.FAILED, e.getMessage(), null);
        }
    }


}
