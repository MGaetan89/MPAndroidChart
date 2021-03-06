package com.github.mikephil.charting.formatter;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * @author Philipp Jahoda
 */
public class DefaultAxisValueFormatter extends ValueFormatter {
    /**
     * DecimalFormat for formatting.
     */
    protected DecimalFormat mFormat;

    /**
     * The number of decimal digits this formatter uses.
     */
    protected int digits;

    /**
     * Constructor that specifies with how many digits the value should be formatted.
     *
     * @param digits
     */
    public DefaultAxisValueFormatter(int digits) {
        this.digits = digits;

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0) {
                buffer.append('.');
            }

            buffer.append('0');
        }

        mFormat = new DecimalFormat("###,###,###,##0" + buffer.toString());
    }

    @NonNull
    @Override
    public String getFormattedValue(float value) {
        // avoid memory allocations here (for performance)
        return mFormat.format(value);
    }

    /**
     * Returns the number of decimal digits this formatter uses or -1, if unspecified.
     */
    public int getDecimalDigits() {
        return digits;
    }
}
