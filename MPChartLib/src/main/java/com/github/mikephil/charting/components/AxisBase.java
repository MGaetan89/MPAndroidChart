package com.github.mikephil.charting.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;

import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base-class of all axes (previously called labels).
 *
 * @author Philipp Jahoda
 */
public abstract class AxisBase extends ComponentBase {
    /**
     * Custom formatter that is used instead of the auto-formatter if set.
     */
    @Nullable
    protected IAxisValueFormatter mAxisValueFormatter;

    @ColorInt
    private int mGridColor = Color.GRAY;

    private float mGridLineWidth = 1f;

    @ColorInt
    private int mAxisLineColor = Color.GRAY;

    private float mAxisLineWidth = 1f;

    /**
     * The actual array of entries.
     */
    public float[] mEntries = new float[0];

    /**
     * Axis label entries only used for centered labels.
     */
    public float[] mCenteredEntries = new float[0];

    /**
     * The number of entries the legend contains.
     */
    public int mEntryCount;

    /**
     * The number of decimal digits to use.
     */
    public int mDecimals;

    /**
     * The number of label entries the axis should have.
     */
    private int mLabelCount = 6;

    /**
     * The minimum interval between axis values.
     */
    protected float mGranularity = 1f;

    /**
     * When true, axis labels are controlled by the `granularity` property.
     * When false, axis values could possibly be repeated.
     * This could happen if two adjacent axis values are rounded to same value.  If using
     * granularity this could be avoided by having fewer axis values visible.
     */
    protected boolean mGranularityEnabled = false;

    /**
     * If true, the set number of y-labels will be forced.
     */
    protected boolean mForceLabels = false;

    /**
     * Flag indicating if the grid lines for this axis should be drawn.
     */
    protected boolean mDrawGridLines = true;

    /**
     * Flag that indicates if the line alongside the axis is drawn or not.
     */
    protected boolean mDrawAxisLine = true;

    /**
     * Flag that indicates of the labels of this axis should be drawn or not.
     */
    protected boolean mDrawLabels = true;

    protected boolean mCenterAxisLabels = false;

    /**
     * The path effect of the axis line that makes dashed lines possible.
     */
    @Nullable
    private DashPathEffect mAxisLineDashPathEffect;

    /**
     * The path effect of the grid lines that makes dashed lines possible.
     */
    @Nullable
    private DashPathEffect mGridDashPathEffect;

    /**
     * Array of limit lines that can be set for the axis.
     */
    @NonNull
    protected List<LimitLine> mLimitLines;

    /**
     * Flag indicating the limit lines layer depth.
     */
    protected boolean mDrawLimitLineBehindData = false;

    /**
     * Flag indicating the grid lines layer depth.
     */
    protected boolean mDrawGridLinesBehindData = true;

    /**
     * Extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`.
     */
    protected float mSpaceMin = 0f;

    /**
     * Extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`.
     */
    protected float mSpaceMax = 0f;

    /**
     * Flag indicating that the axis-min value has been customized.
     */
    protected boolean mCustomAxisMin = false;

    /**
     * Flag indicating that the axis-max value has been customized.
     */
    protected boolean mCustomAxisMax = false;

    /**
     * Don't touch this directly, use setter.
     */
    public float mAxisMaximum = 0f;

    /**
     * Don't touch this directly, use setter.
     */
    public float mAxisMinimum = 0f;

    /**
     * The total range of values this axis covers.
     */
    public float mAxisRange = 0f;

