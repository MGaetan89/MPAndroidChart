package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RadarDataTest : ChartDataTest<RadarEntry, IRadarDataSet, RadarData>() {
	override val chartType = "Radar"

	@Before
	fun before() {
		this.data = RadarData()
		this.dataSets = mutableListOf(
			RadarDataSet(mutableListOf(), "$chartType 1"),
			RadarDataSet(mutableListOf(RadarEntry(1f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			RadarDataSet(mutableListOf(RadarEntry(2f), RadarEntry(3f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = RadarEntry(4f)
	}

	@Test
	override fun calcMinMaxY() {
		this.data.calcMinMaxY(0f, 3f)

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSets.forEach(this.data::addDataSet)
		this.data.calcMinMaxY(0f, 3f)

		assertThat(this.data.yMax).isEqualTo(3f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)
	}

	@Test
	override fun addDataSet() {
		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.yMax).isEqualTo(3f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")
	}

	@Test
	override fun addDataSet_axisLeft() {
		this.data.addDataSet(this.dataSets[2])

		assertThat(this.data.yMax).isEqualTo(3f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[2])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 3")
	}

	@Test
	override fun addDataSet_axisRight() {
		this.data.addDataSet(this.dataSets[1])

		assertThat(this.data.yMax).isEqualTo(1f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

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

		assertThat(this.data.yMax).isEqualTo(3f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
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

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.addEntry(this.entry, 1)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(4f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.addEntry(this.entry, 4)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(4f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.removeEntry(null, 4)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(4f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.removeEntry(null, 0)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(4f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		assertThat(this.data.removeEntry(0f, 4)).isFalse()
		assertThat(this.data.removeEntry(1f, 1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)
	}

	@Test
	fun constructorList() {
		this.data = RadarData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = RadarData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = RadarData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun setLabels() {
		assertThat(this.data.labels).isEmpty()

		this.data.setLabels("A", "B", "C")
		assertThat(this.data.labels).containsExactly("A", "B", "C")

		this.data.setLabels()
		assertThat(this.data.labels).isEmpty()

		this.data.labels = listOf("D", "E", "F")
		assertThat(this.data.labels).containsExactly("D", "E", "F")

		this.data.labels = emptyList()
		assertThat(this.data.labels).isEmpty()
	}

	@Test
	override fun getEntryForHighlight() {
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 1)))
			.isEqualTo(this.dataSets[1].getEntryForIndex(0))
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 1)))
			.isEqualTo(this.dataSets[1].getEntryForIndex(1))
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 1)))
			.isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 2)))
			.isEqualTo(this.dataSets[2].getEntryForIndex(0))
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 2)))
			.isEqualTo(this.dataSets[2].getEntryForIndex(1))
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 2)))
			.isEqualTo(this.dataSets[2].getEntryForIndex(2))
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 2)))
			.isNull()
	}
}
