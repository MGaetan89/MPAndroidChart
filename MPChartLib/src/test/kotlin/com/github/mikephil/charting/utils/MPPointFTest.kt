package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MPPointFTest {
	@Test
	fun getInstance_copy() {
		val point = MPPointF.getInstance(MPPointF(1f, 2f))
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)
	}

	@Test
	fun getInstance_empty() {
		val point = MPPointF.getInstance()
		assertThat(point.getX()).isEqualTo(0f)
		assertThat(point.getY()).isEqualTo(0f)
	}

	@Test
	fun getInstance_x_y() {
		val point = MPPointF.getInstance(1f, 2f)
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)
	}

	@Test
	fun instantiate() {
		val point = MPPointF(1f, 2f)
		val instance = point.instantiate() as MPPointF
		assertThat(instance.getX()).isEqualTo(0f)
		assertThat(instance.getY()).isEqualTo(0f)
	}

	@Test
	fun point() {
		val point = MPPointF(1f, 2f)
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)
	}

	@Test
	fun point_empty() {
		val point = MPPointF()
		assertThat(point.getX()).isEqualTo(0f)
		assertThat(point.getY()).isEqualTo(0f)
	}
}
