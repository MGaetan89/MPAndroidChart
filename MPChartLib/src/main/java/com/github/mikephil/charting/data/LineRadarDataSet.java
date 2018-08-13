package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Base data set for line and radar DataSets.
 *
 * @author Philipp Jahoda
 */
public abstract class LineRadarDataSet<T extends Entry> extends LineScatterCandleRadarDataSet<T> implements ILineRadarDataSet<T> {
    /**
     * The color that is used for filling the line surface.
     */
    @ColorInt
    private int mFillColor = 0x8CEAFF;

    /**
     * The drawable to be used for filling the line surface.
     */
    @Nullable
    protected Drawable mFillDrawable;

    /**
     * Transparency used for filling line surface.
     */
    private int mFillAlpha = 85;

    /**
     * The width of the drawn data lines.
     */
    private float mLineWidth = 2.5f;

    /**
     * If true, the data will also be drawn filled.
     */
    private boolean mDrawFilled = false;

    public LineRadarDataSet(List<T> yValues, String label) {
        super(yValues, label);
    }

    @ColorInt
    @Override
    public int getFillColor() {
        return mFillColor;
    }

    /**
     * Sets the color that is used for filling the area below the line. Resets an eventually set
     * "fillDrawable".
     *
     * @param color
     */
    public void setFillColor(@ColorInt int color) {
        mFillColor = color;
        mFillDrawable = null;
    }

    @Nullable
    @Override
    public Drawable getFillDrawable() {
        return mFillDrawable;
    }

    /**
     * Sets the drawable to be used to fill the area below the line.
     *
     * @param drawable
     */
    public void setFillDrawable(@Nullable Drawable drawable) {
        this.mFillDrawable = drawable;
    }

    @Override
    public int getFillAlpha() {
        return mFillAlpha;
    }

    /**
     * Sets the alpha value (transparency) that is used for filling the line surface (0-255).
     *
     * @param alpha
     */
    public void setFillAlpha(int alpha) {
        mFillAlpha = alpha;
    }

    /**
     * Set the line width of the chart (min = 0.2f, max = 10f).
     * NOTE: thinner line == better performance, thicker line == worse performance.
     *
     * @param width
     */
    public void setLineWidth(float width) {
        if (width < 0f) {
            width = 0f;
        }

        if (width > 10.0f) {
            width = 10.0f;
        }

        mLineWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public float getLineWidth() {
        return mLineWidth;
    }

    @Override
    public void setDrawFilled(boolean filled) {
        mDrawFilled = filled;
    }

    @Override
    public boolean isDrawFilledEnabled() {
        return mDrawFilled;
    }

    protected void copy(LineRadarDataSet lineRadarDataSet) {
        super.copy(lineRadarDataSet);
        lineRadarDataSet.mDrawFilled = mDrawFilled;
        lineRadarDataSet.mFillAlpha = mFillAlpha;
        lineRadarDataSet.mFillColor = mFillColor;
        lineRadarDataSet.mFillDrawable = mFillDrawable;
        lineRadarDataSet.mLineWidth = mLineWidth;
    }
}
