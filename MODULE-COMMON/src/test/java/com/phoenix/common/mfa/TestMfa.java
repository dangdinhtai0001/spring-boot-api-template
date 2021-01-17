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

package com.phoenix.common.mfa;

import com.phoenix.common.util.Base32;
import org.junit.Test;

import java.security.SecureRandom;

public class TestMfa {
    @Test
    public void testBase32() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[(32 * 5) / 8];
        secureRandom.nextBytes(bytes);

        String key = Base32.encode(bytes);

        System.out.println(key);
    }

    @Test
    public void testCreateMfaQr() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[(32 * 5) / 8];
        secureRandom.nextBytes(bytes);

        String key = Base32.encode(bytes);

        System.out.println(key);

        TotpData data = (TotpData) MfaDataFactory.getMfaData(MfaType.TOTP, "example@example.com", key,
                "AppName", HashingAlgorithm.SHA1, 6, 30);

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        String mimeType = generator.getImageMimeType();

        String dataUri = MfaUtils.getDataUriForImage(imageData, mimeType);

        System.out.println(dataUri);
    }
}
