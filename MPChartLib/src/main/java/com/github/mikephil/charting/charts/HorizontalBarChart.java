package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * BarChart with horizontal bar orientation. In this implementation, x- and y-axis are switched,
 * meaning the YAxis class represents the horizontal values and the XAxis class represents the
 * vertical values.
 *
 * @author Philipp Jahoda
 */
public class HorizontalBarChart extends BarChart {
    public HorizontalBarChart(Context context) {
        super(context);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        mViewPortHandler = new ViewPortHandler();

        super.init();

        mLeftAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);
        mRightAxisTransformer = new TransformerHorizontalBarChart(mViewPortHandler);

        mRenderer = new HorizontalBarChartRenderer(this, mAnimator, mViewPortHandler);
        setHighlighter(new HorizontalBarHighlighter(this));

        //noinspection SuspiciousNameCombination
        mAxisRendererLeft = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        //noinspection SuspiciousNameCombination
        mAxisRendererRight = new YAxisRendererHorizontalBarChart(mViewPortHandler, mAxisRight, mRightAxisTransformer);
        mXAxisRenderer = new XAxisRendererHorizontalBarChart(mViewPortHandler, mXAxis, mLeftAxisTransformer, this);
    }

    private RectF mOffsetsBuffer = new RectF();

    @Override
    public void calculateOffsets() {
        calculateLegendOffsets(mOffsetsBuffer);

        float offsetLeft = mOffsetsBuffer.left;
        float offsetTop = mOffsetsBuffer.top;
        float offsetRight = mOffsetsBuffer.right;
        float offsetBottom = mOffsetsBuffer.bottom;

        // Offsets for y-labels
        if (mAxisLeft.needsOffset()) {
            offsetTop += mAxisLeft.getRequiredHeightSpace(mAxisRendererLeft.getPaintAxisLabels());
        }

        if (mAxisRight.needsOffset()) {
            offsetBottom += mAxisRight.getRequiredHeightSpace(mAxisRendererRight.getPaintAxisLabels());
        }

        float xLabelWidth = mXAxis.mLabelRotatedWidth;

        if (mXAxis.isEnabled()) {
            // Offsets for x-labels
            if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {
                offsetLeft += xLabelWidth;
            } else if (mXAxis.getPosition() == XAxisPosition.TOP) {
                offsetRight += xLabelWidth;
            } else if (mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
                offsetLeft += xLabelWidth;
                offsetRight += xLabelWidth;
            }
        }

        offsetTop += getExtraTopOffset();
        offsetRight += getExtraRightOffset();
        offsetBottom += getExtraBottomOffset();
        offsetLeft += getExtraLeftOffset();

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft), Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight), Math.max(minOffset, offsetBottom)
        );

        if (mLogEnabled) {
            Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
            Log.i(LOG_TAG, "Content: " + mViewPortHandler.getContentRect().toString());
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    @Override
    protected void prepareValuePxMatrix() {
        mRightAxisTransformer.prepareMatrixValuePx(mAxisRight.mAxisMinimum, mAxisRight.mAxisRange, mXAxis.mAxisRange, mXAxis.mAxisMinimum);
        mLeftAxisTransformer.prepareMatrixValuePx(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisRange, mXAxis.mAxisRange, mXAxis.mAxisMinimum);
    }

    @Override
    protected float[] getMarkerPosition(@NonNull Highlight highlight) {
        return new float[]{highlight.getDrawY(), highlight.getDrawX()};
    }

    @Override
    public void getBarBounds(@NonNull BarEntry entry, @NonNull RectF outputRect) {
        IBarDataSet set = mData.getDataSetForEntry(entry);
        if (set == null) {
            outputRect.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
            return;
        }

        float y = entry.getY();
        float x = entry.getX();

        float barWidth = mData.getBarWidth();

        float top = x - barWidth / 2f;
        float bottom = x + barWidth / 2f;
        float left = y >= 0 ? y : 0;
        float right = y <= 0 ? y : 0;

        outputRect.set(left, top, right, bottom);

        getTransformer(set.getAxisDependency()).rectValueToPixel(outputRect);
    }

    protected float[] mGetPositionBuffer = new float[2];

    /**
     * Returns a recyclable MPPointF instance.
     *
     * @param entry
     * @param axis
     */
    @Nullable
    @Override
    public MPPointF getPosition(Entry entry, AxisDependency axis) {
        if (entry == null) {
            return null;
        }

        float[] values = mGetPositionBuffer;
        values[0] = entry.getY();
        values[1] = entry.getX();

        getTransformer(axis).pointValuesToPixel(values);

        return MPPointF.getInstance(values[0], values[1]);
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at
     * the given touch point inside the BarChart.
     *
     * @param x
     * @param y
     */
    @Nullable
    @Override
    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (mData == null) {
            if (mLogEnabled) {
                Log.e(LOG_TAG, "Can't select by touch. No data set.");
            }

            return null;
        } else {
            //noinspection SuspiciousNameCombination
            return getHighlighter().getHighlight(y, x); // switch x and y
        }
    }

    @Override
    public float getLowestVisibleX() {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), posForGetLowestVisibleX);
        return (float) Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.y);
    }

    @Override
    public float getHighestVisibleX() {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), posForGetHighestVisibleX);
        return (float) Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.y);
    }

    @Override
    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = mXAxis.mAxisRange / (maxXRange);
        mViewPortHandler.setMinimumScaleY(xScale);
    }

    @Override
    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = mXAxis.mAxisRange / (minXRange);
        mViewPortHandler.setMaximumScaleY(xScale);
    }

    @Override
    public void setVisibleXRange(float minXRange, float maxXRange) {
        float minScale = mXAxis.mAxisRange / minXRange;
        float maxScale = mXAxis.mAxisRange / maxXRange;
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale);
    }

    @Override
    public void setVisibleYRangeMaximum(float maxYRange, AxisDependency axis) {
        float yScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinimumScaleX(yScale);
    }

    @Override
    public void setVisibleYRangeMinimum(float minYRange, AxisDependency axis) {
        float yScale = getAxisRange(axis) / minYRange;
        mViewPortHandler.setMaximumScaleX(yScale);
    }

    @Override
    public void setVisibleYRange(float minYRange, float maxYRange, AxisDependency axis) {
        float minScale = getAxisRange(axis) / minYRange;
        float maxScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }
}
