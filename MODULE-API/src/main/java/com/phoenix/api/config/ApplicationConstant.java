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

public final class ApplicationConstant {

    /**
     * file name what save in4 of secret key
     */
    public static final String KEY_FILE = "key.dat";

    /**
     * Field to represent API key on the requests/responses header
     */
    public static final String HEADER_API_KEY = "X-api-key";

    /**
     * Field to represent API Rate Limit Remaining on the requests/responses header
     */
    public static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";

    /**
     * Field to represent API Rate Limit Retry After Seconds on the requests/responses header
     */
    public static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";

}
