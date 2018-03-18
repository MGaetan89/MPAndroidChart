package com.github.mikephil.charting.components

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class AxisBaseTest<T : AxisBase> : ComponentBaseTest<T>() {
	@Test
	fun setDrawGridLines() {
		assertThat(this.component.isDrawGridLinesEnabled).isTrue()

		this.component.setDrawGridLines(false)
		assertThat(this.component.isDrawGridLinesEnabled).isFalse()

		this.component.setDrawGridLines(true)
		assertThat(this.component.isDrawGridLinesEnabled).isTrue()
	}

	@Test
	fun setDrawAxisLine() {
		assertThat(this.component.isDrawAxisLineEnabled).isTrue()

		this.component.setDrawAxisLine(false)
		assertThat(this.component.isDrawAxisLineEnabled).isFalse()

		this.component.setDrawAxisLine(true)
		assertThat(this.component.isDrawAxisLineEnabled).isTrue()
	}

	@Test
	fun setCenterAxisLabels() {
		assertThat(this.component.isCenterAxisLabelsEnabled).isFalse()

		this.component.setCenterAxisLabels(true)
		this.component.mEntryCount = 0
		assertThat(this.component.isCenterAxisLabelsEnabled).isFalse()

		this.component.setCenterAxisLabels(true)
		this.component.mEntryCount = 42
		assertThat(this.component.isCenterAxisLabelsEnabled).isTrue()

		this.component.setCenterAxisLabels(false)
		this.component.mEntryCount = 0
		assertThat(this.component.isCenterAxisLabelsEnabled).isFalse()

		this.component.setCenterAxisLabels(false)
		this.component.mEntryCount = 42
		assertThat(this.component.isCenterAxisLabelsEnabled).isFalse()
	}

	@Test
	fun setGridColor() {
		assertThat(this.component.gridColor).isEqualTo(Color.GRAY)

		this.component.gridColor = Color.RED
		assertThat(this.component.gridColor).isEqualTo(Color.RED)
	}

	@Test
	fun setAxisLineWidth() {
		assertThat(this.component.axisLineWidth).isEqualTo(1f)

		this.component.axisLineWidth = -5f
		assertThat(this.component.axisLineWidth).isEqualTo(-5f)

		this.component.axisLineWidth = -2.5f
		assertThat(this.component.axisLineWidth).isEqualTo(-2.5f)

		this.component.axisLineWidth = 0f
		assertThat(this.component.axisLineWidth).isEqualTo(0f)

		this.component.axisLineWidth = 2.5f
		assertThat(this.component.axisLineWidth).isEqualTo(2.5f)

		this.component.axisLineWidth = 5f
		assertThat(this.component.axisLineWidth).isEqualTo(5f)
	}

	@Test
	fun setGridLineWidth() {
		assertThat(this.component.gridLineWidth).isEqualTo(1f)

		this.component.gridLineWidth = -5f
		assertThat(this.component.gridLineWidth).isEqualTo(-5f)

		this.component.gridLineWidth = -2.5f
		assertThat(this.component.gridLineWidth).isEqualTo(-2.5f)

		this.component.gridLineWidth = 0f
		assertThat(this.component.gridLineWidth).isEqualTo(0f)

		this.component.gridLineWidth = 2.5f
		assertThat(this.component.gridLineWidth).isEqualTo(2.5f)

		this.component.gridLineWidth = 5f
		assertThat(this.component.gridLineWidth).isEqualTo(5f)
	}

	@Test
	fun setAxisLineColor() {
		assertThat(this.component.axisLineColor).isEqualTo(Color.GRAY)

		this.component.axisLineColor = Color.RED
		assertThat(this.component.axisLineColor).isEqualTo(Color.RED)
	}

	@Test
	fun setDrawLabels() {
		assertThat(this.component.isDrawLabelsEnabled).isTrue()

		this.component.setDrawLabels(false)
		assertThat(this.component.isDrawLabelsEnabled).isFalse()

		this.component.setDrawLabels(true)
		assertThat(this.component.isDrawLabelsEnabled).isTrue()
	}

	@Test
	fun setLabelCount() {
		assertThat(this.component.labelCount).isEqualTo(6)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.labelCount = 1
		assertThat(this.component.labelCount).isEqualTo(2)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.labelCount = 14
		assertThat(this.component.labelCount).isEqualTo(14)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.labelCount = 26
		assertThat(this.component.labelCount).isEqualTo(25)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.setLabelCount(1, true)
		assertThat(this.component.labelCount).isEqualTo(2)
		assertThat(this.component.isForceLabelsEnabled).isTrue()

		this.component.setLabelCount(1, false)
		assertThat(this.component.labelCount).isEqualTo(2)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.setLabelCount(14, true)
		assertThat(this.component.labelCount).isEqualTo(14)
		assertThat(this.component.isForceLabelsEnabled).isTrue()

		this.component.setLabelCount(14, false)
		assertThat(this.component.labelCount).isEqualTo(14)
		assertThat(this.component.isForceLabelsEnabled).isFalse()

		this.component.setLabelCount(26, true)
		assertThat(this.component.labelCount).isEqualTo(25)
		assertThat(this.component.isForceLabelsEnabled).isTrue()

		this.component.setLabelCount(26, false)
		assertThat(this.component.labelCount).isEqualTo(25)
		assertThat(this.component.isForceLabelsEnabled).isFalse()
	}

	@Test
	fun setGranularityEnabled() {
		assertThat(this.component.isGranularityEnabled).isFalse()

		this.component.isGranularityEnabled = true
		assertThat(this.component.isGranularityEnabled).isTrue()

		this.component.isGranularityEnabled = false
		assertThat(this.component.isGranularityEnabled).isFalse()
	}

	@Test
	fun setGranularity() {
		assertThat(this.component.granularity).isEqualTo(1f)
		assertThat(this.component.isGranularityEnabled).isFalse()

		this.component.granularity = -5f
		assertThat(this.component.granularity).isEqualTo(-5f)
		assertThat(this.component.isGranularityEnabled).isTrue()

		this.component.granularity = -2.5f
		assertThat(this.component.granularity).isEqualTo(-2.5f)
		assertThat(this.component.isGranularityEnabled).isTrue()

		this.component.granularity = 0f
		assertThat(this.component.granularity).isEqualTo(0f)
		assertThat(this.component.isGranularityEnabled).isTrue()

		this.component.granularity = 2.5f
		assertThat(this.component.granularity).isEqualTo(2.5f)
		assertThat(this.component.isGranularityEnabled).isTrue()

		this.component.granularity = 5f
		assertThat(this.component.granularity).isEqualTo(5f)
		assertThat(this.component.isGranularityEnabled).isTrue()
	}

	@Test
	fun addLimitLine() {
		assertThat(this.component.limitLines).isEmpty()

		val limitLine = LimitLine(5f)

		this.component.addLimitLine(limitLine)
		assertThat(this.component.limitLines).containsExactly(limitLine)

		this.component.removeLimitLine(limitLine)
		assertThat(this.component.limitLines).isEmpty()

		val limitLines = listOf(
			LimitLine(-5f),
			LimitLine(0f),
			LimitLine(5f)
		)

		limitLines.forEach(this.component::addLimitLine)
		assertThat(this.component.limitLines).containsExactlyElementsIn(limitLines)

		this.component.removeLimitLine(limitLines[1])
		assertThat(this.component.limitLines).containsExactly(limitLines[0], limitLines[2])

		this.component.removeAllLimitLines()
		assertThat(this.component.limitLines).isEmpty()
	}

	@Test
	fun setDrawLimitLinesBehindData() {
		assertThat(this.component.isDrawLimitLinesBehindDataEnabled).isFalse()

		this.component.setDrawLimitLinesBehindData(true)
		assertThat(this.component.isDrawLimitLinesBehindDataEnabled).isTrue()

		this.component.setDrawLimitLinesBehindData(false)
		assertThat(this.component.isDrawLimitLinesBehindDataEnabled).isFalse()
	}
}
