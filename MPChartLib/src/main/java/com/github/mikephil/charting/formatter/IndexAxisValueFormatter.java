package com.github.mikephil.charting.formatter;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This formatter is used for passing an array of x-axis labels, on whole x steps.
 */
public class IndexAxisValueFormatter extends ValueFormatter {
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
        setValues(values);
    }

    /**
     * Constructor that specifies axis labels.
     *
     * @param values The values string array
     */
    public IndexAxisValueFormatter(@Nullable Collection<String> values) {
        if (values != null && !values.isEmpty()) {
            setValues(values.toArray(new String[0]));
        }
    }

    @Override
    public String getFormattedValue(float value) {
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
