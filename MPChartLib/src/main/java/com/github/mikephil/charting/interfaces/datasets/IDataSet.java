package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.model.GradientColor;

import java.util.List;

/**
 * @author Philipp Jahoda
 */
public interface IDataSet<T extends Entry> {
    /**
     * Returns the minimum y-value this DataSet holds.
     */
    float getYMin();

    /**
     * Returns the maximum y-value this DataSet holds.
     */
    float getYMax();

    /**
     * Returns the minimum x-value this DataSet holds.
     */
    float getXMin();

    /**
     * Returns the maximum x-value this DataSet holds.
     */
    float getXMax();

    /**
     * Returns the number of y-values this DataSet represents.
     */
    int getEntryCount();

    /**
     * Calculates the minimum and maximum x and y values (mXMin, mXMax, mYMin, mYMax).
     */
    void calcMinMax();

    /**
     * Calculates the min and max y-values from the Entry closest to the given fromX to the Entry
     * closest to the given toX value. This is only needed for the autoScaleMinMax feature.
     *
     * @param fromX
     * @param toX
     */
    void calcMinMaxY(float fromX, float toX);

    /**
     * Returns the first Entry object found at the given x-value with binary search. If the no Entry
     * at the specified x-value is found, this method returns the Entry at the closest x-value
     * according to the rounding. INFORMATION: This method does calculations at runtime. Do not
     * over-use in performance critical situations.
     *
     * @param xValue     the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding   determine whether to round up/down/closest
     */
    @Nullable
    T getEntryForXValue(float xValue, float closestToY, @NonNull DataSet.Rounding rounding);

    /**
     * Returns the first Entry object found at the given x-value with binary search. If the no Entry
     * at the specified x-value is found, this method returns the Entry at the closest x-value.
     * INFORMATION: This method does calculations at runtime. Do not over-use in performance
     * critical situations.
     *
     * @param xValue     the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     */
    @Nullable
    T getEntryForXValue(float xValue, float closestToY);

    /**
     * Returns all Entry objects found at the given x-value with binary search. An empty array if no
     * Entry object at that x-value. INFORMATION: This method does calculations at runtime. Do not
     * over-use in performance critical situations.
     *
     * @param xValue
     */
    @NonNull
    List<T> getEntriesForXValue(float xValue);

    /**
     * Returns the Entry object found at the given index (NOT xIndex) in the values array.
     *
     * @param index
     */
    @Nullable
    T getEntryForIndex(int index);

    /**
     * Returns the first Entry index found at the given x-value with binary search. If the no Entry
     * at the specified x-value is found, this method returns the Entry at the closest x-value
     * according to the rounding. INFORMATION: This method does calculations at runtime. Do not
     * over-use in performance critical situations.
     *
     * @param xValue     the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @param rounding   determine whether to round up/down/closest
     *                   if there is no Entry matching the provided x-value
     */
    int getEntryIndex(float xValue, float closestToY, @NonNull DataSet.Rounding rounding);

    /**
     * Returns the position of the provided entry in the DataSets Entry array, or -1 if doesn't exist.
     *
     * @param e
     */
    int getEntryIndex(T e);

    /**
     * This method returns the actual index in the Entry array of the DataSet for a given xIndex.
     * IMPORTANT: This method does calculations at runtime, do not over-use in performance critical
     * situations.
     *
     * @param xIndex
     */
    int getIndexInEntries(int xIndex);

    /**
     * Adds an Entry to the DataSet dynamically. Entries are added to the end of the list. This will
     * also recalculate the current minimum and maximum values of the DataSet and the value-sum.
     *
     * @param e
     */
    boolean addEntry(@NonNull T e);

    /**
     * Adds an Entry to the DataSet dynamically. Entries are added to their appropriate index in the
     * values array respective to their x-position. This will also recalculate the current minimum
     * and maximum values of the DataSet and the value-sum.
     *
     * @param e
     */
    void addEntryOrdered(@NonNull T e);

    /**
     * Removes the first Entry (at index 0) of this DataSet from the entries array. Returns true if
     * successful, false if not.
     */
    boolean removeFirst();

    /**
     * Removes the last Entry (at index size-1) of this DataSet from the entries array. Returns true
     * if successful, false if not.
     */
    boolean removeLast();

    /**
     * Removes an Entry from the DataSets entries array. This will also recalculate the current
     * minimum and maximum values of the DataSet and the value-sum. Returns true if an Entry was
     * removed, false if no Entry could be removed.
     *
     * @param e
     */
    boolean removeEntry(T e);

    /**
     * Removes the Entry object closest to the given x-value from the DataSet. Returns true if an
     * Entry was removed, false if no Entry could be removed.
     *
     * @param xValue
     */
    boolean removeEntryByXValue(float xValue);

    /**
     * Removes the Entry object at the given index in the values array from the DataSet. Returns
     * true if an Entry was removed, false if no Entry could be removed.
     *
     * @param index
     */
    boolean removeEntry(int index);

    /**
     * Checks if this DataSet contains the specified Entry. Returns true if so, false if not.
     * NOTE: Performance is pretty bad on this one, do not over-use in performance critical
     * situations.
     *
     * @param entry
     */
    boolean contains(T entry);

    /**
     * Removes all values from this DataSet and does all necessary recalculations.
     */
    void clear();

    /**
     * Returns the label string that describes the DataSet.
     */
    @NonNull
    String getLabel();

    /**
     * Sets the label string that describes the DataSet.
     *
     * @param label
     */
    void setLabel(@NonNull String label);

