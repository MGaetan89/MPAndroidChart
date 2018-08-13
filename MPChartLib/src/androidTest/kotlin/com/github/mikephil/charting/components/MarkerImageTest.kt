package com.github.mikephil.charting.components

import androidx.test.annotation.UiThreadTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.TestActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.test.R
import com.github.mikephil.charting.utils.FSize
import com.github.mikephil.charting.utils.MPPointF
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerImageTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	@Test
	@UiThreadTest
	fun markerImage() {
		val marker = MarkerImage(this.activityRule.activity, R.drawable.ic_android_black_24dp)
		assertThat(marker.chartView).isNull()
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))
		assertThat(marker.size).isEqualTo(FSize(0f, 0f))

		// setOffset()
		marker.setOffset(MPPointF(1f, 2f))
		assertThat(marker.offset).isEqualTo(MPPointF(1f, 2f))

		marker.setOffset(null)
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		marker.setOffset(3f, 4f)
		assertThat(marker.offset).isEqualTo(MPPointF(3f, 4f))

		// setSize()
		marker.setSize(FSize(1f, 2f))
		assertThat(marker.size).isEqualTo(FSize(1f, 2f))

		marker.setSize(null)
		assertThat(marker.size).isEqualTo(FSize(0f, 0f))

		// setChartView()
		val chart = LineChart(this.activityRule.activity)
		marker.chartView = chart
		assertThat(marker.chartView).isEqualTo(chart)

		marker.chartView = null
		assertThat(marker.chartView as Any?).isNull()

		// getOffsetForDrawingAtPoint()
		marker.chartView = null
		marker.setOffset(null)
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(null)
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-36f, -36f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-30f, -40f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-36f, -36f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-30f, -40f))
	}

	@Test
	@UiThreadTest
	fun markerImage_noDrawable() {
		val marker = MarkerImage(this.activityRule.activity, 0)
		assertThat(marker.chartView).isNull()
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))
		assertThat(marker.size).isEqualTo(FSize(0f, 0f))

		// setOffset()
		marker.setOffset(MPPointF(1f, 2f))
		assertThat(marker.offset).isEqualTo(MPPointF(1f, 2f))

		marker.setOffset(null)
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		marker.setOffset(3f, 4f)
		assertThat(marker.offset).isEqualTo(MPPointF(3f, 4f))

		// setSize()
		marker.setSize(FSize(1f, 2f))
		assertThat(marker.size).isEqualTo(FSize(1f, 2f))

		marker.setSize(null)
		assertThat(marker.size).isEqualTo(FSize(0f, 0f))

		// setChartView()
		val chart = LineChart(this.activityRule.activity)
		marker.chartView = chart
		assertThat(marker.chartView).isEqualTo(chart)

		marker.chartView = null
		assertThat(marker.chartView as Any?).isNull()

		// getOffsetForDrawingAtPoint()
		marker.chartView = null
		marker.setOffset(null)
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(null)
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-30f, -40f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		marker.setSize(FSize(30f, 40f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-30f, -40f))
	}
}
