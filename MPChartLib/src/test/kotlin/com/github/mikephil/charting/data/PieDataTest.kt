package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PieDataTest : ChartDataTest<PieEntry, IPieDataSet, PieData>() {
	override val chartType = "Pie"

	@Before
	fun before() {
		this.data = PieData()
		this.dataSets = mutableListOf(
			PieDataSet(mutableListOf(), "$chartType 1"),
			PieDataSet(mutableListOf(PieEntry(1f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			PieDataSet(mutableListOf(PieEntry(2f), PieEntry(3f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = PieEntry(4f)
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
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)
	}

	@Test
	override fun getDataSetByLabel() {
		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", true)).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", false))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartType 1", true))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", true))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartType 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", true)).isNull()
	}

	@Test
	override fun getDataSetByIndex() {
		assertThat(this.data.getDataSetByIndex(-1)).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isNull()
		assertThat(this.data.getDataSetByIndex(1)).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getDataSetByIndex(-1)).isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByIndex(0)).isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByIndex(1)).isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByIndex(2)).isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByIndex(3)).isEqualTo(this.dataSets[0])
	}

	@Test
	override fun addDataSet() {
		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.yMax).isEqualTo(3f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

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
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

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
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

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
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(3f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(2)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[0], this.dataSets[2])

		assertThat(this.data.removeDataSet(2)).isTrue()

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[2])

		assertThat(this.data.removeDataSet(1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(0)
		assertThat(this.data.dataSets).isEmpty()
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
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.addEntry(this.entry, 4)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.removeEntry(null, 4)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		this.data.removeEntry(null, 0)

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(0f)
		assertThat(this.data.xMin).isEqualTo(0f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)

		assertThat(this.data.removeEntry(0f, 4)).isTrue()
		assertThat(this.data.removeEntry(1f, 1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(4f)
		assertThat(this.data.yMin).isEqualTo(1f)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(4f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(2f)
		assertThat(this.data.mRightAxisMax).isEqualTo(1f)
		assertThat(this.data.mRightAxisMin).isEqualTo(1f)
	}

	@Test
	override fun getDataSetForEntry() {
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isNull()

		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isEqualTo(this.dataSets[0])

		this.data.removeEntry(this.entry, 0)
		this.data.addEntry(this.entry, 1)
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isEqualTo(this.dataSets[0])
	}

	@Test
	fun constructorDataSet() {
		this.data = PieData(this.dataSets[1])

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSet).isEqualTo(this.dataSets[1])
	}

	@Test
	fun emptyConstructor() {
		this.data = PieData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun setDataSet() {
		assertThat(this.data.dataSet).isNull()

		this.data.dataSet = this.dataSets[0]
		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSet).isEqualTo(this.dataSets[0])

		this.data.dataSet = this.dataSets[1]
		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSet).isEqualTo(this.dataSets[1])

		this.data.dataSet = this.dataSets[2]
		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSet).isEqualTo(this.dataSets[2])

		this.data.dataSet = null
		assertThat(this.data.dataSetCount).isEqualTo(0)
		assertThat(this.data.dataSet as Any?).isNull()
	}

	@Test
	override fun getEntryForHighlight() {
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 1))).isNull()

		this.data.dataSet = this.dataSets[1]
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0)))
			.isEqualTo(this.dataSets[1].getEntryForIndex(0))
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 0)))
			.isEqualTo(this.dataSets[1].getEntryForIndex(1))
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 0))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 1)))
			.isEqualTo(this.dataSets[1].getEntryForIndex(0))
	}

	@Test
	fun getYValueSum() {
		assertThat(this.data.yValueSum).isEqualTo(0f)

		this.data.dataSet = this.dataSets[0]
		assertThat(this.data.yValueSum).isEqualTo(0f)

		this.data.dataSet = this.dataSets[1]
		assertThat(this.data.yValueSum).isEqualTo(1f)

		this.data.dataSet = this.dataSets[2]
		assertThat(this.data.yValueSum).isEqualTo(5f)
	}
}