    /**
     * Default constructor.
     */
    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(5f);
        this.mLimitLines = new ArrayList<>();
    }

    /**
     * Set this to true to enable drawing the grid lines for this axis.
     *
     * @param enabled
     */
    public void setDrawGridLines(boolean enabled) {
        mDrawGridLines = enabled;
    }

    /**
     * Returns true if drawing grid lines is enabled for this axis.
     */
    public boolean isDrawGridLinesEnabled() {
        return mDrawGridLines;
    }

    /**
     * Set this to true if the line alongside the axis should be drawn or not.
     *
     * @param enabled
     */
    public void setDrawAxisLine(boolean enabled) {
        mDrawAxisLine = enabled;
    }

    /**
     * Returns true if the line alongside the axis should be drawn.
     */
    public boolean isDrawAxisLineEnabled() {
        return mDrawAxisLine;
    }

    /**
     * Centers the axis labels instead of drawing them at their original position. This is useful
     * especially for grouped BarChart.
     *
     * @param enabled
     */
    public void setCenterAxisLabels(boolean enabled) {
        mCenterAxisLabels = enabled;
    }

    public boolean isCenterAxisLabelsEnabled() {
        return mCenterAxisLabels && mEntryCount > 0;
    }

    /**
     * Sets the color of the grid lines for this axis (the horizontal lines coming from each label).
     *
     * @param color
     */
    public void setGridColor(@ColorInt int color) {
        mGridColor = color;
    }

    /**
     * Returns the color of the grid lines for this axis (the horizontal lines coming from each
     * label).
     */
    @ColorInt
    public int getGridColor() {
        return mGridColor;
    }

    /**
     * Sets the width of the border surrounding the chart in dp.
     *
     * @param width
     */
    public void setAxisLineWidth(float width) {
        mAxisLineWidth = Utils.convertDpToPixel(width);
    }

    /**
     * Returns the width of the axis line (line alongside the axis).
     */
    public float getAxisLineWidth() {
        return mAxisLineWidth;
    }

    /**
     * Sets the width of the grid lines that are drawn away from each axis label.
     */
    public void setGridLineWidth(float width) {
        mGridLineWidth = Utils.convertDpToPixel(width);
    }

    /**
     * Returns the width of the grid lines that are drawn away from each axis label.
     */
    public float getGridLineWidth() {
        return mGridLineWidth;
    }

    /**
     * Sets the color of the border surrounding the chart.
     *
     * @param color
     */
    public void setAxisLineColor(@ColorInt int color) {
        mAxisLineColor = color;
    }

    /**
     * Returns the color of the axis line (line alongside the axis).
     */
    @ColorInt
    public int getAxisLineColor() {
        return mAxisLineColor;
    }

    /**
     * Set this to true to enable drawing the labels of this axis (this will not affect drawing the
     * grid lines or axis lines).
     *
     * @param enabled
     */
    public void setDrawLabels(boolean enabled) {
        mDrawLabels = enabled;
    }

    /**
     * Returns true if drawing the labels is enabled for this axis.
     */
    public boolean isDrawLabelsEnabled() {
        return mDrawLabels;
    }

    /**
     * Sets the number of label entries for the y-axis, between 2 and 25 included. Be aware that
     * this number is not fixed.
     *
     * @param count the number of y-axis labels that should be displayed
     */
    public void setLabelCount(@IntRange(from = 2, to = 25) int count) {
        setLabelCount(count, false);
    }

    /**
     * Sets the number of label entries for the y-axis, between 2 and 25 included. Be aware that
     * this number is not fixed (if force == false) and can only be approximated.
     *
     * @param count the number of y-axis labels that should be displayed
     * @param force if enabled, the set label count will be forced, meaning that the exact specified
     *              count of labels will be drawn and evenly distributed alongside the axis - this
     *              might cause labels to have uneven values
     */
    public void setLabelCount(@IntRange(from = 2, to = 25) int count, boolean force) {
        mLabelCount = Math.max(2, Math.min(count, 25));
        mForceLabels = force;
    }

    /**
     * Returns true if forcing the y-label count is enabled.
     */
    public boolean isForceLabelsEnabled() {
        return mForceLabels;
    }

    /**
     * Returns the number of label entries the y-axis should have.
     */
    public int getLabelCount() {
        return mLabelCount;
    }

    /**
     * Returns true if granularity is enabled.
     */
    public boolean isGranularityEnabled() {
        return mGranularityEnabled;
    }

    /**
     * Enabled/disable granularity control on axis value intervals. If enabled, the axis interval is
     * not allowed to go below a certain granularity.
     *
     * @param enabled
     */
    public void setGranularityEnabled(boolean enabled) {
        mGranularityEnabled = enabled;
    }

    /**
     * Returns the minimum interval between axis values.
     */
    public float getGranularity() {
        return mGranularity;
    }

    /**
     * Set a minimum interval for the axis when zooming in. The axis is not allowed to go below that
     * limit. This can be used to avoid label duplicating when zooming in.
     *
     * @param granularity
     */
    public void setGranularity(float granularity) {
        mGranularity = granularity;
        // Set this to true if it was disabled, as it makes no sense to call this method with granularity disabled
        mGranularityEnabled = true;
    }

    /**
     * Adds a new LimitLine to this axis.
     *
     * @param limitLine
     */
    public void addLimitLine(@NonNull LimitLine limitLine) {
        mLimitLines.add(limitLine);

        if (mLimitLines.size() > 6) {
            Log.w("MPAndroidChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
        }
    }

    /**
     * Removes the specified LimitLine from the axis.
     *
     * @param limitLine
     */
    public void removeLimitLine(@NonNull LimitLine limitLine) {
        mLimitLines.remove(limitLine);
    }

    /**
     * Removes all LimitLines from the axis.
     */
    public void removeAllLimitLines() {
        mLimitLines.clear();
    }

    /**
     * Returns the LimitLines of this axis.
     */
    @NonNull
    public List<LimitLine> getLimitLines() {
        return mLimitLines;
    }

    /**
     * If this is set to true, the LimitLines are drawn behind the actual data, otherwise on top.
     *
     * @param enabled
     */
    public void setDrawLimitLinesBehindData(boolean enabled) {
        mDrawLimitLineBehindData = enabled;
    }

    public boolean isDrawLimitLinesBehindDataEnabled() {
        return mDrawLimitLineBehindData;
    }

    /**
     * If this is set to false, the grid lines are draw on top of the actual data,
     * otherwise behind. Default: true
     *
     * @param enabled
     */
    public void setDrawGridLinesBehindData(boolean enabled) { mDrawGridLinesBehindData = enabled; }

    public boolean isDrawGridLinesBehindDataEnabled() {
        return mDrawGridLinesBehindData;
    }

    /**
     * Returns the longest formatted label (in terms of characters), this axis contains.
     */
    @NonNull
    public String getLongestLabel() {
        String longest = "";
        for (int i = 0; i < mEntries.length; i++) {
            String text = getFormattedLabel(i);
            if (text != null && longest.length() < text.length()) {
                longest = text;
            }
        }

        return longest;
    }

    public String getFormattedLabel(int index) {
        if (index < 0 || index >= mEntries.length)
            return "";
        else
            return getValueFormatter().getFormattedValue(mEntries[index], this);
    }

    /**
     * Sets the formatter to be used for formatting the axis labels. If no formatter is set, the
     * chart will automatically determine a reasonable formatting (concerning decimals) for all the
     * values that are drawn inside the chart. Use chart.getDefaultValueFormatter() to use the
     * formatter calculated by the chart.
     *
     * @param formatter
     */
    public void setValueFormatter(@Nullable IAxisValueFormatter formatter) {
        if (formatter == null) {
            mAxisValueFormatter = new DefaultAxisValueFormatter(mDecimals);
        } else {
            mAxisValueFormatter = formatter;
        }
    }

    /**
     * Returns the formatter used for formatting the axis labels.
     */
    @NonNull
    public IAxisValueFormatter getValueFormatter() {
        if (mAxisValueFormatter == null ||
                (mAxisValueFormatter instanceof DefaultAxisValueFormatter &&
                        ((DefaultAxisValueFormatter) mAxisValueFormatter).getDecimalDigits() != mDecimals)) {
            mAxisValueFormatter = new DefaultAxisValueFormatter(mDecimals);
        }

        return mAxisValueFormatter;
    }

    /**
     * Enables the grid line to be drawn in dashed mode, e.g. like this "- - - - - -".
     * THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.  Keep in mind that hardware
     * acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableGridDashedLine(float lineLength, float spaceLength, float phase) {
        mGridDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    /**
     * Enables the grid line to be drawn in dashed mode, e.g. like this "- - - - - -".
     * THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF. Keep in mind that hardware
     * acceleration boosts performance.
     *
     * @param effect the DashPathEffect
     */
    public void setGridDashedLine(@Nullable DashPathEffect effect) {
        mGridDashPathEffect = effect;
    }

    /**
     * Disables the grid line to be drawn in dashed mode.
     */
    public void disableGridDashedLine() {
        mGridDashPathEffect = null;
    }

    /**
     * Returns true if the grid dashed-line effect is enabled, false if not.
     */
    public boolean isGridDashedLineEnabled() {
        return mGridDashPathEffect != null;
    }

    /**
     * Returns the DashPathEffect that is set for grid line.
     */
    @Nullable
    public DashPathEffect getGridDashPathEffect() {
        return mGridDashPathEffect;
    }

    /**
     * Enables the axis line to be drawn in dashed mode, e.g. like this "- - - - - -".
     * THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF. Keep in mind that hardware
     * acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableAxisLineDashedLine(float lineLength, float spaceLength, float phase) {
        mAxisLineDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    /**
     * Enables the axis line to be drawn in dashed mode, e.g. like this "- - - - - -".
     * THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF. Keep in mind that hardware
     * acceleration boosts performance.
     *
     * @param effect the DashPathEffect
     */
    public void setAxisLineDashedLine(@Nullable DashPathEffect effect) {
        mAxisLineDashPathEffect = effect;
    }

    /**
     * Disables the axis line to be drawn in dashed mode.
     */
    public void disableAxisLineDashedLine() {
        mAxisLineDashPathEffect = null;
    }

    /**
     * Returns true if the axis dashed-line effect is enabled, false if not.
     */
    public boolean isAxisLineDashedLineEnabled() {
        return mAxisLineDashPathEffect != null;
    }

    /**
     * Returns the DashPathEffect that is set for axis line.
     */
    @Nullable
    public DashPathEffect getAxisLineDashPathEffect() {
        return mAxisLineDashPathEffect;
    }

    public float getAxisMaximum() {
        return mAxisMaximum;
    }

    public float getAxisMinimum() {
        return mAxisMinimum;
    }

    /**
     * By calling this method, any custom maximum value that has been previously set is reseted,
     * and the calculation is done automatically.
     */
    public void resetAxisMaximum() {
        mCustomAxisMax = false;
    }

    /**
     * Returns true if the axis max value has been customized (and is not calculated automatically).
     */
    public boolean isAxisMaxCustom() {
        return mCustomAxisMax;
    }

    /**
     * By calling this method, any custom minimum value that has been previously set is reseted,
     * and the calculation is done automatically.
     */
    public void resetAxisMinimum() {
        mCustomAxisMin = false;
    }

    /**
     * Returns true if the axis min value has been customized (and is not calculated automatically).
     */
    public boolean isAxisMinCustom() {
        return mCustomAxisMin;
    }

    /**
     * Set a custom minimum value for this axis. If set, this value will not be calculated
     * automatically depending on the provided data. Use resetAxisMinValue() to undo this. Do not
     * forget to call setStartAtZero(false) if you use this method. Otherwise, the axis-minimum
     * value will still be forced to 0.
     *
     * @param min
     */
    public void setAxisMinimum(float min) {
        mCustomAxisMin = true;
        mAxisMinimum = min;
        this.mAxisRange = Math.abs(mAxisMaximum - min);
    }

    /**
     * Set a custom maximum value for this axis. If set, this value will not be calculated
     * automatically depending on the provided data. Use resetAxisMaxValue() to undo this.
     *
     * @param max
     */
    public void setAxisMaximum(float max) {
        mCustomAxisMax = true;
        mAxisMaximum = max;
        this.mAxisRange = Math.abs(max - mAxisMinimum);
    }

    /**
     * Calculates the minimum / maximum  and range values of the axis with the given minimum and
     * maximum values from the chart data.
     *
     * @param dataMin the min value according to chart data
     * @param dataMax the max value according to chart data
     */
    public void calculate(float dataMin, float dataMax) {
        // If custom, use value as is, else use data value
        float min = mCustomAxisMin ? mAxisMinimum : (dataMin - mSpaceMin);
        float max = mCustomAxisMax ? mAxisMaximum : (dataMax + mSpaceMax);

        // Temporary range (before calculations)
        float range = Math.abs(max - min);

        // In case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        this.mAxisMinimum = min;
        this.mAxisMaximum = max;

        // Actual range
        this.mAxisRange = Math.abs(max - min);
    }

    /**
     * Gets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`.
     */
    public float getSpaceMin() {
        return mSpaceMin;
    }

    /**
     * Sets extra spacing for `axisMinimum` to be added to automatically calculated `axisMinimum`.
     */
    public void setSpaceMin(float mSpaceMin) {
        this.mSpaceMin = mSpaceMin;
    }

    /**
     * Gets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`.
     */
    public float getSpaceMax() {
        return mSpaceMax;
    }

    /**
     * Sets extra spacing for `axisMaximum` to be added to automatically calculated `axisMaximum`.
     */
    public void setSpaceMax(float mSpaceMax) {
        this.mSpaceMax = mSpaceMax;
    }
}
