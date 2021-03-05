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
 *
 */

package com.phoenix.api.config;


public final class ApplicationUrls {
    /**
     * public url matchers
     */
    public static final String[] PUBLIC_MATCHERS =
            {
                    "/webjars/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/sbat/index/**",
                    "/sbat/error/**",
                    "/lang",
                    "/h2-console/**"
            };

    /**
     * swagger public urls
     */
    public static final String[] SWAGGER_MATCHERS =
            {

            };

    public static final String AUTH_PREFIX = "/auth";
    public static final String CREATE_ACCOUNT = "/create-account";
    public static final String SIGN_IN_BY_PASSWORD = "/sign-in/password";
    public static final String CREATE_QR_CODE_FOR_SIGN_IN = "/create-qr-code";
    public static final String SIGN_IN_BY_QR_CODE = "/sign-in/qr";
}
