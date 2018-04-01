package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BubbleDataSetTest : BarLineScatterCandleBubbleDataSetTest<BubbleEntry, BubbleDataSet>() {
	@Before
	fun before() {
		this.dataSet = BubbleDataSet(mutableListOf(), "BubbleDataSet")
		this.values = mutableListOf(
			BubbleEntry(1f, 2f, 1f), BubbleEntry(3f, 4f, 2f), BubbleEntry(5f, 6f, 3f)
		)
		this.entry = BubbleEntry(7f, 8f, 4f)
	}

	@Test
	fun setHighlightCircleWidth() {
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(2.5f)

		this.dataSet.highlightCircleWidth = -5f
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(-5f)

		this.dataSet.highlightCircleWidth = -2.5f
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(-2.5f)

		this.dataSet.highlightCircleWidth = 0f
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(0f)

		this.dataSet.highlightCircleWidth = 2.5f
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(2.5f)

		this.dataSet.highlightCircleWidth = 5f
		assertThat(this.dataSet.highlightCircleWidth).isEqualTo(5f)
	}

	@Test
	fun calcMinMax_checkSize() {
		this.dataSet.calcMinMax(this.values[1])
		assertThat(this.dataSet.maxSize).isEqualTo(2f)
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)

		this.dataSet.calcMinMax(this.values[0])
		assertThat(this.dataSet.maxSize).isEqualTo(2f)
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.calcMinMax(this.values[2])
		assertThat(this.dataSet.maxSize).isEqualTo(3f)
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as BubbleDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as BubbleDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}
	}

	@Test
	fun setNormalizeSizeEnabled() {
		assertThat(this.dataSet.isNormalizeSizeEnabled).isTrue()

		this.dataSet.isNormalizeSizeEnabled = false
		assertThat(this.dataSet.isNormalizeSizeEnabled).isFalse()

		this.dataSet.isNormalizeSizeEnabled = true
		assertThat(this.dataSet.isNormalizeSizeEnabled).isTrue()
	}
}
