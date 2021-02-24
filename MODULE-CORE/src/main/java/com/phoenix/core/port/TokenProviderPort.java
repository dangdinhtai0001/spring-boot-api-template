package com.phoenix.core.port;

import com.phoenix.common.security.component.Claims;

public interface TokenProviderPort {

    public String generateTokenFromClaims(Claims claims);

    public String generateTokenFromClaims(Claims claims, long expiration);

    public long getExpiryDuration();
}
