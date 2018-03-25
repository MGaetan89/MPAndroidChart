package com.github.mikephil.charting.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

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
    public void setDataSet(IPieDataSet dataSet) {
        mDataSets.clear();
        mDataSets.add(dataSet);
        notifyDataChanged();
    }

    /**
     * Returns the DataSet this PieData object represents. A PieData object can only contain one
     * DataSet.
     */
    public IPieDataSet getDataSet() {
        return mDataSets.get(0);
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
        return index == 0 ? getDataSet() : null;
    }

    @Nullable
    @Override
    public IPieDataSet getDataSetByLabel(@NonNull String label, boolean ignoreCase) {
        IPieDataSet firstDataSet = mDataSets.get(0);
        String firstLabel = firstDataSet.getLabel();
        boolean labelMatch = ignoreCase ? label.equalsIgnoreCase(firstLabel) : label.equals(firstLabel);
        return labelMatch ? firstDataSet : null;
    }

    @Nullable
    @Override
    public Entry getEntryForHighlight(@NonNull Highlight highlight) {
        return getDataSet().getEntryForIndex((int) highlight.getX());
    }

    /**
     * Returns the sum of all values in this PieData object.
     */
    public float getYValueSum() {
        float sum = 0;
        for (int i = 0; i < getDataSet().getEntryCount(); i++) {
            sum += getDataSet().getEntryForIndex(i).getY();
        }

        return sum;
    }
}
