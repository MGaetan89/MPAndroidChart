package com.github.mikephil.charting.data;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A PieData object can only represent one DataSet. Unlike all other charts, the legend labels of
 * the PieChart are created from the x-values array, and not from the DataSet labels. Each PieData
 * object can only represent one PieDataSet (multiple PieDataSets inside a single PieChart are not
 * possible).
 *
 * @author Philipp Jahoda
 */
public class PieData extends ChartData<IPieDataSet> {
    public PieData() {
        super();
    }

    public PieData(IPieDataSet dataSet) {
        super(dataSet);
    }

    /**
     * Sets the PieDataSet this data object should represent.
     *
     * @param dataSet
     */
    public void setDataSet(@Nullable IPieDataSet dataSet) {
        mDataSets.clear();
        if (dataSet != null) {
            mDataSets.add(dataSet);
            notifyDataChanged();
        }
    }

    /**
     * Returns the DataSet this PieData object represents. A PieData object can only contain one
     * DataSet.
     */
    @Nullable
    public IPieDataSet getDataSet() {
        if (mDataSets.isEmpty()) {
            return null;
        } else {
            return mDataSets.get(0);
        }
    }

    /**
     * The PieData object can only have one DataSet. Use getDataSet() method instead.
     *
     * @param index
     * @return
     */
    @Nullable
    @Override
    public IPieDataSet getDataSetByIndex(int index) {
        return getDataSet();
    }

    @Nullable
    @Override
    public IPieDataSet getDataSetByLabel(@NonNull String label, boolean ignoreCase) {
        IPieDataSet dataSet = getDataSet();
        if (dataSet == null) {
            return null;
        }

        String firstLabel = dataSet.getLabel();
        boolean labelMatch = ignoreCase ? label.equalsIgnoreCase(firstLabel) : label.equals(firstLabel);
        return labelMatch ? dataSet : null;
    }

    @Nullable
    @Override
    public Entry getEntryForHighlight(@NonNull Highlight highlight) {
        IPieDataSet dataSet = getDataSet();
        if (dataSet == null) {
            return null;
        } else {
            return dataSet.getEntryForIndex((int) highlight.getX());
        }
    }

    /**
     * Returns the sum of all values in this PieData object.
     */
    public float getYValueSum() {
        IPieDataSet dataSet = getDataSet();
        if (dataSet == null) {
            return 0f;
        }

        float sum = 0f;
        for (int i = 0, count = dataSet.getEntryCount(); i < count; i++) {
            //noinspection ConstantConditions
            sum += dataSet.getEntryForIndex(i).getY();
        }

        return sum;
    }
}
