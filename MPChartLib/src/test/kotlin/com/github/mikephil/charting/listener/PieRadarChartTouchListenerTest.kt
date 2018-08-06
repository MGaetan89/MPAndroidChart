package com.github.mikephil.charting.listener

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.data.PieData
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class PieRadarChartTouchListenerTest : ChartTouchListenerTest<PieRadarChartBase<*>, PieRadarChartTouchListener>() {
    @Before
    fun before() {
        this.chart = PieChart(mock()).apply {
            this.data = PieData()
        }
        this.listener = PieRadarChartTouchListener(this.chart)
    }
}
