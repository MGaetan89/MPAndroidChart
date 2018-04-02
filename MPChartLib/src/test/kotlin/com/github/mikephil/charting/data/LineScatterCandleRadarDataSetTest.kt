package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class LineScatterCandleRadarDataSetTest<E : Entry, T : LineScatterCandleRadarDataSet<E>> :
	BarLineScatterCandleBubbleDataSetTest<E, T>() {
	@Test
	fun setDrawHorizontalHighlightIndicator() {
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isTrue()

		this.dataSet.setDrawHorizontalHighlightIndicator(false)
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isFalse()

		this.dataSet.setDrawHorizontalHighlightIndicator(true)
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isTrue()
	}

	@Test
	fun setDrawVerticalHighlightIndicator() {
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isTrue()

		this.dataSet.setDrawVerticalHighlightIndicator(false)
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isFalse()

		this.dataSet.setDrawVerticalHighlightIndicator(true)
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isTrue()
	}

	@Test
	fun setDrawHighlightIndicators() {
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isTrue()
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isTrue()

		this.dataSet.setDrawHighlightIndicators(false)
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isFalse()
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isFalse()

		this.dataSet.setDrawHighlightIndicators(true)
		assertThat(this.dataSet.isHorizontalHighlightIndicatorEnabled).isTrue()
		assertThat(this.dataSet.isVerticalHighlightIndicatorEnabled).isTrue()
	}

	@Test
	fun setHighlightLineWidth() {
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(0.5f)

		this.dataSet.highlightLineWidth = -5f
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(-5f)

		this.dataSet.highlightLineWidth = -2.5f
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(-2.5f)

		this.dataSet.highlightLineWidth = 0f
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(0f)

		this.dataSet.highlightLineWidth = 2.5f
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(2.5f)

		this.dataSet.highlightLineWidth = 5f
		assertThat(this.dataSet.highlightLineWidth).isEqualTo(5f)
	}

	@Test
	fun dashedHighlightLine() {
		assertThat(this.dataSet.isDashedHighlightLineEnabled).isFalse()
		assertThat(this.dataSet.dashPathEffectHighlight).isNull()

		this.dataSet.enableDashedHighlightLine(1f, 2f, 3f)
		assertThat(this.dataSet.isDashedHighlightLineEnabled).isTrue()
		assertThat(this.dataSet.dashPathEffectHighlight).isNotNull()

		this.dataSet.disableDashedHighlightLine()
		assertThat(this.dataSet.isDashedHighlightLineEnabled).isFalse()
		assertThat(this.dataSet.dashPathEffectHighlight).isNull()
	}
}
