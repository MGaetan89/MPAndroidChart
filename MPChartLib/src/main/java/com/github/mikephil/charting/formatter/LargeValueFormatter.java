package com.github.mikephil.charting.formatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;

/**
 * Predefined value-formatter that formats large numbers in a pretty way.
 * Outputs: 856 = 856; 1000 = 1k; 5821 = 5.8k; 10500 = 10k; 101800 = 102k;
 * 2000000 = 2m; 7800000 = 7.8m; 92150000 = 92m; 123200000 = 123m; 9999999 =
 * 10m; 1000000000 = 1b; Special thanks to Roman Gromov
 * (https://github.com/romangromov) for this piece of code.
 *
 * @author Philipp Jahoda
 * @author Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 */
public class LargeValueFormatter extends ValueFormatter {
    @NonNull
    private String[] mSuffix = new String[]{
            "", "k", "m", "b", "t"
    };
    private int mMaxLength = 5;
    private DecimalFormat mFormat;
    private String mText = "";

    public LargeValueFormatter() {
        this(null);
    }

    /**
     * Creates a formatter that appends a specified text to the result String.
     *
     * @param appendix a text that will be appended
     */
    public LargeValueFormatter(@Nullable String appendix) {
        mFormat = new DecimalFormat("###E00");

        this.setAppendix(appendix);
        this.setSuffix(null);
    }

    @Override
    public String getFormattedValue(float value) {
        return makePretty(value) + mText;
    }

    /**
     * Set an appendix text to be added at the end of the formatted value.
     *
     * @param appendix
     */
    public void setAppendix(@Nullable String appendix) {
        if (appendix == null) {
            this.mText = "";
        } else {
            this.mText = appendix;
        }
    }

    /**
     * Set custom suffix to be appended after the values.
     * Default suffix: ["", "k", "m", "b", "t"]
     *
     * @param suffix new suffix
     */
    public void setSuffix(@Nullable String[] suffix) {
        if (suffix == null) {
            mSuffix = new String[]{"", "k", "m", "b", "t"};
        } else {
            mSuffix = suffix;
        }
    }

    /**
     * Formats each number properly. Special thanks to Roman Gromov
     * (https://github.com/romangromov) for this piece of code.
     */
    @NonNull
    private String makePretty(double number) {
        String formattedNumber = mFormat.format(number);

        int numericValue1 = Character.getNumericValue(formattedNumber.charAt(formattedNumber.length() - 1));
        int numericValue2 = Character.getNumericValue(formattedNumber.charAt(formattedNumber.length() - 2));
        int combined = Integer.parseInt(numericValue2 + "" + numericValue1);

        formattedNumber = formattedNumber.replaceAll("E[0-9][0-9]", mSuffix[combined / 3]);

        while (formattedNumber.length() > mMaxLength || formattedNumber.matches("[0-9]+\\.[a-z]")) {
            formattedNumber = formattedNumber.substring(0, formattedNumber.length() - 2) + formattedNumber.substring(formattedNumber.length() - 1);
        }

        return formattedNumber;
    }
}
