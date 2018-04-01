package com.github.mikephil.charting.data

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BarDataSetTest : BarLineScatterCandleBubbleDataSetTest<BarEntry, BarDataSet>() {
	@Before
	fun before() {
		this.dataSet = BarDataSet(mutableListOf(), "BarDataSet")
		this.values = mutableListOf(BarEntry(1f, 2f), BarEntry(3f, 4f), BarEntry(5f, 6f))
		this.entry = BarEntry(7f, 8f)
	}

	@Test
	override fun setHighLightColor() {
		assertThat(this.dataSet.highLightColor).isEqualTo(Color.BLACK)

		this.dataSet.highLightColor = Color.RED
		assertThat(this.dataSet.highLightColor).isEqualTo(Color.RED)
	}
}
