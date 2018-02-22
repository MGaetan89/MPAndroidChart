package com.github.mikephil.charting.highlight

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RangeTest {
	@Test
	fun contains() {
		val range = Range(-5f, 5f)
		assertThat(range.contains(-6f)).isFalse()
		assertThat(range.contains(-5f)).isFalse()
		assertThat(range.contains(-4f)).isTrue()
		assertThat(range.contains(0f)).isTrue()
		assertThat(range.contains(4f)).isTrue()
		assertThat(range.contains(5f)).isTrue()
		assertThat(range.contains(6f)).isFalse()
	}

	@Test
	fun contains_emptyRange() {
		val range = Range(0f, 0f)
		assertThat(range.contains(-1f)).isFalse()
		assertThat(range.contains(0f)).isFalse()
		assertThat(range.contains(1f)).isFalse()
	}

	@Test
	fun isLarger() {
		val range = Range(-5f, 5f)
		assertThat(range.isLarger(-6f)).isFalse()
		assertThat(range.isLarger(-5f)).isFalse()
		assertThat(range.isLarger(-4f)).isFalse()
		assertThat(range.isLarger(0f)).isFalse()
		assertThat(range.isLarger(4f)).isFalse()
		assertThat(range.isLarger(5f)).isFalse()
		assertThat(range.isLarger(6f)).isTrue()
	}

	@Test
	fun isLarger_emptyRange() {
		val range = Range(0f, 0f)
		assertThat(range.isLarger(-1f)).isFalse()
		assertThat(range.isLarger(0f)).isFalse()
		assertThat(range.isLarger(1f)).isTrue()
	}

	@Test
	fun isSmaller() {
		val range = Range(-5f, 5f)
		assertThat(range.isSmaller(-6f)).isTrue()
		assertThat(range.isSmaller(-5f)).isFalse()
		assertThat(range.isSmaller(-4f)).isFalse()
		assertThat(range.isSmaller(0f)).isFalse()
		assertThat(range.isSmaller(4f)).isFalse()
		assertThat(range.isSmaller(5f)).isFalse()
		assertThat(range.isSmaller(6f)).isFalse()
	}

	@Test
	fun isSmaller_emptyRange() {
		val range = Range(0f, 0f)
		assertThat(range.isSmaller(-1f)).isTrue()
		assertThat(range.isSmaller(0f)).isFalse()
		assertThat(range.isSmaller(1f)).isFalse()
	}
}
