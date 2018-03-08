package com.github.mikephil.charting.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

@Suppress("ReplaceCallWithComparison")
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
	}

	@Test
	fun getInstance() {
		val size = FSize.getInstance(1f, 2f)
		assertThat(size.height).isEqualTo(2f)
		assertThat(size.width).isEqualTo(1f)
	}
}
