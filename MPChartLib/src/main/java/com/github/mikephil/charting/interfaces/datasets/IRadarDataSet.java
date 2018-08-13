package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.RadarEntry;

import androidx.annotation.ColorInt;

/**
 * @author Philipp Jahoda
 */
public interface IRadarDataSet extends ILineRadarDataSet<RadarEntry> {
    /**
     * Flag indicating whether highlight circle should be drawn or not.
     */
    boolean isDrawHighlightCircleEnabled();

    /**
     * Sets whether highlight circle should be drawn or not.
     */
    void setDrawHighlightCircleEnabled(boolean enabled);

    @ColorInt
    int getHighlightCircleFillColor();

    /**
     * The stroke color for highlight circle. If Utils.COLOR_NONE, the color of the data set is
     * taken.
     */
    @ColorInt
    int getHighlightCircleStrokeColor();

    int getHighlightCircleStrokeAlpha();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    float getHighlightCircleStrokeWidth();
}
