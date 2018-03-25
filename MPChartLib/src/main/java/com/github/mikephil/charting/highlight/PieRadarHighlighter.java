package com.github.mikephil.charting.highlight;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Jahoda
 */
public abstract class PieRadarHighlighter<T extends PieRadarChartBase> implements IHighlighter {
    protected T mChart;

    /**
     * Buffer for storing previously highlighted values.
     */
    protected List<Highlight> mHighlightBuffer = new ArrayList<>();

    public PieRadarHighlighter(T chart) {
        this.mChart = chart;
    }

    @Nullable
    @Override
    public Highlight getHighlight(float x, float y) {
        float touchDistanceToCenter = mChart.distanceToCenter(x, y);

        // Check if a slice was touched
        if (touchDistanceToCenter > mChart.getRadius()) {
            // If no slice was touched, highlight nothing
            return null;
        } else {
            float angle = mChart.getAngleForPoint(x, y);
            if (mChart instanceof PieChart) {
                angle /= mChart.getAnimator().getPhaseY();
            }

            int index = mChart.getIndexForAngle(angle);
            ChartData data = mChart.getData();
            if (data == null) {
                return null;
            }

            IDataSet maxEntryCountSet = data.getMaxEntryCountSet();
            if (maxEntryCountSet == null) {
                return null;
            }

            // Check if the index could be found
            if (index < 0 || index >= maxEntryCountSet.getEntryCount()) {
                return null;
            } else {
                return getClosestHighlight(index, x, y);
            }
        }
    }

    /**
     * Returns the closest Highlight object of the given objects based on the touch position inside
     * the chart.
     *
     * @param index
     * @param x
     * @param y
     */
    protected abstract Highlight getClosestHighlight(int index, float x, float y);
}
