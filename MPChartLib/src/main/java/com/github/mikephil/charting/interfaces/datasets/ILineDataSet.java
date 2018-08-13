package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public interface ILineDataSet extends ILineRadarDataSet<Entry> {
    /**
     * Returns the drawing mode for this line data set.
     */
    LineDataSet.Mode getMode();

    /**
     * Returns the intensity of the cubic lines (the effect intensity). Max 1f = very cubic,
     * min = 0.05f = low cubic effect.
     */
    float getCubicIntensity();

    /**
     * Returns the size of the drawn circles.
     */
    float getCircleRadius();

    /**
     * Returns the hole radius of the drawn circles.
     */
    float getCircleHoleRadius();

    /**
     * Returns the color at the given index of the DataSet's circle-color array.
     *
     * @param index
     */
    @ColorInt
    int getCircleColor(int index);

    /**
     * Returns the number of colors in this DataSet's circle-color array.
     */
    int getCircleColorCount();

    /**
     * Returns true if drawing circles for this DataSet is enabled, false if not
     */
    boolean isDrawCirclesEnabled();

    /**
     * Returns the color of the inner circle (the circle-hole).
     */
    int getCircleHoleColor();

    /**
     * Returns true if drawing the circle-holes is enabled, false if not.
     */
    boolean isDrawCircleHoleEnabled();

    /**
     * Returns the DashPathEffect that is used for drawing the lines.
     */
    @Nullable
    DashPathEffect getDashPathEffect();

    /**
     * Returns true if the dashed-line effect is enabled, false if not. If the DashPathEffect object
     * is null, also return false here.
     */
    boolean isDashedLineEnabled();

    /**
     * Returns the IFillFormatter that is set for this DataSet.
     */
    @NonNull
    IFillFormatter getFillFormatter();
}
