package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LargeValueFormatterTest {
	@Test
	fun getFormattedValue_noAppendix_defaultSuffix() {
		val formatter = LargeValueFormatter()

		assertThat(formatter.decimalDigits).isEqualTo(0)

		assertThat(formatter.getFormattedValue(-1000000000000f, null, 0, null)).isEqualTo("-1t")
		assertThat(formatter.getFormattedValue(-1000000000f, null, 0, null)).isEqualTo("-1b")
		assertThat(formatter.getFormattedValue(-1234567.89f, null, 0, null)).isEqualTo("-1.2m")
		assertThat(formatter.getFormattedValue(-1234.56f, null, 0, null)).isEqualTo("-1.2k")
		assertThat(formatter.getFormattedValue(-2.5f, null, 0, null)).isEqualTo("-2.5")
		assertThat(formatter.getFormattedValue(0f, null, 0, null)).isEqualTo("0")
		assertThat(formatter.getFormattedValue(2.5f, null, 0, null)).isEqualTo("2.5")
		assertThat(formatter.getFormattedValue(1234.56f, null, 0, null)).isEqualTo("1.23k")
		assertThat(formatter.getFormattedValue(1234567.89f, null, 0, null)).isEqualTo("1.23m")
		assertThat(formatter.getFormattedValue(1000000000f, null, 0, null)).isEqualTo("1b")
		assertThat(formatter.getFormattedValue(1000000000000f, null, 0, null)).isEqualTo("1t")

		assertThat(formatter.getFormattedValue(-1000000000000f, null)).isEqualTo("-1t")
		assertThat(formatter.getFormattedValue(-1000000000f, null)).isEqualTo("-1b")
		assertThat(formatter.getFormattedValue(-1234567.89f, null)).isEqualTo("-1.2m")
		assertThat(formatter.getFormattedValue(-1234.56f, null)).isEqualTo("-1.2k")
		assertThat(formatter.getFormattedValue(-2.5f, null)).isEqualTo("-2.5")
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("0")
		assertThat(formatter.getFormattedValue(2.5f, null)).isEqualTo("2.5")
		assertThat(formatter.getFormattedValue(1234.56f, null)).isEqualTo("1.23k")
		assertThat(formatter.getFormattedValue(1234567.89f, null)).isEqualTo("1.23m")
		assertThat(formatter.getFormattedValue(1000000000f, null)).isEqualTo("1b")
		assertThat(formatter.getFormattedValue(1000000000000f, null)).isEqualTo("1t")
	}

	@Test
	fun getFormattedValue_appendix_customSuffix() {
		val formatter = LargeValueFormatter(" USD")
		formatter.setSuffix(arrayOf("", "k", "m", "b", "t", "q"))

		assertThat(formatter.decimalDigits).isEqualTo(0)

		assertThat(formatter.getFormattedValue(-1000000000000000f, null, 0, null)).isEqualTo("-1q USD")
		assertThat(formatter.getFormattedValue(-1000000000000f, null, 0, null)).isEqualTo("-1t USD")
		assertThat(formatter.getFormattedValue(-1000000000f, null, 0, null)).isEqualTo("-1b USD")
		assertThat(formatter.getFormattedValue(-1234567.89f, null, 0, null)).isEqualTo("-1.2m USD")
		assertThat(formatter.getFormattedValue(-1234.56f, null, 0, null)).isEqualTo("-1.2k USD")
		assertThat(formatter.getFormattedValue(-2.5f, null, 0, null)).isEqualTo("-2.5 USD")
		assertThat(formatter.getFormattedValue(0f, null, 0, null)).isEqualTo("0 USD")
		assertThat(formatter.getFormattedValue(2.5f, null, 0, null)).isEqualTo("2.5 USD")
		assertThat(formatter.getFormattedValue(1234.56f, null, 0, null)).isEqualTo("1.23k USD")
		assertThat(formatter.getFormattedValue(1234567.89f, null, 0, null)).isEqualTo("1.23m USD")
		assertThat(formatter.getFormattedValue(1000000000f, null, 0, null)).isEqualTo("1b USD")
		assertThat(formatter.getFormattedValue(1000000000000f, null, 0, null)).isEqualTo("1t USD")
		assertThat(
			formatter.getFormattedValue(1000000000000000f, null, 0, null)
		).isEqualTo("1q USD")

		assertThat(formatter.getFormattedValue(-1000000000000000f, null)).isEqualTo("-1q USD")
		assertThat(formatter.getFormattedValue(-1000000000000f, null)).isEqualTo("-1t USD")
		assertThat(formatter.getFormattedValue(-1000000000f, null)).isEqualTo("-1b USD")
		assertThat(formatter.getFormattedValue(-1234567.89f, null)).isEqualTo("-1.2m USD")
		assertThat(formatter.getFormattedValue(-1234.56f, null)).isEqualTo("-1.2k USD")
		assertThat(formatter.getFormattedValue(-2.5f, null)).isEqualTo("-2.5 USD")
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("0 USD")
		assertThat(formatter.getFormattedValue(2.5f, null)).isEqualTo("2.5 USD")
		assertThat(formatter.getFormattedValue(1234.56f, null)).isEqualTo("1.23k USD")
		assertThat(formatter.getFormattedValue(1234567.89f, null)).isEqualTo("1.23m USD")
		assertThat(formatter.getFormattedValue(1000000000f, null)).isEqualTo("1b USD")
		assertThat(formatter.getFormattedValue(1000000000000f, null)).isEqualTo("1t USD")
		assertThat(formatter.getFormattedValue(1000000000000000f, null)).isEqualTo("1q USD")
	}
}
