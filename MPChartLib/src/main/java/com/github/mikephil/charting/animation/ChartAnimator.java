package com.github.mikephil.charting.animation;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

/**
 * Object responsible for all animations in the Chart.
 *
 * @author Philipp Jahoda
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
     * Animates the drawing/rendering of the chart on both x- and y-axis with the specified
     * animation time. If animate(...) is called, no further calling of invalidate() is necessary to
     * refresh the chart.
     *
     * @param durationMillisX
     * @param durationMillisY
     * @param easingX
     * @param easingY
     */
    public void animateXY(int durationMillisX, int durationMillisY, @Nullable TimeInterpolator easingX, @Nullable TimeInterpolator easingY) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(durationMillisY);
        if (easingY != null) {
            animatorY.setInterpolator(easingY);
        }

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(durationMillisX);
        if (easingX != null) {
            animatorX.setInterpolator(easingX);
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
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
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
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
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
     * Animates the drawing/rendering of the chart on both x- and y-axis with the specified
     * animation time. If animate(...) is called, no further calling of invalidate() is necessary to
     * refresh the chart.
     *
     * @param durationMillisX
     * @param durationMillisY
     * @param easingX
     * @param easingY
     */
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingOption easingX, Easing.EasingOption easingY) {
        animateXY(durationMillisX, durationMillisY, Easing.getEasingFunctionFromOption(easingX), Easing.getEasingFunctionFromOption(easingY));
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
     */
    public void animateX(int durationMillis, Easing.EasingOption easing) {
        animateX(durationMillis, Easing.getEasingFunctionFromOption(easing));
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
     */
    public void animateY(int durationMillis, Easing.EasingOption easing) {
        animateY(durationMillis, Easing.getEasingFunctionFromOption(easing));
    }

    /**
     * Animates the drawing/rendering of the chart on both x- and y-axis with the specified
     * animation time. If animate(...) is called, no further calling of invalidate() is necessary to
     * refresh the chart.
     *
     * @param durationMillisX
     * @param durationMillisY
     */
    public void animateXY(int durationMillisX, int durationMillisY) {
        animateXY(durationMillisX, durationMillisY, (TimeInterpolator) null, null);
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {
        animateX(durationMillis, (TimeInterpolator) null);
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {
        animateY(durationMillis, (TimeInterpolator) null);
    }

    /**
     * Returns the y-phase that is used to animate the values.
     */
    @FloatRange(from = 0f, to = 1f)
    public float getPhaseY() {
        return mPhaseY;
    }

    /**
     * This modifies the y-phase that is used to animate the values.
     *
     * @param phase
     */
    public void setPhaseY(@FloatRange(from = 0f, to = 1f) float phase) {
        mPhaseY = Math.max(Math.min(phase, 1f), 0f);
    }

    /**
     * Returns the x-phase that is used to animate the values.
     */
    @FloatRange(from = 0f, to = 1f)
    public float getPhaseX() {
        return mPhaseX;
    }

    /**
     * This modifies the x-phase that is used to animate the values.
     *
     * @param phase
     */
    public void setPhaseX(@FloatRange(from = 0f, to = 1f) float phase) {
        mPhaseX = Math.max(Math.min(phase, 1f), 0f);
    }
}
