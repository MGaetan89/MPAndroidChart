package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * TouchListener for Bar-, Line-, Scatter- and CandleStickChart with handles all touch interaction.
 * Long press == Zoom out. Double-Tap == Zoom in.
 *
 * @author Philipp Jahoda
 */
public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>> {
    /**
     * The original touch-matrix from the chart.
     */
    private Matrix mMatrix;

    /**
     * Matrix for saving the original matrix state.
     */
    @NonNull
    private final Matrix mSavedMatrix = new Matrix();

    /**
     * Point where the touch action started.
     */
    @NonNull
    private final MPPointF mTouchStartPoint = MPPointF.getInstance(0f, 0f);

    /**
     * Center between two pointers (fingers on the display).
     */
    @NonNull
    private final MPPointF mTouchPointCenter = MPPointF.getInstance(0f, 0f);

    private float mSavedXDist = 1f;
    private float mSavedYDist = 1f;
    private float mSavedDist = 1f;

    @Nullable
    private IDataSet mClosestDataSetToTouch;

    /**
     * Used for tracking velocity of dragging.
     */
    @Nullable
    private VelocityTracker mVelocityTracker;

    private long mDecelerationLastTime = 0L;
    private final MPPointF mDecelerationCurrentPoint = MPPointF.getInstance(0L, 0L);
    private final MPPointF mDecelerationVelocity = MPPointF.getInstance(0f, 0f);

    /**
     * The distance of movement that will be counted as a drag.
     */
    private float mDragTriggerDist;

    /**
     * The minimum distance between the pointers that will trigger a zoom gesture.
     */
    private float mMinScalePointerDistance;

    /**
     * Constructor with initialization parameters.
     *
     * @param chart               instance of the chart
     * @param touchMatrix         the touch-matrix of the chart
     * @param dragTriggerDistance the minimum movement distance that will be interpreted as a "drag"
     *                            gesture in dp (3dp equals to about 9 pixels on a 5.5" FHD screen)
     */
    public BarLineChartTouchListener(@NonNull BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart, Matrix touchMatrix, float dragTriggerDistance) {
        super(chart);

        this.mMatrix = touchMatrix;
        this.mDragTriggerDist = Utils.convertDpToPixel(dragTriggerDistance);
        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        if (mTouchMode == NONE && mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }

        if (!mChart.isDragEnabled() && !mChart.isScaleXEnabled() && !mChart.isScaleYEnabled()) {
            return true;
        }

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startAction(event);
                stopDeceleration();
                saveTouchStart(event);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() >= 2) {
                    mChart.disableScroll();

                    saveTouchStart(event);

                    // Get the distance between the pointers on the x-axis
                    mSavedXDist = getXDist(event);

                    // Get the distance between the pointers on the y-axis
                    mSavedYDist = getYDist(event);

                    // Get the total distance between the pointers
                    mSavedDist = spacing(event);

                    if (mSavedDist > 10f) {
                        if (mChart.isPinchZoomEnabled()) {
                            mTouchMode = PINCH_ZOOM;
                        } else {
                            if (mChart.isScaleXEnabled() != mChart.isScaleYEnabled()) {
                                mTouchMode = mChart.isScaleXEnabled() ? X_ZOOM : Y_ZOOM;
                            } else {
                                mTouchMode = mSavedXDist > mSavedYDist ? X_ZOOM : Y_ZOOM;
                            }
                        }
                    }

                    // Determine the touch-pointer center
                    midPoint(mTouchPointCenter, event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchMode == DRAG) {
                    mChart.disableScroll();

                    float x = mChart.isDragXEnabled() ? event.getX() - mTouchStartPoint.x : 0f;
                    float y = mChart.isDragYEnabled() ? event.getY() - mTouchStartPoint.y : 0f;

                    performDrag(event, x, y);
                } else if (mTouchMode == X_ZOOM || mTouchMode == Y_ZOOM || mTouchMode == PINCH_ZOOM) {
                    mChart.disableScroll();

                    if (mChart.isScaleXEnabled() || mChart.isScaleYEnabled()) {
                        performZoom(event);
                    }
                } else if (mTouchMode == NONE && Math.abs(distance(event.getX(), mTouchStartPoint.x, event.getY(), mTouchStartPoint.y)) > mDragTriggerDist) {
                    if (mChart.isDragEnabled()) {
                        boolean shouldPan = !mChart.isFullyZoomedOut() || !mChart.hasNoDragOffset();

                        if (shouldPan) {
                            float distanceX = Math.abs(event.getX() - mTouchStartPoint.x);
                            float distanceY = Math.abs(event.getY() - mTouchStartPoint.y);

                            // Disable dragging in a direction that's disallowed
                            if ((mChart.isDragXEnabled() || distanceY >= distanceX) && (mChart.isDragYEnabled() || distanceY <= distanceX)) {
                                mLastGesture = ChartGesture.DRAG;
                                mTouchMode = DRAG;
                            }
                        } else {
                            if (mChart.isHighlightPerDragEnabled()) {
                                mLastGesture = ChartGesture.DRAG;

                                if (mChart.isHighlightPerDragEnabled()) {
                                    performHighlightDrag(event);
                                }
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                final int pointerId = event.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, Utils.getMaximumFlingVelocity());
                final float velocityY = velocityTracker.getYVelocity(pointerId);
                final float velocityX = velocityTracker.getXVelocity(pointerId);

                if (Math.abs(velocityX) > Utils.getMinimumFlingVelocity() || Math.abs(velocityY) > Utils.getMinimumFlingVelocity()) {
                    if (mTouchMode == DRAG && mChart.isDragDecelerationEnabled()) {
                        stopDeceleration();

                        mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                        mDecelerationCurrentPoint.x = event.getX();
                        mDecelerationCurrentPoint.y = event.getY();
                        mDecelerationVelocity.x = velocityX;
                        mDecelerationVelocity.y = velocityY;

                        // This causes computeScroll to fire, recommended for this by Google
                        Utils.postInvalidateOnAnimation(mChart);
                    }
                }

                if (mTouchMode == X_ZOOM || mTouchMode == Y_ZOOM || mTouchMode == PINCH_ZOOM || mTouchMode == POST_ZOOM) {
                    // Range might have changed, which means that Y-axis labels could have changed
                    // in size, affecting Y-axis size. So we need to recalculate offsets.
                    mChart.calculateOffsets();
                    mChart.postInvalidate();
                }

                mTouchMode = NONE;
                mChart.enableScroll();

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                endAction(event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (mVelocityTracker != null) {
                    Utils.velocityTrackerPointerUpCleanUpIfNecessary(event, mVelocityTracker);
                }

                mTouchMode = POST_ZOOM;
                break;

            case MotionEvent.ACTION_CANCEL:
                mTouchMode = NONE;
                endAction(event);
                break;
        }

        // Perform the transformation, update the chart
        mMatrix = mChart.getViewPortHandler().refresh(mMatrix, mChart, true);

        return true;
    }

    /**
     * Saves the current Matrix state and the touch-start point.
     *
     * @param event
     */
    private void saveTouchStart(MotionEvent event) {
        mSavedMatrix.set(mMatrix);

        mTouchStartPoint.x = event.getX();
        mTouchStartPoint.y = event.getY();
        mClosestDataSetToTouch = mChart.getDataSetByTouchPoint(event.getX(), event.getY());
    }

    /**
     * Performs all necessary operations needed for dragging.
     *
     * @param event
     */
    private void performDrag(MotionEvent event, float distanceX, float distanceY) {
        mLastGesture = ChartGesture.DRAG;

        mMatrix.set(mSavedMatrix);

        // Check if axis is inverted
        if (inverted()) {
            // If there is an inverted HorizontalBarChart
            if (mChart instanceof HorizontalBarChart) {
                distanceX = -distanceX;
            } else {
                distanceY = -distanceY;
            }
        }

        mMatrix.postTranslate(distanceX, distanceY);

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartTranslate(event, distanceX, distanceY);
        }
    }

    /**
     * Performs the all operations necessary for pinch and axis zoom.
     *
     * @param event
     */
    private void performZoom(MotionEvent event) {
        if (event.getPointerCount() >= 2) { // Two finger zoom
            // Get the distance between the pointers of the touch event
            float totalDist = spacing(event);
            if (totalDist > mMinScalePointerDistance) {
                // Get the translation
                MPPointF t = getTrans(mTouchPointCenter.x, mTouchPointCenter.y);
                ViewPortHandler h = mChart.getViewPortHandler();
                OnChartGestureListener listener = mChart.getOnChartGestureListener();

                // Take actions depending on the activated touch mode
                if (mTouchMode == PINCH_ZOOM) {
                    mLastGesture = ChartGesture.PINCH_ZOOM;

                    float scale = totalDist / mSavedDist; // Total scale

                    boolean isZoomingOut = scale < 1f;
                    boolean canZoomMoreX = isZoomingOut ? h.canZoomOutMoreX() : h.canZoomInMoreX();
                    boolean canZoomMoreY = isZoomingOut ? h.canZoomOutMoreY() : h.canZoomInMoreY();

                    float scaleX = mChart.isScaleXEnabled() ? scale : 1f;
                    float scaleY = mChart.isScaleYEnabled() ? scale : 1f;

                    if (canZoomMoreY || canZoomMoreX) {
                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(scaleX, scaleY, t.x, t.y);

                        if (listener != null) {
                            listener.onChartScale(event, scaleX, scaleY);
                        }
                    }
                } else if (mTouchMode == X_ZOOM && mChart.isScaleXEnabled()) {
                    mLastGesture = ChartGesture.X_ZOOM;

                    float xDist = getXDist(event);
                    float scaleX = xDist / mSavedXDist; // x-axis scale

                    boolean isZoomingOut = scaleX < 1f;
                    boolean canZoomMoreX = isZoomingOut ? h.canZoomOutMoreX() : h.canZoomInMoreX();

                    if (canZoomMoreX) {
                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(scaleX, 1f, t.x, t.y);

                        if (listener != null) {
                            listener.onChartScale(event, scaleX, 1f);
                        }
                    }
                } else if (mTouchMode == Y_ZOOM && mChart.isScaleYEnabled()) {
                    mLastGesture = ChartGesture.Y_ZOOM;

                    float yDist = getYDist(event);
                    float scaleY = yDist / mSavedYDist; // y-axis scale

                    boolean isZoomingOut = scaleY < 1f;
                    boolean canZoomMoreY = isZoomingOut ? h.canZoomOutMoreY() : h.canZoomInMoreY();

                    if (canZoomMoreY) {
                        mMatrix.set(mSavedMatrix);
                        mMatrix.postScale(1f, scaleY, t.x, t.y);

                        if (listener != null) {
                            listener.onChartScale(event, 1f, scaleY);
                        }
                    }
                }

                MPPointF.recycleInstance(t);
            }
        }
    }

    /**
     * Highlights upon dragging, generates callbacks for the selection-listener.
     *
     * @param event
     */
    private void performHighlightDrag(MotionEvent event) {
        Highlight highlight = mChart.getHighlightByTouchPoint(event.getX(), event.getY());

        if (highlight != null && !highlight.equals(mLastHighlighted)) {
            mLastHighlighted = highlight;
            mChart.highlightValue(highlight, true);
        }
    }

    /**
     * Determines the center point between two pointer touch points.
     *
     * @param point
     * @param event
     */
    private static void midPoint(MPPointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.x = x / 2f;
        point.y = y / 2f;
    }

    /**
     * Returns the distance between two pointer touch points.
     *
     * @param event
     */
    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the distance on the x-axis between two pointers (fingers on the display).
     *
     * @param event
     * @return
     */
    private static float getXDist(MotionEvent event) {
        return Math.abs(event.getX(0) - event.getX(1));
    }

    /**
     * Calculates the distance on the y-axis between two pointers (fingers on the display).
     *
     * @param event
     * @return
     */
    private static float getYDist(MotionEvent event) {
        return Math.abs(event.getY(0) - event.getY(1));
    }

    /**
     * Returns a recyclable MPPointF instance. Returns the correct translation depending on the
     * provided x and y touch points.
     *
     * @param x
     * @param y
     */
    public MPPointF getTrans(float x, float y) {
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();

        float xTrans = x - viewPortHandler.offsetLeft();
        float yTrans;

        // Check if axis is inverted
        if (inverted()) {
            yTrans = -(y - viewPortHandler.offsetTop());
        } else {
            yTrans = -(mChart.getMeasuredHeight() - y - viewPortHandler.offsetBottom());
        }

        return MPPointF.getInstance(xTrans, yTrans);
    }

    /**
     * Returns true if the current touch situation should be interpreted as inverted, false if not.
     */
    private boolean inverted() {
        return (mClosestDataSetToTouch == null && mChart.isAnyAxisInverted())
                || (mClosestDataSetToTouch != null && mChart.isInverted(mClosestDataSetToTouch.getAxisDependency()));
    }

    /**
     * Returns the matrix object the listener holds.
     */
    public Matrix getMatrix() {
        return mMatrix;
    }

    /**
     * Sets the minimum distance that will be interpreted as a "drag" by the chart in dp.
     *
     * @param dragTriggerDistance
     */
    public void setDragTriggerDist(float dragTriggerDistance) {
        this.mDragTriggerDist = Utils.convertDpToPixel(dragTriggerDistance);
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        mLastGesture = ChartGesture.DOUBLE_TAP;

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartDoubleTapped(event);
        }

        // Check if double-tap zooming is enabled
        if (mChart.isDoubleTapToZoomEnabled() && mChart.getData().getEntryCount() > 0) {
            MPPointF trans = getTrans(event.getX(), event.getY());

            mChart.zoom(mChart.isScaleXEnabled() ? 1.4f : 1f, mChart.isScaleYEnabled() ? 1.4f : 1f, trans.x, trans.y);

            if (mChart.isLogEnabled()) {
                Log.i("BarLineChartTouch", "Double-Tap, Zooming In, x: " + trans.x + ", y: " + trans.y);
            }

            MPPointF.recycleInstance(trans);
        }

        return super.onDoubleTap(event);
    }

    @Override
    public void onLongPress(MotionEvent event) {
        mLastGesture = ChartGesture.LONG_PRESS;

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartLongPressed(event);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        mLastGesture = ChartGesture.SINGLE_TAP;

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartSingleTapped(event);
        }

        if (!mChart.isHighlightPerTapEnabled()) {
            return false;
        }

        Highlight highlight = mChart.getHighlightByTouchPoint(event.getX(), event.getY());
        performHighlight(highlight, event);

        return super.onSingleTapUp(event);
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        mLastGesture = ChartGesture.FLING;

        OnChartGestureListener listener = mChart.getOnChartGestureListener();
        if (listener != null) {
            listener.onChartFling(event1, event2, velocityX, velocityY);
        }

        return super.onFling(event1, event2, velocityX, velocityY);
    }

    public void stopDeceleration() {
        mDecelerationVelocity.x = 0;
        mDecelerationVelocity.y = 0;
    }

    public void computeScroll() {
        if (mDecelerationVelocity.x == 0f && mDecelerationVelocity.y == 0f) {
            return; // There's no deceleration in progress
        }

        final long currentTime = AnimationUtils.currentAnimationTimeMillis();

        mDecelerationVelocity.x *= mChart.getDragDecelerationFrictionCoef();
        mDecelerationVelocity.y *= mChart.getDragDecelerationFrictionCoef();

        final float timeInterval = (float) (currentTime - mDecelerationLastTime) / 1000f;

        float distanceX = mDecelerationVelocity.x * timeInterval;
        float distanceY = mDecelerationVelocity.y * timeInterval;

        mDecelerationCurrentPoint.x += distanceX;
        mDecelerationCurrentPoint.y += distanceY;

        MotionEvent event = MotionEvent.obtain(currentTime, currentTime, MotionEvent.ACTION_MOVE, mDecelerationCurrentPoint.x, mDecelerationCurrentPoint.y, 0);

        float dragDistanceX = mChart.isDragXEnabled() ? mDecelerationCurrentPoint.x - mTouchStartPoint.x : 0f;
        float dragDistanceY = mChart.isDragYEnabled() ? mDecelerationCurrentPoint.y - mTouchStartPoint.y : 0f;

        performDrag(event, dragDistanceX, dragDistanceY);

        event.recycle();
        mMatrix = mChart.getViewPortHandler().refresh(mMatrix, mChart, false);

        mDecelerationLastTime = currentTime;

        if (Math.abs(mDecelerationVelocity.x) >= 0.01f || Math.abs(mDecelerationVelocity.y) >= 0.01f) {
            Utils.postInvalidateOnAnimation(mChart); // This causes computeScroll to fire, recommended for this by Google
        } else {
            // Range might have changed, which means that Y-axis labels could have changed in size,
            // affecting Y-axis size. So we need to recalculate offsets.
            mChart.calculateOffsets();
            mChart.postInvalidate();

            stopDeceleration();
        }
    }
}
