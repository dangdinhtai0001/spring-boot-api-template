package com.phoenix.core.bussiness;

import com.phoenix.common.security.Scope;
import com.phoenix.common.security.TokenProvider;
import com.phoenix.common.security.component.Claims;
import com.phoenix.common.security.component.DefaultClaims;
import com.phoenix.common.util.IdGenerator;
import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.model.AccessToken;

import java.security.InvalidKeyException;
import java.util.Date;
import java.util.Optional;

public final class UseCaseUtils {
    public static AccessToken generateAccessToken(Optional<DomainUser> optional, TokenProvider tokenProvider) throws InvalidKeyException, com.phoenix.common.exception.security.InvalidKeyException {
        DomainUser user = optional.get();
        long expiration = tokenProvider.getExpiryDuration();
        Date now = new Date();
        //String accessTokenScope = Collections.collectionToString(user.getRoles(), " ");
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
