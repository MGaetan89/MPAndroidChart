package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CandleDataTest : ChartDataTest<CandleEntry, ICandleDataSet, CandleData>() {
	override val chartType = "Candle"

	@Before
	fun before() {
		this.data = CandleData()
		this.dataSets = mutableListOf(
			CandleDataSet(mutableListOf(), "$chartType 1"),
			CandleDataSet(mutableListOf(CandleEntry(1f, 2f, 3f, 4f, 5f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			CandleDataSet(
				mutableListOf(
					CandleEntry(6f, 7f, 8f, 9f, 10f),
					CandleEntry(11f, 12f, 13f, 14f, 15f)
				),
				"$chartType 3"
			).also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = CandleEntry(16f, 17f, 18f, 19f, 20f)
	}

	@Test
	override fun addDataSet() {
		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.yMax).isEqualTo(12f)
		assertThat(this.data.yMin).isEqualTo(3f)
		assertThat(this.data.xMax).isEqualTo(11f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(12f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(8f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(3f)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")
	}

	@Test
	override fun addDataSet_axisLeft() {
		this.data.addDataSet(this.dataSets[2])

		assertThat(this.data.yMax).isEqualTo(12f)
		assertThat(this.data.yMin).isEqualTo(8f)
		assertThat(this.data.xMax).isEqualTo(11f)
		assertThat(this.data.xMin).isEqualTo(6f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(12f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(8f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[2])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 3")
	}

	@Test
	override fun addDataSet_axisRight() {
		this.data.addDataSet(this.dataSets[1])

		assertThat(this.data.yMax).isEqualTo(2f)
		assertThat(this.data.yMin).isEqualTo(3f)
		assertThat(this.data.xMax).isEqualTo(1f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(3f)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[1])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 2")
	}

	@Test
	override fun removeDataSet() {
		assertThat(this.data.removeDataSet(null)).isFalse()
		assertThat(this.data.removeDataSet(this.dataSets[1])).isFalse()

		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.removeDataSet(this.dataSets[1])).isTrue()

		assertThat(this.data.yMax).isEqualTo(12f)
		assertThat(this.data.yMin).isEqualTo(8f)
		assertThat(this.data.xMax).isEqualTo(11f)
		assertThat(this.data.xMin).isEqualTo(6f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(12f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(8f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(2)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[0], this.dataSets[2])

		assertThat(this.data.removeDataSet(2)).isFalse()
		assertThat(this.data.removeDataSet(1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[0])
	}

	@Test
	override fun addEntry() {
		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)

		assertThat(this.data.yMax).isEqualTo(17.5f)
		assertThat(this.data.yMin).isEqualTo(3f)
		assertThat(this.data.xMax).isEqualTo(16f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(17.5f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(8f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(3f)

		this.data.addEntry(this.entry, 1)

		assertThat(this.data.yMax).isEqualTo(17.5f)
		assertThat(this.data.yMin).isEqualTo(3f)
		assertThat(this.data.xMax).isEqualTo(16f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(17.5f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(8f)
		assertThat(this.data.mRightAxisMax).isEqualTo(17.5f)
		assertThat(this.data.mRightAxisMin).isEqualTo(3f)
	}

	@Test
	fun constructorList() {
		this.data = CandleData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = CandleData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = CandleData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}
}
