package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class ChartDataTest<T : ChartData<*>> {
	protected lateinit var data: T

	@Test
	fun getDataSetIndexByLabel() {
		val data = BarData()
		val dataSets = listOf(
			BarDataSet(emptyList(), ""),
			BarDataSet(emptyList(), "q1 2018"),
			BarDataSet(emptyList(), "Q1 2018"),
			BarDataSet(emptyList(), "q2 2018"),
			BarDataSet(emptyList(), "Q2 2018")
		)

		assertThat(data.getDataSetIndexByLabel(emptyList(), "", false)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "", true)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "q1 2018", false)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "q1 2018", true)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "Q2 2018", false)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "Q2 2018", true)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "Q3 2018", false)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(emptyList(), "Q3 2018", true)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(dataSets, "", false)).isEqualTo(0)
		assertThat(data.getDataSetIndexByLabel(dataSets, "", true)).isEqualTo(0)
		assertThat(data.getDataSetIndexByLabel(dataSets, "q1 2018", false)).isEqualTo(1)
		assertThat(data.getDataSetIndexByLabel(dataSets, "q1 2018", true)).isEqualTo(1)
		assertThat(data.getDataSetIndexByLabel(dataSets, "Q2 2018", false)).isEqualTo(4)
		assertThat(data.getDataSetIndexByLabel(dataSets, "Q2 2018", true)).isEqualTo(3)
		assertThat(data.getDataSetIndexByLabel(dataSets, "Q3 2018", false)).isEqualTo(-1)
		assertThat(data.getDataSetIndexByLabel(dataSets, "Q3 2018", true)).isEqualTo(-1)
	}

	@Test
	fun getFirstLeft() {
		val data = BarData()
		val dataSets = listOf(
			BarDataSet(emptyList(), ""),
			BarDataSet(emptyList(), "q1 2018").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(emptyList(), "Q1 2018").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			},
			BarDataSet(emptyList(), "q2 2018").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(emptyList(), "Q2 2018").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)

		assertThat(data.getFirstLeft(emptyList())).isNull()
		assertThat(data.getFirstLeft(dataSets)).isEqualTo(dataSets[0])
	}

	@Test
	fun getFirstRight() {
		val data = BarData()
		val dataSets = listOf(
			BarDataSet(emptyList(), ""),
			BarDataSet(emptyList(), "q1 2018").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(emptyList(), "Q1 2018").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			},
			BarDataSet(emptyList(), "q2 2018").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(emptyList(), "Q2 2018").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)

		assertThat(data.getFirstRight(emptyList())).isNull()
		assertThat(data.getFirstRight(dataSets)).isEqualTo(dataSets[1])
	}
}
