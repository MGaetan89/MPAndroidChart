package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.highlight.Range;

/**
 * Entry class for the BarChart (especially stacked bars).
 *
 * @author Philipp Jahoda
 */
public class BarEntry extends Entry {
    /**
     * The values the stacked bar chart holds.
     */
    @Nullable
    private float[] mYValues;

    /**
     * The ranges for the individual stack values - automatically calculated.
     */
    private Range[] mRanges;

    /**
     * The sum of all negative values this entry (if stacked) contains.
     */
    private float mNegativeSum;

    /**
     * The sum of all positive values this entry (if stacked) contains.
     */
    private float mPositiveSum;

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    public BarEntry(float x, float y) {
        super(x, y);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(float x, float y, @Nullable Object data) {
        super(x, y, data);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    public BarEntry(float x, float y, @Nullable Drawable icon) {
        super(x, y, icon);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(float x, float y, @Nullable Drawable icon, @Nullable Object data) {
        super(x, y, icon, data);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack.
     *
     * @param x
     * @param values - the stack values, use at least 2
     */
    public BarEntry(float x, @Nullable float[] values) {
        this(x, values, null, null);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack.
     *
     * @param x
     * @param values - the stack values, use at least 2
     * @param data   - Spot for additional data this Entry represents.
     */
    public BarEntry(float x, @Nullable float[] values, @Nullable Object data) {
        this(x, values, null, data);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack.
     *
     * @param x
     * @param values - the stack values, use at least 2
     * @param icon   - icon image
     */
    public BarEntry(float x, @Nullable float[] values, @Nullable Drawable icon) {
        this(x, values, icon, null);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack.
     *
     * @param x
     * @param values - the stack values, use at least 2
     * @param icon   - icon image
     * @param data   - Spot for additional data this Entry represents.
     */
    public BarEntry(float x, @Nullable float[] values, @Nullable Drawable icon, @Nullable Object data) {
        super(x, calcSum(values), icon, data);

        this.mYValues = values;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    @NonNull
    public BarEntry copy() {
        BarEntry copied = new BarEntry(getX(), getY(), getIcon(), getData());
        if (mYValues != null) {
            copied.setVals(mYValues);
        }
        return copied;
    }

    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is
     * represented (then, use getY()).
     */
    @Nullable
    public float[] getYVals() {
        return mYValues;
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param values
     */
    public void setVals(float[] values) {
        setY(calcSum(values));
        mYValues = values;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not
     * stacked.
     */
    public Range[] getRanges() {
        return mRanges;
    }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     */
    public boolean isStacked() {
        return mYValues != null && mYValues.length > 0;
    }

    public float getSumBelow(int stackIndex) {
        if (mYValues == null || mYValues.length == 0) {
            return 0f;
        }

        float remainder = 0f;
        int index = mYValues.length - 1;

        while (index >= 0 && stackIndex < index) {
            remainder += mYValues[index];
            index--;
        }

        return remainder;
    }

    /**
     * Returns the sum of all positive values this entry (if stacked) contains.
     */
    public float getPositiveSum() {
        return mPositiveSum;
    }

    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive
     * number).
     */
    public float getNegativeSum() {
        return mNegativeSum;
    }

    private void calcPosNegSum() {
        float[] values = getYVals();
        if (values == null || values.length == 0) {
            mNegativeSum = 0f;
            mPositiveSum = 0f;
            return;
        }

        float sumNeg = 0f;
        float sumPos = 0f;

        for (float value : values) {
            if (value <= 0f) {
                sumNeg += Math.abs(value);
            } else {
                sumPos += value;
            }
        }

        mNegativeSum = sumNeg;
        mPositiveSum = sumPos;
    }

    /**
     * Calculates the sum across all values of the given stack.
     *
     * @param values
     * @return
     */
    private static float calcSum(float[] values) {
        if (values == null || values.length == 0) {
            return 0f;
        }

        float sum = 0f;
        for (float value : values) {
            sum += value;
        }

        return sum;
    }

    protected void calcRanges() {
        float[] values = getYVals();
        if (values == null || values.length == 0) {
            mRanges = null;
            return;
        }

        mRanges = new Range[values.length];

        float negRemain = -getNegativeSum();
        float posRemain = 0f;

        for (int i = 0; i < mRanges.length; i++) {
            float value = values[i];
            if (value < 0) {
                mRanges[i] = new Range(negRemain, negRemain - value);
                negRemain -= value;
            } else {
                mRanges[i] = new Range(posRemain, posRemain + value);
                posRemain += value;
            }
        }
    }
}
