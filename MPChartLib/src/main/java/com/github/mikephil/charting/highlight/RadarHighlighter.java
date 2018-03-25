package com.github.mikephil.charting.highlight;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.List;

/**
 * @author Philipp Jahoda
 */
public class RadarHighlighter extends PieRadarHighlighter<RadarChart> {
    public RadarHighlighter(RadarChart chart) {
        super(chart);
    }

    @Nullable
    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {
        List<Highlight> highlights = getHighlightsAtIndex(index);
        float distanceToCenter = mChart.distanceToCenter(x, y) / mChart.getFactor();
        Highlight closest = null;
        float distance = Float.MAX_VALUE;

        for (int i = 0; i < highlights.size(); i++) {
            Highlight high = highlights.get(i);
            float cDistance = Math.abs(high.getY() - distanceToCenter);
            if (cDistance < distance) {
                closest = high;
                distance = cDistance;
            }
        }

        return closest;
    }

    /**
     * Returns an array of Highlight objects for the given index. The Highlight objects give
     * information about the value at the selected index and the DataSet it belongs to.
     * INFORMATION: This method does calculations at runtime. Do not over-use in performance
     * critical situations.
     *
     * @param index
     */
    @NonNull
    protected List<Highlight> getHighlightsAtIndex(int index) {
        mHighlightBuffer.clear();

        RadarData data = mChart.getData();
        if (data == null) {
            return mHighlightBuffer;
        }

        float phaseX = mChart.getAnimator().getPhaseX();
        float phaseY = mChart.getAnimator().getPhaseY();
        float sliceAngle = mChart.getSliceAngle();
        float factor = mChart.getFactor();

        MPPointF pOut = MPPointF.getInstance(0f, 0f);
        for (int i = 0; i < data.getDataSetCount(); i++) {
            IDataSet<?> dataSet = data.getDataSetByIndex(i);
            if (dataSet == null) {
                continue;
            }

            final Entry entry = dataSet.getEntryForIndex(index);
            float y = (entry.getY() - mChart.getYChartMin());

            Utils.getPosition(
                    mChart.getCenterOffsets(), y * factor * phaseY,
                    sliceAngle * index * phaseX + mChart.getRotationAngle(), pOut
            );

            mHighlightBuffer.add(new Highlight(index, entry.getY(), pOut.x, pOut.y, i, dataSet.getAxisDependency()));
        }

        return mHighlightBuffer;
    }
}
