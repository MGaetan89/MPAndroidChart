package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;

public class XAxisRendererRadarChart extends XAxisRenderer {
    private RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);

        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas canvas) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled()) {
            return;
        }

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final MPPointF drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f);

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0, 0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {
            String label = mXAxis.getValueFormatter().getAxisLabel(i, mXAxis);

            float angle = (sliceAngle * i + mChart.getRotationAngle()) % 360f;

            Utils.getPosition(center, mChart.getYRange() * factor + mXAxis.mLabelRotatedWidth / 2f, angle, pOut);

            drawLabel(canvas, label, pOut.x, pOut.y - mXAxis.mLabelRotatedHeight / 2f, drawLabelAnchor, labelRotationAngleDegrees);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(drawLabelAnchor);
    }

    /**
     * XAxis LimitLines on RadarChart not yet supported.
     *
     * @param canvas
     */
    @Override
    public void renderLimitLines(@NonNull Canvas canvas) {
        // Unused
    }
}
