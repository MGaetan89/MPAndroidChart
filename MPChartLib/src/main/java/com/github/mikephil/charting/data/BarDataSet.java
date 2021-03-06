package com.github.mikephil.charting.data;

import android.graphics.Color;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BarDataSet extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet {
    /**
     * The maximum number of bars that are stacked upon each other, this value is calculated from
     * the Entries that are added to the DataSet.
     */
    private int mStackSize = 1;

    /**
     * The color used for drawing the bar shadows.
     */
    @ColorInt
    private int mBarShadowColor = 0xD7D7D7;

    private float mBarBorderWidth = 0f;

    @ColorInt
    private int mBarBorderColor = Color.BLACK;

    /**
     * The alpha value used to draw the highlight indicator bar.
     */
    private int mHighLightAlpha = 120;

    /**
     * The overall entry count, including counting each stack-value individually.
     */
    private int mEntryCountStacks = 0;

    /**
     * Array of labels used to describe the different values of the stacked bars.
     */
    @NonNull
    private String[] mStackLabels = new String[]{
            "Stack"
    };

    public BarDataSet(@NonNull List<BarEntry> yValues, @NonNull String label) {
        super(yValues, label);

        mHighLightColor = Color.BLACK;

        calcStackSize(yValues);
        calcEntryCountIncludingStacks(yValues);
    }

    @Nullable
    @Override
    public DataSet<BarEntry> copy() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        BarDataSet copied = new BarDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(BarDataSet barDataSet) {
        super.copy(barDataSet);
        barDataSet.mStackSize = mStackSize;
        barDataSet.mBarShadowColor = mBarShadowColor;
        barDataSet.mBarBorderWidth = mBarBorderWidth;
        barDataSet.mStackLabels = mStackLabels;
        barDataSet.mHighLightAlpha = mHighLightAlpha;
    }

    /**
     * Calculates the total number of entries this DataSet represents, including stacks. All values
     * belonging to a stack are calculated separately.
     */
    private void calcEntryCountIncludingStacks(@NonNull List<BarEntry> yValues) {
        mEntryCountStacks = 0;

        for (int i = 0; i < yValues.size(); i++) {
            float[] values = yValues.get(i).getYVals();

            if (values == null) {
                mEntryCountStacks++;
            } else {
                mEntryCountStacks += values.length;
            }
        }
    }

    /**
     * Calculates the maximum stack size that occurs in the Entries array of this DataSet.
     */
    private void calcStackSize(@NonNull List<BarEntry> yValues) {
        for (int i = 0; i < yValues.size(); i++) {
            float[] values = yValues.get(i).getYVals();
            if (values != null && values.length > mStackSize) {
                mStackSize = values.length;
            }
        }
    }

    @Override
    protected void calcMinMax(@NonNull BarEntry entry) {
        if (!Float.isNaN(entry.getY())) {
            if (entry.getYVals() == null) {
                if (entry.getY() < mYMin) {
                    mYMin = entry.getY();
                }

                if (entry.getY() > mYMax) {
                    mYMax = entry.getY();
                }
            } else {
                if (-entry.getNegativeSum() < mYMin) {
                    mYMin = -entry.getNegativeSum();
                }

                if (entry.getPositiveSum() > mYMax) {
                    mYMax = entry.getPositiveSum();
                }
            }

            calcMinMaxX(entry);
        }
    }

    @Override
    public int getStackSize() {
        return mStackSize;
    }

    @Override
    public boolean isStacked() {
        return mStackSize > 1;
    }

    /**
     * Returns the overall entry count, including counting each stack-value individually.
     */
    public int getEntryCountStacks() {
        return mEntryCountStacks;
    }

    /**
     * Sets the color used for drawing the bar-shadows. The bar shadows is a surface behind the bar
     * that indicates the maximum value.
     *
     * @param color
     */
    public void setBarShadowColor(@ColorInt int color) {
        mBarShadowColor = color;
    }

    @ColorInt
    @Override
    public int getBarShadowColor() {
        return mBarShadowColor;
    }

    /**
     * Sets the width used for drawing borders around the bars. If borderWidth == 0, no border will
     * be drawn.
     *
     * @return
     */
    public void setBarBorderWidth(float width) {
        mBarBorderWidth = width;
    }

    /**
     * Returns the width used for drawing borders around the bars. If borderWidth == 0, no border
     * will be drawn.
     */
    @Override
    public float getBarBorderWidth() {
        return mBarBorderWidth;
    }

    /**
     * Sets the color drawing borders around the bars.
     */
    public void setBarBorderColor(@ColorInt int color) {
        mBarBorderColor = color;
    }

    /**
     * Returns the color drawing borders around the bars.
     */
    @ColorInt
    @Override
    public int getBarBorderColor() {
        return mBarBorderColor;
    }

    /**
     * Set the alpha value (transparency) that is used for drawing the highlight indicator bar.
     * Min = 0 (fully transparent), max = 255 (fully opaque)
     *
     * @param alpha
     */
    public void setHighLightAlpha(int alpha) {
        mHighLightAlpha = alpha;
    }

    @Override
    public int getHighLightAlpha() {
        return mHighLightAlpha;
    }

    /**
     * Sets labels for different values of bar-stacks, in case there are one.
     *
     * @param labels
     */
    public void setStackLabels(@NonNull String[] labels) {
        mStackLabels = labels;
    }

    @NonNull
    @Override
    public String[] getStackLabels() {
        return mStackLabels;
    }
}
