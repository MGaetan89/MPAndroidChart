package com.github.mikephil.charting.formatter

import androidx.test.annotation.UiThreadTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.TestActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultFillFormatterTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	private lateinit var formatter: DefaultFillFormatter

	@Before
	fun before() {
		this.formatter = DefaultFillFormatter()
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition() {
		val yVals = listOf(Entry(0f, 0f), Entry(-1f, -1f), Entry(2f, 2f), Entry(1.3f, 1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_differentData() {
		val yVals = listOf(Entry(0f, 0f), Entry(-1f, -1f), Entry(2f, 2f), Entry(1.3f, 1.3f))
		val yVals2 = listOf(Entry(0f, 0f), Entry(1f, 1f), Entry(-2f, -2f), Entry(-1.3f, -1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataSet2 = LineDataSet(yVals2, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet2))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_differentData_negative() {
		val yVals = listOf(Entry(0f, 0f), Entry(-1f, -1f), Entry(-2f, -2f), Entry(-1.3f, -1.3f))
		val yVals2 = listOf(Entry(0f, 0f), Entry(1f, 1f), Entry(2f, 2f), Entry(1.3f, 1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataSet2 = LineDataSet(yVals2, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet2))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_differentData_positive() {
		val yVals = listOf(Entry(0f, 0f), Entry(1f, 1f), Entry(2f, 2f), Entry(1.3f, 1.3f))
		val yVals2 = listOf(Entry(0f, 0f), Entry(-1f, -1f), Entry(-2f, -2f), Entry(-1.3f, -1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataSet2 = LineDataSet(yVals2, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet2))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_empty() {
		val dataSet = LineDataSet(emptyList(), "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider))
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_negative() {
		val yVals = listOf(Entry(0f, 0f), Entry(-1f, -1f), Entry(-2f, -2f), Entry(-1.3f, -1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(0.19999999f)
	}

	@Test
	@UiThreadTest
	fun getFillLinePosition_positive() {
		val yVals = listOf(Entry(0f, 0f), Entry(1f, 1f), Entry(2f, 2f), Entry(1.3f, 1.3f))
		val dataSet = LineDataSet(yVals, "")
		val dataProvider = LineChart(this.activityRule.activity)
		dataProvider.data = LineData(listOf(dataSet))

		val formatter = DefaultFillFormatter()
		assertThat(formatter.getFillLinePosition(dataSet, dataProvider)).isEqualTo(-0.19999999f)
	}
}
