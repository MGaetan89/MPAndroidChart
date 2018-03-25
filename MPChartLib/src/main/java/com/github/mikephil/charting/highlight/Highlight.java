package com.github.mikephil.charting.highlight;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.YAxis;

/**
 * Contains information needed to determine the highlighted value.
 *
 * @author Philipp Jahoda
 */
public class Highlight {
    /**
     * The x-value of the highlighted value.
     */
    private final float mX;

    /**
     * The y-value of the highlighted value.
     */
    private final float mY;

    /**
     * The x-pixel of the highlight.
     */
    private final float mXPx;

    /**
     * The y-pixel of the highlight.
     */
    private final float mYPx;

    /**
     * The index of the data object - in case it refers to more than one.
     */
    private int mDataIndex = -1;

    /**
     * The index of the data set the highlighted value is in.
     */
    private final int mDataSetIndex;

    /**
     * The index of the highlighted value in a stacked bar entry, default -1.
     */
    private final int mStackIndex;

    /**
     * The axis the highlighted value belongs to.
     */
    @Nullable
    private final YAxis.AxisDependency axis;

    /**
     * The x-position (pixels) on which this highlight object was last drawn.
     */
    private float mDrawX;

    /**
     * The y-position (pixels) on which this highlight object was last drawn.
     */
    private float mDrawY;

    public Highlight(float x, float y, int dataSetIndex) {
        this(x, y, 0f, 0f, dataSetIndex, -1, null);
    }

    public Highlight(float x, int dataSetIndex, int stackIndex) {
        this(x, Float.NaN, 0f, 0f, dataSetIndex, stackIndex, null);
    }

    /**
     * Constructor.
     *
     * @param x            the x-value of the highlighted value
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     */
    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, YAxis.AxisDependency axis) {
        this(x, y, xPx, yPx, dataSetIndex, -1, axis);
    }

    /**
     * Constructor, only used for stacked-barchart.
     *
     * @param x            the index of the highlighted value on the x-axis
     * @param y            the y-value of the highlighted value
     * @param dataSetIndex the index of the DataSet the highlighted value belongs to
     * @param stackIndex   references which value of a stacked-bar entry has been selected
     */
    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, int stackIndex, @Nullable YAxis.AxisDependency axis) {
        this.mX = x;
        this.mY = y;
        this.mXPx = xPx;
        this.mYPx = yPx;
        this.mDataSetIndex = dataSetIndex;
        this.mStackIndex = stackIndex;
        this.axis = axis;
    }

    /**
     * Returns the x-value of the highlighted value.
     */
    public float getX() {
        return mX;
    }

    /**
     * Returns the y-value of the highlighted value.
     */
    public float getY() {
        return mY;
    }

    /**
     * Returns the x-position of the highlight in pixels.
     */
    public float getXPx() {
        return mXPx;
    }

    /**
     * Returns the y-position of the highlight in pixels.
     */
    public float getYPx() {
        return mYPx;
    }

    /**
     * Returns the index of the data object - in case it refers to more than one.
     */
    public int getDataIndex() {
        return mDataIndex;
    }

    public void setDataIndex(int mDataIndex) {
        this.mDataIndex = mDataIndex;
    }

    /**
     * Returns the index of the DataSet the highlighted value is in.
     */
    public int getDataSetIndex() {
        return mDataSetIndex;
    }

    /**
     * Only needed if a stacked-barchart entry was highlighted. References the selected value within
     * the stacked-entry.
     *
     * @return
     */
    public int getStackIndex() {
        return mStackIndex;
    }

    public boolean isStacked() {
        return mStackIndex >= 0;
    }

    /**
     * Returns the axis the highlighted value belongs to.
     */
    @Nullable
    public YAxis.AxisDependency getAxis() {
        return axis;
    }

    /**
     * Sets the x- and y-position (pixels) where this highlight was last drawn.
     *
     * @param x
     * @param y
     */
    public void setDraw(float x, float y) {
        this.mDrawX = x;
        this.mDrawY = y;
    }

    /**
     * Returns the x-position in pixels where this highlight object was last drawn.
     */
    public float getDrawX() {
        return mDrawX;
    }

    /**
     * Returns the y-position in pixels where this highlight object was last drawn.
     */
    public float getDrawY() {
        return mDrawY;
    }

    /**
     * Returns true if this highlight object is equal to the other.
     *
     * @param h
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    public boolean equalTo(@Nullable Highlight h) {
        return this.equals(h);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Highlight)) {
            return false;
        }

        Highlight other = (Highlight) obj;
        return this.mDataSetIndex == other.mDataSetIndex && this.mX == other.mX
                && this.mStackIndex == other.mStackIndex && this.mDataIndex == other.mDataIndex;
    }

    @Override
    public String toString() {
        return "Highlight, x: " + mX + ", y: " + mY + ", dataSetIndex: " + mDataSetIndex
                + ", stackIndex (only stacked barentry): " + mStackIndex;
    }
}
