package com.xxmassdeveloper.mpchartexample.utils;

import android.support.annotation.NonNull;

public class Utils {
    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it yourself.
     */
    @NonNull
    private static final int[] POW_10 = {
            1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };

    /**
     * Formats the given number to the given number of decimals, and returns the number as a string,
     * maximum 35 characters. If thousands are separated, the separating character is a dot (".").
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @return
     */
    public static String formatNumber(float number, int digitCount, boolean separateThousands) {
        return formatNumber(number, digitCount, separateThousands, '.');
    }

    /**
     * Formats the given number to the given number of decimals, and returns the number as a string,
     * maximum 35 characters.
     *
     * @param number
     * @param digitsCount
     * @param separateThousands set this to true to separate thousands values
     * @param separateChar      a character to be paced between the "thousands"
     * @return
     */
    private static String formatNumber(float number, int digitsCount, boolean separateThousands, char separateChar) {
        if (number == 0f) {
            return "0";
        }

        char[] out = new char[35];
        boolean negative = false;
        boolean zero = false;
        if (number < 1f && number > -1f) {
            zero = true;
        }

        if (number < 0f) {
            negative = true;
            number = -number;
        }

        if (digitsCount > POW_10.length) {
            digitsCount = POW_10.length - 1;
        }

        number *= POW_10[digitsCount];
        long lval = Math.round(number);
        int ind = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;

        while (lval != 0 || charCount < (digitsCount + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[ind--] = (char) (digit + '0');
            charCount++;

            // Add decimal point
            if (charCount == digitsCount) {
                out[ind--] = ',';
                charCount++;
                decimalPointAdded = true;
                // Add thousand separators
            } else if (separateThousands && lval != 0 && charCount > digitsCount) {
                if (decimalPointAdded) {
                    if ((charCount - digitsCount) % 4 == 0) {
                        out[ind--] = separateChar;
                        charCount++;
                    }
                } else {
                    if ((charCount - digitsCount) % 4 == 3) {
                        out[ind--] = separateChar;
                        charCount++;
                    }
                }
            }
        }

        // If number around zero (between 1 and -1)
        if (zero) {
            out[ind--] = '0';
            charCount += 1;
        }

        // If the number is negative
        if (negative) {
            out[ind--] = '-';
            charCount += 1;
        }

        int start = out.length - charCount;

        return new String(out, start, out.length - start);
    }
}
