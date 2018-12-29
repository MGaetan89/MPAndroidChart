package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * A formatter specifically for stacked BarChart that allows to specify whether the all stack values
 * or just the top value should be drawn.
 *
 * @author Philipp Jahoda
 */
public class StackedValueFormatter extends ValueFormatter {
    /**
     * if true, all stack values of the stacked bar entry are drawn, else only top.
     */
    private final boolean mDrawWholeStack;

    /**
     * A string that should be appended behind the value.
     */
    @NonNull
    private final String mSuffix;

    @NonNull
    private final DecimalFormat mFormat;

    /**
     * Constructor.
     *
     * @param drawWholeStack if true, all stack values of the stacked bar entry are drawn, else only top
     * @param suffix         a string that should be appended behind the value
     * @param decimals       the number of decimal digits to use
     */
    public StackedValueFormatter(boolean drawWholeStack, @NonNull String suffix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mSuffix = suffix;

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0) {
                buffer.append(".");
            }

            buffer.append("0");
        }

        this.mFormat = new DecimalFormat("###,###,###,##0" + buffer.toString());
    }

    @Override
    public String getBarStackedLabel(float value, BarEntry entry) {
        if (!mDrawWholeStack) {
            float[] values = entry.getYVals();

            if (values != null) {
                // find out if we are on top of the stack
                if (values[values.length - 1] == value) {
                    // return the "sum" across all stack values
                    return mFormat.format(entry.getY()) + mSuffix;
                } else {
                    return "";
                }
            }
        }

        // return the "proposed" value
        return mFormat.format(value) + mSuffix;
    }
}
