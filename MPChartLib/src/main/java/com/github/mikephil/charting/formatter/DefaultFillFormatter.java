package com.github.mikephil.charting.formatter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
            float max;
            float min;

            if (data.getYMax() > 0) {
                max = 0f;
            } else {
                max = dataProvider.getYChartMax();
            }

            if (data.getYMin() < 0) {
                min = 0f;
            } else {
                min = dataProvider.getYChartMin();
            }

            return dataSet.getYMin() >= 0 ? min : max;
        }
    }
}
