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
	override fun addEntry() {
		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 1)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 4)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 4)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 0)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.removeEntry(0f, 4)).isFalse()
		assertThat(this.data.removeEntry(1f, 1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(4f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(3f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(8f)
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
