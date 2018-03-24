package com.github.mikephil.charting.listener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * @author Philipp Jahoda
 */
public abstract class ChartTouchListener<T extends Chart<?>>
        extends GestureDetector.SimpleOnGestureListener
        implements View.OnTouchListener {
    public enum ChartGesture {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }

    /**
     * The last touch gesture that has been performed.
     **/
    protected ChartGesture mLastGesture = ChartGesture.NONE;

    // States
    protected static final int NONE = 0;
    protected static final int DRAG = 1;
    protected static final int X_ZOOM = 2;
    protected static final int Y_ZOOM = 3;
    protected static final int PINCH_ZOOM = 4;
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;

    /**
     * Integer field that holds the current touch-state.
     */
    protected int mTouchMode = NONE;

    /**
     * The last highlighted object (via touch).
     */
    @Nullable
    protected Highlight mLastHighlighted;

    /**
     * The GestureDetector used for detecting taps and long presses, ...
     */
    @Nullable
    protected GestureDetector mGestureDetector;

    /**
     * The chart the listener represents.
     */
    @NonNull
    protected T mChart;

    public ChartTouchListener(@NonNull T chart) {
        this.mChart = chart;

        mGestureDetector = new GestureDetector(chart.getContext(), this);
    }

    /**
     * Calls the OnChartGestureListener to do the start callback.
     *
     * @param event
     */
    public void startAction(MotionEvent event) {
        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartGestureStart(event, mLastGesture);
        }
    }

    /**
     * Calls the OnChartGestureListener to do the end callback.
     *
     * @param event
     */
    public void endAction(MotionEvent event) {
        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartGestureEnd(event, mLastGesture);
        }
    }

    /**
     * Sets the last value that was highlighted via touch.
     *
     * @param highlight
     */
    public void setLastHighlighted(@Nullable Highlight highlight) {
        mLastHighlighted = highlight;
    }

    /**
     * Returns the touch mode the listener is currently in.
     */
    public int getTouchMode() {
        return mTouchMode;
    }

    /**
     * Returns the last gesture that has been performed on the chart.
     */
    public ChartGesture getLastGesture() {
        return mLastGesture;
    }

    /**
     * Perform a highlight operation.
     *
     * @param event
     */
    protected void performHighlight(@Nullable Highlight highlight, MotionEvent event) {
        if (highlight == null || highlight.equals(mLastHighlighted)) {
            mChart.highlightValue(null, true);
            mLastHighlighted = null;
        } else {
            mChart.highlightValue(highlight, true);
            mLastHighlighted = highlight;
        }
    }

    /**
     * Returns the distance between two points.
     *
     * @param eventX
     * @param startX
     * @param eventY
     * @param startY
     */
    protected static float distance(float eventX, float startX, float eventY, float startY) {
        float dx = eventX - startX;
        float dy = eventY - startY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
