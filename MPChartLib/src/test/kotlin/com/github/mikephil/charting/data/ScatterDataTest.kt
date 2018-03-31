package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ScatterDataTest : ChartDataTest<Entry, IScatterDataSet, ScatterData>() {
	override val chartType = "Scatter"

	@Before
	fun before() {
		this.data = ScatterData()
		this.dataSets = mutableListOf(
			ScatterDataSet(mutableListOf(), "$chartType 1"),
			ScatterDataSet(mutableListOf(Entry(1f, 2f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			ScatterDataSet(mutableListOf(Entry(3f, 4f), Entry(5f, 6f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = Entry(7f, 8f)
	}

	@Test
	fun constructorList() {
		this.data = ScatterData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = ScatterData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = ScatterData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun getGreatestShapeSize() {
		assertThat(this.data.greatestShapeSize).isEqualTo(0f)

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.greatestShapeSize).isEqualTo(15f)
	}
}
