package com.github.mikephil.charting.highlight;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Jahoda
 */
public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider> implements IHighlighter {
    /**
     * Instance of the data-provider.
     */
    protected T mChart;

    /**
     * Buffer for storing previously highlighted values.
     */
    protected List<Highlight> mHighlightBuffer = new ArrayList<>();

    public ChartHighlighter(T chart) {
        this.mChart = chart;
    }

    @Nullable
    @Override
    public Highlight getHighlight(float x, float y) {
        MPPointD pos = getValsForTouch(x, y);
        float xVal = (float) pos.x;
        MPPointD.recycleInstance(pos);

        return getHighlightForX(xVal, x, y);
    }

    /**
     * Returns a recyclable MPPointD instance. Returns the corresponding xPos for a given
     * touch-position in pixels.
     *
     * @param x
     * @param y
     */
    protected MPPointD getValsForTouch(float x, float y) {
        // Take any transformer to determine the x-axis value
        return mChart.getTransformer(YAxis.AxisDependency.LEFT).getValuesByTouchPoint(x, y);
    }

    /**
     * Returns the corresponding Highlight for a given xVal and x- and y-touch position in pixels.
     *
     * @param xVal
     * @param x
     * @param y
     */
    @Nullable
    protected Highlight getHighlightForX(float xVal, float x, float y) {
        List<Highlight> closestValues = getHighlightsAtXValue(xVal, x, y);
        if (closestValues.isEmpty()) {
            return null;
        }

        float leftAxisMinDist = getMinimumDistance(closestValues, y, YAxis.AxisDependency.LEFT);
        float rightAxisMinDist = getMinimumDistance(closestValues, y, YAxis.AxisDependency.RIGHT);

        YAxis.AxisDependency axis = leftAxisMinDist < rightAxisMinDist ? YAxis.AxisDependency.LEFT : YAxis.AxisDependency.RIGHT;

        return getClosestHighlightByPixel(closestValues, x, y, axis, mChart.getMaxHighlightDistance());
    }

    /**
     * Returns the minimum distance from a touch value (in pixels) to the closest value (in pixels)
     * that is displayed in the chart.
     *
     * @param closestValues
     * @param pos
     * @param axis
     */
    protected float getMinimumDistance(@NonNull List<Highlight> closestValues, float pos, YAxis.AxisDependency axis) {
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < closestValues.size(); i++) {
            Highlight highlight = closestValues.get(i);
            if (highlight.getAxis() == axis) {
                float tempDistance = Math.abs(getHighlightPos(highlight) - pos);
                if (tempDistance < distance) {
                    distance = tempDistance;
                }
            }
        }

        return distance;
    }

    protected float getHighlightPos(@NonNull Highlight highlight) {
        return highlight.getYPx();
    }

    /**
     * Returns a list of Highlight objects representing the entries closest to the given xVal. The
     * returned list contains two objects per DataSet (closest rounding up, closest rounding down).
     *
     * @param xVal the transformed x-value of the x-touch position
     * @param x    touch position
     * @param y    touch position
     */
    @NonNull
    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {
        mHighlightBuffer.clear();

        BarLineScatterCandleBubbleData data = getData();
        if (data == null) {
            return mHighlightBuffer;
        }

        for (int i = 0, dataSetCount = data.getDataSetCount(); i < dataSetCount; i++) {
            IDataSet dataSet = data.getDataSetByIndex(i);
            // Don't include DataSets that cannot be highlighted
            if (dataSet == null || !dataSet.isHighlightEnabled()) {
                continue;
            }

            mHighlightBuffer.addAll(buildHighlights(dataSet, i, xVal, DataSet.Rounding.CLOSEST));
        }

        return mHighlightBuffer;
    }

    /**
     * Returns an array of `Highlight` objects corresponding to the selected xValue and dataSetIndex.
     *
     * @param set
     * @param dataSetIndex
     * @param xVal
     * @param rounding
     */
    @NonNull
    protected List<Highlight> buildHighlights(@NonNull IDataSet set, int dataSetIndex, float xVal, @NonNull DataSet.Rounding rounding) {
        ArrayList<Highlight> highlights = new ArrayList<>();
        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.size() == 0) {
            // Try to find closest x-value and take all entries for that x-value
            final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.size() == 0) {
            return highlights;
        }

        for (Entry entry : entries) {
            MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entry.getX(), entry.getY());

            highlights.add(new Highlight(entry.getX(), entry.getY(), (float) pixels.x, (float) pixels.y, dataSetIndex, set.getAxisDependency()));
        }

        return highlights;
    }

    /**
     * Returns the Highlight of the DataSet that contains the closest value on the y-axis.
     *
     * @param closestValues        contains two Highlight objects per DataSet closest to the selected x-position (determined by
     *                             rounding up an down)
     * @param x
     * @param y
     * @param axis                 the closest axis
     * @param minSelectionDistance
     */
    @Nullable
    public Highlight getClosestHighlightByPixel(@NonNull List<Highlight> closestValues, float x, float y, @Nullable YAxis.AxisDependency axis, float minSelectionDistance) {
        Highlight closest = null;
        float distance = minSelectionDistance;
        for (int i = 0; i < closestValues.size(); i++) {
            Highlight highlight = closestValues.get(i);
            if (axis == null || highlight.getAxis() == axis) {
                float cDistance = getDistance(x, y, highlight.getXPx(), highlight.getYPx());
                if (cDistance < distance) {
                    closest = highlight;
                    distance = cDistance;
                }
            }
        }

        return closest;
    }

    /**
     * Returns the distance between the two given points.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x1 - x2, y1 - y2);
    }

    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getData();
    }
}
