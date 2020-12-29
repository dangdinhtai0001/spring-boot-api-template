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

package com.phoenix.infrastructure.adapter;

import com.phoenix.common.util.Mapper;
import com.phoenix.core.port.repositories.UserRepositoryPort;
import com.phoenix.domain.entity.User;
import com.phoenix.infrastructure.entities.primary.UserEntity;
import com.phoenix.infrastructure.repositories.primary.UserRepository;

import java.util.Optional;

public class UserRepositoryAdapter implements UserRepositoryPort {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserRepositoryAdapter(Mapper mapper, UserRepository userRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = (UserEntity) this.mapper.convert(user);

        this.userRepository.save(userEntity);
    }

    @Override
    public Optional findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional findByUsername(String username) {
        return userRepository.findByEmail(username);
    }
}