    /**
     * Returns the axis this DataSet should be plotted against.
     */
    @NonNull
    YAxis.AxisDependency getAxisDependency();

    /**
     * Set the y-axis this DataSet should be plotted against (either LEFT or RIGHT).
     *
     * @param dependency
     */
    void setAxisDependency(@NonNull YAxis.AxisDependency dependency);

    /**
     * Returns all the colors that are set for this DataSet.
     */
    @ColorInt
    @NonNull
    List<Integer> getColors();

    /**
     * Returns the first color (index 0) of the colors-array this DataSet contains. This is only
     * used for performance reasons when only one color is in the colors array (size == 1).
     */
    @ColorInt
    int getColor();

    /**
     * Returns the Gradient color model
     *
     * @return
     */
    GradientColor getGradientColor();

    /**
     * Returns the Gradient colors
     *
     * @return
     */
    @NonNull
    List<GradientColor> getGradientColors();

    /**
     * Returns the Gradient colors
     *
     * @param index
     * @return
     */
    GradientColor getGradientColor(int index);

    /**
     * Returns the color at the given index of the DataSet's color array.
     *
     * @param index
     */
    @ColorInt
    int getColor(int index);

    /**
     * Returns true if highlighting of values is enabled, false if not.
     */
    boolean isHighlightEnabled();

    /**
     * If set to true, value highlighting is enabled which means that values can be highlighted
     * programmatically or by touch gesture.
     *
     * @param enabled
     */
    void setHighlightEnabled(boolean enabled);

    /**
     * Sets the formatter to be used for drawing the values inside the chart. If no formatter is
     * set, the chart will automatically determine a reasonable formatting (concerning decimals) for
     * all the values that are drawn inside the chart. Use chart.getDefaultValueFormatter() to use
     * the formatter calculated by the chart.
     *
     * @param formatter
     */
    void setValueFormatter(@NonNull IValueFormatter formatter);

    /**
     * Returns the formatter used for drawing the values inside the chart.
     */
    @NonNull
    IValueFormatter getValueFormatter();

    /**
     * Returns true if the valueFormatter object of this DataSet is null.
     */
    boolean needsFormatter();

    /**
     * Sets the color the value-labels of this DataSet should have.
     *
     * @param color
     */
    void setValueTextColor(@ColorInt int color);

    /**
     * Sets a list of colors to be used as the colors for the drawn values.
     *
     * @param colors
     */
    void setValueTextColors(@ColorInt @NonNull List<Integer> colors);

    /**
     * Sets a Typeface for the value-labels of this DataSet.
     *
     * @param typeface
     */
    void setValueTypeface(Typeface typeface);

    /**
     * Sets the text-size of the value-labels of this DataSet in dp.
     *
     * @param size
     */
    void setValueTextSize(float size);

    /**
     * Returns only the first color of all colors that are set to be used for the values.
     */
    @ColorInt
    int getValueTextColor();

    /**
     * Returns the color at the specified index that is used for drawing the values inside the chart.
     *
     * @param index
     */
    @ColorInt
    int getValueTextColor(int index);

    /**
     * Returns the typeface that is used for drawing the values inside the chart.
     */
    Typeface getValueTypeface();

    /**
     * Returns the text size that is used for drawing the values inside the chart.
     */
    float getValueTextSize();

    /**
     * The form to draw for this data set in the legend. Return `DEFAULT` to use the default legend
     * form.
     */
    @NonNull
    Legend.LegendForm getForm();

    /**
     * The form size to draw for this data set in the legend. Return `Float.NaN` to use the default
     * legend form size.
     */
    float getFormSize();

    /**
     * The line width for drawing the form of this data set in the legend. Return `Float.NaN` to use
     * the default legend form line width.
     */
    float getFormLineWidth();

    /**
     * The line dash path effect used for shapes that consist of lines. Return `null` to use the
     * default legend form line dash effect.
     */
    @Nullable
    DashPathEffect getFormLineDashEffect();

    /**
     * Set this to true to draw y-values on the chart. NOTE (for bar and line charts): if
     * `maxVisibleCount` is reached, no values will be drawn even if this is enabled.
     *
     * @param enabled
     */
    void setDrawValues(boolean enabled);

    /**
     * Returns true if y-value drawing is enabled, false if not.
     */
    boolean isDrawValuesEnabled();

    /**
     * Set this to true to draw y-icons on the chart. NOTE (for bar and line charts): if
     * `maxVisibleCount` is reached, no icons will be drawn even if this is enabled.
     *
     * @param enabled
     */
    void setDrawIcons(boolean enabled);

    /**
     * Returns true if y-icon drawing is enabled, false if not.
     */
    boolean isDrawIconsEnabled();

    /**
     * Offset of icons drawn on the chart. For all charts except Pie and Radar it will be ordinary
     * (x offset, y offset). For Pie and Radar chart it will be (y offset, distance from center
     * offset); so if you want icon to be rendered under value, you should increase X component of
     * MPPointF, and if you want icon to be rendered closet to center, you should decrease height
     * component of MPPointF.
     *
     * @param offset
     */
    void setIconsOffset(@NonNull MPPointF offset);

    /**
     * Get the offset for drawing icons.
     */
    @NonNull
    MPPointF getIconsOffset();

    /**
     * Set the visibility of this DataSet. If not visible, the DataSet will not be drawn to the
     * chart upon refreshing it.
     *
     * @param visible
     */
    void setVisible(boolean visible);

    /**
     * Returns true if this DataSet is visible inside the chart, or false if it is currently hidden.
     */
    boolean isVisible();
}
