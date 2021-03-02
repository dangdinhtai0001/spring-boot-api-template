package com.phoenix.adapter.repository;

import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.persistence.primary.UserEntity;
import com.phoenix.infrastructure.repositories.primary.UserRepository;
import com.phoenix.infrastructure.repositories.primary.UserRepositoryImp;

import java.util.Optional;

public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final UserRepositoryImp userRepositoryImp;

    public UserRepositoryAdapter(UserRepository userRepository, UserRepositoryImp userRepositoryImp) {
        this.userRepository = userRepository;
        this.userRepositoryImp = userRepositoryImp;
    }

    @Override
    public Optional<UserEntity> createUser(DomainUser domainUser) {
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<DomainUser> findUserByEmailOrUsername(String username) {
        return Optional.empty();
    }
}
