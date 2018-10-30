package com.github.mikephil.charting.animation;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

/**
 * Object responsible for all animations in the Chart.
 *
 * @author Philipp Jahoda
 * @author Mick Ashton
 */
public class ChartAnimator {
    /**
     * Object that is updated upon animation update.
     */
    @Nullable
    private final AnimatorUpdateListener mListener;

    /**
     * The phase that is animated and influences the drawn values on the y-axis.
     */
    @FloatRange(from = 0f, to = 1f)
    protected float mPhaseY = 1f;

    /**
     * The phase that is animated and influences the drawn values on the x-axis.
     */
    @FloatRange(from = 0f, to = 1f)
    protected float mPhaseX = 1f;

    public ChartAnimator() {
        this(null);
    }

    public ChartAnimator(@Nullable AnimatorUpdateListener listener) {
        mListener = listener;
    }

    /**
     * Animates values along the X axis, in a linear fashion.
     *
     * @param durationMillis animation duration
     */
    public void animateX(int durationMillis) {
        animateX(durationMillis, null);
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis animation duration
     * @param easing         EasingFunction
     */
    public void animateX(int durationMillis, @Nullable TimeInterpolator easing) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillis);
        if (easing != null) {
            animatorX.setInterpolator(easing);
        }
        if (mListener != null) {
            animatorX.addUpdateListener(mListener);
        }
        animatorX.start();
    }

    /**
     * Animates values along both the X and Y axes, in a linear fashion.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     */
    public void animateXY(int durationMillisX, int durationMillisY) {
        animateXY(durationMillisX, durationMillisY, null, null);
    }

    /**
     * Animates values along both the X and Y axes.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     * @param easing          EasingFunction for both axes
     */
    public void animateXY(int durationMillisX, int durationMillisY, TimeInterpolator easing) {
        animateXY(durationMillisX, durationMillisY, easing, easing);
    }

    /**
     * Animates values along both the X and Y axes.
     *
     * @param durationMillisX animation duration along the X axis
     * @param durationMillisY animation duration along the Y axis
     * @param easingX         EasingFunction for the X axis
     * @param easingY         EasingFunction for the Y axis
     */
    public void animateXY(int durationMillisX, int durationMillisY, @Nullable TimeInterpolator easingX, @Nullable TimeInterpolator easingY) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillisX);
        if (easingX != null) {
            animatorX.setInterpolator(easingX);
        }

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillisY);
        if (easingY != null) {
            animatorY.setInterpolator(easingY);
        }

        // Make sure only one animator produces update-callbacks (which then call invalidate())
        if (mListener != null) {
            if (durationMillisX > durationMillisY) {
                animatorX.addUpdateListener(mListener);
            } else {
                animatorY.addUpdateListener(mListener);
            }
        }

        animatorX.start();
        animatorY.start();
    }

    /**
     * Animates values along the Y axis, in a linear fashion.
     *
     * @param durationMillis animation duration
     */
    public void animateY(int durationMillis) {
        animateY(durationMillis, null);
    }

    /**
     * Animates values along the Y axis.
     *
     * @param durationMillis animation duration along the Y axis
     * @param easing         EasingFunction
     */
    public void animateY(int durationMillis, @Nullable TimeInterpolator easing) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillis);
        if (easing != null) {
            animatorY.setInterpolator(easing);
        }
        if (mListener != null) {
            animatorY.addUpdateListener(mListener);
        }
        animatorY.start();
    }

    /**
     * Gets the Y axis phase of the animation.
     *
     * @return float value of {@link #mPhaseY}
     */
    @FloatRange(from = 0f, to = 1f)
    public float getPhaseY() {
        return mPhaseY;
    }

    /**
     * Sets the Y axis phase of the animation.
     *
     * @param phase float value between 0 - 1
     */
    public void setPhaseY(@FloatRange(from = 0f, to = 1f) float phase) {
        mPhaseY = Math.max(Math.min(phase, 1f), 0f);
    }

    /**
     * Gets the X axis phase of the animation.
     *
     * @return float value of {@link #mPhaseX}
     */
    @FloatRange(from = 0f, to = 1f)
    public float getPhaseX() {
        return mPhaseX;
    }

    /**
     * Sets the X axis phase of the animation.
     *
     * @param phase float value between 0 - 1
     */
    public void setPhaseX(@FloatRange(from = 0f, to = 1f) float phase) {
        mPhaseX = Math.max(Math.min(phase, 1f), 0f);
    }
}
