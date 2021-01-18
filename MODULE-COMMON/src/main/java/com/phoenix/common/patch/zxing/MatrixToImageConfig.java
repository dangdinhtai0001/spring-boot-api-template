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

package com.phoenix.common.patch.zxing;

import java.awt.image.BufferedImage;

/**
 * Encapsulates custom configuration used in methods of {@link MatrixToImageWriter}.
 */
public final class MatrixToImageConfig {

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;

    private final int onColor;
    private final int offColor;

    /**
     * Creates a default config with on color {@link #BLACK} and off color {@link #WHITE}, generating normal
     * black-on-white barcodes.
     */
    public MatrixToImageConfig() {
        this(BLACK, WHITE);
    }

    /**
     * @param onColor pixel on color, specified as an ARGB value as an int
     * @param offColor pixel off color, specified as an ARGB value as an int
     */
    public MatrixToImageConfig(int onColor, int offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
    }

    public int getPixelOnColor() {
        return onColor;
    }

    public int getPixelOffColor() {
        return offColor;
    }

    int getBufferedImageColorModel() {
        if (onColor == BLACK && offColor == WHITE) {
            // Use faster BINARY if colors match default
            return BufferedImage.TYPE_BYTE_BINARY;
        }
        if (hasTransparency(onColor) || hasTransparency(offColor)) {
            // Use ARGB representation if colors specify non-opaque alpha
            return BufferedImage.TYPE_INT_ARGB;
        }
        // Default otherwise to RGB representation with ignored alpha channel
        return BufferedImage.TYPE_INT_RGB;
    }

    private static boolean hasTransparency(int argb) {
        return (argb & 0xFF000000) != 0xFF000000;
    }

}
