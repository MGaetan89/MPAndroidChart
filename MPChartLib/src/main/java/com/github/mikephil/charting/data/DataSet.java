package com.github.mikephil.charting.data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The DataSet class represents one group or type of entries (Entry) in the Chart that belong
 * together. It is designed to logically separate different groups of values inside the Chart (e.g.
 * the values for a specific line in the LineChart, or the values of a specific group of bars in the
 * BarChart).
 *
 * @author Philipp Jahoda
 */
public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {
    /**
     * The entries that this DataSet represents/holds together.
     */
    @NonNull
    protected List<T> mValues;

    /**
     * Maximum y-value in the value array.
     */
    protected float mYMax = -Float.MAX_VALUE;

    /**
     * Minimum y-value in the value array.
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

    /**
     * Creates a new DataSet object with the given values (entries) it represents. Also, a label
     * that describes the DataSet can be specified. The label can also be used to retrieve the
     * DataSet from a ChartData object.
     *
     * @param values
     * @param label
     */
    public DataSet(@NonNull List<T> values, String label) {
        super(label);

        this.mValues = values;

        calcMinMax();
    }

    @Override
    public void calcMinMax() {
        if (mValues.isEmpty()) {
            return;
        }

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        for (T entry : mValues) {
            calcMinMax(entry);
        }
    }

    @Override
    public void calcMinMaxY(float fromX, float toX) {
        if (mValues.isEmpty()) {
            return;
        }

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;

        int indexFrom = getEntryIndex(fromX, Float.NaN, Rounding.DOWN);
        int indexTo = getEntryIndex(toX, Float.NaN, Rounding.UP);

        for (int i = indexFrom; i <= indexTo; i++) {
            // Only recalculate y
            calcMinMaxY(mValues.get(i));
        }
    }

    /**
     * Updates the min and max x and y value of this DataSet based on the given Entry.
     *
     * @param entry
     */
    protected void calcMinMax(@NonNull T entry) {
        calcMinMaxX(entry);
        calcMinMaxY(entry);
    }

    protected void calcMinMaxX(@NonNull T entry) {
        if (entry.getX() < mXMin) {
            mXMin = entry.getX();
        }

        if (entry.getX() > mXMax) {
            mXMax = entry.getX();
        }
    }

    protected void calcMinMaxY(@NonNull T entry) {
        if (entry.getY() < mYMin) {
            mYMin = entry.getY();
        }

        if (entry.getY() > mYMax) {
            mYMax = entry.getY();
        }
    }

    @Override
    public int getEntryCount() {
        return mValues.size();
    }

    /**
     * Returns the array of entries that this DataSet represents.
     */
    public List<T> getValues() {
        return mValues;
    }

    /**
     * Sets the array of entries that this DataSet represents, and calls notifyDataSetChanged().
     */
    public void setValues(@NonNull List<T> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    /**
     * Returns an exact copy of the DataSet this method is used on.
     */
    public abstract DataSet<T> copy();

    @NonNull
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toSimpleString());
        for (int i = 0; i < mValues.size(); i++) {
            buffer.append(mValues.get(i).toString()).append(" ");
        }
        return buffer.toString();
    }

    /**
     * Returns a simple string representation of the DataSet with the type and the number of Entries.
     */
    @NonNull
    public String toSimpleString() {
        return "DataSet, label: " + (getLabel() == null ? "" : getLabel()) + ", entries: " + mValues.size() + "\n";
    }

    @Override
    public float getYMin() {
        return mYMin;
    }

    @Override
    public float getYMax() {
        return mYMax;
    }

    @Override
    public float getXMin() {
        return mXMin;
    }

    @Override
    public float getXMax() {
        return mXMax;
    }

    @Override
    public void addEntryOrdered(@Nullable T entry) {
        if (entry == null) {
            return;
        }

        calcMinMax(entry);

        if (mValues.size() > 0 && mValues.get(mValues.size() - 1).getX() > entry.getX()) {
            int closestIndex = getEntryIndex(entry.getX(), entry.getY(), Rounding.UP);
            mValues.add(closestIndex, entry);
        } else {
            mValues.add(entry);
        }
    }

    @Override
    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean addEntry(@Nullable T entry) {
        if (entry == null) {
            return false;
        }

        List<T> values = mValues;

        calcMinMax(entry);

        // Add the entry
        return values.add(entry);
    }

    @Override
    public boolean removeEntry(@Nullable T entry) {
        if (entry == null) {
            return false;
        }

        // Remove the entry
        boolean removed = mValues.remove(entry);
        if (removed) {
            calcMinMax();
        }

        return removed;
    }

    @Override
    public int getEntryIndex(Entry entry) {
        return mValues.indexOf(entry);
    }

    @Nullable
    @Override
    public T getEntryForXValue(float xValue, float closestToY, Rounding rounding) {
        int index = getEntryIndex(xValue, closestToY, rounding);
        if (index > -1) {
            return mValues.get(index);
        }

        return null;
    }

    @Override
    public T getEntryForXValue(float xValue, float closestToY) {
        return getEntryForXValue(xValue, closestToY, Rounding.CLOSEST);
    }

    @Override
    public T getEntryForIndex(int index) {
        return mValues.get(index);
    }

    @Override
    public int getEntryIndex(float xValue, float closestToY, Rounding rounding) {
        if (mValues.isEmpty()) {
            return -1;
        }

        int low = 0;
        int high = mValues.size() - 1;
        int closest = high;

        while (low < high) {
            int m = (low + high) / 2;

            final float d1 = mValues.get(m).getX() - xValue;
            final float d2 = mValues.get(m + 1).getX() - xValue;
            final float ad1 = Math.abs(d1);
            final float ad2 = Math.abs(d2);

            if (ad2 < ad1) {
                // [m + 1] is closer to xValue
                // Search in an higher place
                low = m + 1;
            } else if (ad1 < ad2) {
                // [m] is closer to xValue
                // Search in a lower place
                high = m;
            } else {
                // We have multiple sequential x-value with same distance
                if (d1 >= 0f) {
                    // Search in a lower place
                    high = m;
                } else if (d1 < 0f) {
                    // Search in an higher place
                    low = m + 1;
                }
            }

            closest = high;
        }

        if (closest != -1) {
            float closestXValue = mValues.get(closest).getX();
            if (rounding == Rounding.UP) {
                // If rounding up, and found x-value is lower than specified x, and we can go upper...
                if (closestXValue < xValue && closest < mValues.size() - 1) {
                    ++closest;
                }
            } else if (rounding == Rounding.DOWN) {
                // If rounding down, and found x-value is upper than specified x, and we can go lower...
                if (closestXValue > xValue && closest > 0) {
                    --closest;
                }
            }

            // Search by closest to y-value
            if (!Float.isNaN(closestToY)) {
                while (closest > 0 && mValues.get(closest - 1).getX() == closestXValue) {
                    closest -= 1;
                }

                float closestYValue = mValues.get(closest).getY();
                int closestYIndex = closest;

                while (true) {
                    closest += 1;
                    if (closest >= mValues.size()) {
                        break;
                    }

                    final Entry value = mValues.get(closest);
                    if (value.getX() != closestXValue) {
                        break;
                    }

                    if (Math.abs(value.getY() - closestToY) < Math.abs(closestYValue - closestToY)) {
                        closestYValue = closestToY;
                        closestYIndex = closest;
                    }
                }

                closest = closestYIndex;
            }
        }

        return closest;
    }

    @NonNull
    @Override
    public List<T> getEntriesForXValue(float xValue) {
        List<T> entries = new ArrayList<>();

        int low = 0;
        int high = mValues.size() - 1;

        while (low <= high) {
            int m = (high + low) / 2;
            T entry = mValues.get(m);

            // If we have a match
            if (xValue == entry.getX()) {
                while (m > 0 && mValues.get(m - 1).getX() == xValue) {
                    m--;
                }

                high = mValues.size();

                // Loop over all "equal" entries
                for (; m < high; m++) {
                    entry = mValues.get(m);
                    if (entry.getX() == xValue) {
                        entries.add(entry);
                    } else {
                        break;
                    }
                }

                break;
            } else {
                if (xValue > entry.getX()) {
                    low = m + 1;
                } else {
                    high = m - 1;
                }
            }
        }

        return entries;
    }

    /**
     * Determines how to round DataSet index values fo
     * {@link DataSet#getEntryIndex(float, float, Rounding)} DataSet.getEntryIndex()} when an exact
     * x-index is not found.
     */
    public enum Rounding {
        UP,
        DOWN,
        CLOSEST,
    }
}
