package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.utils.Transformer;

import androidx.annotation.Nullable;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    Transformer getTransformer(AxisDependency axis);

    boolean isInverted(AxisDependency axis);

    float getLowestVisibleX();

    float getHighestVisibleX();

    @Nullable
    BarLineScatterCandleBubbleData getData();
}
