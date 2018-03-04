package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.Paint
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LimitLineTest : ComponentBaseTest<LimitLine>() {
	@Before
	fun before() {
		this.component = LimitLine(4.2f)
	}

	@Test
	fun getLimit() {
		assertThat(this.component.limit).isEqualTo(4.2f)
	}

	@Test
	fun lineWidth() {
		assertThat(this.component.lineWidth).isEqualTo(2f)

		this.component.lineWidth = 0.1f
		assertThat(this.component.lineWidth).isEqualTo(0.2f)

		this.component.lineWidth = 5f
		assertThat(this.component.lineWidth).isEqualTo(5f)

		this.component.lineWidth = 13f
		assertThat(this.component.lineWidth).isEqualTo(12f)
	}

	@Test
	fun lineColor() {
		assertThat(this.component.lineColor).isEqualTo(Color.rgb(237, 91, 91))

		this.component.lineColor = Color.YELLOW
		assertThat(this.component.lineColor).isEqualTo(Color.YELLOW)
	}

	@Test
	fun dashedLine() {
		assertThat(this.component.isDashedLineEnabled).isFalse()
		assertThat(this.component.dashPathEffect).isNull()

		this.component.enableDashedLine(5f, 2f, 1f)
		assertThat(this.component.isDashedLineEnabled).isTrue()
		assertThat(this.component.dashPathEffect).isNotNull()

		this.component.disableDashedLine()
		assertThat(this.component.isDashedLineEnabled).isFalse()
		assertThat(this.component.dashPathEffect).isNull()
	}

	@Test
	fun textStyle() {
		assertThat(this.component.textStyle).isEqualTo(Paint.Style.FILL_AND_STROKE)

		this.component.textStyle = Paint.Style.FILL
		assertThat(this.component.textStyle).isEqualTo(Paint.Style.FILL)

		this.component.textStyle = Paint.Style.STROKE
		assertThat(this.component.textStyle).isEqualTo(Paint.Style.STROKE)
	}

	@Test
	fun labelPosition() {
		assertThat(this.component.labelPosition).isEqualTo(LimitLine.LimitLabelPosition.RIGHT_TOP)

		this.component.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
		assertThat(this.component.labelPosition).isEqualTo(LimitLine.LimitLabelPosition.RIGHT_BOTTOM)

		this.component.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
		assertThat(this.component.labelPosition).isEqualTo(LimitLine.LimitLabelPosition.LEFT_TOP)

		this.component.labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
		assertThat(this.component.labelPosition).isEqualTo(LimitLine.LimitLabelPosition.LEFT_BOTTOM)

		this.component.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
		assertThat(this.component.labelPosition).isEqualTo(LimitLine.LimitLabelPosition.RIGHT_TOP)
	}

	@Test
	fun label() {
		assertThat(this.component.label).isEmpty()

		this.component.setLabel("Hello, World!")
		assertThat(this.component.label).isEqualTo("Hello, World!")

		this.component.setLabel(null)
		assertThat(this.component.label).isEmpty()

		this.component.setLabel("Hello, Android!")
		assertThat(this.component.label).isEqualTo("Hello, Android!")

		this.component.setLabel("")
		assertThat(this.component.label).isEmpty()
	}
}
