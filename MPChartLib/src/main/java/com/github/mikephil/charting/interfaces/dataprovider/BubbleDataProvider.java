package com.github.mikephil.charting.interfaces.dataprovider;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    BubbleData getBubbleData();
}
