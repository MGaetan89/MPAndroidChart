package com.github.mikephil.charting.components;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import com.github.mikephil.charting.utils.Utils;

/**
 * Class representing the y-axis labels settings and its entries. Only use the setter methods to
 * modify it. Do not access public variables directly. Be aware that not all features the YLabels
 * class provides are suitable for the RadarChart. Customizations that affect the value range of the
 * axis need to be applied before setting data for the chart.
 *
 * @author Philipp Jahoda
 */
public class YAxis extends AxisBase {
    /**
     * Indicates if the bottom y-label entry is drawn or not.
     */
    private boolean mDrawBottomYLabelEntry = true;

    /**
     * Indicates if the top y-label entry is drawn or not.
     */
    private boolean mDrawTopYLabelEntry = true;

    /**
     * Flag that indicates if the axis is inverted or not.
     */
    protected boolean mInverted = false;

    /**
     * Flag that indicates if the zero-line should be drawn regardless of other grid lines.
     */
    protected boolean mDrawZeroLine = false;

    /**
     * Flag indicating that auto scale min restriction should be used.
     */
    private boolean mUseAutoScaleRestrictionMin = false;

    /**
     * Flag indicating that auto scale max restriction should be used.
     */
    private boolean mUseAutoScaleRestrictionMax = false;

    /**
     * Color of the zero line.
     */
    @ColorInt
    protected int mZeroLineColor = Color.GRAY;

    /**
     * Width of the zero line in pixels.
     */
    protected float mZeroLineWidth = 1f;

    /**
     * Axis space from the largest value to the top in percent of the total axis range.
     */
    protected float mSpacePercentTop = 10f;

    /**
     * Axis space from the smallest value to the bottom in percent of the total axis range.
     */
    protected float mSpacePercentBottom = 10f;

    /**
     * The position of the y-labels relative to the chart.
     */
    private YAxisLabelPosition mPosition = YAxisLabelPosition.OUTSIDE_CHART;

    /**
     * The side this axis object represents.
     */
    private AxisDependency mAxisDependency;

    /**
     * The minimum width that the axis should take (in dp).
     */
    protected float mMinWidth = 0f;

    /**
     * The maximum width that the axis can take (in dp).
     * Use {@code Float.POSITIVE_INFINITY} for disabling the maximum.
     */
    protected float mMaxWidth = Float.POSITIVE_INFINITY;

    /**
     * Enum for the position of the y-labels relative to the chart.
     */
    public enum YAxisLabelPosition {
        OUTSIDE_CHART, INSIDE_CHART
    }

    /**
     * Enum that specifies the axis a DataSet should be plotted against, either LEFT or RIGHT.
     *
     * @author Philipp Jahoda
     */
    public enum AxisDependency {
        LEFT, RIGHT
    }

    public YAxis() {
        this(AxisDependency.LEFT);
    }

    public YAxis(AxisDependency position) {
        super();

        this.mAxisDependency = position;
        this.mYOffset = 0f;
    }

    public AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    /**
     * Returns the minimum width that the axis should take (in dp).
     */
    public float getMinWidth() {
        return mMinWidth;
    }

    /**
     * Sets the minimum width that the axis should take (in dp).
     *
     * @param minWidth
     */
    public void setMinWidth(float minWidth) {
        mMinWidth = minWidth;
    }

    /**
     * Returns the maximum width that the axis can take (in dp).
     */
    public float getMaxWidth() {
        return mMaxWidth;
    }

    /**
     * Sets the maximum width that the axis can take (in dp).
     *
     * @param maxWidth
     */
    public void setMaxWidth(float maxWidth) {
        mMaxWidth = maxWidth;
    }

    /**
     * Returns the position of the y-labels.
     */
    public YAxisLabelPosition getLabelPosition() {
        return mPosition;
    }

    /**
     * Sets the position of the y-labels.
     *
     * @param pos
     */
    public void setPosition(YAxisLabelPosition pos) {
        mPosition = pos;
    }

    /**
     * Returns true if drawing the top y-axis label entry is enabled.
     */
    public boolean isDrawTopYLabelEntryEnabled() {
        return mDrawTopYLabelEntry;
    }

    /**
     * Returns true if drawing the bottom y-axis label entry is enabled.
     */
    public boolean isDrawBottomYLabelEntryEnabled() {
        return mDrawBottomYLabelEntry;
    }

    /**
     * Set this to true to enable drawing the top y-label entry. Disabling this can be helpful when
     * the top y-label and left x-label interfere with each other.
     *
     * @param enabled
     */
    public void setDrawTopYLabelEntry(boolean enabled) {
        mDrawTopYLabelEntry = enabled;
    }

    /**
     * If this is set to true, the y-axis is inverted which means that low values are on top of the
     * chart, high values on bottom.
     *
     * @param enabled
     */
    public void setInverted(boolean enabled) {
        mInverted = enabled;
    }

