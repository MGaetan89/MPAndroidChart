package com.github.mikephil.charting.data

import com.github.mikephil.charting.highlight.Highlight
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PieDataTest : ChartDataTest<PieData>() {
	@Before
	fun before() {
		this.data = PieData()
	}

	@Test
	fun constructorDataSet() {
		val dataSet = PieDataSet(listOf(PieEntry(1f), PieEntry(2f)), "Pie")

		this.data = PieData(dataSet)

		assertThat(this.data.dataSet).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByIndex(0)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByIndex(1)).isEqualTo(dataSet)

		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", true)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByLabel("Pie", false)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByLabel("Pie", true)).isEqualTo(dataSet)

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0)))
			.isEqualTo(dataSet.values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 0)))
			.isEqualTo(dataSet.values[1])
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 0))).isNull()

		assertThat(this.data.yValueSum).isEqualTo(3f)

		this.data.dataSet = null
		assertThat(this.data.dataSet as Any?).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isNull()
		assertThat(this.data.getDataSetByIndex(1)).isNull()

		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", true)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", true)).isNull()

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()

		assertThat(this.data.yValueSum).isEqualTo(0f)
	}

	@Test
	fun emptyConstructor() {
		this.data = PieData()

		assertThat(this.data.dataSet).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isNull()
		assertThat(this.data.getDataSetByIndex(1)).isNull()

		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", true)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", true)).isNull()

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()

		assertThat(this.data.yValueSum).isEqualTo(0f)

		val dataSet = PieDataSet(listOf(PieEntry(1f), PieEntry(2f)), "Pie")
		this.data.dataSet = dataSet
		assertThat(this.data.dataSet).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByIndex(0)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByIndex(1)).isEqualTo(dataSet)

		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", true)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByLabel("Pie", false)).isEqualTo(dataSet)
		assertThat(this.data.getDataSetByLabel("Pie", true)).isEqualTo(dataSet)

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0)))
			.isEqualTo(dataSet.values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 0)))
			.isEqualTo(dataSet.values[1])
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 0))).isNull()

		assertThat(this.data.yValueSum).isEqualTo(3f)

		this.data.dataSet = null
		assertThat(this.data.dataSet as Any?).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isNull()
		assertThat(this.data.getDataSetByIndex(1)).isNull()

		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("pie", true)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", false)).isNull()
		assertThat(this.data.getDataSetByLabel("Pie", true)).isNull()

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()

		assertThat(this.data.yValueSum).isEqualTo(0f)
	}
}
