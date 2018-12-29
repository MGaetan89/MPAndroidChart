package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PercentFormatterTest {
	@Test
	fun getFormattedValue() {
		val formatter = PercentFormatter()

		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0.0")

		assertThat(
			formatter.getFormattedValue(-1234.567891f, null, 0, null)
		).isEqualTo("-1,234.6 %")
		assertThat(formatter.getFormattedValue(-123.45f, null, 0, null)).isEqualTo("-123.4 %")
		assertThat(formatter.getFormattedValue(0f, null, 0, null)).isEqualTo("0.0 %")
		assertThat(formatter.getFormattedValue(123.45f, null, 0, null)).isEqualTo("123.4 %")
		assertThat(
			formatter.getFormattedValue(1234.567891f, null, 0, null)
		).isEqualTo("1,234.6 %")

		assertThat(formatter.getFormattedValue(-1234.567891f, null)).isEqualTo("-1,234.6 %")
		assertThat(formatter.getFormattedValue(-123.45f, null)).isEqualTo("-123.4 %")
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("0.0 %")
		assertThat(formatter.getFormattedValue(123.45f, null)).isEqualTo("123.4 %")
		assertThat(formatter.getFormattedValue(1234.567891f, null)).isEqualTo("1,234.6 %")
	}
}
