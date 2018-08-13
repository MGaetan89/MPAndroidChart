package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

/**
 * @author Philipp Jahoda
 */
public class BarHighlighter extends ChartHighlighter<BarDataProvider> {
    public BarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    @Nullable
    @Override
    public Highlight getHighlight(float x, float y) {
        Highlight highlight = super.getHighlight(x, y);
        if (highlight == null) {
            return null;
        }

        BarData barData = mChart.getBarData();
        if (barData == null) {
            return null;
        }

        IBarDataSet set = barData.getDataSetByIndex(highlight.getDataSetIndex());
        if (set == null) {
            return null;
        }

        if (set.isStacked()) {
            MPPointD pos = getValsForTouch(x, y);
            Highlight stackedHighlight = getStackedHighlight(highlight, set, (float) pos.x, (float) pos.y);
            MPPointD.recycleInstance(pos);
            return stackedHighlight;
        }

        return highlight;
    }

    /**
     * This method creates the Highlight object that also indicates which value of a stacked
     * BarEntry has been selected.
     *
     * @param highlight the Highlight to work with looking for stacked values
     * @param set
     * @param xVal
     * @param yVal
     * @return
     */
    @Nullable
    public Highlight getStackedHighlight(@NonNull Highlight highlight, @NonNull IBarDataSet set, float xVal, float yVal) {
        BarEntry entry = set.getEntryForXValue(xVal, yVal);
        if (entry == null) {
            return null;
        }

        // Not stacked
        if (entry.getYVals() == null) {
            return highlight;
        } else {
            Range[] ranges = entry.getRanges();
            if (ranges.length > 0) {
                int stackIndex = getClosestStackIndex(ranges, yVal);
                MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(highlight.getX(), ranges[stackIndex].to);

                Highlight stackedHigh = new Highlight(
                        entry.getX(), entry.getY(), (float) pixels.x, (float) pixels.y,
                        highlight.getDataSetIndex(), stackIndex, highlight.getAxis()
                );

                MPPointD.recycleInstance(pixels);

                return stackedHigh;
            }
        }

        return null;
    }

    /**
     * Returns the index of the closest value inside the values array/ranges (stacked bar chart) to
     * the value given as a parameter.
     *
     * @param ranges
     * @param value
     */
    protected int getClosestStackIndex(@NonNull @Size(min = 1) Range[] ranges, float value) {
        int stackIndex = 0;
        for (Range range : ranges) {
            if (range.contains(value)) {
                return stackIndex;
            } else {
                stackIndex++;
            }
        }

        int length = Math.max(ranges.length - 1, 0);

        return (value > ranges[length].to) ? length : 0;
    }

    @Override
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(x1 - x2);
    }

    @Override
    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getBarData();
    }
}
