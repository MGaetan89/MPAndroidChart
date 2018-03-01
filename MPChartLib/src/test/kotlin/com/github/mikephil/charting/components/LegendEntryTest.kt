package com.github.mikephil.charting.components

import android.graphics.Color
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LegendEntryTest {
	@Test
	fun legendEntry() {
		val legendEntry = LegendEntry("Foo", Legend.LegendForm.NONE, 5f, 10f, null, Color.RED)

		assertThat(legendEntry.label).isEqualTo("Foo")
		assertThat(legendEntry.form).isEqualTo(Legend.LegendForm.NONE)
		assertThat(legendEntry.formSize).isEqualTo(5f)
		assertThat(legendEntry.formLineWidth).isEqualTo(10f)
		assertThat(legendEntry.formLineDashEffect).isNull()
		assertThat(legendEntry.formColor).isEqualTo(Color.RED)
	}

	@Test
	fun legendEntry_emptyConstructor() {
		val legendEntry = LegendEntry()

		assertThat(legendEntry.label).isNull()
		assertThat(legendEntry.form).isEqualTo(Legend.LegendForm.DEFAULT)
		assertThat(legendEntry.formSize).isEqualTo(Float.NaN)
		assertThat(legendEntry.formLineWidth).isEqualTo(Float.NaN)
		assertThat(legendEntry.formLineDashEffect).isNull()
		assertThat(legendEntry.formColor).isEqualTo(ColorTemplate.COLOR_NONE)
	}
}
