package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.github.mikephil.charting.utils.Utils;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The limit line is an additional feature for all Line-, Bar- and ScatterCharts. It allows the
 * displaying of an additional line in the chart that marks a certain maximum / limit on the
 * specified axis (x- or y-axis).
 *
 * @author Philipp Jahoda
 */
public class LimitLine extends ComponentBase {
    /**
     * Limit / maximum (the y-value or xIndex).
     */
    private final float mLimit;

    /**
     * The width of the limit line.
     */
    private float mLineWidth = 2f;

    /**
     * The color of the limit line.
     */
    @ColorInt
    private int mLineColor = 0xED5B5B;

    /**
     * The style of the label text.
     */
    private Paint.Style mTextStyle = Paint.Style.FILL_AND_STROKE;

    /**
     * The label string that is drawn next to the limit line.
     */
    @NonNull
    private String mLabel = "";

    /**
     * The path effect of this LimitLine that makes dashed lines possible.
     */
    @Nullable
    private DashPathEffect mDashPathEffect;

    /**
     * Indicates the position of the LimitLine label.
     */
    private LimitLabelPosition mLabelPosition = LimitLabelPosition.RIGHT_TOP;

    /**
     * LimitLine possible position.
     */
    public enum LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * Constructor with limit.
     *
     * @param limit - the position (the value) on the y-axis (y-value)
     *              or x-axis (xIndex) where this line should appear
     */
    public LimitLine(float limit) {
        this(limit, null);
    }

    /**
     * Constructor with limit and label.
     *
     * @param limit - the position (the value) on the y-axis (y-value)
     *              or x-axis (xIndex) where this line should appear
     * @param label - the label to display next to the limit line
     */
    public LimitLine(float limit, @Nullable String label) {
        mLimit = limit;

        setLabel(label);
    }

    /**
     * Returns the limit that is set for this line.
     */
    public float getLimit() {
        return mLimit;
    }

    /**
     * Set the line width of the chart, between 0.2f and 12f included.
     * NOTE: thinner line == better performance, thicker line == worse performance
     *
     * @param width
     */
    public void setLineWidth(@FloatRange(from = 0.2f, to = 12f) float width) {
        float adjustedWidth = Math.max(0.2f, Math.min(width, 12f));

        mLineWidth = Utils.convertDpToPixel(adjustedWidth);
    }

    /**
     * Returns the width of limit line.
     */
    public float getLineWidth() {
        return mLineWidth;
    }

    /**
     * Sets the line color for this LimitLine.
     *
     * @param color
     */
    public void setLineColor(@ColorInt int color) {
        mLineColor = color;
    }

    /**
     * Returns the color that is used for this LimitLine.
     */
    @ColorInt
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this "- - - - - -".
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    /**
     * Returns true if the dashed-line effect is enabled, false if not.
     */
    public boolean isDashedLineEnabled() {
        return mDashPathEffect != null;
    }

    /**
     * Returns the DashPathEffect that is set for this LimitLine.
     */
    @Nullable
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    /**
     * Sets the text style of the label that is drawn next to the LimitLine.
     *
     * @param style
     */
    public void setTextStyle(Paint.Style style) {
        this.mTextStyle = style;
    }

    /**
     * Returns the text style of the label that is drawn next to the LimitLine.
     */
    public Paint.Style getTextStyle() {
        return mTextStyle;
    }

    /**
     * Sets the position of the LimitLine value label (either on the right or on the left edge of
     * the chart). Not supported for RadarChart.
     *
     * @param pos
     */
    public void setLabelPosition(LimitLabelPosition pos) {
        mLabelPosition = pos;
    }

    /**
     * Returns the position of the LimitLine label.
     */
    public LimitLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    /**
     * Sets the label that is drawn next to the limit line.
     *
     * @param label
     */
    public void setLabel(@Nullable String label) {
        if (label == null) {
            mLabel = "";
        } else {
            mLabel = label;
        }
    }

    /**
     * Returns the label that is drawn next to the limit line.
     */
    @NonNull
    public String getLabel() {
        return mLabel;
    }
}
