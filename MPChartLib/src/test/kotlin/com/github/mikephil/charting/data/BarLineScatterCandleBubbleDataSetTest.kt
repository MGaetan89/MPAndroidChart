package com.github.mikephil.charting.data

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class BarLineScatterCandleBubbleDataSetTest<E : Entry, T : BarLineScatterCandleBubbleDataSet<E>> :
	DataSetTest<E, T>() {

	@Test
	open fun setHighLightColor() {
		assertThat(this.dataSet.highLightColor).isEqualTo(0xFFBB73)

		this.dataSet.highLightColor = Color.RED
		assertThat(this.dataSet.highLightColor).isEqualTo(Color.RED)
	}
}
