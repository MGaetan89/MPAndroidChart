package com.github.mikephil.charting.data;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that holds all relevant data that represents the chart. That involves at least one (or
 * more) DataSets, and an array of x-values.
 *
 * @author Philipp Jahoda
 */
public abstract class ChartData<T extends IDataSet<? extends Entry>> {
    /**
     * Maximum y-value in the value array across all axes.
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * The minimum y-value in the value array across all axes.
     */
    protected float mYMin = Float.MAX_VALUE;

    /**
     * Maximum x-value in the value array.
     */
    protected float mXMax = -Float.MAX_VALUE;

    /**
     * Minimum x-value in the value array.
     */
    protected float mXMin = Float.MAX_VALUE;

    protected float mLeftAxisMax = -Float.MAX_VALUE;

    protected float mLeftAxisMin = Float.MAX_VALUE;

    protected float mRightAxisMax = -Float.MAX_VALUE;

    protected float mRightAxisMin = Float.MAX_VALUE;

    /**
     * Array that holds all DataSets the ChartData object represents.
     */
    @NonNull
    protected List<T> mDataSets;

    /**
     * Default constructor.
     */
    public ChartData() {
        this(new ArrayList<T>());
    }

    /**
     * Constructor taking single or multiple DataSet objects.
     *
     * @param dataSets
     */
    public ChartData(T... dataSets) {
        this(new ArrayList<>(Arrays.asList(dataSets)));
    }

    /**
     * Constructor for chart data.
     *
     * @param sets the data set array
     */
    public ChartData(@NonNull List<T> sets) {
        this.mDataSets = sets;
        notifyDataChanged();
    }

    /**
     * Call this method to let the ChartData know that the underlying data has changed. Calling this
     * performs all necessary recalculations needed when the contained data has changed.
     */
    public void notifyDataChanged() {
        calcMinMax();
    }

    /**
     * Calc minimum and maximum y-values over all DataSets. Tell DataSets to recalculate their min
     * and max y-values, this is only needed for autoScaleMinMax.
     *
     * @param fromX the x-value to start the calculation from
     * @param toX   the x-value to which the calculation should be performed
     */
    public void calcMinMaxY(float fromX, float toX) {
        for (T set : mDataSets) {
            set.calcMinMaxY(fromX, toX);
        }

        // Apply the new data
        calcMinMax();
    }

    /**
     * Calc minimum and maximum values (both x and y) over all DataSets.
     */
    protected void calcMinMax() {
        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        for (T set : mDataSets) {
            calcMinMax(set);
        }

        mLeftAxisMax = -Float.MAX_VALUE;
        mLeftAxisMin = Float.MAX_VALUE;
        mRightAxisMax = -Float.MAX_VALUE;
        mRightAxisMin = Float.MAX_VALUE;

        // Left axis
        T firstLeft = getFirstLeft(mDataSets);
        if (firstLeft != null) {
            mLeftAxisMax = firstLeft.getYMax();
            mLeftAxisMin = firstLeft.getYMin();

            for (T dataSet : mDataSets) {
                if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
                    if (dataSet.getYMin() < mLeftAxisMin) {
                        mLeftAxisMin = dataSet.getYMin();
                    }

                    if (dataSet.getYMax() > mLeftAxisMax) {
                        mLeftAxisMax = dataSet.getYMax();
                    }
                }
            }
        }

