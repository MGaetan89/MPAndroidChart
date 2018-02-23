package com.github.mikephil.charting.formatter

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StackedValueFormatterTest {
	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(false, "", -2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(false, "", 2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5.00")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5.00")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15.00")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_noAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(false, "", 0)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(false, " USD", -2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(false, " USD", 2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5.00 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5.00 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15.00 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_notDrawWholeStack_withAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(false, " USD", 0)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("15 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(true, "", -2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(true, "", 2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5.00")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5.00")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5.00")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6.00")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7.00")
	}

	@Test
	fun getFormattedValue_drawWholeStack_noAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(true, "", 0)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_negativeDecimals() {
		val formatter = StackedValueFormatter(true, " USD", -2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7 USD")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_positiveDecimals() {
		val formatter = StackedValueFormatter(true, " USD", 2)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5.00 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5.00 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5.00 USD")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6.00 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7.00 USD")
	}

	@Test
	fun getFormattedValue_drawWholeStack_withAppendix_zeroDecimals() {
		val formatter = StackedValueFormatter(true, " USD", 0)

		assertThat(
			formatter.getFormattedValue(5f, Entry(0f, 1f), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, null), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(5f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("5 USD")
		assertThat(
			formatter.getFormattedValue(6f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("6 USD")
		assertThat(
			formatter.getFormattedValue(7f, BarEntry(0f, floatArrayOf(4f, 5f, 6f)), 0, null)
		).isEqualTo("7 USD")
	}
}
