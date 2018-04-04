package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LineDataSetTest : LineRadarDataSetTest<Entry, LineDataSet>() {
	@Before
	fun before() {
		this.dataSet = LineDataSet(mutableListOf(), "LineDataSet")
		this.values = mutableListOf(
			Entry(1f, 2f), Entry(3f, 4f), Entry(5f, 6f)
		)
		this.entry = Entry(7f, 8f)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as LineDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.mode).isEqualTo(dataSet.mode)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.circleRadius).isEqualTo(dataSet.circleRadius)
			assertThat(this.circleHoleRadius).isEqualTo(dataSet.circleHoleRadius)
			assertThat(this.circleColors).containsExactlyElementsIn(dataSet.circleColors).inOrder()
			assertThat(this.dashPathEffect).isEqualTo(dataSet.dashPathEffect)
			assertThat(this.isDrawCirclesEnabled).isEqualTo(dataSet.isDrawCirclesEnabled)
			assertThat(this.isDrawCircleHoleEnabled).isEqualTo(dataSet.isDrawCircleHoleEnabled)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as LineDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.mode).isEqualTo(dataSet.mode)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.circleRadius).isEqualTo(dataSet.circleRadius)
			assertThat(this.circleHoleRadius).isEqualTo(dataSet.circleHoleRadius)
			assertThat(this.circleColors).containsExactlyElementsIn(dataSet.circleColors).inOrder()
			assertThat(this.dashPathEffect).isEqualTo(dataSet.dashPathEffect)
			assertThat(this.isDrawCirclesEnabled).isEqualTo(dataSet.isDrawCirclesEnabled)
			assertThat(this.isDrawCircleHoleEnabled).isEqualTo(dataSet.isDrawCircleHoleEnabled)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}
	}

	@Test
	fun setMode() {
		assertThat(this.dataSet.mode).isEqualTo(LineDataSet.Mode.LINEAR)

		LineDataSet.Mode.values().forEach {
			this.dataSet.mode = it
			assertThat(this.dataSet.mode).isEqualTo(it)
		}
	}

	@Test
	fun setCubicIntensity() {
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.2f)

		this.dataSet.cubicIntensity = 0f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.05f)

		this.dataSet.cubicIntensity = 0.05f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.05f)

		this.dataSet.cubicIntensity = 0.25f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.25f)

		this.dataSet.cubicIntensity = 0.5f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.5f)

		this.dataSet.cubicIntensity = 0.75f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(0.75f)

		this.dataSet.cubicIntensity = 1f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(1f)

		this.dataSet.cubicIntensity = 1.05f
		assertThat(this.dataSet.cubicIntensity).isEqualTo(1f)
	}

	@Test
	fun setCircleRadius() {
		assertThat(this.dataSet.circleRadius).isEqualTo(8f)

		this.dataSet.circleRadius = 0f
		assertThat(this.dataSet.circleRadius).isEqualTo(8f)

		this.dataSet.circleRadius = 1f
		assertThat(this.dataSet.circleRadius).isEqualTo(1f)

		this.dataSet.circleRadius = 5f
		assertThat(this.dataSet.circleRadius).isEqualTo(5f)

		this.dataSet.circleRadius = 12.5f
		assertThat(this.dataSet.circleRadius).isEqualTo(12.5f)

		this.dataSet.circleRadius = -1f
		assertThat(this.dataSet.circleRadius).isEqualTo(12.5f)
	}

	@Test
	fun setCircleHoleRadius() {
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(4f)

		this.dataSet.circleHoleRadius = 0f
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(4f)

		this.dataSet.circleHoleRadius = 0.5f
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(0.5f)

		this.dataSet.circleHoleRadius = 5f
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(5f)

		this.dataSet.circleHoleRadius = 12.5f
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(12.5f)

		this.dataSet.circleHoleRadius = -1f
		assertThat(this.dataSet.circleHoleRadius).isEqualTo(12.5f)
	}

	@Test
	fun isDashedLineEnabled() {
		assertThat(this.dataSet.dashPathEffect).isNull()
		assertThat(this.dataSet.isDashedLineEnabled).isFalse()

		this.dataSet.enableDashedLine(1f, 2f, 3f)
		assertThat(this.dataSet.dashPathEffect).isNotNull()
		assertThat(this.dataSet.isDashedLineEnabled).isTrue()

		this.dataSet.disableDashedLine()
		assertThat(this.dataSet.dashPathEffect).isNull()
		assertThat(this.dataSet.isDashedLineEnabled).isFalse()
	}

	@Test
	fun setDrawCircles() {
		assertThat(this.dataSet.isDrawCirclesEnabled).isTrue()

		this.dataSet.setDrawCircles(false)
		assertThat(this.dataSet.isDrawCirclesEnabled).isFalse()

		this.dataSet.setDrawCircles(true)
		assertThat(this.dataSet.isDrawCirclesEnabled).isTrue()
	}

	@Test
	fun setCircleColorHole() {
		assertThat(this.dataSet.circleHoleColor).isEqualTo(Color.WHITE)

		this.dataSet.setCircleColorHole(Color.RED)
		assertThat(this.dataSet.circleHoleColor).isEqualTo(Color.RED)
	}

	@Test
	fun setDrawCircleHole() {
		assertThat(this.dataSet.isDrawCircleHoleEnabled).isTrue()

		this.dataSet.setDrawCircleHole(false)
		assertThat(this.dataSet.isDrawCircleHoleEnabled).isFalse()

		this.dataSet.setDrawCircleHole(true)
		assertThat(this.dataSet.isDrawCircleHoleEnabled).isTrue()
	}

	@Test
	fun setFillFormatter() {
		assertThat(this.dataSet.fillFormatter).isInstanceOf(DefaultFillFormatter::class.java)

		val formatter = IFillFormatter { _, _ -> 42f }
		this.dataSet.setFillFormatter(formatter)
		assertThat(this.dataSet.fillFormatter).isEqualTo(formatter)

		this.dataSet.setFillFormatter(null)
		assertThat(this.dataSet.fillFormatter).isInstanceOf(DefaultFillFormatter::class.java)
	}
}
