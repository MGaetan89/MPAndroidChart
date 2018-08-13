package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.BarEntry;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * @author Philipp Jahoda
 */
public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {
    /**
     * Returns true if this DataSet is stacked (stack size > 1) or not.
     */
    boolean isStacked();

    /**
     * Returns the maximum number of bars that can be stacked upon another in this DataSet. This
     * should return 1 for non stacked bars, and > 1 for stacked bars.
     */
    int getStackSize();

    /**
     * Returns the color used for drawing the bar-shadows. The bar shadows is a surface behind the
     * bar that indicates the maximum value.
     */
    @ColorInt
    int getBarShadowColor();

    /**
     * Returns the width used for drawing borders around the bars. If borderWidth == 0, no border
     * will be drawn.
     */
    float getBarBorderWidth();

    /**
     * Returns the color drawing borders around the bars.
     */
    @ColorInt
    int getBarBorderColor();

    /**
     * Returns the alpha value (transparency) that is used for drawing the highlight indicator.
     */
    int getHighLightAlpha();

    /**
     * Returns the labels used for the different value-stacks in the legend. This is only relevant
     * for stacked bar entries.
     */
    @NonNull
    String[] getStackLabels();
}
