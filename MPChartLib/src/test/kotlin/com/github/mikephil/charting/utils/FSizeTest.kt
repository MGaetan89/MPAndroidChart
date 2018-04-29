package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

@Suppress("ReplaceCallWithBinaryOperator")
class FSizeTest {
	@Test
	fun fSize() {
		val size = FSize(1f, 2f)
		assertThat(size.height).isEqualTo(2f)
		assertThat(size.width).isEqualTo(1f)
		assertThat(size.equals(null)).isFalse()
		assertThat(size.equals(42)).isFalse()
		assertThat(size == size).isTrue()
		assertThat(size == FSize(0f, 0f)).isFalse()
		assertThat(size == FSize(1f, 1f)).isFalse()
		assertThat(size == FSize(1f, 2f)).isTrue()
		assertThat(size == FSize(2f, 1f)).isFalse()
		assertThat(size == FSize(2f, 2f)).isFalse()
		assertThat(size.hashCode()).isEqualTo(2139095040)
		assertThat(size.toString()).isEqualTo("1.0x2.0")

		val other = size.instantiate() as FSize
		assertThat(other.height).isEqualTo(0f)
		assertThat(other.width).isEqualTo(0f)

		FSize.recycleInstance(size)
		assertThat(size.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}

	@Test
	fun fSize_empty() {
		val size = FSize()
		assertThat(size.height).isEqualTo(0f)
		assertThat(size.width).isEqualTo(0f)
		assertThat(size.equals(null)).isFalse()
		assertThat(size.equals(42)).isFalse()
		assertThat(size == size).isTrue()
		assertThat(size == FSize(0f, 0f)).isTrue()
		assertThat(size == FSize(0f, 1f)).isFalse()
		assertThat(size == FSize(1f, 2f)).isFalse()
		assertThat(size == FSize(2f, 1f)).isFalse()
		assertThat(size == FSize(2f, 2f)).isFalse()
		assertThat(size.hashCode()).isEqualTo(0)
		assertThat(size.toString()).isEqualTo("0.0x0.0")

		val other = size.instantiate() as FSize
		assertThat(other.height).isEqualTo(0f)
		assertThat(other.width).isEqualTo(0f)

		FSize.recycleInstance(size)
		assertThat(size.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}

	@Test
	fun getInstance() {
		val size = FSize.getInstance(1f, 2f)
		assertThat(size.height).isEqualTo(2f)
		assertThat(size.width).isEqualTo(1f)

		FSize.recycleInstance(size)
		assertThat(size.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
	}

	@Test
	fun recycleInstances() {
		val sizes = listOf(
			FSize.getInstance(0f, 1f),
			FSize.getInstance(2f, 3f),
			FSize.getInstance(4f, 5f)
		)
		sizes.forEach {
			assertThat(it.currentOwnerId).isEqualTo(ObjectPool.Poolable.NO_OWNER)
		}

		FSize.recycleInstances(sizes)
		sizes.forEach {
			assertThat(it.currentOwnerId).isNotEqualTo(ObjectPool.Poolable.NO_OWNER)
		}
	}
}
