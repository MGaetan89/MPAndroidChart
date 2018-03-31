package com.github.mikephil.charting.data

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet

abstract class BarLineScatterCandleBubbleDataTest<E : Entry, D : IBarLineScatterCandleBubbleDataSet<E>, T : BarLineScatterCandleBubbleData<D>> :
	ChartDataTest<E, D, T>()
