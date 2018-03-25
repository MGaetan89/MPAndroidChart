package com.github.mikephil.charting.interfaces.dataprovider;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    ScatterData getScatterData();
}
