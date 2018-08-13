package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import androidx.annotation.NonNull;

/**
 * Default formatter that calculates the position of the filled line.
 *
 * @author Philipp Jahoda
 */
public class DefaultFillFormatter implements IFillFormatter {
    @Override
    public float getFillLinePosition(@NonNull ILineDataSet dataSet, @NonNull LineDataProvider dataProvider) {
        if (dataSet.getYMax() > 0 && dataSet.getYMin() < 0) {
            return 0f;
        } else {
            LineData data = dataProvider.getLineData();
            if (dataSet.getYMin() >= 0) {
                if (data.getYMin() < 0) {
                    return 0f;
                } else {
                    return dataProvider.getYChartMin();
                }
            } else {
                if (data.getYMax() > 0) {
                    return 0f;
                } else {
                    return dataProvider.getYChartMax();
                }
            }
        }
    }
}
