package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PercentFormatterTest {
	@Test
	fun getFormattedValue() {
		val formatter = PercentFormatter()

		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0.0")

		assertThat(formatter.getFormattedValue(-1234.567891f)).isEqualTo("-1,234.6 %")
		assertThat(formatter.getFormattedValue(-123.45f)).isEqualTo("-123.4 %")
		assertThat(formatter.getFormattedValue(0f)).isEqualTo("0.0 %")
		assertThat(formatter.getFormattedValue(123.45f)).isEqualTo("123.4 %")
		assertThat(formatter.getFormattedValue(1234.567891f)).isEqualTo("1,234.6 %")

		assertThat(formatter.getFormattedValue(-1234.567891f)).isEqualTo("-1,234.6 %")
		assertThat(formatter.getFormattedValue(-123.45f)).isEqualTo("-123.4 %")
		assertThat(formatter.getFormattedValue(0f)).isEqualTo("0.0 %")
		assertThat(formatter.getFormattedValue(123.45f)).isEqualTo("123.4 %")
		assertThat(formatter.getFormattedValue(1234.567891f)).isEqualTo("1,234.6 %")
	}
}
