package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.data.BarData;

import androidx.annotation.Nullable;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    BarData getBarData();

    boolean isDrawBarShadowEnabled();

    boolean isDrawValueAboveBarEnabled();

    boolean isHighlightFullBarEnabled();
}
