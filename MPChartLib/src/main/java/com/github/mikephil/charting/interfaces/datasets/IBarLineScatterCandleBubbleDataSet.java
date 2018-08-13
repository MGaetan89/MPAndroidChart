package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.Entry;

import androidx.annotation.ColorInt;

/**
 * @author Philipp Jahoda
 */
public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {
    /**
     * Returns the color that is used for drawing the highlight indicators.
     */
    @ColorInt
    int getHighLightColor();
}
