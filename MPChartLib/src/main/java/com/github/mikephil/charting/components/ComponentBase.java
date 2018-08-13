package com.github.mikephil.charting.components;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.utils.Utils;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

/**
 * Base class for every chart components (Axis, Legend, LimitLine, ...).
 *
 * @author Philipp Jahoda
 */
public abstract class ComponentBase {
    /**
     * Flag that indicates if this component is enabled or not.
     */
    protected boolean mEnabled = true;

    /**
     * The offset in pixels this component has on the x-axis.
     */
    protected float mXOffset = 5f;

    /**
     * The offset in pixels this component has on the Y-axis.
     */
    protected float mYOffset = 5f;

    /**
     * The typeface used for the labels
     */
    @Nullable
    protected Typeface mTypeface;

    /**
     * The text size of the labels.
     */
    protected float mTextSize = Utils.convertDpToPixel(10f);

    /**
     * The text color to use for the labels.
     */
    @ColorInt
    protected int mTextColor = Color.BLACK;

    public ComponentBase() {
    }

    /**
     * Returns the used offset on the x-axis for drawing the axis or legend labels. This offset is
     * applied before and after the label.
     */
    public float getXOffset() {
        return mXOffset;
    }

    /**
     * Sets the used x-axis offset for the labels on this axis.
     *
     * @param xOffset
     */
    public void setXOffset(float xOffset) {
        mXOffset = Utils.convertDpToPixel(xOffset);
    }

    /**
     * Returns the used offset on the x-axis for drawing the axis labels. This offset is applied
     * before and after the label.
     *
     * @return
     */
    public float getYOffset() {
        return mYOffset;
    }

    /**
     * Sets the used y-axis offset for the labels on this axis. For the legend, higher offset means
     * the legend as a whole will be placed further away from the top.
     *
     * @param yOffset
     */
    public void setYOffset(float yOffset) {
        mYOffset = Utils.convertDpToPixel(yOffset);
    }

    /**
     * Returns the Typeface used for the labels, returns null if none is set.
     */
    @Nullable
    public Typeface getTypeface() {
        return mTypeface;
    }

    /**
     * Sets a specific Typeface for the labels.
     *
     * @param typeface
     */
    public void setTypeface(@Nullable Typeface typeface) {
        mTypeface = typeface;
    }

    /**
     * Sets the size of the label text in density pixels, between 6f and 24f included.
     *
     * @param size the text size, in DP
     */
    public void setTextSize(@FloatRange(from = 6f, to = 24f) float size) {
        float adjustedSize = Math.max(6f, Math.min(size, 24f));

        mTextSize = Utils.convertDpToPixel(adjustedSize);
    }

    /**
     * Returns the text size that is currently set for the labels, in pixels.
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Sets the text color to use for the labels.
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
    }

    /**
     * Returns the text color that is set for the labels.
     */
    @ColorInt
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Set this to true if this component should be enabled (should be drawn), false if not. If
     * disabled, nothing of this component will be drawn.
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    /**
     * Returns true if this component is enabled (should be drawn), false if not.
     */
    public boolean isEnabled() {
        return mEnabled;
    }
}