    /**
     * Returns true, if the y-axis is inverted.
     */
    public boolean isInverted() {
        return mInverted;
    }

    /**
     * Sets the top axis space in percent of the full range.
     *
     * @param percent
     */
    public void setSpaceTop(float percent) {
        mSpacePercentTop = percent;
    }

    /**
     * Returns the top axis space in percent of the full range.
     */
    public float getSpaceTop() {
        return mSpacePercentTop;
    }

    /**
     * Sets the bottom axis space in percent of the full range.
     *
     * @param percent
     */
    public void setSpaceBottom(float percent) {
        mSpacePercentBottom = percent;
    }

    /**
     * Returns the bottom axis space in percent of the full range.
     */
    public float getSpaceBottom() {
        return mSpacePercentBottom;
    }

    public boolean isDrawZeroLineEnabled() {
        return mDrawZeroLine;
    }

    /**
     * Set this to true to draw the zero-line regardless of weather other grid-lines are enabled or
     * not.
     *
     * @param mDrawZeroLine
     */
    public void setDrawZeroLine(boolean mDrawZeroLine) {
        this.mDrawZeroLine = mDrawZeroLine;
    }

    @ColorInt
    public int getZeroLineColor() {
        return mZeroLineColor;
    }

    /**
     * Sets the color of the zero line.
     *
     * @param color
     */
    public void setZeroLineColor(@ColorInt int color) {
        mZeroLineColor = color;
    }

    public float getZeroLineWidth() {
        return mZeroLineWidth;
    }

    /**
     * Sets the width of the zero line in dp.
     *
     * @param width
     */
    public void setZeroLineWidth(float width) {
        this.mZeroLineWidth = Utils.convertDpToPixel(width);
    }

    /**
     * This is for normal (not horizontal) charts horizontal spacing.
     *
     * @param paint
     * @return
     */
    public float getRequiredWidthSpace(Paint paint) {
        paint.setTextSize(mTextSize);

        String label = getLongestLabel();
        float width = Utils.calcTextWidth(paint, label) + getXOffset() * 2f;

        float minWidth = getMinWidth();
        float maxWidth = getMaxWidth();

        if (minWidth > 0f) {
            minWidth = Utils.convertDpToPixel(minWidth);
        }

        if (maxWidth > 0f && maxWidth != Float.POSITIVE_INFINITY) {
            maxWidth = Utils.convertDpToPixel(maxWidth);
        }

        return Math.max(minWidth, Math.min(width, maxWidth > 0f ? maxWidth : width));
    }

    /**
     * This is for HorizontalBarChart vertical spacing.
     *
     * @param paint
     * @return
     */
    public float getRequiredHeightSpace(Paint paint) {
        paint.setTextSize(mTextSize);

        String label = getLongestLabel();
        return Utils.calcTextHeight(paint, label) + getYOffset() * 2f;
    }

    /**
     * Returns true if this axis needs horizontal offset, false if no offset is needed.
     */
    public boolean needsOffset() {
        return isEnabled() && isDrawLabelsEnabled() && getLabelPosition() == YAxisLabelPosition.OUTSIDE_CHART;
    }

    /**
     * Returns true if autoscale restriction for axis min value is enabled.
     */
    public boolean isUseAutoScaleMinRestriction() {
        return mUseAutoScaleRestrictionMin;
    }

    /**
     * Sets autoscale restriction for axis min value as enabled/disabled.
     */
    public void setUseAutoScaleMinRestriction(boolean isEnabled) {
        mUseAutoScaleRestrictionMin = isEnabled;
    }

    /**
     * Returns true if autoscale restriction for axis max value is enabled.
     */
    public boolean isUseAutoScaleMaxRestriction() {
        return mUseAutoScaleRestrictionMax;
    }

    /**
     * Sets autoscale restriction for axis max value as enabled/disabled.
     */
    public void setUseAutoScaleMaxRestriction(boolean isEnabled) {
        mUseAutoScaleRestrictionMax = isEnabled;
    }

    @Override
    public void calculate(float dataMin, float dataMax) {
        float min = dataMin;
        float max = dataMax;

        // If custom, use value as is, else use data value
        if (mCustomAxisMin) {
            if (mUseAutoScaleRestrictionMin) {
                min = Math.min(dataMin, mAxisMinimum);
            } else {
                min = mAxisMinimum;
            }
        }

        if (mCustomAxisMax) {
            if (mUseAutoScaleRestrictionMax) {
                max = Math.max(max, mAxisMaximum);
            } else {
                max = mAxisMaximum;
            }
        }

        // Temporary range (before calculations)
        float range = Math.abs(max - min);

        // In case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        float bottomSpace = range / 100f * getSpaceBottom();
        this.mAxisMinimum = min - bottomSpace;

        float topSpace = range / 100f * getSpaceTop();
        this.mAxisMaximum = max + topSpace;

        // Calc actual range
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }
}
