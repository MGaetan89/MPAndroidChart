package com.github.mikephil.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This is the base data set of all DataSets. It's purpose is to implement critical methods
 * provided by the IDataSet interface.
 *
 * @author Philipp Jahoda
 */
public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {
    /**
     * List representing all colors that are used for this DataSet.
     */
    @ColorInt
    @NonNull
    protected List<Integer> mColors = Collections.emptyList();

    protected GradientColor mGradientColor = null;

    @NonNull
    protected List<GradientColor> mGradientColors = Collections.emptyList();

    /**
     * List representing all colors that are used for drawing the actual values for this DataSet.
     */
    @ColorInt
    @NonNull
    protected List<Integer> mValueColors = Collections.emptyList();

    /**
     * Label that describes the DataSet or the data the DataSet represents.
     */
    @NonNull
    private String mLabel = "DataSet";

    /**
     * This specifies which axis this DataSet should be plotted against.
     */
    @NonNull
    protected YAxis.AxisDependency mAxisDependency = YAxis.AxisDependency.LEFT;

    /**
     * If true, value highlighting is enabled.
     */
    protected boolean mHighlightEnabled = true;

    /**
     * Custom formatter that is used instead of the auto-formatter if set.
     */
    @NonNull
    protected transient ValueFormatter mValueFormatter;

    /**
     * The typeface used for the value text.
     */
    @Nullable
    protected Typeface mValueTypeface;

    @NonNull
    private Legend.LegendForm mForm = Legend.LegendForm.DEFAULT;
    private float mFormSize = Float.NaN;
    private float mFormLineWidth = Float.NaN;

    @Nullable
    private DashPathEffect mFormLineDashEffect = null;

    /**
     * If true, y-values are drawn on the chart.
     */
    protected boolean mDrawValues = true;

    /**
     * If true, y-icons are drawn on the chart.
     */
    protected boolean mDrawIcons = true;

    /**
     * The offset for drawing icons (in dp).
     */
    @NonNull
    protected MPPointF mIconsOffset = new MPPointF();

    /**
     * The size of the value-text labels.
     */
    protected float mValueTextSize = 17f;

    /**
     * Flag that indicates if the DataSet is visible or not.
     */
    protected boolean mVisible = true;

    /**
     * Default constructor.
     */
    public BaseDataSet() {
        mColors = new ArrayList<>();
        mGradientColors = new ArrayList<>();
        mValueColors = new ArrayList<>();

        // Default color
        mColors.add(0x8CEAFF);
        mValueColors.add(Color.BLACK);
    }

    /**
     * Constructor with label.
     *
     * @param label
     */
    public BaseDataSet(@NonNull String label) {
        this();
        this.mLabel = label;
    }

    /**
     * Use this method to tell the data set that the underlying data has changed.
     */
    public void notifyDataSetChanged() {
        calcMinMax();
    }

    @ColorInt
    @NonNull
    @Override
    public List<Integer> getColors() {
        return mColors;
    }

    @ColorInt
    @NonNull
    public List<Integer> getValueColors() {
        return mValueColors;
    }

    @ColorInt
    @Override
    public int getColor() {
        return mColors.get(0);
    }

    @ColorInt
    @Override
    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }

    @Override
    public GradientColor getGradientColor() {
        return mGradientColor;
    }

    @NonNull
    @Override
    public List<GradientColor> getGradientColors() {
        return mGradientColors;
    }

    @Override
    public GradientColor getGradientColor(int index) {
        return mGradientColors.get(index % mGradientColors.size());
    }

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused as soon as the
     * number of Entries the DataSet represents is higher than the size of the colors array.
     *
     * @param colors
     */
    public void setColors(@ColorInt @NonNull List<Integer> colors) {
        this.mColors = colors;
    }

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused as soon as the
     * number of Entries the DataSet represents is higher than the size of the colors array.
     *
     * @param colors
     */
    public void setColors(@ColorInt int... colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    /**
     * Sets the colors that should be used fore this DataSet. Colors are reused as soon as the
     * number of Entries the DataSet represents is higher than the size of the colors array. You can
     * use "new int[] { R.color.red, R.color.green, ... }" to provide colors for this method.
     * Internally, the colors are resolved using getResources().getColor(...).
     *
     * @param colors
     */
    public void setColors(@ColorRes int[] colors, @NonNull Context context) {
        mColors.clear();

        for (int color : colors) {
            mColors.add(context.getResources().getColor(color));
        }
    }

    /**
     * Adds a new color to the colors array of the DataSet.
     *
     * @param color
     */
    public void addColor(int color) {
        mColors.add(color);
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet. Internally, this recreates
     * the colors array and adds the specified color.
     *
     * @param color
     */
    public void setColor(@ColorInt int color) {
        mColors = new ArrayList<>();
        mColors.add(color);
    }

    /**
     * Sets the start and end color for gradient color, ONLY color that should be used for this DataSet.
     *
     * @param startColor
     * @param endColor
     */
    public void setGradientColor(@ColorInt int startColor, @ColorInt int endColor) {
        mGradientColor = new GradientColor(startColor, endColor);
    }

    /**
     * Sets the start and end color for gradient colors, ONLY color that should be used for this DataSet.
     *
     * @param gradientColors
     */
    public void setGradientColors(List<GradientColor> gradientColors) {
        this.mGradientColors = gradientColors;
    }

    /**
     * Sets a color with a specific alpha value.
     *
     * @param color
     * @param alpha from 0-255
     */
    public void setColor(@ColorInt int color, int alpha) {
        setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    /**
     * Sets colors with a specific alpha value.
     *
     * @param colors
     * @param alpha
     */
    public void setColors(int[] colors, int alpha) {
        resetColors();
        for (int color : colors) {
            addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    /**
     * Resets all colors of this DataSet and recreates the colors array.
     */
    public void resetColors() {
        mColors.clear();
    }

    @Override
    public void setLabel(@NonNull String label) {
        mLabel = label;
    }

    @NonNull
    @Override
    public String getLabel() {
        return mLabel;
    }

    @Override
    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;
    }

    @Override
    public boolean isHighlightEnabled() {
        return mHighlightEnabled;
    }

    @Override
    public void setValueFormatter(@NonNull ValueFormatter formatter) {
        mValueFormatter = formatter;
    }

    @NonNull
    @Override
    public ValueFormatter getValueFormatter() {
        if (needsFormatter()) {
            return Utils.getDefaultValueFormatter();
        } else {
            return mValueFormatter;
        }
    }

    @Override
    public boolean needsFormatter() {
        return false;
    }

    @Override
    public void setValueTextColor(@ColorInt int color) {
        mValueColors = new ArrayList<>();
        mValueColors.add(color);
    }

    @Override
    public void setValueTextColors(@ColorInt @NonNull List<Integer> colors) {
        mValueColors = colors;
    }

    @Override
    public void setValueTypeface(@Nullable Typeface typeface) {
        mValueTypeface = typeface;
    }

    @Override
    public void setValueTextSize(float size) {
        mValueTextSize = Utils.convertDpToPixel(size);
    }

    @ColorInt
    @Override
    public int getValueTextColor() {
        return mValueColors.get(0);
    }

    @ColorInt
    @Override
    public int getValueTextColor(int index) {
        return mValueColors.get(index % mValueColors.size());
    }

    @Nullable
    @Override
    public Typeface getValueTypeface() {
        return mValueTypeface;
    }

    @Override
    public float getValueTextSize() {
        return mValueTextSize;
    }

    public void setForm(@NonNull Legend.LegendForm form) {
        mForm = form;
    }

    @NonNull
    @Override
    public Legend.LegendForm getForm() {
        return mForm;
    }

    public void setFormSize(float formSize) {
        mFormSize = formSize;
    }

    @Override
    public float getFormSize() {
        return mFormSize;
    }

    public void setFormLineWidth(float formLineWidth) {
        mFormLineWidth = formLineWidth;
    }

    @Override
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    public void setFormLineDashEffect(@Nullable DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    @Nullable
    @Override
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    @Override
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    @Override
    public boolean isDrawValuesEnabled() {
        return mDrawValues;
    }

    @Override
    public void setDrawIcons(boolean enabled) {
        mDrawIcons = enabled;
    }

    @Override
    public boolean isDrawIconsEnabled() {
        return mDrawIcons;
    }

    @Override
    public void setIconsOffset(@NonNull MPPointF offsetDp) {
        mIconsOffset.x = offsetDp.x;
        mIconsOffset.y = offsetDp.y;
    }

    @NonNull
    @Override
    public MPPointF getIconsOffset() {
        return mIconsOffset;
    }

    @Override
    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return mVisible;
    }

    @NonNull
    @Override
    public YAxis.AxisDependency getAxisDependency() {
        return mAxisDependency;
    }

    @Override
    public void setAxisDependency(@NonNull YAxis.AxisDependency dependency) {
        mAxisDependency = dependency;
    }

    @Override
    public int getIndexInEntries(int xIndex) {
        for (int i = 0, count = getEntryCount(); i < count; i++) {
            T entry = getEntryForIndex(i);
            //noinspection ConstantConditions
            if (xIndex == entry.getX()) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean removeFirst() {
        if (getEntryCount() > 0) {
            T entry = getEntryForIndex(0);
            return removeEntry(entry);
        } else {
            return false;
        }
    }

    @Override
    public boolean removeLast() {
        if (getEntryCount() > 0) {
            T entry = getEntryForIndex(getEntryCount() - 1);
            return removeEntry(entry);
        } else {
            return false;
        }
    }

    @Override
    public boolean removeEntryByXValue(float xValue) {
        T entry = getEntryForXValue(xValue, Float.NaN);
        return removeEntry(entry);
    }

    @Override
    public boolean removeEntry(int index) {
        T entry = getEntryForIndex(index);
        return removeEntry(entry);
    }

    @Override
    public boolean contains(T entry) {
        for (int i = 0, count = getEntryCount(); i < count; i++) {
            T currentEntry = getEntryForIndex(i);
            //noinspection ConstantConditions
            if (currentEntry.equals(entry)) {
                return true;
            }
        }

        return false;
    }

    protected void copy(BaseDataSet baseDataSet) {
        baseDataSet.mAxisDependency = mAxisDependency;
        baseDataSet.mColors = mColors;
        baseDataSet.mDrawIcons = mDrawIcons;
        baseDataSet.mDrawValues = mDrawValues;
        baseDataSet.mForm = mForm;
        baseDataSet.mFormLineDashEffect = mFormLineDashEffect;
        baseDataSet.mFormLineWidth = mFormLineWidth;
        baseDataSet.mFormSize = mFormSize;
        baseDataSet.mGradientColor = mGradientColor;
        baseDataSet.mGradientColors = mGradientColors;
        baseDataSet.mHighlightEnabled = mHighlightEnabled;
        baseDataSet.mIconsOffset = mIconsOffset;
        baseDataSet.mValueColors = mValueColors;
        baseDataSet.mValueFormatter = mValueFormatter;
        baseDataSet.mValueTextSize = mValueTextSize;
        baseDataSet.mVisible = mVisible;
    }
}
