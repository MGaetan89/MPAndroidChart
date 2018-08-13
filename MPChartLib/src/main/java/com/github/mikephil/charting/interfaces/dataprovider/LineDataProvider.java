package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import androidx.annotation.Nullable;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {
    @Nullable
    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency axis);
}
