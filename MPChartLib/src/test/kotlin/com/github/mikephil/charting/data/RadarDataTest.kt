package com.github.mikephil.charting.data

import com.github.mikephil.charting.highlight.Highlight
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RadarDataTest : ChartDataTest<RadarData>() {
	@Before
	fun before() {
		this.data = RadarData()
	}

	@Test
	fun constructorList() {
		val dataSets = listOf(
			RadarDataSet(emptyList(), "A"),
			RadarDataSet(listOf(RadarEntry(1f)), "B"),
			RadarDataSet(listOf(RadarEntry(2f), RadarEntry(3f)), "C")
		)

		this.data = RadarData(dataSets)

		assertThat(this.data.labels).isEmpty()

		this.data.labels = listOf("A", "B", "C")
		assertThat(this.data.labels).containsExactly("A", "B", "C")

		this.data.setLabels("D", "E", "F")
		assertThat(this.data.labels).containsExactly("D", "E", "F")

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 1)))
			.isEqualTo(dataSets[1].values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 1))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 2)))
			.isEqualTo(dataSets[2].values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 2)))
			.isEqualTo(dataSets[2].values[1])
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 1))).isNull()
	}

	@Test
	fun constructorVarargs() {
		val dataSets = listOf(
			RadarDataSet(emptyList(), "A"),
			RadarDataSet(listOf(RadarEntry(1f)), "B"),
			RadarDataSet(listOf(RadarEntry(2f), RadarEntry(3f)), "C")
		)

		this.data = RadarData(*dataSets.toTypedArray())

		assertThat(this.data.labels).isEmpty()

		this.data.labels = listOf("A", "B", "C")
		assertThat(this.data.labels).containsExactly("A", "B", "C")

		this.data.setLabels("D", "E", "F")
		assertThat(this.data.labels).containsExactly("D", "E", "F")

		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 1)))
			.isEqualTo(dataSets[1].values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 1))).isNull()
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 2)))
			.isEqualTo(dataSets[2].values[0])
		assertThat(this.data.getEntryForHighlight(Highlight(1f, 1f, 2)))
			.isEqualTo(dataSets[2].values[1])
		assertThat(this.data.getEntryForHighlight(Highlight(2f, 1f, 1))).isNull()
	}

	@Test
	fun emptyConstructor() {
		this.data = RadarData()

		assertThat(this.data.labels).isEmpty()

		this.data.labels = listOf("A", "B", "C")
		assertThat(this.data.labels).containsExactly("A", "B", "C")

		this.data.setLabels("D", "E", "F")
		assertThat(this.data.labels).containsExactly("D", "E", "F")

		assertThat(this.data.getEntryForHighlight(Highlight(1f, 2f, 0))).isNull()
	}
}
