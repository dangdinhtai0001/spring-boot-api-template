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
import com.phoenix.domain.payload.CreateAccountPayload;
import com.phoenix.domain.persistence.primary.UserEntity;

import java.util.Optional;

public class CreateAccount implements UseCase<CreateAccountPayload, Object> {

    private final PasswordEncoderPort passwordEncoder;
    private final UserRepositoryPort userRepository;

    public CreateAccount(PasswordEncoderPort passwordEncoder,
                         UserRepositoryPort userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void validate(CreateAccountPayload user) {
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
            throw new UserAlreadyExistsException("Email: "+ user.getEmail() + " is already exist.");
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already exist.");
    }

    @Override
    public UseCaseResponse<Object> execute(CreateAccountPayload payload) {
        try {
            //STEP-1. validate input
            validate(payload);

            //STEP-2. Normalize input
            DomainUser domainUser = normalize(payload);

            //STEP-3. Encode password
            String password = passwordEncoder.encode(domainUser.getPassword());
            domainUser.setPassword(password);

            //STEP-4. Stored
            int stored = userRepository.createUser(domainUser);

            if (stored == -1) {
                return new UseCaseResponse<>(UseCaseStatus.FAILED,
                        "Cannot store to database!!!",
                        null);
            }

            return new UseCaseResponse<>(UseCaseStatus.SUCCESS, "", domainUser);
        } catch (Exception e) {
            return new UseCaseResponse<>(UseCaseStatus.FAILED, e.getMessage(), null);
        }

    }

    private DomainUser normalize(CreateAccountPayload payload) {
        DomainUser domainUser = new DomainUser();

        domainUser.setUsername(payload.getUsername());
        domainUser.setEmail(payload.getEmail());
        domainUser.setPassword(payload.getPassword());
        domainUser.setEnabled(false);
        domainUser.setLocked(true);
        domainUser.setRoles(payload.getRoles());

        return domainUser;
    }
}
