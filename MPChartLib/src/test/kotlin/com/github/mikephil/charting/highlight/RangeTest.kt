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
}
