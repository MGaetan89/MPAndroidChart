package com.github.mikephil.charting.utils;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds convenience methods related to colors.
 *
 * @author Philipp Jahoda
 */
public final class ColorTemplate {
    /**
     * An "invalid" color that indicates that no color is set.
     */
    public static final int COLOR_NONE = 0x00112233;

    /**
     * This "color" is used for the Legend creation and indicates that the next form should be
     * skipped.
     */
    public static final int COLOR_SKIP = 0x00112234;

    /**
     * Sets the alpha component of the given color.
     *
     * @param color
     * @param alpha 0 - 255
     * @return
     */
    public static int colorWithAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    /**
     * Turns an array of colors into an {@link ArrayList} of colors.
     *
     * @param colors
     * @return
     */
    @NonNull
    public static List<Integer> createColors(@ColorInt int[] colors) {
        List<Integer> result = new ArrayList<>();
        for (int color : colors) {
            result.add(color);
        }

        return result;
    }
}
