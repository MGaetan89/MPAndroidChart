package com.github.mikephil.charting.components

import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.ImageView
import android.widget.RelativeLayout
import com.github.mikephil.charting.R
import com.github.mikephil.charting.TestActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.utils.MPPointF
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarkerViewTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	@Test
	@UiThreadTest
	fun markerView() {
		val marker = MarkerView(this.activityRule.activity, R.layout.layout)
		assertThat(marker.chartView).isNull()
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		// setupLayoutResource()
		val view = marker.findViewById<ImageView>(R.id.image)

		assertThat(marker.childCount).isEqualTo(1)
		assertThat(view).isNotNull()

		with(view.layoutParams) {
			assertThat(this).isNotNull()
			assertThat(this).isInstanceOf(RelativeLayout.LayoutParams::class.java)
			assertThat(this.height).isEqualTo(RelativeLayout.LayoutParams.MATCH_PARENT)
			assertThat(this.width).isEqualTo(RelativeLayout.LayoutParams.MATCH_PARENT)
		}

		// setOffset()
		marker.setOffset(MPPointF(1f, 2f))
		assertThat(marker.offset).isEqualTo(MPPointF(1f, 2f))

		marker.setOffset(null)
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		marker.setOffset(3f, 4f)
		assertThat(marker.offset).isEqualTo(MPPointF(3f, 4f))

		// setChartView()
		val chart = LineChart(this.activityRule.activity)
		marker.chartView = chart
		assertThat(marker.chartView).isEqualTo(chart)

		marker.chartView = null
		assertThat(marker.chartView as Any?).isNull()

		// getOffsetForDrawingAtPoint()
		marker.chartView = null
		marker.setOffset(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-36f, -36f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(-36f, -36f))
	}

	@Test
	@UiThreadTest
	fun markerView_noLayout() {
		val marker = MarkerView(this.activityRule.activity, 0)
		assertThat(marker.chartView).isNull()
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		// setupLayoutResource()
		assertThat(marker.childCount).isEqualTo(0)

		// setOffset()
		marker.setOffset(MPPointF(1f, 2f))
		assertThat(marker.offset).isEqualTo(MPPointF(1f, 2f))

		marker.setOffset(null)
		assertThat(marker.offset).isEqualTo(MPPointF(0f, 0f))

		marker.setOffset(3f, 4f)
		assertThat(marker.offset).isEqualTo(MPPointF(3f, 4f))

		// setChartView()
		val chart = LineChart(this.activityRule.activity)
		marker.chartView = chart
		assertThat(marker.chartView).isEqualTo(chart)

		marker.chartView = null
		assertThat(marker.chartView as Any?).isNull()

		// getOffsetForDrawingAtPoint()
		marker.chartView = null
		marker.setOffset(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = null
		marker.setOffset(MPPointF(10f, 20f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(10f, 20f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(null)
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))

		marker.chartView = LineChart(this.activityRule.activity)
		marker.setOffset(MPPointF(10f, 20f))
		assertThat(marker.getOffsetForDrawingAtPoint(0f, 0f)).isEqualTo(MPPointF(0f, 0f))
	}
}
