package com.phoenix.core.bussiness.auth;

import com.phoenix.common.exception.runtime.EmailValidationException;
import com.phoenix.common.exception.runtime.UserAlreadyExistsException;
import com.phoenix.common.exception.runtime.UserValidationException;
import com.phoenix.common.lang.Strings;
import com.phoenix.common.validation.Validation;
import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.core.common.UseCaseStatus;
import com.phoenix.core.port.PasswordEncoderPort;
import com.phoenix.core.port.UserRepositoryPort;
import com.phoenix.domain.entity.DomainUser;
import com.phoenix.domain.persistence.primary.UserEntity;

import java.util.Optional;

public class CreateAccount implements UseCase<DomainUser, String> {

    private final PasswordEncoderPort passwordEncoder;
    private final UserRepositoryPort userRepository;

    public CreateAccount(PasswordEncoderPort passwordEncoder, UserRepositoryPort userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void validate(DomainUser user) {
        if (user == null)
            throw new UserValidationException("User should not be null");
        if (user.getUsername() != null && !Validation.isValidUsername(user.getUsername()))
            throw new UserValidationException(user.getUsername() + " is invalid.");
        if (Strings.isBlankOrNull(user.getEmail()))
            throw new UserValidationException("Email should not be null.");
        if (!Validation.isValidEmail(user.getEmail()))
            throw new EmailValidationException(user.getEmail() + " is invalid.");
        if (!Strings.isNullOrNotBlank(user.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException(user.getEmail() + " is already exist.");
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already exist.");
    }

    @Override
    public UseCaseResponse<String> execute(DomainUser domainUser) throws Exception {
        try {
            //STEP-1. validate input
            validate(domainUser);

            //STEP-2. Normalize input
            domainUser = normalize(domainUser);

            //STEP-3. Encode password
            String password = passwordEncoder.encode(domainUser.getPassword());
            domainUser.setPassword(password);

            //STEP-4. Stored
            Optional<UserEntity> optional = userRepository.createUser(domainUser);

            if (optional.isEmpty()) {
                return new UseCaseResponse<>(UseCaseStatus.FAILED, "Cannot store to database!!!", "");
            }
        } catch (Exception e) {
            return new UseCaseResponse<>(UseCaseStatus.FAILED, e.getMessage(), "");
        }
        return new UseCaseResponse<>(UseCaseStatus.SUCCESS, "", "");
    }

    private DomainUser normalize(DomainUser domainUser) {
        return domainUser;
    }
}