        // Right axis
        T firstRight = getFirstRight(mDataSets);
        if (firstRight != null) {
            mRightAxisMax = firstRight.getYMax();
            mRightAxisMin = firstRight.getYMin();

            for (T dataSet : mDataSets) {
                if (dataSet.getAxisDependency() == AxisDependency.RIGHT) {
                    if (dataSet.getYMin() < mRightAxisMin) {
                        mRightAxisMin = dataSet.getYMin();
                    }

                    if (dataSet.getYMax() > mRightAxisMax) {
                        mRightAxisMax = dataSet.getYMax();
                    }
                }
            }
        }
    }

    /**
     * Returns the number of LineDataSets this object contains.
     */
    public int getDataSetCount() {
        return mDataSets.size();
    }

    /**
     * Returns the smallest y-value the data object contains.
     */
    public float getYMin() {
        return mYMin;
    }

    /**
     * Returns the minimum y-value for the specified axis.
     *
     * @param axis
     */
    public float getYMin(AxisDependency axis) {
        if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMin == Float.MAX_VALUE) {
                return mRightAxisMin;
            } else {
                return mLeftAxisMin;
            }
        } else {
            if (mRightAxisMin == Float.MAX_VALUE) {
                return mLeftAxisMin;
            } else {
                return mRightAxisMin;
            }
        }
    }

    /**
     * Returns the greatest y-value the data object contains.
     */
    public float getYMax() {
        return mYMax;
    }

    /**
     * Returns the maximum y-value for the specified axis.
     *
     * @param axis
     */
    public float getYMax(AxisDependency axis) {
        if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMax == -Float.MAX_VALUE) {
                return mRightAxisMax;
            } else {
                return mLeftAxisMax;
            }
        } else {
            if (mRightAxisMax == -Float.MAX_VALUE) {
                return mLeftAxisMax;
            } else {
                return mRightAxisMax;
            }
        }
    }

    /**
     * Returns the minimum x-value this data object contains.
     */
    public float getXMin() {
        return mXMin;
    }

    /**
     * Returns the maximum x-value this data object contains.
     */
    public float getXMax() {
        return mXMax;
    }

    /**
     * Returns all DataSet objects this ChartData object holds.
     */
    public List<T> getDataSets() {
        return mDataSets;
    }

    /**
     * Retrieve the index of a DataSet with a specific label from the ChartData. Search can be case
     * sensitive or not. IMPORTANT: This method does calculations at runtime, do not over-use in
     * performance critical situations.
     *
     * @param dataSets   the DataSet array to search
     * @param label
     * @param ignoreCase if true, the search is not case-sensitive
     * @return
     */
    protected int getDataSetIndexByLabel(@NonNull List<T> dataSets, @NonNull String label, boolean ignoreCase) {
        if (ignoreCase) {
            for (int i = 0; i < dataSets.size(); i++) {
                if (label.equalsIgnoreCase(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < dataSets.size(); i++) {
                if (label.equals(dataSets.get(i).getLabel())) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Returns the labels of all DataSets as a string array.
     */
    @NonNull
    public String[] getDataSetLabels() {
        String[] types = new String[mDataSets.size()];
        for (int i = 0; i < mDataSets.size(); i++) {
            types[i] = mDataSets.get(i).getLabel();
        }

        return types;
    }

    /**
     * Returns the Entry for a corresponding highlight object.
     *
     * @param highlight
     */
    @Nullable
    public Entry getEntryForHighlight(@NonNull Highlight highlight) {
        T dataSet = getDataSetByIndex(highlight.getDataIndex());
        if (dataSet == null) {
            return null;
        } else {
            return dataSet.getEntryForXValue(highlight.getX(), highlight.getY());
        }
    }

    /**
     * Returns the DataSet object with the given label. Search can be case sensitive or not.
     * IMPORTANT: This method does calculations at runtime. Use with care in performance critical
     * situations.
     *
     * @param label
     * @param ignoreCase
     */
    @Nullable
    public T getDataSetByLabel(String label, boolean ignoreCase) {
        int index = getDataSetIndexByLabel(mDataSets, label, ignoreCase);
        return getDataSetByIndex(index);
    }

    @Nullable
    public T getDataSetByIndex(int index) {
        if (index < 0 || index >= mDataSets.size()) {
            return null;
        }

        return mDataSets.get(index);
    }

    /**
     * Adds a DataSet dynamically.
     *
     * @param dataSet
     */
    public void addDataSet(@Nullable T dataSet) {
        if (dataSet != null) {
            calcMinMax(dataSet);

            mDataSets.add(dataSet);
        }
    }

    /**
     * Removes the given DataSet from this data object. Also recalculates all minimum and maximum
     * values. Returns true if a DataSet was removed, false if no DataSet could be removed.
     *
     * @param dataSet
     */
    public boolean removeDataSet(@Nullable T dataSet) {
        if (dataSet == null) {
            return false;
        }

        boolean removed = mDataSets.remove(dataSet);

        // If a DataSet was removed
        if (removed) {
            calcMinMax();
        }

        return removed;
    }

    /**
     * Removes the DataSet at the given index in the DataSet array from the data object. Also
     * recalculates all minimum and maximum values. Returns true if a DataSet was removed, false if
     * no DataSet could be removed.
     *
     * @param index
     */
    public boolean removeDataSet(int index) {
        T dataSet = getDataSetByIndex(index);
        return removeDataSet(dataSet);
    }

    /**
     * Adds an Entry to the DataSet at the specified index. Entries are added to the end of the list.
     *
     * @param entry
     * @param dataSetIndex
     */
    public void addEntry(@NonNull Entry entry, int dataSetIndex) {
        IDataSet dataSet = getDataSetByIndex(dataSetIndex);
        // Add the entry to the data set
        if (dataSet != null && dataSet.addEntry(entry)) {
            calcMinMax(entry, dataSet.getAxisDependency());
        }
    }

    /**
     * Adjusts the current minimum and maximum values based on the provided Entry object.
     *
     * @param entry
     * @param axis
     */
    protected void calcMinMax(@NonNull Entry entry, AxisDependency axis) {
        if (mYMax < entry.getY()) {
            mYMax = entry.getY();
        }

        if (mYMin > entry.getY()) {
            mYMin = entry.getY();
        }

        if (mXMax < entry.getX()) {
            mXMax = entry.getX();
        }

        if (mXMin > entry.getX()) {
            mXMin = entry.getX();
        }

        if (axis == AxisDependency.LEFT) {
            if (mLeftAxisMax < entry.getY()) {
                mLeftAxisMax = entry.getY();
            }

            if (mLeftAxisMin > entry.getY()) {
                mLeftAxisMin = entry.getY();
            }
        } else {
            if (mRightAxisMax < entry.getY()) {
                mRightAxisMax = entry.getY();
            }

            if (mRightAxisMin > entry.getY()) {
                mRightAxisMin = entry.getY();
            }
        }
    }

    /**
     * Adjusts the minimum and maximum values based on the given DataSet.
     *
     * @param dataSet
     */
    protected void calcMinMax(@NonNull T dataSet) {
        if (mYMax < dataSet.getYMax()) {
            mYMax = dataSet.getYMax();
        }

        if (mYMin > dataSet.getYMin()) {
            mYMin = dataSet.getYMin();
        }

        if (mXMax < dataSet.getXMax()) {
            mXMax = dataSet.getXMax();
        }

        if (mXMin > dataSet.getXMin()) {
            mXMin = dataSet.getXMin();
        }

        if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
            if (mLeftAxisMax < dataSet.getYMax()) {
                mLeftAxisMax = dataSet.getYMax();
            }

            if (mLeftAxisMin > dataSet.getYMin()) {
                mLeftAxisMin = dataSet.getYMin();
            }
        } else {
            if (mRightAxisMax < dataSet.getYMax()) {
                mRightAxisMax = dataSet.getYMax();
            }

            if (mRightAxisMin > dataSet.getYMin()) {
                mRightAxisMin = dataSet.getYMin();
            }
        }
    }

    /**
     * Removes the given Entry object from the DataSet at the specified index.
     *
     * @param entry
     * @param dataSetIndex
     */
    public boolean removeEntry(@Nullable Entry entry, int dataSetIndex) {
        IDataSet dataSet = getDataSetByIndex(dataSetIndex);
        if (dataSet == null || entry == null) {
            return false;
        }

        // Remove the entry from the data set
        boolean removed = dataSet.removeEntry(entry);
        if (removed) {
            calcMinMax();
        }

        return removed;
    }

    /**
     * Removes the Entry object closest to the given DataSet at the specified index. Returns true if
     * an Entry was removed, false if no Entry was found that meets the specified requirements.
     *
     * @param xValue
     * @param dataSetIndex
     */
    public boolean removeEntry(float xValue, int dataSetIndex) {
        T dataSet = getDataSetByIndex(dataSetIndex);
        if (dataSet == null) {
            return false;
        }

        Entry entry = dataSet.getEntryForXValue(xValue, Float.NaN);
        return entry != null && removeEntry(entry, dataSetIndex);
    }

    /**
     * Returns the DataSet that contains the provided Entry, or null, if no DataSet contains this
     * Entry.
     *
     * @param entry
     */
    @Nullable
    public T getDataSetForEntry(@Nullable Entry entry) {
        if (entry == null) {
            return null;
        }

        for (int i = 0; i < mDataSets.size(); i++) {
            T set = mDataSets.get(i);
            for (int j = 0; j < set.getEntryCount(); j++) {
                if (entry.equalTo(set.getEntryForXValue(entry.getX(), entry.getY()))) {
                    return set;
                }
            }
        }

        return null;
    }

    /**
     * Returns all colors used across all DataSet objects this object represents.
     */
    public int[] getColors() {
        int colorsCount = 0;
        for (int i = 0; i < mDataSets.size(); i++) {
            colorsCount += mDataSets.get(i).getColors().size();
        }

        int[] allColors = new int[colorsCount];
        int count = 0;

        for (int i = 0; i < mDataSets.size(); i++) {
            List<Integer> colors = mDataSets.get(i).getColors();
            for (Integer color : colors) {
                allColors[count] = color;
                count++;
            }
        }

        return allColors;
    }

    /**
     * Returns the index of the provided DataSet in the DataSet array of this data object, or -1 if
     * it does not exist.
     *
     * @param dataSet
     */
    public int getIndexOfDataSet(T dataSet) {
        return mDataSets.indexOf(dataSet);
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the left axis.
     * Returns null if no DataSet with left dependency could be found.
     */
    @Nullable
    protected T getFirstLeft(@NonNull List<T> sets) {
        for (T dataSet : sets) {
            if (dataSet.getAxisDependency() == AxisDependency.LEFT) {
                return dataSet;
            }
        }

        return null;
    }

    /**
     * Returns the first DataSet from the datasets-array that has it's dependency on the right axis.
     * Returns null if no DataSet with right dependency could be found.
     */
    @Nullable
    public T getFirstRight(@NonNull List<T> sets) {
        for (T dataSet : sets) {
            if (dataSet.getAxisDependency() == AxisDependency.RIGHT) {
                return dataSet;
            }
        }

        return null;
    }

    /**
     * Sets a custom IValueFormatter for all DataSets this data object contains.
     *
     * @param formatter
     */
    public void setValueFormatter(@Nullable IValueFormatter formatter) {
        if (formatter != null) {
            for (T set : mDataSets) {
                set.setValueFormatter(formatter);
            }
        }
    }

    /**
     * Sets the color of the value-text (color in which the value-labels are drawn) for all DataSets
     * this data object contains.
     *
     * @param color
     */
    public void setValueTextColor(@ColorInt int color) {
        for (T set : mDataSets) {
            set.setValueTextColor(color);
        }
    }

    /**
     * Sets the same list of value-colors for all DataSets this data object contains.
     *
     * @param colors
     */
    public void setValueTextColors(@ColorInt List<Integer> colors) {
        for (T set : mDataSets) {
            set.setValueTextColors(colors);
        }
    }

    /**
     * Sets the Typeface for all value-labels for all DataSets this data object contains.
     *
     * @param typeface
     */
    public void setValueTypeface(Typeface typeface) {
        for (T set : mDataSets) {
            set.setValueTypeface(typeface);
        }
    }

    /**
     * Sets the size (in dp) of the value-text for all DataSets this data object contains.
     *
     * @param size
     */
    public void setValueTextSize(float size) {
        for (T set : mDataSets) {
            set.setValueTextSize(size);
        }
    }

    /**
     * Enables/disables drawing values (value-text) for all DataSets this data object contains.
     *
     * @param enabled
     */
    public void setDrawValues(boolean enabled) {
        for (T set : mDataSets) {
            set.setDrawValues(enabled);
        }
    }

    /**
     * Enables/disables highlighting values for all DataSets this data object contains. If set to
     * true, this means that values can be highlighted programmatically or by touch gesture.
     */
    public void setHighlightEnabled(boolean enabled) {
        for (T set : mDataSets) {
            set.setHighlightEnabled(enabled);
        }
    }

    /**
     * Returns true if highlighting of all underlying values is enabled, false if not.
     */
    public boolean isHighlightEnabled() {
        for (T set : mDataSets) {
            if (!set.isHighlightEnabled()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Clears this data object from all DataSets and removes all Entries. Don't forget to invalidate
     * the chart after this.
     */
    public void clearValues() {
        mDataSets.clear();
        notifyDataChanged();
    }

    /**
     * Checks if this data object contains the specified DataSet. Returns true if so, false if not.
     *
     * @param dataSet
     */
    public boolean contains(T dataSet) {
        for (T set : mDataSets) {
            if (set.equals(dataSet)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the total entry count across all DataSet objects this data object contains.
     */
    public int getEntryCount() {
        int count = 0;
        for (T set : mDataSets) {
            count += set.getEntryCount();
        }

        return count;
    }

    /**
     * Returns the DataSet object with the maximum number of entries or null if there are no DataSets.
     */
    @Nullable
    public T getMaxEntryCountSet() {
        if (mDataSets.isEmpty()) {
            return null;
        }

        T max = mDataSets.get(0);
        for (T set : mDataSets) {
            if (set.getEntryCount() > max.getEntryCount()) {
                max = set;
            }
        }

        return max;
    }
}
