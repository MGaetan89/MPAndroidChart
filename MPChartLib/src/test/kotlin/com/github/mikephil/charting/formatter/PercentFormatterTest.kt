package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.DecimalFormat

class PercentFormatterTest {
	@Test
	fun getFormattedValue() {
		val formatter = PercentFormatter()

		assertThat(formatter.decimalDigits).isEqualTo(1)
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

	@Test
	fun getFormattedValue_customFormatter() {
		val formatter = PercentFormatter(DecimalFormat("###,###,##0"))

		assertThat(formatter.decimalDigits).isEqualTo(1)
		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0")

		assertThat(
			formatter.getFormattedValue(-1234.567891f, null, 0, null)
		).isEqualTo("-1,235 %")
		assertThat(formatter.getFormattedValue(-123.45f, null, 0, null)).isEqualTo("-123 %")
		assertThat(formatter.getFormattedValue(0f, null, 0, null)).isEqualTo("0 %")
		assertThat(formatter.getFormattedValue(123.45f, null, 0, null)).isEqualTo("123 %")
		assertThat(
			formatter.getFormattedValue(1234.567891f, null, 0, null)
		).isEqualTo("1,235 %")

		assertThat(formatter.getFormattedValue(-1234.567891f, null)).isEqualTo("-1,235 %")
		assertThat(formatter.getFormattedValue(-123.45f, null)).isEqualTo("-123 %")
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("0 %")
		assertThat(formatter.getFormattedValue(123.45f, null)).isEqualTo("123 %")
		assertThat(formatter.getFormattedValue(1234.567891f, null)).isEqualTo("1,235 %")
	}
}
