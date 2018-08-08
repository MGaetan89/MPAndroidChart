package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * Touch listener for the PieChart.
 *
 * @author Philipp Jahoda
 */
public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>> {
    @NonNull
    private final MPPointF mTouchStartPoint = MPPointF.getInstance(0f, 0f);

    /**
     * The angle where the dragging started.
     */
    private float mStartAngle = 0f;

    @NonNull
    private final ArrayList<AngularVelocitySample> velocitySamples = new ArrayList<>();

    private long mDecelerationLastTime = 0L;
    private float mDecelerationAngularVelocity = 0f;

    public PieRadarChartTouchListener(@NonNull PieRadarChartBase<?> chart) {
        super(chart);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mGestureDetector != null && mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        // If rotation by touch is enabled
        if (mChart.isRotationEnabled()) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startAction(event);
                    stopDeceleration();
                    resetVelocity();

                    if (mChart.isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }

                    setGestureStartAngle(x, y);
                    mTouchStartPoint.x = x;
                    mTouchStartPoint.y = y;

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mChart.isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }

                    if (mTouchMode == NONE && distance(x, mTouchStartPoint.x, y, mTouchStartPoint.y) > Utils.convertDpToPixel(8f)) {
                        mLastGesture = ChartGesture.ROTATE;
                        mTouchMode = ROTATE;
                        mChart.disableScroll();
                    } else if (mTouchMode == ROTATE) {
                        updateGestureRotation(x, y);
                        mChart.invalidate();
                    }

                    endAction(event);
                    break;

                case MotionEvent.ACTION_UP:
                    if (mChart.isDragDecelerationEnabled()) {
                        stopDeceleration();
                        sampleVelocity(x, y);

                        mDecelerationAngularVelocity = calculateVelocity();

                        if (mDecelerationAngularVelocity != 0f) {
                            mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();

                            // This causes computeScroll to fire, recommended for this by Google
                            Utils.postInvalidateOnAnimation(mChart);
                        }
                    }

                    mChart.enableScroll();
                    mTouchMode = NONE;

                    endAction(event);
                    break;
            }
        }

        return true;
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
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
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

        return true;
    }

    private void resetVelocity() {
        velocitySamples.clear();
    }

    private void sampleVelocity(float touchLocationX, float touchLocationY) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();

        velocitySamples.add(new AngularVelocitySample(currentTime, mChart.getAngleForPoint(touchLocationX, touchLocationY)));

        // Remove samples older than our sample time - 1 seconds
        for (int i = 0, count = velocitySamples.size(); i < count - 2; i++) {
            if (currentTime - velocitySamples.get(i).time > 1000L) {
                velocitySamples.remove(0);
                i--;
                count--;
            } else {
                break;
            }
        }
    }

    private float calculateVelocity() {
        if (velocitySamples.isEmpty()) {
            return 0f;
        }

        AngularVelocitySample firstSample = velocitySamples.get(0);
        AngularVelocitySample lastSample = velocitySamples.get(velocitySamples.size() - 1);

        // Look for a sample that's closest to the latest sample, but not the same, so we can deduce the direction
        AngularVelocitySample beforeLastSample = firstSample;
        for (int i = velocitySamples.size() - 1; i >= 0; i--) {
            beforeLastSample = velocitySamples.get(i);
            if (beforeLastSample.angle != lastSample.angle) {
                break;
            }
        }

        // Calculate the sampling time
        float timeDelta = (lastSample.time - firstSample.time) / 1000f;
        if (timeDelta == 0f) {
            timeDelta = 0.1f;
        }

        // Calculate clockwise/ccw by choosing two values that should be closest to each other, so
        // if the angles are two far from each other we know they are inverted "for sure"
        boolean clockwise = lastSample.angle >= beforeLastSample.angle;
        if (Math.abs(lastSample.angle - beforeLastSample.angle) > 270f) {
            clockwise = !clockwise;
        }

        // Now if the "gesture" is over a too big of an angle - then we know the angles are inverted,
        // and we need to move them closer to each other from both sides of the 360.0 wrapping point
        if (lastSample.angle - firstSample.angle > 180f) {
            firstSample.angle += 360f;
        } else if (firstSample.angle - lastSample.angle > 180f) {
            lastSample.angle += 360f;
        }

        // The velocity
        float velocity = Math.abs((lastSample.angle - firstSample.angle) / timeDelta);

        // Direction?
        if (clockwise) {
            return velocity;
        } else {
            return -velocity;
        }
    }

    /**
     * Sets the starting angle of the rotation, this is only used by the touch listener, x and y is
     * the touch position.
     *
     * @param x
     * @param y
     */
    public void setGestureStartAngle(float x, float y) {
        mStartAngle = mChart.getAngleForPoint(x, y) - mChart.getRawRotationAngle();
    }

    float getGestureStartAngle() {
        return mStartAngle;
    }

    /**
     * Updates the view rotation depending on the given touch position, also takes the starting
     * angle into consideration.
     *
     * @param x
     * @param y
     */
    public void updateGestureRotation(float x, float y) {
        mChart.setRotationAngle(mChart.getAngleForPoint(x, y) - mStartAngle);
    }

    /**
     * Sets the deceleration-angular-velocity to 0.
     */
    public void stopDeceleration() {
        mDecelerationAngularVelocity = 0f;
    }

    public void computeScroll() {
        if (mDecelerationAngularVelocity == 0f) {
            return; // There's no deceleration in progress
        }

        final long currentTime = AnimationUtils.currentAnimationTimeMillis();

        mDecelerationAngularVelocity *= mChart.getDragDecelerationFrictionCoef();

        final float timeInterval = (float) (currentTime - mDecelerationLastTime) / 1000f;

        mChart.setRotationAngle(mChart.getRotationAngle() + mDecelerationAngularVelocity * timeInterval);

        mDecelerationLastTime = currentTime;

        if (Math.abs(mDecelerationAngularVelocity) >= 0.001f) {
            // This causes computeScroll to fire, recommended for this by Google
            Utils.postInvalidateOnAnimation(mChart);
        } else {
            stopDeceleration();
        }
    }

    private class AngularVelocitySample {
        public long time;
        public float angle;

        AngularVelocitySample(long time, float angle) {
            this.time = time;
            this.angle = angle;
        }
    }
}
