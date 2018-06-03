package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.Paint
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class YAxisTest : AxisBaseTest<YAxis>() {
	@Before
	fun before() {
		this.component = YAxis()
	}

	@Test
	override fun yOffset() {
		assertThat(this.component.yOffset).isEqualTo(0f)

		this.component.yOffset = 10f
		assertThat(this.component.yOffset).isEqualTo(10f)
	}

	@Test
	fun getAxisDependency() {
		YAxis.AxisDependency.values().forEach {
			val component = YAxis(it)
			assertThat(component.axisDependency).isEqualTo(it)
		}
	}

	@Test
	fun setMinWidth() {
		assertThat(this.component.minWidth).isEqualTo(0f)

		this.component.minWidth = -5f
		assertThat(this.component.minWidth).isEqualTo(-5f)

		this.component.minWidth = -2.5f
		assertThat(this.component.minWidth).isEqualTo(-2.5f)

		this.component.minWidth = 0f
		assertThat(this.component.minWidth).isEqualTo(0f)

		this.component.minWidth = 2.5f
		assertThat(this.component.minWidth).isEqualTo(2.5f)

		this.component.minWidth = 5f
		assertThat(this.component.minWidth).isEqualTo(5f)
	}

	@Test
	fun setMaxWidth() {
		assertThat(this.component.maxWidth).isEqualTo(Float.POSITIVE_INFINITY)

		this.component.maxWidth = -5f
		assertThat(this.component.maxWidth).isEqualTo(-5f)

		this.component.maxWidth = -2.5f
		assertThat(this.component.maxWidth).isEqualTo(-2.5f)

		this.component.maxWidth = 0f
		assertThat(this.component.maxWidth).isEqualTo(0f)

		this.component.maxWidth = 2.5f
		assertThat(this.component.maxWidth).isEqualTo(2.5f)

		this.component.maxWidth = 5f
		assertThat(this.component.maxWidth).isEqualTo(5f)
	}

	@Test
	fun setPosition() {
		assertThat(this.component.labelPosition).isEqualTo(YAxis.YAxisLabelPosition.OUTSIDE_CHART)

		YAxis.YAxisLabelPosition.values().forEach {
			this.component.setPosition(it)
			assertThat(this.component.labelPosition).isEqualTo(it)
		}
	}

	@Test
	fun setDrawTopYLabelEntry() {
		assertThat(this.component.isDrawTopYLabelEntryEnabled).isTrue()

		this.component.setDrawTopYLabelEntry(false)
		assertThat(this.component.isDrawTopYLabelEntryEnabled).isFalse()

		this.component.setDrawTopYLabelEntry(true)
		assertThat(this.component.isDrawTopYLabelEntryEnabled).isTrue()
	}

	@Test
	fun isDrawBottomYLabelEntryEnabled() {
		assertThat(this.component.isDrawBottomYLabelEntryEnabled).isTrue()
	}

	@Test
	fun setInverted() {
		assertThat(this.component.isInverted).isFalse()

		this.component.isInverted = true
		assertThat(this.component.isInverted).isTrue()

		this.component.isInverted = false
		assertThat(this.component.isInverted).isFalse()
	}

	@Test
	fun setSpaceTop() {
		assertThat(this.component.spaceTop).isEqualTo(10f)

		this.component.spaceTop = -5f
		assertThat(this.component.spaceTop).isEqualTo(-5f)

		this.component.spaceTop = -2.5f
		assertThat(this.component.spaceTop).isEqualTo(-2.5f)

		this.component.spaceTop = 0f
		assertThat(this.component.spaceTop).isEqualTo(0f)

		this.component.spaceTop = 2.5f
		assertThat(this.component.spaceTop).isEqualTo(2.5f)

		this.component.spaceTop = 5f
		assertThat(this.component.spaceTop).isEqualTo(5f)
	}

	@Test
	fun setSpaceBottom() {
		assertThat(this.component.spaceBottom).isEqualTo(10f)

		this.component.spaceBottom = -5f
		assertThat(this.component.spaceBottom).isEqualTo(-5f)

		this.component.spaceBottom = -2.5f
		assertThat(this.component.spaceBottom).isEqualTo(-2.5f)

		this.component.spaceBottom = 0f
		assertThat(this.component.spaceBottom).isEqualTo(0f)

		this.component.spaceBottom = 2.5f
		assertThat(this.component.spaceBottom).isEqualTo(2.5f)

		this.component.spaceBottom = 5f
		assertThat(this.component.spaceBottom).isEqualTo(5f)
	}

	@Test
	fun setDrawZeroLine() {
		assertThat(this.component.isDrawZeroLineEnabled).isFalse()

		this.component.setDrawZeroLine(true)
		assertThat(this.component.isDrawZeroLineEnabled).isTrue()

		this.component.setDrawZeroLine(false)
		assertThat(this.component.isDrawZeroLineEnabled).isFalse()
	}

	@Test
	fun setZeroLineColor() {
		assertThat(this.component.zeroLineColor).isEqualTo(Color.GRAY)

		this.component.zeroLineColor = Color.RED
		assertThat(this.component.zeroLineColor).isEqualTo(Color.RED)
	}

	@Test
	fun setZeroLineWidth() {
		assertThat(this.component.zeroLineWidth).isEqualTo(1f)

		this.component.zeroLineWidth = -5f
		assertThat(this.component.zeroLineWidth).isEqualTo(-5f)

		this.component.zeroLineWidth = -2.5f
		assertThat(this.component.zeroLineWidth).isEqualTo(-2.5f)

		this.component.zeroLineWidth = 0f
		assertThat(this.component.zeroLineWidth).isEqualTo(0f)

		this.component.zeroLineWidth = 2.5f
		assertThat(this.component.zeroLineWidth).isEqualTo(2.5f)

		this.component.zeroLineWidth = 5f
		assertThat(this.component.zeroLineWidth).isEqualTo(5f)
	}

	@Test
	fun getRequiredWidthSpace() {
		val paint = Paint()

		assertThat(this.component.getRequiredWidthSpace(paint)).isEqualTo(10f)

		this.component.minWidth = -2.5f
		this.component.maxWidth = -5f
		assertThat(this.component.getRequiredWidthSpace(paint)).isEqualTo(10f)

		this.component.minWidth = 0f
		this.component.maxWidth = 0f
		assertThat(this.component.getRequiredWidthSpace(paint)).isEqualTo(10f)

		this.component.minWidth = 2.5f
		this.component.maxWidth = 5f
		assertThat(this.component.getRequiredWidthSpace(paint)).isEqualTo(5f)

		this.component.minWidth = 2.5f
		this.component.maxWidth = Float.POSITIVE_INFINITY
		assertThat(this.component.getRequiredWidthSpace(paint)).isEqualTo(10f)
	}

	@Test
	fun getRequiredHeightSpace() {
		val paint = Paint()

		assertThat(this.component.getRequiredHeightSpace(paint)).isEqualTo(0f)
	}

	@Test
	fun needsOffset() {
		assertThat(this.component.needsOffset()).isTrue()

		this.component.isEnabled = false
		this.component.setDrawLabels(false)
		this.component.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = false
		this.component.setDrawLabels(false)
		this.component.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = false
		this.component.setDrawLabels(true)
		this.component.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = false
		this.component.setDrawLabels(true)
		this.component.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = true
		this.component.setDrawLabels(false)
		this.component.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = true
		this.component.setDrawLabels(false)
		this.component.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = true
		this.component.setDrawLabels(true)
		this.component.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
		assertThat(this.component.needsOffset()).isFalse()

		this.component.isEnabled = true
		this.component.setDrawLabels(true)
		this.component.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
		assertThat(this.component.needsOffset()).isTrue()
	}

	@Test
	fun setUseAutoScaleMinRestriction() {
		assertThat(this.component.isUseAutoScaleMinRestriction).isFalse()

		this.component.isUseAutoScaleMinRestriction = true
		assertThat(this.component.isUseAutoScaleMinRestriction).isTrue()

		this.component.isUseAutoScaleMinRestriction = false
		assertThat(this.component.isUseAutoScaleMinRestriction).isFalse()
	}

	@Test
	fun setUseAutoScaleMaxRestriction() {
		assertThat(this.component.isUseAutoScaleMaxRestriction).isFalse()

		this.component.isUseAutoScaleMaxRestriction = true
		assertThat(this.component.isUseAutoScaleMaxRestriction).isTrue()

		this.component.isUseAutoScaleMaxRestriction = false
		assertThat(this.component.isUseAutoScaleMaxRestriction).isFalse()
	}
}
