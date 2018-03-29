package com.github.mikephil.charting.renderer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Philipp Jahoda
 */
public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {
    /**
     * Buffer for storing the current minimum and maximum visible x.
     */
    protected XBounds mXBounds = new XBounds();

    public BarLineScatterCandleBubbleRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Returns true if the DataSet values should be drawn, false if not.
     *
     * @param set
     */
    protected boolean shouldDrawValues(@NonNull IDataSet set) {
        return set.isVisible() && (set.isDrawValuesEnabled() || set.isDrawIconsEnabled());
    }

    /**
     * Checks if the provided entry object is in bounds for drawing considering the current
     * animation phase.
     *
     * @param entry
     * @param set
     */
    protected boolean isInBoundsX(@Nullable Entry entry, @NonNull IBarLineScatterCandleBubbleDataSet set) {
        if (entry == null) {
            return false;
        }

        float entryIndex = set.getEntryIndex(entry);

        return entryIndex < set.getEntryCount() * mAnimator.getPhaseX();
    }

    /**
     * Class representing the bounds of the current viewport in terms of indices in the values array
     * of a DataSet.
     */
    protected class XBounds {
        /**
         * Minimum visible entry index.
         */
        public int min;

        /**
         * Maximum visible entry index.
         */
        public int max;

        /**
         * Range of visible entry indices.
         */
        public int range;

        /**
         * Calculates the minimum and maximum x values as well as the range between them.
         *
         * @param chart
         * @param dataSet
         */
        public void set(@NonNull BarLineScatterCandleBubbleDataProvider chart, @NonNull IBarLineScatterCandleBubbleDataSet dataSet) {
            float phaseX = Math.max(0f, Math.min(1f, mAnimator.getPhaseX()));

            float low = chart.getLowestVisibleX();
            float high = chart.getHighestVisibleX();

            Entry entryFrom = dataSet.getEntryForXValue(low, Float.NaN, DataSet.Rounding.DOWN);
            Entry entryTo = dataSet.getEntryForXValue(high, Float.NaN, DataSet.Rounding.UP);

            min = entryFrom == null ? 0 : dataSet.getEntryIndex(entryFrom);
            max = entryTo == null ? 0 : dataSet.getEntryIndex(entryTo);
            range = (int) ((max - min) * phaseX);
        }
    }
}
