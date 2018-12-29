package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DefaultAxisValueFormatterTest {
	@Test
	fun getFormattedValue_negativeDigits() {
		val formatter = DefaultAxisValueFormatter(-5)

		assertThat(formatter.decimalDigits).isEqualTo(-5)
		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0")

		assertThat(formatter.getFormattedValue(-1234.567891f)).isEqualTo("-1,235")
		assertThat(formatter.getFormattedValue(-123.45f)).isEqualTo("-123")
		assertThat(formatter.getFormattedValue(0f)).isEqualTo("0")
		assertThat(formatter.getFormattedValue(123.45f)).isEqualTo("123")
		assertThat(formatter.getFormattedValue(1234.567891f)).isEqualTo("1,235")
	}

	@Test
	fun getFormattedValue_positiveDigits() {
		val formatter = DefaultAxisValueFormatter(5)

		assertThat(formatter.decimalDigits).isEqualTo(5)
		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0.00000")

		assertThat(formatter.getFormattedValue(-1234.567891f)).isEqualTo("-1,234.56787")
		assertThat(formatter.getFormattedValue(-123.45f)).isEqualTo("-123.45000")
		assertThat(formatter.getFormattedValue(0f)).isEqualTo("0.00000")
		assertThat(formatter.getFormattedValue(123.45f)).isEqualTo("123.45000")
		assertThat(formatter.getFormattedValue(1234.567891f)).isEqualTo("1,234.56787")
	}

	@Test
	fun getFormattedValue_zeroDigits() {
		val formatter = DefaultAxisValueFormatter(0)

		assertThat(formatter.decimalDigits).isEqualTo(0)
		assertThat(formatter.mFormat.toPattern()).isEqualTo("#,##0")

		assertThat(formatter.getFormattedValue(-1234.567891f)).isEqualTo("-1,235")
		assertThat(formatter.getFormattedValue(-123.45f)).isEqualTo("-123")
		assertThat(formatter.getFormattedValue(0f)).isEqualTo("0")
		assertThat(formatter.getFormattedValue(123.45f)).isEqualTo("123")
		assertThat(formatter.getFormattedValue(1234.567891f)).isEqualTo("1,235")
	}
}
