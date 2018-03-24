package com.github.mikephil.charting.listener;

import android.view.MotionEvent;

/**
 * Listener for callbacks when doing gestures on the chart.
 *
 * @author Philipp Jahoda
 */
public interface OnChartGestureListener {
    /**
     * Callbacks when a touch-gesture has started on the chart (ACTION_DOWN).
     *
     * @param event
     * @param lastPerformedGesture
     */
    void onChartGestureStart(MotionEvent event, ChartTouchListener.ChartGesture lastPerformedGesture);

    /**
     * Callbacks when a touch-gesture has ended on the chart (ACTION_UP, ACTION_CANCEL).
     *
     * @param event
     * @param lastPerformedGesture
     */
    void onChartGestureEnd(MotionEvent event, ChartTouchListener.ChartGesture lastPerformedGesture);

    /**
     * Callbacks when the chart is long pressed.
     *
     * @param event
     */
    void onChartLongPressed(MotionEvent event);

    /**
     * Callbacks when the chart is double-tapped.
     *
     * @param event
     */
    void onChartDoubleTapped(MotionEvent event);

    /**
     * Callbacks when the chart is single-tapped.
     *
     * @param event
     */
    void onChartSingleTapped(MotionEvent event);

    /**
     * Callbacks then a fling gesture is made on the chart.
     *
     * @param event1
     * @param event2
     * @param velocityX
     * @param velocityY
     */
    void onChartFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY);

    /**
     * Callbacks when the chart is scaled/zoomed via pinch zoom gesture.
     *
     * @param event
     * @param scaleX scale factor on the x-axis
     * @param scaleY scale factor on the y-axis
     */
    void onChartScale(MotionEvent event, float scaleX, float scaleY);

    /**
     * Callbacks when the chart is moved/translated via drag gesture.
     *
     * @param event
     * @param dX    translation distance on the x-axis
     * @param dY    translation distance on the y-axis
     */
    void onChartTranslate(MotionEvent event, float dX, float dY);
}
