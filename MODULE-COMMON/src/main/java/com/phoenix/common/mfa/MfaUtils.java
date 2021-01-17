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


import org.apache.commons.net.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MfaUtils {
    /**
     * @return The URI/message to encode into the QR image, in the format specified here:
     * https://github.com/google/google-authenticator/wiki/Key-Uri-Format
     */
    public static String getUri(MfaData data) {
        String uri = "";
        if (data instanceof TotpData) {
            TotpData totpData = (TotpData) data;
            uri = "otpauth://" +
                    uriEncode(totpData.getType()) + "/" +
                    uriEncode(totpData.getLabel()) + "?" +
                    "secret=" + uriEncode(totpData.getSecret()) +
                    "&issuer=" + uriEncode(totpData.getIssuer()) +
                    "&algorithm=" + uriEncode(totpData.getAlgorithm().getFriendlyName()) +
                    "&digits=" + totpData.getDigits() +
                    "&period=" + totpData.getPeriod();
        }

        if (data instanceof HotpData) {
            HotpData hotpData = (HotpData) data;
            uri = "otpauth://" +
                    uriEncode(hotpData.getType()) + "/" +
                    uriEncode(hotpData.getLabel()) + "?" +
                    "secret=" + uriEncode(hotpData.getSecret()) +
                    "&issuer=" + uriEncode(hotpData.getIssuer()) +
                    "&algorithm=" + uriEncode(hotpData.getAlgorithm().getFriendlyName()) +
                    "&digits=" + hotpData.getDigits() +
                    "&period=" + hotpData.getCounter();
        }

        return uri;
    }

    public static String createImage(String base64, String imgFileName) throws IOException {
        String imageDataBytes = base64.substring(base64.indexOf(",") + 1);
        byte[] imgBytes = Base64.decodeBase64(imageDataBytes);

        File imgFile = new File(imgFileName);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
        boolean b = ImageIO.write(img, "png", imgFile);

        if (b) {
            return imgFile.getPath();
        } else
            return null;
    }

    private static String uriEncode(String text) {
        // Null check
        if (text == null) {
            return "";
        }

        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            // This should never throw, as we are certain the charset specified (UTF-8) is valid
            throw new RuntimeException("Could not URI encode QrData.");
        }
    }


    /**
     * Given the raw data of an image and the mime type, returns
     * a data URI string representing the image for embedding in
     * HTML/CSS.
     *
     * @param data The raw bytes of the image.
     * @param mimeType The mime type of the image.
     * @return The data URI string representing the image.
     */
    public static String getDataUriForImage(byte[] data, String mimeType) {
        String encodedData = new String(Base64.encodeBase64(data));

        return String.format("data:%s;base64,%s", mimeType, encodedData);
    }
}
