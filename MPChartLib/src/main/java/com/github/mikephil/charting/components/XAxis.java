package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;

/**
 * Class representing the x-axis labels settings. Only use the setter methods to modify it. Do not
 * access public variables directly. Be aware that not all features the XLabels class provides are
 * suitable for the RadarChart.
 *
 * @author Philipp Jahoda
 */
public class XAxis extends AxisBase {
    /**
     * Width of the x-axis labels in pixels. This is automatically calculated by the computeSize()
     * methods in the renderer.
     */
    public int mLabelWidth = 1;

    /**
     * Height of the x-axis labels in pixels. This is automatically calculated by the computeSize()
     * methods in the renderer.
     */
    public int mLabelHeight = 1;

    /**
     * Width of the (rotated) x-axis labels in pixels. This is automatically calculated by the
     * computeSize() methods in the renderer.
     */
    public int mLabelRotatedWidth = 1;

    /**
     * Height of the (rotated) x-axis labels in pixels. This is automatically calculated by the
     * computeSize() methods in the renderer.
     */
    public int mLabelRotatedHeight = 1;

    /**
     * This is the angle for drawing the X axis labels (in degrees).
     */
    protected float mLabelRotationAngle = 0f;

    /**
     * If set to true, the chart will avoid that the first and last label entry in the chart "clip"
     * off the edge of the chart.
     */
    private boolean mAvoidFirstLastClipping = false;

    /**
     * The position of the x-labels relative to the chart.
     */
    private XAxisPosition mPosition = XAxisPosition.TOP;

    /**
     * Enum for the position of the x-labels relative to the chart.
     */
    public enum XAxisPosition {
        TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE
    }

    public XAxis() {
        super();

        mYOffset = Utils.convertDpToPixel(4f);
    }

    /**
     * Returns the position of the x-labels.
     */
    public XAxisPosition getPosition() {
        return mPosition;
    }

    /**
     * Sets the position of the x-labels.
     *
     * @param pos
     */
    public void setPosition(XAxisPosition pos) {
        mPosition = pos;
    }

    /**
     * Returns the angle for drawing the X axis labels (in degrees).
     */
    public float getLabelRotationAngle() {
        return mLabelRotationAngle;
    }

    /**
     * Sets the angle for drawing the X axis labels (in degrees).
     *
     * @param angle the angle in degrees
     */
    public void setLabelRotationAngle(float angle) {
        mLabelRotationAngle = angle;
    }

    /**
     * If set to true, the chart will avoid that the first and last label entry in the chart "clip"
     * off the edge of the chart or the screen.
     *
     * @param enabled
     */
    public void setAvoidFirstLastClipping(boolean enabled) {
        mAvoidFirstLastClipping = enabled;
    }

    /**
     * Returns true if avoid-first-last clipping is enabled, false if not.
     */
    public boolean isAvoidFirstLastClippingEnabled() {
        return mAvoidFirstLastClipping;
    }
}
