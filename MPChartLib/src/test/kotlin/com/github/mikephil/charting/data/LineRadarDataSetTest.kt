package com.github.mikephil.charting.data

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class LineRadarDataSetTest<E : Entry, T : LineRadarDataSet<E>> :
	LineScatterCandleRadarDataSetTest<E, T>() {
	@Test
	fun setFillColor() {
		assertThat(this.dataSet.fillColor).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.fillDrawable).isNull()

		this.dataSet.fillColor = Color.RED
		assertThat(this.dataSet.fillColor).isEqualTo(Color.RED)
		assertThat(this.dataSet.fillDrawable).isNull()
	}

	@Test
	fun setFillAlpha() {
		assertThat(this.dataSet.fillAlpha).isEqualTo(85)

		this.dataSet.fillAlpha = -50
		assertThat(this.dataSet.fillAlpha).isEqualTo(-50)

		this.dataSet.fillAlpha = 0
		assertThat(this.dataSet.fillAlpha).isEqualTo(0)

		this.dataSet.fillAlpha = 50
		assertThat(this.dataSet.fillAlpha).isEqualTo(50)

		this.dataSet.fillAlpha = 100
		assertThat(this.dataSet.fillAlpha).isEqualTo(100)

		this.dataSet.fillAlpha = 150
		assertThat(this.dataSet.fillAlpha).isEqualTo(150)

		this.dataSet.fillAlpha = 200
		assertThat(this.dataSet.fillAlpha).isEqualTo(200)

		this.dataSet.fillAlpha = 250
		assertThat(this.dataSet.fillAlpha).isEqualTo(250)

		this.dataSet.fillAlpha = 300
		assertThat(this.dataSet.fillAlpha).isEqualTo(300)
	}

	@Test
	fun setLineWidth() {
		assertThat(this.dataSet.lineWidth).isEqualTo(2.5f)

		this.dataSet.lineWidth = -5f
		assertThat(this.dataSet.lineWidth).isEqualTo(0f)

		this.dataSet.lineWidth = 0f
		assertThat(this.dataSet.lineWidth).isEqualTo(0f)

		this.dataSet.lineWidth = 1f
		assertThat(this.dataSet.lineWidth).isEqualTo(1f)

		this.dataSet.lineWidth = 2.5f
		assertThat(this.dataSet.lineWidth).isEqualTo(2.5f)

		this.dataSet.lineWidth = 10f
		assertThat(this.dataSet.lineWidth).isEqualTo(10f)

		this.dataSet.lineWidth = 11f
		assertThat(this.dataSet.lineWidth).isEqualTo(10f)
	}

	@Test
	fun setDrawFilled() {
		assertThat(this.dataSet.isDrawFilledEnabled).isFalse()

		this.dataSet.setDrawFilled(true)
		assertThat(this.dataSet.isDrawFilledEnabled).isTrue()

		this.dataSet.setDrawFilled(false)
		assertThat(this.dataSet.isDrawFilledEnabled).isFalse()
	}
}
