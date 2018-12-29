package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StackedValueFormatterTest {
	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(false, "", -2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(false, "", 2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(false, "", 0)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(false, " USD", -2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(false, " USD", 2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(false, " USD", 0)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(true, "", -2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(true, "", 2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(true, "", 0)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(true, " USD", -2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(true, " USD", 2)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(true, " USD", 0)

		assertThat(formatter.getFormattedValue(5f)).isEqualTo("5.0")
		assertThat(formatter.getFormattedValue(6f)).isEqualTo("6.0")
		assertThat(formatter.getFormattedValue(7f)).isEqualTo("7.0")
	}
}
