package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

class ObjectPoolTest {
	companion object {
		class TestPoolable(var foo: Int, var bar: Int) : ObjectPool.Poolable() {
			companion object {
				val pool = ObjectPool.create(4, TestPoolable(0, 0))
				val pool2 = ObjectPool.create(4, TestPoolable(0, 0))

				fun getInstance(foo: Int, bar: Int) = this.pool.get().apply {
					this.foo = foo
					this.bar = bar
				}

				fun recycleInstance(instance: TestPoolable) = this.pool.recycle(instance)

				fun recycleInstance2(instance: TestPoolable) = this.pool2.recycle(instance)

				fun recycleInstances(instances: List<TestPoolable>) = this.pool.recycle(instances)
			}

			override fun instantiate() = TestPoolable(0, 0)
		}
	}

	@Test
	fun objectPool() {
		val poolId = TestPoolable.pool.poolId
		val poolId2 = TestPoolable.pool2.poolId
		assertThat(poolId).isNotEqualTo(poolId2)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(4)

		var testPoolable = TestPoolable.getInstance(6, 7)
		assertThat(testPoolable.foo).isEqualTo(6)
		assertThat(testPoolable.bar).isEqualTo(7)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(3)

		TestPoolable.recycleInstance(testPoolable)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(4)

		testPoolable = TestPoolable.getInstance(20, 30)
		assertThat(testPoolable.foo).isEqualTo(20)
		assertThat(testPoolable.bar).isEqualTo(30)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(3)

		TestPoolable.recycleInstance(testPoolable)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(4)

		val testPoolables = mutableListOf<TestPoolable>()
		testPoolables.add(TestPoolable.getInstance(12, 24))
		testPoolables.add(TestPoolable.getInstance(1, 2))
		testPoolables.add(TestPoolable.getInstance(3, 5))
		testPoolables.add(TestPoolable.getInstance(6, 8))

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(0)

		TestPoolable.recycleInstances(testPoolables)
		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(4)

		testPoolables.clear()

		testPoolables.add(TestPoolable.getInstance(12, 24))
		testPoolables.add(TestPoolable.getInstance(1, 2))
		testPoolables.add(TestPoolable.getInstance(3, 5))
		testPoolables.add(TestPoolable.getInstance(6, 8))
		testPoolables.add(TestPoolable.getInstance(8, 9))
		assertThat(testPoolables[0].foo).isEqualTo(12)
		assertThat(testPoolables[0].bar).isEqualTo(24)
		assertThat(testPoolables[1].foo).isEqualTo(1)
		assertThat(testPoolables[1].bar).isEqualTo(2)
		assertThat(testPoolables[2].foo).isEqualTo(3)
		assertThat(testPoolables[2].bar).isEqualTo(5)
		assertThat(testPoolables[3].foo).isEqualTo(6)
		assertThat(testPoolables[3].bar).isEqualTo(8)
		assertThat(testPoolables[4].foo).isEqualTo(8)
		assertThat(testPoolables[4].bar).isEqualTo(9)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(4)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(3)

		TestPoolable.recycleInstances(testPoolables)
		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(8)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(8)

		testPoolables.clear()

		testPoolables.add(TestPoolable.getInstance(0, 0))
		testPoolables.add(TestPoolable.getInstance(6, 8))
		testPoolables.add(TestPoolable.getInstance(1, 2))
		testPoolables.add(TestPoolable.getInstance(3, 5))
		testPoolables.add(TestPoolable.getInstance(8, 9))
		testPoolables.add(TestPoolable.getInstance(12, 24))
		testPoolables.add(TestPoolable.getInstance(12, 24))
		testPoolables.add(TestPoolable.getInstance(12, 24))
		testPoolables.add(TestPoolable.getInstance(6, 8))
		testPoolables.add(TestPoolable.getInstance(6, 8))
		assertThat(testPoolables[0].foo).isEqualTo(0)
		assertThat(testPoolables[0].bar).isEqualTo(0)
		assertThat(testPoolables[1].foo).isEqualTo(6)
		assertThat(testPoolables[1].bar).isEqualTo(8)
		assertThat(testPoolables[2].foo).isEqualTo(1)
		assertThat(testPoolables[2].bar).isEqualTo(2)
		assertThat(testPoolables[3].foo).isEqualTo(3)
		assertThat(testPoolables[3].bar).isEqualTo(5)
		assertThat(testPoolables[4].foo).isEqualTo(8)
		assertThat(testPoolables[4].bar).isEqualTo(9)
		assertThat(testPoolables[5].foo).isEqualTo(12)
		assertThat(testPoolables[5].bar).isEqualTo(24)
		assertThat(testPoolables[6].foo).isEqualTo(12)
		assertThat(testPoolables[6].bar).isEqualTo(24)
		assertThat(testPoolables[7].foo).isEqualTo(12)
		assertThat(testPoolables[7].bar).isEqualTo(24)
		assertThat(testPoolables[8].foo).isEqualTo(6)
		assertThat(testPoolables[8].bar).isEqualTo(8)
		assertThat(testPoolables[9].foo).isEqualTo(6)
		assertThat(testPoolables[9].bar).isEqualTo(8)

		for (poolable in testPoolables) {
			TestPoolable.recycleInstance(poolable)
		}

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(16)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(16)

		testPoolables.clear()

		testPoolable = TestPoolable.getInstance(9001, 9001)
		assertThat(testPoolable.foo).isEqualTo(9001)
		assertThat(testPoolable.bar).isEqualTo(9001)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(16)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(15)

		TestPoolable.recycleInstance(testPoolable)

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(16)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(16)

		try {
			TestPoolable.recycleInstance(testPoolable)
			fail("Should have failed")
		} catch (exception: IllegalArgumentException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("The object passed is already stored in this pool.")
		}

		try {
			TestPoolable.recycleInstance2(testPoolable)
			fail("Should have failed")
		} catch (exception: IllegalArgumentException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("The object to recycle already belongs to poolId '$poolId'. Object cannot belong to different pools instances simultaneously.")
		}

		TestPoolable.pool.replenishPercentage = -0.5f
		assertThat(TestPoolable.pool.replenishPercentage).isEqualTo(0f)

		TestPoolable.pool.replenishPercentage = 1.5f
		assertThat(TestPoolable.pool.replenishPercentage).isEqualTo(1f)

		TestPoolable.pool.replenishPercentage = 0.5f
		assertThat(TestPoolable.pool.replenishPercentage).isEqualTo(0.5f)

		for (i in 16 downTo 1) {
			testPoolables.add(TestPoolable.getInstance(0, 0))
		}

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(16)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(0)

		testPoolables.add(TestPoolable.getInstance(0, 0))

		assertThat(TestPoolable.pool.poolCapacity).isEqualTo(16)
		assertThat(TestPoolable.pool.poolCount).isEqualTo(7)
	}

	@Test
	fun objectPool_wrongCapacity() {
		try {
			ObjectPool.create(-5, TestPoolable(0, 0))
			fail("Should have failed")
		} catch (exception: IllegalArgumentException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("ObjectPool's capacity must be greater than 0, got '-5'.")
		}

		try {
			ObjectPool.create(0, TestPoolable(0, 0))
			fail("Should have failed")
		} catch (exception: IllegalArgumentException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("ObjectPool's capacity must be greater than 0, got '0'.")
		}
	}
}
