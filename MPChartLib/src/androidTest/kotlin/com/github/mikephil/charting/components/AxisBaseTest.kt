package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.DashPathEffect
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
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

	@Test
	fun getLongestLabel() {
		assertThat(this.component.longestLabel).isEmpty()

		this.component.mEntries = floatArrayOf(-5f, -2.5f, 0f, 2.5f, 5f)
		assertThat(this.component.longestLabel).isEqualTo("-5")
	}

	@Test
	fun getFormattedLabel() {
		assertThat(this.component.getFormattedLabel(-1)).isEmpty()
		assertThat(this.component.getFormattedLabel(0)).isEmpty()
		assertThat(this.component.getFormattedLabel(1)).isEmpty()

		this.component.mEntries = floatArrayOf(-1000f, -5f, -2.5f, 0f, 2.5f, 5f, 1000f)
		assertThat(this.component.getFormattedLabel(-1)).isEmpty()
		assertThat(this.component.getFormattedLabel(0)).isEqualTo("-1,000")
		assertThat(this.component.getFormattedLabel(1)).isEqualTo("-5")
		assertThat(this.component.getFormattedLabel(2)).isEqualTo("-2")
		assertThat(this.component.getFormattedLabel(3)).isEqualTo("0")
		assertThat(this.component.getFormattedLabel(4)).isEqualTo("2")
		assertThat(this.component.getFormattedLabel(5)).isEqualTo("5")
		assertThat(this.component.getFormattedLabel(6)).isEqualTo("1,000")
		assertThat(this.component.getFormattedLabel(7)).isEmpty()
	}

	@Test
	fun setValueFormatter() {
		with(this.component.valueFormatter) {
			assertThat(this).isNotNull()
			assertThat(this).isInstanceOf(DefaultAxisValueFormatter::class.java)
			assertThat((this as DefaultAxisValueFormatter).decimalDigits).isEqualTo(0)
		}

		val formatter = LargeValueFormatter()
		this.component.setValueFormatter(formatter)
		assertThat(this.component.valueFormatter).isEqualTo(formatter)

		this.component.mDecimals = 2
		assertThat(this.component.valueFormatter).isEqualTo(formatter)

		this.component.setValueFormatter(null)
		with(this.component.valueFormatter) {
			assertThat(this).isNotNull()
			assertThat(this).isInstanceOf(DefaultAxisValueFormatter::class.java)
			assertThat((this as DefaultAxisValueFormatter).decimalDigits).isEqualTo(2)
		}

		this.component.mDecimals = 0
		with(this.component.valueFormatter) {
			assertThat(this).isNotNull()
			assertThat(this).isInstanceOf(DefaultAxisValueFormatter::class.java)
			assertThat((this as DefaultAxisValueFormatter).decimalDigits).isEqualTo(0)
		}
	}

	@Test
	fun setGridDashedLine() {
		assertThat(this.component.gridDashPathEffect).isNull()
		assertThat(this.component.isGridDashedLineEnabled).isFalse()

		this.component.enableGridDashedLine(5f, 8f, 2.5f)
		assertThat(this.component.gridDashPathEffect).isNotNull()
		assertThat(this.component.isGridDashedLineEnabled).isTrue()

		this.component.disableGridDashedLine()
		assertThat(this.component.gridDashPathEffect).isNull()
		assertThat(this.component.isGridDashedLineEnabled).isFalse()

		this.component.setGridDashedLine(DashPathEffect(floatArrayOf(1f, 2.5f), 5f))
		assertThat(this.component.gridDashPathEffect).isNotNull()
		assertThat(this.component.isGridDashedLineEnabled).isTrue()

		this.component.disableGridDashedLine()
		assertThat(this.component.gridDashPathEffect).isNull()
		assertThat(this.component.isGridDashedLineEnabled).isFalse()
	}

	@Test
	fun setAxisLineDashedLine() {
		assertThat(this.component.axisLineDashPathEffect).isNull()
		assertThat(this.component.isAxisLineDashedLineEnabled).isFalse()

		this.component.enableAxisLineDashedLine(5f, 8f, 2.5f)
		assertThat(this.component.axisLineDashPathEffect).isNotNull()
		assertThat(this.component.isAxisLineDashedLineEnabled).isTrue()

		this.component.disableAxisLineDashedLine()
		assertThat(this.component.axisLineDashPathEffect).isNull()
		assertThat(this.component.isAxisLineDashedLineEnabled).isFalse()

		this.component.setAxisLineDashedLine(DashPathEffect(floatArrayOf(1f, 2.5f), 5f))
		assertThat(this.component.axisLineDashPathEffect).isNotNull()
		assertThat(this.component.isAxisLineDashedLineEnabled).isTrue()

		this.component.disableAxisLineDashedLine()
		assertThat(this.component.axisLineDashPathEffect).isNull()
		assertThat(this.component.isAxisLineDashedLineEnabled).isFalse()
	}

	@Test
	fun setAxisMinimum() {
		assertThat(this.component.isAxisMinCustom).isFalse()
		assertThat(this.component.axisMinimum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.axisMinimum = -5f
		assertThat(this.component.isAxisMinCustom).isTrue()
		assertThat(this.component.axisMinimum).isEqualTo(-5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.resetAxisMinimum()
		assertThat(this.component.isAxisMinCustom).isFalse()
		assertThat(this.component.axisMinimum).isEqualTo(-5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.axisMinimum = 0f
		assertThat(this.component.isAxisMinCustom).isTrue()
		assertThat(this.component.axisMinimum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.resetAxisMinimum()
		assertThat(this.component.isAxisMinCustom).isFalse()
		assertThat(this.component.axisMinimum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.axisMinimum = 5f
		assertThat(this.component.isAxisMinCustom).isTrue()
		assertThat(this.component.axisMinimum).isEqualTo(5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.resetAxisMinimum()
		assertThat(this.component.isAxisMinCustom).isFalse()
		assertThat(this.component.axisMinimum).isEqualTo(5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)
	}

	@Test
	fun setAxisMaximum() {
		assertThat(this.component.isAxisMaxCustom).isFalse()
		assertThat(this.component.axisMaximum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.axisMaximum = -5f
		assertThat(this.component.isAxisMaxCustom).isTrue()
		assertThat(this.component.axisMaximum).isEqualTo(-5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.resetAxisMaximum()
		assertThat(this.component.isAxisMaxCustom).isFalse()
		assertThat(this.component.axisMaximum).isEqualTo(-5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.axisMaximum = 0f
		assertThat(this.component.isAxisMaxCustom).isTrue()
		assertThat(this.component.axisMaximum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.resetAxisMaximum()
		assertThat(this.component.isAxisMaxCustom).isFalse()
		assertThat(this.component.axisMaximum).isEqualTo(0f)
		assertThat(this.component.mAxisRange).isEqualTo(0f)

		this.component.axisMaximum = 5f
		assertThat(this.component.isAxisMaxCustom).isTrue()
		assertThat(this.component.axisMaximum).isEqualTo(5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)

		this.component.resetAxisMaximum()
		assertThat(this.component.isAxisMaxCustom).isFalse()
		assertThat(this.component.axisMaximum).isEqualTo(5f)
		assertThat(this.component.mAxisRange).isEqualTo(5f)
	}

	@Test
	fun setSpaceMin() {
		assertThat(this.component.spaceMin).isEqualTo(0f)

		this.component.spaceMin = -5f
		assertThat(this.component.spaceMin).isEqualTo(-5f)

		this.component.spaceMin = -2.5f
		assertThat(this.component.spaceMin).isEqualTo(-2.5f)

		this.component.spaceMin = 0f
		assertThat(this.component.spaceMin).isEqualTo(0f)

		this.component.spaceMin = 2.5f
		assertThat(this.component.spaceMin).isEqualTo(2.5f)

		this.component.spaceMin = 5f
		assertThat(this.component.spaceMin).isEqualTo(5f)
	}

	@Test
	fun setSpaceMax() {
		assertThat(this.component.spaceMax).isEqualTo(0f)

		this.component.spaceMax = -5f
		assertThat(this.component.spaceMax).isEqualTo(-5f)

		this.component.spaceMax = -2.5f
		assertThat(this.component.spaceMax).isEqualTo(-2.5f)

		this.component.spaceMax = 0f
		assertThat(this.component.spaceMax).isEqualTo(0f)

		this.component.spaceMax = 2.5f
		assertThat(this.component.spaceMax).isEqualTo(2.5f)

		this.component.spaceMax = 5f
		assertThat(this.component.spaceMax).isEqualTo(5f)
	}
}
