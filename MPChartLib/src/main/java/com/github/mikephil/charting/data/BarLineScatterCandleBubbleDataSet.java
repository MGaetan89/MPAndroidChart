package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * Base class of all DataSets for Bar-, Line-, Scatter- and CandleStickChart.
 *
 * @author Philipp Jahoda
 */
public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry>
        extends DataSet<T>
        implements IBarLineScatterCandleBubbleDataSet<T> {
    /**
     * Default highlight color.
     */
    @ColorInt
    protected int mHighLightColor = 0xFFBB73;

    public BarLineScatterCandleBubbleDataSet(@NonNull List<T> yValues, @NonNull String label) {
        super(yValues, label);
    }

    /**
     * Sets the color that is used for drawing the highlight indicators.
     *
     * @param color
     */
    public void setHighLightColor(@ColorInt int color) {
        mHighLightColor = color;
    }

    @ColorInt
    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }

    protected void copy(BarLineScatterCandleBubbleDataSet barLineScatterCandleBubbleDataSet) {
        super.copy(barLineScatterCandleBubbleDataSet);

        barLineScatterCandleBubbleDataSet.mHighLightColor = mHighLightColor;
    }
}
