package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import org.junit.Before

class BarDataTest : BarLineScatterCandleBubbleDataTest<BarEntry, IBarDataSet, BarData>() {
	override val chartType = "Bar"

	@Before
	fun before() {
		this.data = BarData()
		this.dataSets = mutableListOf(
			BarDataSet(mutableListOf(), "$chartType 1"),
			BarDataSet(mutableListOf(BarEntry(1f, 2f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(mutableListOf(BarEntry(3f, 4f), BarEntry(5f, 6f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = BarEntry(7f, 8f)
	}
}
