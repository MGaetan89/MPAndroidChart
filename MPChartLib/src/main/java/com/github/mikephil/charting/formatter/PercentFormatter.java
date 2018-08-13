package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * This IValueFormatter is just for convenience and simply puts a " %" sign after each value.
 * (Recommended for PieChart)
 *
 * @author Philipp Jahoda
 */
public class PercentFormatter implements IValueFormatter, IAxisValueFormatter {
    @NonNull
    protected DecimalFormat mFormat;

    public PercentFormatter() {
        this(new DecimalFormat("###,###,##0.0"));
    }

    /**
     * Allow a custom DecimalFormat.
     *
     * @param format
     */
    public PercentFormatter(@NonNull DecimalFormat format) {
        this.mFormat = format;
    }

    @NonNull
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " %";
    }

    @NonNull
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " %";
    }

    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    public int getDecimalDigits() {
        return 1;
    }
}
