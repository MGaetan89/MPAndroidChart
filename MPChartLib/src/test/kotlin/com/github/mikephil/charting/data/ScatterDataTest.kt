package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ScatterDataTest : ChartDataTest<ScatterData>() {
	@Before
	fun before() {
		this.data = ScatterData()
	}

	@Test
	fun constructorList() {
		this.data = ScatterData(
			listOf(
				ScatterDataSet(emptyList(), "Bubble 1").also { it.scatterShapeSize = 20f },
				ScatterDataSet(emptyList(), "Bubble 2").also { it.scatterShapeSize = 15f },
				ScatterDataSet(emptyList(), "Bubble 3").also { it.scatterShapeSize = 10f }
			)
		)

		assertThat(this.data.greatestShapeSize).isEqualTo(20f)
	}

	@Test
	fun constructorVarargs() {
		this.data = ScatterData(
			ScatterDataSet(emptyList(), "Bubble 1").also { it.scatterShapeSize = 20f },
			ScatterDataSet(emptyList(), "Bubble 2").also { it.scatterShapeSize = 15f },
			ScatterDataSet(emptyList(), "Bubble 3").also { it.scatterShapeSize = 10f }
		)

		assertThat(this.data.greatestShapeSize).isEqualTo(20f)
	}

	@Test
	fun emptyConstructor() {
		this.data = ScatterData()

		assertThat(this.data.greatestShapeSize).isEqualTo(0f)
	}
}
