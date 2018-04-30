package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MPPointDTest {
	@Test
	fun getInstance() {
		val point = MPPointD.getInstance(1.0, 2.0)
		assertThat(point.x).isEqualTo(1.0)
		assertThat(point.y).isEqualTo(2.0)

		MPPointD.recycleInstance(point)
		assertThat(point.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}

	@Test
	fun instantiate() {
		val point = MPPointD.getInstance(1.0, 2.0)
		val instance = point.instantiate() as MPPointD
		assertThat(instance.x).isEqualTo(0.0)
		assertThat(instance.y).isEqualTo(0.0)

		MPPointD.recycleInstance(point)
		assertThat(point.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}

	@Test
	fun recycleInstances() {
		val points = listOf(
			MPPointD.getInstance(0.0, 1.0),
			MPPointD.getInstance(2.0, 3.0),
			MPPointD.getInstance(4.0, 5.0)
		)
		points.forEach {
			assertThat(it.currentOwnerId).isEqualTo(ObjectPool.Poolable.NO_OWNER)
		}

		MPPointD.recycleInstances(points)
		points.forEach {
			assertThat(it.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
		}
	}

	@Test
	fun testToString() {
		val point = MPPointD.getInstance(1.0, 2.0)
		assertThat(point.toString()).isEqualTo("MPPointD, x: 1.0, y: 2.0")

		MPPointD.recycleInstance(point)
		assertThat(point.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}
}
