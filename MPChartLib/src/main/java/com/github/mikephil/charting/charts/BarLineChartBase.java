package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob;
import com.github.mikephil.charting.jobs.AnimatedZoomJob;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.github.mikephil.charting.jobs.ZoomJob;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base-class of LineChart, BarChart, ScatterChart and CandleStickChart.
 *
 * @author Philipp Jahoda
 */
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>
        extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {
    /**
     * The maximum number of entries to which values will be drawn (entry numbers greater than this
     * value will cause value-labels to disappear).
     */
    protected int mMaxVisibleCount = 100;

    /**
     * Flag that indicates if auto scaling on the y axis is enabled.
     */
    protected boolean mAutoScaleMinMaxEnabled = false;

    /**
     * Flag that indicates if pinch-zoom is enabled. if true, both x and y axis can be scaled with 2
     * fingers, if false, x and y axis can be scaled separately.
     */
    protected boolean mPinchZoomEnabled = false;

    /**
     * Flag that indicates if double tap zoom is enabled or not.
     */
    protected boolean mDoubleTapToZoomEnabled = true;

    /**
     * Flag that indicates if highlighting per dragging over a fully zoomed out chart is enabled.
     */
    protected boolean mHighlightPerDragEnabled = true;

    /**
     * If true, dragging is enabled for the chart.
     */
    private boolean mDragXEnabled = true;
    private boolean mDragYEnabled = true;

    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;

    /**
     * Paint object for the (by default) light grey background of the grid.
     */
    protected Paint mGridBackgroundPaint;

    protected Paint mBorderPaint;

    /**
     * Flag indicating if the grid background should be drawn or not.
     */
    protected boolean mDrawGridBackground = false;

    protected boolean mDrawBorders = false;

    protected boolean mClipValuesToContent = false;

    /**
     * Sets the minimum offset (padding) around the chart.
     */
    protected float mMinOffset = 15f;

    /**
     * Flag indicating if the chart should stay at the same position after a rotation.
     */
    protected boolean mKeepPositionOnRotation = false;

    /**
     * The listener for user drawing on the chart.
     */
    protected OnDrawListener mDrawListener;

    /**
     * The object representing the labels on the left y-axis.
     */
    protected YAxis mAxisLeft;

    /**
     * The object representing the labels on the right y-axis.
     */
    protected YAxis mAxisRight;

    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;

    protected Transformer mLeftAxisTransformer;
    protected Transformer mRightAxisTransformer;

    protected XAxisRenderer mXAxisRenderer;

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis(AxisDependency.LEFT);
        mAxisRight = new YAxis(AxisDependency.RIGHT);

        mLeftAxisTransformer = new Transformer(mViewPortHandler);
        mRightAxisTransformer = new Transformer(mViewPortHandler);

        //noinspection SuspiciousNameCombination
        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        //noinspection SuspiciousNameCombination
        mAxisRendererRight = new YAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);

        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

        setHighlighter(new ChartHighlighter(this));

        mChartTouchListener = new BarLineChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 3f);

        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setStyle(Style.FILL);
        mGridBackgroundPaint.setColor(0xF0F0F0); // light grey

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));
    }

    // For performance tracking
    private long totalTime = 0;
    private long drawCycles = 0;

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null) {
            return;
        }

        long startTime = System.currentTimeMillis();

        // Execute all drawing commands
        drawGridBackground(canvas);

        if (mAutoScaleMinMaxEnabled) {
            autoScale();
        }

        if (mAxisLeft.isEnabled()) {
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());
        }

        if (mAxisRight.isEnabled()) {
            mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum, mAxisRight.isInverted());
        }

        if (mXAxis.isEnabled()) {
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);
        }

        mXAxisRenderer.renderAxisLine(canvas);
        mAxisRendererLeft.renderAxisLine(canvas);
        mAxisRendererRight.renderAxisLine(canvas);

        if (mXAxis.isDrawGridLinesBehindDataEnabled())
            mXAxisRenderer.renderGridLines(canvas);

        if (mAxisLeft.isDrawGridLinesBehindDataEnabled())
            mAxisRendererLeft.renderGridLines(canvas);

        if (mAxisRight.isDrawGridLinesBehindDataEnabled())
            mAxisRendererRight.renderGridLines(canvas);

        if (mXAxis.isEnabled() && mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            mXAxisRenderer.renderLimitLines(canvas);
        }

        if (mAxisLeft.isEnabled() && mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            mAxisRendererLeft.renderLimitLines(canvas);
        }

        if (mAxisRight.isEnabled() && mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            mAxisRendererRight.renderLimitLines(canvas);
        }

        // Make sure the data cannot be drawn outside the content-rect
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());

        mRenderer.drawData(canvas);

        if (!mXAxis.isDrawGridLinesBehindDataEnabled())
            mXAxisRenderer.renderGridLines(canvas);

        if (!mAxisLeft.isDrawGridLinesBehindDataEnabled())
            mAxisRendererLeft.renderGridLines(canvas);

        if (!mAxisRight.isDrawGridLinesBehindDataEnabled())
            mAxisRendererRight.renderGridLines(canvas);

        // If highlighting is enabled
        if (valuesToHighlight()) {
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);
        }

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        mRenderer.drawExtras(canvas);

        if (mXAxis.isEnabled() && !mXAxis.isDrawLimitLinesBehindDataEnabled()) {
            mXAxisRenderer.renderLimitLines(canvas);
        }

        if (mAxisLeft.isEnabled() && !mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
            mAxisRendererLeft.renderLimitLines(canvas);
        }

        if (mAxisRight.isEnabled() && !mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
            mAxisRendererRight.renderLimitLines(canvas);
        }

        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
        mAxisRendererRight.renderAxisLabels(canvas);

        if (isClipValuesToContentEnabled()) {
            clipRestoreCount = canvas.save();
            canvas.clipRect(mViewPortHandler.getContentRect());

            mRenderer.drawValues(canvas);

            canvas.restoreToCount(clipRestoreCount);
        } else {
            mRenderer.drawValues(canvas);
        }

        mLegendRenderer.renderLegend(canvas);

        drawDescription(canvas);

        drawMarkers(canvas);

        if (mLogEnabled) {
            long drawTime = System.currentTimeMillis() - startTime;
            totalTime += drawTime;
            drawCycles += 1;
            long average = totalTime / drawCycles;
            Log.i(LOG_TAG, "Draw time: " + drawTime + " ms, average: " + average + " ms, cycles: " + drawCycles);
        }
    }

    /**
     * Reset performance tracking fields
     */
    public void resetTracking() {
        totalTime = 0;
        drawCycles = 0;
    }

    protected void prepareValuePxMatrix() {
        if (mLogEnabled) {
            Log.i(LOG_TAG, "Preparing Value-Px Matrix, xMin: " + mXAxis.mAxisMinimum + ", xMax: " + mXAxis.mAxisMaximum + ", xDelta: " + mXAxis.mAxisRange);
        }

        mRightAxisTransformer.prepareMatrixValuePx(
                mXAxis.mAxisMinimum, mXAxis.mAxisRange, mAxisRight.mAxisRange, mAxisRight.mAxisMinimum
        );
        mLeftAxisTransformer.prepareMatrixValuePx(
                mXAxis.mAxisMinimum, mXAxis.mAxisRange, mAxisLeft.mAxisRange, mAxisLeft.mAxisMinimum
        );
    }

    protected void prepareOffsetMatrix() {
        mRightAxisTransformer.prepareMatrixOffset(mAxisRight.isInverted());
        mLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted());
    }

    @Override
    public void notifyDataSetChanged() {
        if (mData == null) {
            if (mLogEnabled) {
                Log.i(LOG_TAG, "Preparing... DATA NOT SET.");
            }

            return;
        }

        if (mLogEnabled) {
            Log.i(LOG_TAG, "Preparing...");
        }

        if (mRenderer != null) {
            mRenderer.initBuffers();
        }

        calcMinMax();

        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());
        mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum, mAxisRight.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        if (mLegend != null) {
            mLegendRenderer.computeLegend(mData);
        }

        calculateOffsets();
    }

    /**
     * Performs auto scaling of the axis by recalculating the minimum and maximum y-values based on
     * the entries currently in view.
     */
    protected void autoScale() {
        final float fromX = getLowestVisibleX();
        final float toX = getHighestVisibleX();

        mData.calcMinMaxY(fromX, toX);

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // Calculate axis range (min / max) according to provided data
        if (mAxisLeft.isEnabled()) {
            mAxisLeft.calculate(mData.getYMin(AxisDependency.LEFT), mData.getYMax(AxisDependency.LEFT));
        }

        if (mAxisRight.isEnabled()) {
            mAxisRight.calculate(mData.getYMin(AxisDependency.RIGHT), mData.getYMax(AxisDependency.RIGHT));
        }

        calculateOffsets();
    }

    @Override
    protected void calcMinMax() {
        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // Calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(AxisDependency.LEFT), mData.getYMax(AxisDependency.LEFT));
        mAxisRight.calculate(mData.getYMin(AxisDependency.RIGHT), mData.getYMax(AxisDependency.RIGHT));
    }

    protected void calculateLegendOffsets(@NonNull RectF offsets) {
        offsets.left = 0f;
        offsets.right = 0f;
        offsets.top = 0f;
        offsets.bottom = 0f;

        // Setup offsets for legend
        if (mLegend != null && mLegend.isEnabled() && !mLegend.isDrawInsideEnabled()) {
            switch (mLegend.getOrientation()) {
                case VERTICAL:
                    switch (mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            offsets.left += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent()) + mLegend.getXOffset();
                            break;

                        case RIGHT:
                            offsets.right += Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent()) + mLegend.getXOffset();
                            break;

                        case CENTER:
                            switch (mLegend.getVerticalAlignment()) {
                                case TOP:
                                    offsets.top += Math.min(mLegend.mNeededHeight, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent()) + mLegend.getYOffset();
                                    break;

                                case BOTTOM:
                                    offsets.bottom += Math.min(mLegend.mNeededHeight, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent()) + mLegend.getYOffset();
                                    break;
                            }
                    }
                    break;

                case HORIZONTAL:
                    switch (mLegend.getVerticalAlignment()) {
                        case TOP:
                            offsets.top += Math.min(mLegend.mNeededHeight,
                                    mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                    + mLegend.getYOffset();
                            break;

                        case BOTTOM:
                            offsets.bottom += Math.min(mLegend.mNeededHeight,
                                    mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent())
                                    + mLegend.getYOffset();
                            break;

                        default:
                            break;
                    }
                    break;
            }
        }
    }

    @NonNull
    private final RectF mOffsetsBuffer = new RectF();

    @Override
    public void calculateOffsets() {
        if (!mCustomViewPortEnabled) {
            calculateLegendOffsets(mOffsetsBuffer);

            float offsetLeft = mOffsetsBuffer.left;
            float offsetTop = mOffsetsBuffer.top;
            float offsetRight = mOffsetsBuffer.right;
            float offsetBottom = mOffsetsBuffer.bottom;

            // Offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft.getPaintAxisLabels());
            }

            if (mAxisRight.needsOffset()) {
                offsetRight += mAxisRight.getRequiredWidthSpace(mAxisRendererRight.getPaintAxisLabels());
            }

            if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

                float xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.getYOffset();

                // offsets for x-labels
                if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {

                    offsetBottom += xLabelHeight;

                } else if (mXAxis.getPosition() == XAxisPosition.TOP) {

                    offsetTop += xLabelHeight;

                } else if (mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {

                    offsetBottom += xLabelHeight;
                    offsetTop += xLabelHeight;
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
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /**
     * Draws the grid background.
     */
    protected void drawGridBackground(@NonNull Canvas canvas) {
        if (mDrawGridBackground) {
            // draw the grid background
            canvas.drawRect(mViewPortHandler.getContentRect(), mGridBackgroundPaint);
        }

        if (mDrawBorders) {
            canvas.drawRect(mViewPortHandler.getContentRect(), mBorderPaint);
        }
    }

    /**
     * Returns the Transformer class that contains all matrices and is responsible for transforming
     * values into pixels on the screen and backwards.
     */
    public Transformer getTransformer(AxisDependency axis) {
        if (axis == AxisDependency.LEFT) {
            return mLeftAxisTransformer;
        } else {
            return mRightAxisTransformer;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (mChartTouchListener == null || mData == null) {
            return false;
        }

        // Check if touch gestures are enabled
        return mTouchEnabled && mChartTouchListener.onTouch(this, event);
    }

    @Override
    public void computeScroll() {
        if (mChartTouchListener instanceof BarLineChartTouchListener) {
            ((BarLineChartTouchListener) mChartTouchListener).computeScroll();
        }
    }

    protected Matrix mZoomMatrixBuffer = new Matrix();

    /**
     * Zooms in by 1.4f, into the charts center.
     */
    public void zoomIn() {
        MPPointF center = mViewPortHandler.getContentCenter();

        mViewPortHandler.zoomIn(center.x, -center.y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        MPPointF.recycleInstance(center);

        // Range might have changed, which means that Y-axis labels could have changed in size,
        // affecting Y-axis size. So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms out by 0.7f, from the charts center.
     */
    public void zoomOut() {
        MPPointF center = mViewPortHandler.getContentCenter();

        mViewPortHandler.zoomOut(center.x, -center.y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        MPPointF.recycleInstance(center);

        // Range might have changed, which means that Y-axis labels could have changed in size,
        // affecting Y-axis size. So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms out to original size.
     */
    public void resetZoom() {
        mViewPortHandler.resetZoom(mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        // Range might have changed, which means that Y-axis labels could have changed in size,
        // affecting Y-axis size. So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms in or out by the given scale factor. x and y are the coordinates (in pixels) of the
     * zoom center.
     *
     * @param scaleX if < 1f --> zoom out, if > 1f --> zoom in
     * @param scaleY if < 1f --> zoom out, if > 1f --> zoom in
     * @param x
     * @param y
     */
    public void zoom(float scaleX, float scaleY, float x, float y) {
        mViewPortHandler.zoom(scaleX, scaleY, x, -y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);

        // Range might have changed, which means that Y-axis labels could have changed in size,
        // affecting Y-axis size. So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    /**
     * Zooms in or out by the given scale factor. x and y are the values (NOT PIXELS) of the zoom
     * center.
     *
     * @param scaleX
     * @param scaleY
     * @param xValue
     * @param yValue
     * @param axis   the axis relative to which the zoom should take place
     */
    public void zoom(float scaleX, float scaleY, float xValue, float yValue, AxisDependency axis) {
        Runnable job = ZoomJob.getInstance(mViewPortHandler, scaleX, scaleY, xValue, yValue, getTransformer(axis), axis, this);
        addViewportJob(job);
    }

    /**
     * Zooms to the center of the chart with the given scale factor.
     *
     * @param scaleX
     * @param scaleY
     */
    public void zoomToCenter(float scaleX, float scaleY) {
        MPPointF center = getCenterOffsets();
        Matrix save = mZoomMatrixBuffer;
        mViewPortHandler.zoom(scaleX, scaleY, center.x, -center.y, save);
        mViewPortHandler.refresh(save, this, false);
    }

    /**
     * Zooms by the specified scale factor to the specified values on the specified axis.
     *
     * @param scaleX
     * @param scaleY
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration
     */
    public void zoomAndCenterAnimated(float scaleX, float scaleY, float xValue, float yValue, AxisDependency axis, long duration) {
        MPPointD origin = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);

        Runnable job = AnimatedZoomJob.getInstance(
                mViewPortHandler, this, getTransformer(axis), getAxis(axis), mXAxis.mAxisRange,
                scaleX, scaleY, mViewPortHandler.getScaleX(), mViewPortHandler.getScaleY(), xValue,
                yValue, (float) origin.x, (float) origin.y, duration
        );
        addViewportJob(job);

        MPPointD.recycleInstance(origin);
    }

    @NonNull
    protected Matrix mFitScreenMatrixBuffer = new Matrix();

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's bounds.
     */
    public void fitScreen() {
        Matrix save = mFitScreenMatrixBuffer;
        mViewPortHandler.fitScreen(save);
        mViewPortHandler.refresh(save, this, false);

        calculateOffsets();
        postInvalidate();
    }

    /**
     * Sets the minimum scale factor value to which can be zoomed out. 1f = fitScreen
     *
     * @param scaleX
     * @param scaleY
     */
    public void setScaleMinima(float scaleX, float scaleY) {
        mViewPortHandler.setMinimumScaleX(scaleX);
        mViewPortHandler.setMinimumScaleY(scaleY);
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be maximum visible at once (no
     * further zooming out allowed). If this is e.g. set to 10, no more than a range of 10 on the
     * x-axis can be viewed at once without scrolling.
     *
     * @param maxXRange The maximum visible range of x-values.
     */
    public void setVisibleXRangeMaximum(float maxXRange) {
        float xScale = mXAxis.mAxisRange / (maxXRange);
        mViewPortHandler.setMinimumScaleX(xScale);
    }

    /**
     * Sets the size of the area (range on the x-axis) that should be minimum visible at once (no
     * further zooming in allowed). If this is e.g. set to 10, no less than a range of 10 on the
     * x-axis can be viewed at once without scrolling.
     *
     * @param minXRange The minimum visible range of x-values.
     */
    public void setVisibleXRangeMinimum(float minXRange) {
        float xScale = mXAxis.mAxisRange / (minXRange);
        mViewPortHandler.setMaximumScaleX(xScale);
    }

    /**
     * Limits the maximum and minimum x range that can be visible by pinching and zooming. e.g.
     * minRange=10, maxRange=100 the smallest range to be displayed at once is 10, and no more than
     * a range of 100 values can be viewed at once without scrolling.
     *
     * @param minXRange
     * @param maxXRange
     */
    public void setVisibleXRange(float minXRange, float maxXRange) {
        float minScale = mXAxis.mAxisRange / minXRange;
        float maxScale = mXAxis.mAxisRange / maxXRange;
        mViewPortHandler.setMinMaxScaleX(minScale, maxScale);
    }

    /**
     * Sets the size of the area (range on the y-axis) that should be maximum visible at once.
     *
     * @param maxYRange the maximum visible range on the y-axis
     * @param axis      the axis for which this limit should apply
     */
    public void setVisibleYRangeMaximum(float maxYRange, AxisDependency axis) {
        float yScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinimumScaleY(yScale);
    }

    /**
     * Sets the size of the area (range on the y-axis) that should be minimum visible at once, no
     * further zooming in possible.
     *
     * @param minYRange
     * @param axis      the axis for which this limit should apply
     */
    public void setVisibleYRangeMinimum(float minYRange, AxisDependency axis) {
        float yScale = getAxisRange(axis) / minYRange;
        mViewPortHandler.setMaximumScaleY(yScale);
    }

    /**
     * Limits the maximum and minimum y range that can be visible by pinching and zooming.
     *
     * @param minYRange
     * @param maxYRange
     * @param axis
     */
    public void setVisibleYRange(float minYRange, float maxYRange, AxisDependency axis) {
        float minScale = getAxisRange(axis) / minYRange;
        float maxScale = getAxisRange(axis) / maxYRange;
        mViewPortHandler.setMinMaxScaleY(minScale, maxScale);
    }


    /**
     * Moves the left side of the current viewport to the specified x-position. This also refreshes
     * the chart by calling invalidate().
     *
     * @param xValue
     */
    public void moveViewToX(float xValue) {
        Runnable job = MoveViewJob.getInstance(mViewPortHandler, xValue, 0f, getTransformer(AxisDependency.LEFT), this);

        addViewportJob(job);
    }

    /**
     * This will move the left side of the current viewport to the specified x-value on the x-axis,
     * and center the viewport to the specified y value on the y-axis. This also refreshes the chart
     * by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y-axis
     */
    public void moveViewTo(float xValue, float yValue, AxisDependency axis) {
        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
        Runnable job = MoveViewJob.getInstance(mViewPortHandler, xValue, yValue + yInView / 2f, getTransformer(axis), this);

        addViewportJob(job);
    }

    /**
     * This will move the left side of the current viewport to the specified x-value and center the
     * viewport to the y value animated. This also refreshes the chart by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration the duration of the animation in milliseconds
     */
    public void moveViewToAnimated(float xValue, float yValue, AxisDependency axis, long duration) {
        MPPointD bounds = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);
        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();

        Runnable job = AnimatedMoveViewJob.getInstance(
                mViewPortHandler, xValue, yValue + yInView / 2f, getTransformer(axis),
                this, (float) bounds.x, (float) bounds.y, duration
        );

        addViewportJob(job);

        MPPointD.recycleInstance(bounds);
    }

    /**
     * Centers the viewport to the specified y value on the y-axis. This also refreshes the chart by
     * calling invalidate().
     *
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y-axis
     */
    public void centerViewToY(float yValue, AxisDependency axis) {
        float valuesInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
        Runnable job = MoveViewJob.getInstance(mViewPortHandler, 0f, yValue + valuesInView / 2f, getTransformer(axis), this);

        addViewportJob(job);
    }

    /**
     * This will move the center of the current viewport to the specified x and y value. This also
     * refreshes the chart by calling invalidate().
     *
     * @param xValue
     * @param yValue
     * @param axis   - which axis should be used as a reference for the y axis
     */
    public void centerViewTo(float xValue, float yValue, AxisDependency axis) {
        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
        float xInView = getXAxis().mAxisRange / mViewPortHandler.getScaleX();

        Runnable job = MoveViewJob.getInstance(mViewPortHandler, xValue - xInView / 2f, yValue + yInView / 2f, getTransformer(axis), this);

        addViewportJob(job);
    }

    /**
     * This will move the center of the current viewport to the specified x and y value animated.
     *
     * @param xValue
     * @param yValue
     * @param axis
     * @param duration the duration of the animation in milliseconds
     */
    public void centerViewToAnimated(float xValue, float yValue, AxisDependency axis, long duration) {
        MPPointD bounds = getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), axis);

        float yInView = getAxisRange(axis) / mViewPortHandler.getScaleY();
        float xInView = getXAxis().mAxisRange / mViewPortHandler.getScaleX();

        Runnable job = AnimatedMoveViewJob.getInstance(
                mViewPortHandler, xValue - xInView / 2f, yValue + yInView / 2f,
                getTransformer(axis), this, (float) bounds.x, (float) bounds.y, duration
        );

        addViewportJob(job);

        MPPointD.recycleInstance(bounds);
    }

    /**
     * Flag that indicates if a custom viewport offset has been set.
     */
    private boolean mCustomViewPortEnabled = false;

    /**
     * Sets custom offsets for the current ViewPort (the offsets on the sides of the actual chart
     * window). Setting this will prevent the chart from automatically calculating it's offsets. Use
     * resetViewPortOffsets() to undo this. ONLY USE THIS WHEN YOU KNOW WHAT YOU ARE DOING, else use
     * setExtraOffsets(...).
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setViewPortOffsets(final float left, final float top, final float right, final float bottom) {
        mCustomViewPortEnabled = true;

        post(new Runnable() {
            @Override
            public void run() {
                mViewPortHandler.restrainViewPort(left, top, right, bottom);
                prepareOffsetMatrix();
                prepareValuePxMatrix();
            }
        });
    }

    /**
     * Resets all custom offsets set via setViewPortOffsets(...) method. Allows the chart to again
     * calculate all offsets automatically.
     */
    public void resetViewPortOffsets() {
        mCustomViewPortEnabled = false;
        calculateOffsets();
    }

    /**
     * Returns the range of the specified axis.
     *
     * @param axis
     * @return
     */
    protected float getAxisRange(AxisDependency axis) {
        if (axis == AxisDependency.LEFT) {
            return mAxisLeft.mAxisRange;
        } else {
            return mAxisRight.mAxisRange;
        }
    }

    /**
     * Sets the OnDrawListener.
     *
     * @param drawListener
     */
    public void setOnDrawListener(OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    /**
     * Returns the OnDrawListener.
     */
    @Nullable
    public OnDrawListener getDrawListener() {
        return mDrawListener;
    }

    protected float[] mGetPositionBuffer = new float[2];

    /**
     * Returns a recyclable MPPointF instance. Returns the position (in pixels) the provided Entry
     * has inside the chart view or null, if the provided Entry is null.
     *
     * @param e
     */
    @Nullable
    public MPPointF getPosition(@Nullable Entry e, AxisDependency axis) {
        if (e == null) {
            return null;
        }

        mGetPositionBuffer[0] = e.getX();
        mGetPositionBuffer[1] = e.getY();

        getTransformer(axis).pointValuesToPixel(mGetPositionBuffer);

        return MPPointF.getInstance(mGetPositionBuffer[0], mGetPositionBuffer[1]);
    }

    /**
     * Sets the number of maximum visible drawn values on the chart only active when setDrawValues()
     * is enabled.
     *
     * @param count
     */
    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }

    /**
     * Set this to true to allow highlighting per dragging over the chart surface when it is fully
     * zoomed out.
     *
     * @param enabled
     */
    public void setHighlightPerDragEnabled(boolean enabled) {
        mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return mHighlightPerDragEnabled;
    }

    /**
     * Sets the color for the background of the chart-drawing area (everything behind the grid
     * lines).
     *
     * @param color
     */
    public void setGridBackgroundColor(int color) {
        mGridBackgroundPaint.setColor(color);
    }

    /**
     * Set this to true to enable dragging (moving the chart with the finger) for the chart (this
     * does not effect scaling).
     *
     * @param enabled
     */
    public void setDragEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
        this.mDragYEnabled = enabled;
    }

    /**
     * Returns true if dragging is enabled for the chart, false if not.
     */
    public boolean isDragEnabled() {
        return mDragXEnabled || mDragYEnabled;
    }

    /**
     * Set this to true to enable dragging on the X axis.
     *
     * @param enabled
     */
    public void setDragXEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
    }

    /**
     * Returns true if dragging on the X axis is enabled for the chart, false if not.
     */
    public boolean isDragXEnabled() {
        return mDragXEnabled;
    }

    /**
     * Set this to true to enable dragging on the Y axis.
     *
     * @param enabled
     */
    public void setDragYEnabled(boolean enabled) {
        this.mDragYEnabled = enabled;
    }

    /**
     * Returns true if dragging on the Y axis is enabled for the chart, false if not.
     */
    public boolean isDragYEnabled() {
        return mDragYEnabled;
    }

    /**
     * Set this to true to enable scaling (zooming in and out by gesture) for the chart (this does
     * not effect dragging) on both X- and Y-Axis.
     *
     * @param enabled
     */
    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return mScaleYEnabled;
    }

    /**
     * Set this to true to enable zooming in by double-tap on the chart.
     *
     * @param enabled
     */
    public void setDoubleTapToZoomEnabled(boolean enabled) {
        mDoubleTapToZoomEnabled = enabled;
    }

    /**
     * Returns true if zooming via double-tap is enabled false if not.
     */
    public boolean isDoubleTapToZoomEnabled() {
        return mDoubleTapToZoomEnabled;
    }

    /**
     * Set this to true to draw the grid background, false if not.
     *
     * @param enabled
     */
    public void setDrawGridBackground(boolean enabled) {
        mDrawGridBackground = enabled;
    }

    /**
     * When enabled, the borders rectangle will be rendered. If this is enabled, there is no point
     * drawing the axis-lines of x- and y-axis.
     *
     * @param enabled
     */
    public void setDrawBorders(boolean enabled) {
        mDrawBorders = enabled;
    }

    /**
     * When enabled, the borders rectangle will be rendered. If this is enabled, there is no point
     * drawing the axis-lines of x- and y-axis.
     *
     * @return
     */
    public boolean isDrawBordersEnabled() {
        return mDrawBorders;
    }

    /**
     * When enabled, the values will be clipped to contentRect, otherwise they can bleed outside the
     * content rect.
     *
     * @param enabled
     */
    public void setClipValuesToContent(boolean enabled) {
        mClipValuesToContent = enabled;
    }

    /**
     * When enabled, the values will be clipped to contentRect, otherwise they can bleed outside the
     * content rect.
     *
     * @return
     */
    public boolean isClipValuesToContentEnabled() {
        return mClipValuesToContent;
    }

    /**
     * Sets the width of the border lines in dp.
     *
     * @param width
     */
    public void setBorderWidth(float width) {
        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }

    /**
     * Sets the color of the chart border lines.
     *
     * @param color
     */
    public void setBorderColor(int color) {
        mBorderPaint.setColor(color);
    }

    /**
     * Gets the minimum offset (padding) around the chart.
     */
    public float getMinOffset() {
        return mMinOffset;
    }

    /**
     * Sets the minimum offset (padding) around the chart.
     */
    public void setMinOffset(float minOffset) {
        mMinOffset = minOffset;
    }

    /**
     * Returns true if keeping the position on rotation is enabled and false if not.
     */
    public boolean isKeepPositionOnRotation() {
        return mKeepPositionOnRotation;
    }

    /**
     * Sets whether the chart should keep its position (zoom / scroll) after a rotation (orientation
     * change).
     */
    public void setKeepPositionOnRotation(boolean keepPositionOnRotation) {
        mKeepPositionOnRotation = keepPositionOnRotation;
    }

    /**
     * Returns a recyclable MPPointD instance. Returns the x and y values in the chart at the given
     * touch point (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to getPixelForValues(...).
     *
     * @param x
     * @param y
     */
    @NonNull
    public MPPointD getValuesByTouchPoint(float x, float y, AxisDependency axis) {
        MPPointD result = MPPointD.getInstance(0, 0);
        getValuesByTouchPoint(x, y, axis, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, AxisDependency axis, MPPointD outputPoint) {
        getTransformer(axis).getValuesByTouchPoint(x, y, outputPoint);
    }

    /**
     * Returns a recyclable MPPointD instance. Transforms the given chart values into pixels. This
     * is the opposite method to getValuesByTouchPoint(...).
     *
     * @param x
     * @param y
     */
    public MPPointD getPixelForValues(float x, float y, AxisDependency axis) {
        return getTransformer(axis).getPixelForValues(x, y);
    }

    /**
     * Returns the Entry object displayed at the touched position of the chart.
     *
     * @param x
     * @param y
     */
    @Nullable
    public Entry getEntryByTouchPoint(float x, float y) {
        Highlight highlight = getHighlightByTouchPoint(x, y);
        if (highlight != null) {
            return mData.getEntryForHighlight(highlight);
        }
        return null;
    }

    /**
     * Returns the DataSet object displayed at the touched position of the chart.
     *
     * @param x
     * @param y
     */
    @Nullable
    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(float x, float y) {
        Highlight highlight = getHighlightByTouchPoint(x, y);
        if (highlight != null) {
            return mData.getDataSetByIndex(highlight.getDataSetIndex());
        }
        return null;
    }

    /**
     * Buffer for storing lowest visible x point.
     */
    protected MPPointD posForGetLowestVisibleX = MPPointD.getInstance(0, 0);

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on the chart.
     */
    @Override
    public float getLowestVisibleX() {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), posForGetLowestVisibleX);
        return (float) Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.x);
    }

    /**
     * Buffer for storing highest visible x point.
     */
    protected MPPointD posForGetHighestVisibleX = MPPointD.getInstance(0, 0);

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible on the chart.
     */
    @Override
    public float getHighestVisibleX() {
        getTransformer(AxisDependency.LEFT).getValuesByTouchPoint(mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(), posForGetHighestVisibleX);
        return (float) Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.x);
    }

    /**
     * Returns the range visible on the x-axis.
     */
    public float getVisibleXRange() {
        return Math.abs(getHighestVisibleX() - getLowestVisibleX());
    }

    /**
     * Returns the current x-scale factor.
     */
    public float getScaleX() {
        if (mViewPortHandler == null) {
            return 1f;
        } else {
            return mViewPortHandler.getScaleX();
        }
    }

    /**
     * Returns the current y-scale factor.
     */
    public float getScaleY() {
        if (mViewPortHandler == null) {
            return 1f;
        } else {
            return mViewPortHandler.getScaleY();
        }
    }

    /**
     * Returns true if the chart is fully zoomed out, else false.
     */
    public boolean isFullyZoomedOut() {
        return mViewPortHandler.isFullyZoomedOut();
    }

    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the top axis.
     */
    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    /**
     * Returns the right y-axis object. In the horizontal bar-chart, this is the bottom axis.
     */
    public YAxis getAxisRight() {
        return mAxisRight;
    }

    /**
     * Returns the y-axis object to the corresponding AxisDependency. In the horizontal bar-chart,
     * LEFT == top, RIGHT == BOTTOM.
     *
     * @param axis
     */
    public YAxis getAxis(AxisDependency axis) {
        if (axis == AxisDependency.LEFT) {
            return mAxisLeft;
        } else {
            return mAxisRight;
        }
    }

    @Override
    public boolean isInverted(AxisDependency axis) {
        return getAxis(axis).isInverted();
    }

    /**
     * If set to true, both x and y axis can be scaled simultaneously with 2 fingers, if false,
     * x and y axis can be scaled separately. default: false
     *
     * @param enabled
     */
    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    /**
     * Returns true if pinch-zoom is enabled, false if not.
     */
    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's bounds on the x-axis.
     *
     * @param offset
     */
    public void setDragOffsetX(float offset) {
        mViewPortHandler.setDragOffsetX(offset);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's bounds on the y-axis.
     *
     * @param offset
     */
    public void setDragOffsetY(float offset) {
        mViewPortHandler.setDragOffsetY(offset);
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     */
    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }

    public XAxisRenderer getRendererXAxis() {
        return mXAxisRenderer;
    }

    /**
     * Sets a custom XAxisRenderer and overrides the existing (default) one.
     *
     * @param xAxisRenderer
     */
    public void setXAxisRenderer(XAxisRenderer xAxisRenderer) {
        mXAxisRenderer = xAxisRenderer;
    }

    public YAxisRenderer getRendererLeftYAxis() {
        return mAxisRendererLeft;
    }

    /**
     * Sets a custom axis renderer for the left axis and overwrites the existing one.
     *
     * @param rendererLeftYAxis
     */
    public void setRendererLeftYAxis(YAxisRenderer rendererLeftYAxis) {
        mAxisRendererLeft = rendererLeftYAxis;
    }

    public YAxisRenderer getRendererRightYAxis() {
        return mAxisRendererRight;
    }

    /**
     * Sets a custom axis renderer for the right axis and overwrites the existing one.
     *
     * @param rendererRightYAxis
     */
    public void setRendererRightYAxis(YAxisRenderer rendererRightYAxis) {
        mAxisRendererRight = rendererRightYAxis;
    }

    @Override
    public float getYChartMax() {
        return Math.max(mAxisLeft.mAxisMaximum, mAxisRight.mAxisMaximum);
    }

    @Override
    public float getYChartMin() {
        return Math.min(mAxisLeft.mAxisMinimum, mAxisRight.mAxisMinimum);
    }

    /**
     * Returns true if either the left or the right or both axes are inverted.
     */
    public boolean isAnyAxisInverted() {
        return mAxisLeft.isInverted() || mAxisRight.isInverted();
    }

    /**
     * Flag that indicates if auto scaling on the y axis is enabled. This is especially interesting
     * for charts displaying financial data.
     *
     * @param enabled the y axis automatically adjusts to the min and max y
     *                values of the current x axis range whenever the viewport
     *                changes
     */
    public void setAutoScaleMinMaxEnabled(boolean enabled) {
        mAutoScaleMinMaxEnabled = enabled;
    }

    /**
     * Returns true if auto scaling on the y axis is enabled.
     */
    public boolean isAutoScaleMinMaxEnabled() {
        return mAutoScaleMinMaxEnabled;
    }

    @Override
    public void setPaint(Paint paint, int which) {
        super.setPaint(paint, which);

        if (which == PAINT_GRID_BACKGROUND) {
            mGridBackgroundPaint = paint;
        }
    }

    @Override
    @Nullable
    public Paint getPaint(int which) {
        Paint paint = super.getPaint(which);
        if (paint != null) {
            return paint;
        }

        if (which == PAINT_GRID_BACKGROUND) {
            return mGridBackgroundPaint;
        }

        return null;
    }

    protected float[] mOnSizeChangedBuffer = new float[2];

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        // Saving current position of chart.
        mOnSizeChangedBuffer[0] = mOnSizeChangedBuffer[1] = 0;

        if (mKeepPositionOnRotation) {
            mOnSizeChangedBuffer[0] = mViewPortHandler.contentLeft();
            mOnSizeChangedBuffer[1] = mViewPortHandler.contentTop();
            getTransformer(AxisDependency.LEFT).pixelsToValue(mOnSizeChangedBuffer);
        }

        // Superclass transforms chart
        super.onSizeChanged(w, h, oldWidth, oldHeight);

        if (mKeepPositionOnRotation) {
            // Restoring old position of chart
            getTransformer(AxisDependency.LEFT).pointValuesToPixel(mOnSizeChangedBuffer);
            mViewPortHandler.centerViewPort(mOnSizeChangedBuffer, this);
        } else {
            mViewPortHandler.refresh(mViewPortHandler.getMatrixTouch(), this, true);
        }
    }
}
