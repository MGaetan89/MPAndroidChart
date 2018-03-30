package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BubbleDataTest : ChartDataTest<BubbleData>() {
	@Before
	fun before() {
		this.data = BubbleData()
	}

	@Test
	fun constructorList() {
		this.data = BubbleData(listOf(BubbleDataSet(emptyList(), "Bubble")))

		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(2.5f)
		}

		this.data.setHighlightCircleWidth(-5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-5f)
		}

		this.data.setHighlightCircleWidth(-2.5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-2.5f)
		}

		this.data.setHighlightCircleWidth(0f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(0f)
		}

		this.data.setHighlightCircleWidth(2.5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(2.5f)
		}

		this.data.setHighlightCircleWidth(5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(5f)
		}
	}

	@Test
	fun constructorVarargs() {
		this.data = BubbleData(BubbleDataSet(emptyList(), "Bubble"))

		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(2.5f)
		}

		this.data.setHighlightCircleWidth(-5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-5f)
		}

		this.data.setHighlightCircleWidth(-2.5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-2.5f)
		}

		this.data.setHighlightCircleWidth(0f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(0f)
		}

		this.data.setHighlightCircleWidth(2.5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(2.5f)
		}

		this.data.setHighlightCircleWidth(5f)
		this.data.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(5f)
		}
	}

	@Test
	fun emptyConstructor() {
		this.data = BubbleData()
		this.data.setHighlightCircleWidth(-5f)
	}
}
