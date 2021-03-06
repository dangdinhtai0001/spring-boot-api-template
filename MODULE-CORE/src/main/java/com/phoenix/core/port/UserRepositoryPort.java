package com.phoenix.core.port;

import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.persistence.primary.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    public int createUser(DomainUser domainUser);

    public Optional<UserEntity> findByEmail(String email);

    public Optional<UserEntity> findByUsername(String username);

    public Optional<DomainUser> findUserByEmailOrUsername(String username);

    public int saveUserSecret(String secret, String username);

}
