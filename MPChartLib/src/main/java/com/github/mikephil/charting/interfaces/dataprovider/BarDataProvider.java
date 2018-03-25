package com.github.mikephil.charting.interfaces.dataprovider;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}
