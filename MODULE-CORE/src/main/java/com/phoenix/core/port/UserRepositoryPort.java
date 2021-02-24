package com.phoenix.core.port;

import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.persistence.primary.UserEntity;

import java.util.Optional;

public interface UserRepositoryPort {
    public Optional<UserEntity> createUser(DomainUser domainUser);

    public Optional<UserEntity> findByEmail(String email);

    public Optional<UserEntity> findByUsername(String username);

    public Optional<DomainUser> findUserByEmailOrUsername(String username);
}
