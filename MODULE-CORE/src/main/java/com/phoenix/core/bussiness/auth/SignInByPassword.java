package com.phoenix.core.bussiness.auth;

import com.phoenix.common.exception.runtime.UserValidationException;
import com.phoenix.common.lang.Strings;
import com.phoenix.common.security.Scope;
import com.phoenix.common.security.component.Claims;
import com.phoenix.common.security.component.DefaultClaims;
import com.phoenix.common.util.IdGenerator;
import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.core.common.UseCaseStatus;
import com.phoenix.core.port.AuthenticationManagerPort;
import com.phoenix.core.port.TokenProviderPort;
import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.model.AccessToken;
import com.phoenix.domain.payload.LoginByPasswordPayload;

import java.util.Date;
import java.util.Optional;

public class SignInByPassword implements UseCase<LoginByPasswordPayload, AccessToken> {

    private final AuthenticationManagerPort authenticationManager;
    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;

    public SignInByPassword(AuthenticationManagerPort authenticationManager,
                            UserRepositoryPort userRepository,
                            TokenProviderPort tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void validate(LoginByPasswordPayload loginByPasswordPayload) {
        if (loginByPasswordPayload == null)
            throw new UserValidationException("User should not be null");
        if (!Strings.isNullOrNotBlank(loginByPasswordPayload.getUsername()))
            throw new UserValidationException("Username can be null but not blank.");
        if (!Strings.isNullOrNotBlank(loginByPasswordPayload.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
        if (!Strings.hasLength(loginByPasswordPayload.getEmail()) && !Strings.hasLength(loginByPasswordPayload.getUsername()))
            throw new UserValidationException("Username and email must not be concurrently empty.");
    }

    @Override
    public UseCaseResponse<AccessToken> execute(LoginByPasswordPayload payload) {
        try {
            //STEP-1. validate input
            validate(payload);

            //STEP-2. Authenticate user in payload
            if (Strings.hasLength(payload.getUsername())) {
                authenticationManager.authenticate(payload.getUsername(), payload.getPassword());
            }

            //STEP-3. Fetch user info from database
            Optional<DomainUser> optional = userRepository.findUserByEmailOrUsername(payload.getUsername());

            //STEP-4. Generate access token
            if (optional.isPresent()) {
                AccessToken token = generateAccessToken(optional);
                return new UseCaseResponse<>(UseCaseStatus.SUCCESS, "", token);
            } else {
                return new UseCaseResponse<>(UseCaseStatus.FAILED, "Cannot store to database!!!", null);
            }
        } catch (Exception e) {
            return new UseCaseResponse<>(UseCaseStatus.FAILED, e.getMessage(), null);
        }
    }

    private AccessToken generateAccessToken(Optional<DomainUser> optional) {
        DomainUser user = optional.get();
        long expiration = tokenProvider.getExpiryDuration();
        Date now = new Date();
        String accessTokenScope = Scope.ACCESS.toString();
        String accessTokenId = IdGenerator.generate();

        Claims claims = new DefaultClaims();


        claims.setId(accessTokenId);
        claims.setExpiration(new Date(now.getTime() + expiration));
        claims.setIssuedAt(now);
        claims.setSubject(String.valueOf(user.getId()));
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("scope", accessTokenScope);

        String accessToken = tokenProvider.generateTokenFromClaims(claims);

        String refreshTokenScope = Scope.REFRESH.toString();
        claims.setExpiration(new Date(now.getTime() + expiration * 2));
        String refreshTokenId = IdGenerator.generate();
        claims.put("scope", refreshTokenScope);
        claims.setId(refreshTokenId);

        String refreshToken = tokenProvider.generateTokenFromClaims(claims, expiration * 2);

        AccessToken token = new AccessToken();

        token.setAccessToken(accessToken);
        token.setTokenId(accessTokenId);
        token.setTokenType("Bearer");
        token.setRefreshToken(refreshToken);
        token.setNotBeforePolicy("0");
        token.setScope(accessTokenScope);
        token.setExpiresIn("");
        token.setRefreshExpiresIn("");

        return token;
    }
}

