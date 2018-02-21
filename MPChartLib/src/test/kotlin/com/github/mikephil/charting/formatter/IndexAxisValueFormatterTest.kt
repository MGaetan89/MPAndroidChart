package com.github.mikephil.charting.formatter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class IndexAxisValueFormatterTest {
	@Test
	fun getFormattedValue() {
		val formatter = IndexAxisValueFormatter()

		assertThat(formatter.values).isEmpty()

		assertThat(formatter.getFormattedValue(-1f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(0f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(1f, null)).isEmpty()
	}

	@Test
	fun getFormattedValue_array() {
		val formatter = IndexAxisValueFormatter(arrayOf("Chocolate", "Strawberry", "Cheese"))

		assertThat(formatter.values).isEqualTo(arrayOf("Chocolate", "Strawberry", "Cheese"))

		assertThat(formatter.getFormattedValue(-1f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("Chocolate")
		assertThat(formatter.getFormattedValue(0.5f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(1f, null)).isEqualTo("Strawberry")
		assertThat(formatter.getFormattedValue(1.1f, null)).isEqualTo("Strawberry")
		assertThat(formatter.getFormattedValue(2f, null)).isEqualTo("Cheese")
		assertThat(formatter.getFormattedValue(2.7f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(3f, null)).isEmpty()
	}

	@Test
	fun getFormattedValue_collection() {
		val formatter = IndexAxisValueFormatter(listOf("Chocolate", "Strawberry", "Cheese"))

		assertThat(formatter.values).isEqualTo(arrayOf("Chocolate", "Strawberry", "Cheese"))

		assertThat(formatter.getFormattedValue(-1f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(0f, null)).isEqualTo("Chocolate")
		assertThat(formatter.getFormattedValue(0.5f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(1f, null)).isEqualTo("Strawberry")
		assertThat(formatter.getFormattedValue(1.1f, null)).isEqualTo("Strawberry")
		assertThat(formatter.getFormattedValue(2f, null)).isEqualTo("Cheese")
		assertThat(formatter.getFormattedValue(2.7f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(3f, null)).isEmpty()
	}

	@Test
	fun getFormattedValue_nullArray() {
		val formatter = IndexAxisValueFormatter(null as Array<String>?)

		assertThat(formatter.values).isEmpty()

		assertThat(formatter.getFormattedValue(-1f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(0f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(1f, null)).isEmpty()
	}

	@Test
	fun getFormattedValue_nullCollection() {
		val formatter = IndexAxisValueFormatter(null as Collection<String>?)

		assertThat(formatter.values).isEmpty()

		assertThat(formatter.getFormattedValue(-1f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(0f, null)).isEmpty()
		assertThat(formatter.getFormattedValue(1f, null)).isEmpty()
	}

	@Test
	fun setValues() {
		val formatter = IndexAxisValueFormatter(arrayOf("Chocolate", "Strawberry", "Cheese"))

		assertThat(formatter.values).isEqualTo(arrayOf("Chocolate", "Strawberry", "Cheese"))

		formatter.setValues(null)

		assertThat(formatter.values).isEmpty()
	}
}
