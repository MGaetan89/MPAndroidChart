package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

public class YAxisRendererRadarChart extends YAxisRenderer {
    private RadarChart mChart;

    @NonNull
    private final Path mRenderLimitLinesPathBuffer = new Path();

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart chart) {
        super(viewPortHandler, yAxis, null);

        this.mChart = chart;
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        float yMin = min;
        float yMax = max;

        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);

        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[0];
            mAxis.mCenteredEntries = new float[0];
            mAxis.mEntryCount = 0;
            return;
        }

        // Find out how much spacing (in y value space) between axis values
        double rawInterval = range / labelCount;
        double interval = Utils.roundToNextSignificant(rawInterval);

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.
        if (mAxis.isGranularityEnabled()) {
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;
        }

        // Normalize interval
        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10, (int) Math.log10(interval)));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        boolean centeringEnabled = mAxis.isCenterAxisLabelsEnabled();
        int n = centeringEnabled ? 1 : 0;

        // Force label count
        if (mAxis.isForceLabelsEnabled()) {
            float step = (float) range / (float) (labelCount - 1);
            mAxis.mEntryCount = labelCount;

            if (mAxis.mEntries.length < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[labelCount];
            }

            float v = min;
            for (int i = 0; i < labelCount; i++) {
                mAxis.mEntries[i] = v;
                v += step;
            }

            n = labelCount;

            // No forced count
        } else {
            double first = interval == 0d ? 0d : Math.ceil(yMin / interval) * interval;
            if (centeringEnabled) {
                first -= interval;
            }

            double last = interval == 0d ? 0d : Math.nextUp(Math.floor(yMax / interval) * interval);

            double f;
            int i;

            if (interval != 0d) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }

            n++;

            mAxis.mEntryCount = n;

            if (mAxis.mEntries.length < n) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[n];
            }

            for (f = first, i = 0; i < n; f += interval, ++i) {
                // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                if (f == 0d) {
                    f = 0d;
                }

                mAxis.mEntries[i] = (float) f;
            }
        }

        // Set decimals
        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }

        if (centeringEnabled) {
            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (mAxis.mEntries[1] - mAxis.mEntries[0]) / 2f;
            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }

        mAxis.mAxisMinimum = mAxis.mEntries[0];
        mAxis.mAxisMaximum = mAxis.mEntries[n - 1];
        mAxis.mAxisRange = Math.abs(mAxis.mAxisMaximum - mAxis.mAxisMinimum);
    }

    @Override
    public void renderAxisLabels(@NonNull Canvas canvas) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled()) {
            return;
        }

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0f, 0f);
        float factor = mChart.getFactor();

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled() ? mYAxis.mEntryCount : (mYAxis.mEntryCount - 1);

        for (int j = from; j < to; j++) {
            float r = (mYAxis.mEntries[j] - mYAxis.mAxisMinimum) * factor;

            Utils.getPosition(center, r, mChart.getRotationAngle(), pOut);

            String label = mYAxis.getFormattedLabel(j);

            canvas.drawText(label, pOut.x + 10, pOut.y, mAxisLabelPaint);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    @Override
    public void renderLimitLines(@NonNull Canvas canvas) {
        List<LimitLine> limitLines = mYAxis.getLimitLines();
        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0f, 0f);
        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine limitLine = limitLines.get(i);
            if (!limitLine.isEnabled()) {
                continue;
            }

            mLimitLinePaint.setColor(limitLine.getLineColor());
            mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
            mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());

            float r = (limitLine.getLimit() - mChart.getYChartMin()) * factor;

            Path limitPath = mRenderLimitLinesPathBuffer;
            limitPath.reset();

            for (int j = 0; j < mChart.getData().getMaxEntryCountSet().getEntryCount(); j++) {
                Utils.getPosition(center, r, sliceAngle * j + mChart.getRotationAngle(), pOut);

                if (j == 0) {
                    limitPath.moveTo(pOut.x, pOut.y);
                } else {
                    limitPath.lineTo(pOut.x, pOut.y);
                }
            }

            limitPath.close();

            canvas.drawPath(limitPath, mLimitLinePaint);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }
}
