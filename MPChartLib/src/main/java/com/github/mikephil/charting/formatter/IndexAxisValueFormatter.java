package com.github.mikephil.charting.formatter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.AxisBase;

import java.util.Collection;

/**
 * This formatter is used for passing an array of x-axis labels, on whole x steps.
 */
public class IndexAxisValueFormatter implements IAxisValueFormatter {
    @NonNull
    private String[] mValues = new String[0];
    private int mValueCount = 0;

    /**
     * An empty constructor.
     * Use `setValues` to set the axis labels.
     */
    public IndexAxisValueFormatter() {
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public IndexAxisValueFormatter(@Nullable String[] values) {
        if (values != null && values.length > 0) {
            setValues(values);
        }
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public IndexAxisValueFormatter(@Nullable Collection<String> values) {
        if (values != null && !values.isEmpty()) {
            setValues(values.toArray(new String[values.size()]));
        }
    }

    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);
        if (index < 0 || index >= mValueCount || index != (int) value) {
            return "";
        }

        return mValues[index];
    }

    @NonNull
    public String[] getValues() {
        return mValues;
    }

    public void setValues(@Nullable String[] values) {
        if (values == null) {
            values = new String[0];
        }

        this.mValues = values;
        this.mValueCount = values.length;
    }
}
