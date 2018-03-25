package com.github.mikephil.charting.interfaces.dataprovider;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    CandleData getCandleData();
}
