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
			MPPointF.getInstance(0f, 1f),
			MPPointF.getInstance(2f, 3f),
			MPPointF.getInstance(4f, 5f)
		)
		points.forEach {
			assertThat(it.currentOwnerId).isEqualTo(ObjectPool.Poolable.NO_OWNER)
		}

		MPPointF.recycleInstances(points)
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
