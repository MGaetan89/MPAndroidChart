package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.CombinedData;

import androidx.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {
    @Nullable
    CombinedData getCombinedData();
}
