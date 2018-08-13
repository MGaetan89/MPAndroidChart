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

    private ObjectAnimator xAnimator(int duration, @Nullable TimeInterpolator easing) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f);
        animatorX.setDuration(duration);
        if (easing != null) {
            animatorX.setInterpolator(easing);
        }
        return animatorX;
    }

    private ObjectAnimator yAnimator(int duration, @Nullable TimeInterpolator easing) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "phaseY", 0f, 1f);
        animatorY.setDuration(duration);
        if (easing != null) {
            animatorY.setInterpolator(easing);
        }
        return animatorY;
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
        ObjectAnimator xAnimator = this.xAnimator(durationMillisX, easingX);
        ObjectAnimator yAnimator = this.yAnimator(durationMillisY, easingY);

        // Make sure only one animator produces update-callbacks (which then call invalidate())
        if (mListener != null) {
            if (durationMillisX > durationMillisY) {
                xAnimator.addUpdateListener(mListener);
            } else {
                yAnimator.addUpdateListener(mListener);
            }
        }

        xAnimator.start();
        yAnimator.start();
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
     */
    public void animateX(int durationMillis, @Nullable TimeInterpolator easing) {
        ObjectAnimator xAnimator = this.xAnimator(durationMillis, easing);
        if (mListener != null) {
            xAnimator.addUpdateListener(mListener);
        }
        xAnimator.start();
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing
     */
    public void animateY(int durationMillis, @Nullable TimeInterpolator easing) {
        ObjectAnimator yAnimator = this.yAnimator(durationMillis, easing);
        if (mListener != null) {
            yAnimator.addUpdateListener(mListener);
        }
        yAnimator.start();
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
        animateXY(durationMillisX, durationMillisY, null, null);
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {
        animateX(durationMillis, null);
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {
        animateY(durationMillis, null);
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
