package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MPPointFTest {
	@Test
	fun equals() {
		assertThat(MPPointF(0f, 0f)).isNotEqualTo(null)
		assertThat(MPPointF(0f, 0f)).isNotEqualTo(42)
		assertThat(MPPointF(0f, 0f)).isEqualTo(MPPointF(0f, 0f))
		assertThat(MPPointF(1f, 0f)).isNotEqualTo(MPPointF(0f, 0f))
		assertThat(MPPointF(1f, 0f)).isEqualTo(MPPointF(1f, 0f))
		assertThat(MPPointF(0f, 1f)).isNotEqualTo(MPPointF(0f, 0f))
		assertThat(MPPointF(0f, 1f)).isEqualTo(MPPointF(0f, 1f))
		assertThat(MPPointF(1f, 1f)).isNotEqualTo(MPPointF(0f, 0f))
		assertThat(MPPointF(1f, 1f)).isEqualTo(MPPointF(1f, 1f))
	}

	@Test
	fun getInstance_copy() {
		val point = MPPointF.getInstance(MPPointF(1f, 2f))
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun getInstance_empty() {
		val point = MPPointF.getInstance()
		assertThat(point.getX()).isEqualTo(0f)
		assertThat(point.getY()).isEqualTo(0f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun getInstance_x_y() {
		val point = MPPointF.getInstance(1f, 2f)
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun instantiate() {
		val point = MPPointF(1f, 2f)
		val instance = point.instantiate() as MPPointF
		assertThat(instance.getX()).isEqualTo(0f)
		assertThat(instance.getY()).isEqualTo(0f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun point() {
		val point = MPPointF(1f, 2f)
		assertThat(point.getX()).isEqualTo(1f)
		assertThat(point.getY()).isEqualTo(2f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun point_empty() {
		val point = MPPointF()
		assertThat(point.getX()).isEqualTo(0f)
		assertThat(point.getY()).isEqualTo(0f)

		MPPointF.recycleInstance(point)
	}

	@Test
	fun testToString() {
		assertThat(MPPointF(0f, 0f).toString()).isEqualTo("MPPointF, x: 0.0, y: 0.0")
		assertThat(MPPointF(1f, 0f).toString()).isEqualTo("MPPointF, x: 1.0, y: 0.0")
		assertThat(MPPointF(0f, 1f).toString()).isEqualTo("MPPointF, x: 0.0, y: 1.0")
		assertThat(MPPointF(1f, 1f).toString()).isEqualTo("MPPointF, x: 1.0, y: 1.0")
	}
}
