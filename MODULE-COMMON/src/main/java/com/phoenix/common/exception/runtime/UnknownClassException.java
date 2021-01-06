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

package com.phoenix.common.exception.runtime;

/**
 * A <code>RuntimeException</code> equivalent of the JDK's
 * <code>ClassNotFoundException</code>, to maintain a RuntimeException paradigm.
 *
 * @since 0.1
 */
public class UnknownClassException extends RuntimeException {

    /*
    /**
     * Creates a new UnknownClassException.
     *
    public UnknownClassException() {
        super();
    }*/

    /**
     * Constructs a new UnknownClassException.
     *
     * @param message the reason for the exception
     */
    public UnknownClassException(String message) {
        super(message);
    }

    /*
     * Constructs a new UnknownClassException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     *
    public UnknownClassException(Throwable cause) {
        super(cause);
    }
    */

    /**
     * Constructs a new UnknownClassException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnknownClassException(String message, Throwable cause) {
        // TODO: remove in v1.0, this constructor is only exposed to allow for backward compatible behavior
        super(message, cause);
    }

}