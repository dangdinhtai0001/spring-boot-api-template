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
    public int createUser(DomainUser domainUser) {
        int result = userRepositoryImp.createUser(domainUser);
        return result;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public Optional<DomainUser> findUserByEmailOrUsername(String username) {
        return Optional.ofNullable(userRepositoryImp.findUserByEmailOrUsername(username));
    }

    @Override
    public int saveUserSecret(String secret, String username) {
        return userRepository.saveUserSecret(secret, username);
    }

    @Override
    public String findSecretByUsername(String username) {
        return userRepository.findSecretByUsername(username);
    }
}
