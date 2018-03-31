package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LineDataTest : ChartDataTest<Entry, ILineDataSet, LineData>() {
	override val chartType = "Line"

	@Before
	fun before() {
		this.data = LineData()
		this.dataSets = mutableListOf(
			LineDataSet(mutableListOf(), "$chartType 1"),
			LineDataSet(mutableListOf(Entry(1f, 2f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			LineDataSet(mutableListOf(Entry(3f, 4f), Entry(5f, 6f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = Entry(7f, 8f)
	}

	@Test
	fun constructorList() {
		this.data = LineData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = LineData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = LineData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}
}
