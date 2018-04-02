package com.github.mikephil.charting.data

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BarDataSetTest : BarLineScatterCandleBubbleDataSetTest<BarEntry, BarDataSet>() {
	@Before
	fun before() {
		this.dataSet = BarDataSet(mutableListOf(), "BarDataSet")
		this.values = mutableListOf(BarEntry(1f, 2f), BarEntry(3f, 4f), BarEntry(5f, 6f))
		this.entry = BarEntry(7f, 8f)
	}

	@Test
	override fun setHighLightColor() {
		assertThat(this.dataSet.highLightColor).isEqualTo(Color.BLACK)

		this.dataSet.highLightColor = Color.RED
		assertThat(this.dataSet.highLightColor).isEqualTo(Color.RED)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as BarDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.highLightAlpha).isEqualTo(dataSet.highLightAlpha)
			assertThat(this.stackSize).isEqualTo(dataSet.stackSize)
			assertThat(this.barShadowColor).isEqualTo(dataSet.barShadowColor)
			assertThat(this.stackLabels).isEqualTo(dataSet.stackLabels)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as BarDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.highLightAlpha).isEqualTo(dataSet.highLightAlpha)
			assertThat(this.stackSize).isEqualTo(dataSet.stackSize)
			assertThat(this.barShadowColor).isEqualTo(dataSet.barShadowColor)
			assertThat(this.stackLabels).isEqualTo(dataSet.stackLabels)
		}
	}

	@Test
	fun calcEntryCountIncludingStacks() {
		assertThat(this.dataSet.entryCountStacks).isEqualTo(0)

		this.dataSet = BarDataSet(this.values, "BarDataSet")
		assertThat(this.dataSet.entryCountStacks).isEqualTo(3)

		val values = listOf(
			BarEntry(1f, 2f),
			BarEntry(3f, floatArrayOf()),
			BarEntry(4f, floatArrayOf(5f, 6f, 7f))
		)
		this.dataSet = BarDataSet(values, "BarDataSet")
		assertThat(this.dataSet.entryCountStacks).isEqualTo(4)
	}

	@Test
	fun calcStackSize() {
		assertThat(this.dataSet.stackSize).isEqualTo(1)
		assertThat(this.dataSet.isStacked).isFalse()

		this.dataSet = BarDataSet(this.values, "BarDataSet")
		assertThat(this.dataSet.stackSize).isEqualTo(1)
		assertThat(this.dataSet.isStacked).isFalse()

		val values = listOf(
			BarEntry(1f, 2f),
			BarEntry(3f, floatArrayOf()),
			BarEntry(4f, floatArrayOf(5f, 6f, 7f))
		)
		this.dataSet = BarDataSet(values, "BarDataSet")
		assertThat(this.dataSet.stackSize).isEqualTo(3)
		assertThat(this.dataSet.isStacked).isTrue()
	}

	@Test
	fun setBarShadowColor() {
		assertThat(this.dataSet.barShadowColor).isEqualTo(0xD7D7D7)

		this.dataSet.barShadowColor = Color.RED
		assertThat(this.dataSet.barShadowColor).isEqualTo(Color.RED)
	}

	@Test
	fun setBarBorderWidth() {
		assertThat(this.dataSet.barBorderWidth).isEqualTo(0f)

		this.dataSet.barBorderWidth = -5f
		assertThat(this.dataSet.barBorderWidth).isEqualTo(-5f)

		this.dataSet.barBorderWidth = -2.5f
		assertThat(this.dataSet.barBorderWidth).isEqualTo(-2.5f)

		this.dataSet.barBorderWidth = 0f
		assertThat(this.dataSet.barBorderWidth).isEqualTo(0f)

		this.dataSet.barBorderWidth = 2.5f
		assertThat(this.dataSet.barBorderWidth).isEqualTo(2.5f)

		this.dataSet.barBorderWidth = 5f
		assertThat(this.dataSet.barBorderWidth).isEqualTo(5f)
	}

	@Test
	fun setBarBorderColor() {
		assertThat(this.dataSet.barBorderColor).isEqualTo(Color.BLACK)

		this.dataSet.barBorderColor = Color.RED
		assertThat(this.dataSet.barBorderColor).isEqualTo(Color.RED)
	}

	@Test
	fun setHighLightAlpha() {
		assertThat(this.dataSet.highLightAlpha).isEqualTo(120)

		this.dataSet.highLightAlpha = 255
		assertThat(this.dataSet.highLightAlpha).isEqualTo(255)
	}

	@Test
	fun setStackLabels() {
		assertThat(this.dataSet.stackLabels).asList().containsExactly("Stack")

		this.dataSet.stackLabels = emptyArray()
		assertThat(this.dataSet.stackLabels).isEmpty()

		this.dataSet.stackLabels = arrayOf("Hello", "World")
		assertThat(this.dataSet.stackLabels).asList().containsExactly("Hello", "World").inOrder()
	}
}
