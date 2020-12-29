/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.core.bussiness;

import com.phoenix.common.string_utils.ValidateString;
import com.phoenix.core.exception.UserAlreadyExistsException;
import com.phoenix.core.exception.UserValidationException;
import com.phoenix.core.port.repositories.UserRepositoryPort;
import com.phoenix.core.port.security.PasswordEncoderPort;
import com.phoenix.domain.entity.User;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * @param user : thông tin user đăng kí
     *             <p>
     *             Thực thi use case create user.
     *             Nếu có bất kì lỗi nào sẽ ném ra 1 Exception
     */
    public User execute(User user) throws Exception {
        //0. Validate registerUser
        validate(user);

        //2. Mã hóa mật khẩu
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        //3. Lưu user vào db.
        userRepository.save(user);

        return user;
    }

    /**
     * @param user :
     *             throw UserValidation nếu sai.
     */
    private void validate(User user) {
        if (user == null)
            throw new UserValidationException("User should not be null");
        if (ValidateString.isBlank(user.getEmail()))
            throw new UserValidationException("Email should not be null.");
        if (!ValidateString.isNullOrNotBlank(user.getEmail()))
            throw new UserValidationException("Email can be null but not blank.");
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException(user.getEmail() + " is already exist.");
        if (userRepository.findByUsername("Email: " + user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Username: " + user.getUsername() + " is already exist.");
    }
}
