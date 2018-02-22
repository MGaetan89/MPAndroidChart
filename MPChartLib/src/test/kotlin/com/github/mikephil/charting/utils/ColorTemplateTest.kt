package com.github.mikephil.charting.utils

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ColorTemplateTest {
	@Test
	fun colorWithAlpha() {
		assertThat(ColorTemplate.colorWithAlpha(Color.BLACK, 0)).isEqualTo(0x00000000)
		assertThat(ColorTemplate.colorWithAlpha(Color.BLACK, 127)).isEqualTo(0x7F000000)
		assertThat(ColorTemplate.colorWithAlpha(Color.BLACK, 255)).isEqualTo(Color.BLACK)

		assertThat(ColorTemplate.colorWithAlpha(Color.YELLOW, 0)).isEqualTo(0x00FFFF00)
		assertThat(ColorTemplate.colorWithAlpha(Color.YELLOW, 127)).isEqualTo(0x7FFFFF00)
		assertThat(ColorTemplate.colorWithAlpha(Color.YELLOW, 255)).isEqualTo(Color.YELLOW)

		assertThat(ColorTemplate.colorWithAlpha(Color.WHITE, 0)).isEqualTo(0x00FFFFFF)
		assertThat(ColorTemplate.colorWithAlpha(Color.WHITE, 127)).isEqualTo(0x7FFFFFFF)
		assertThat(ColorTemplate.colorWithAlpha(Color.WHITE, 255)).isEqualTo(Color.WHITE)
	}

	@Test
	fun createColors() {
		assertThat(ColorTemplate.createColors(intArrayOf())).isEmpty()
		assertThat(ColorTemplate.createColors(intArrayOf(Color.BLACK, Color.YELLOW, Color.WHITE)))
			.containsExactly(Color.BLACK, Color.YELLOW, Color.WHITE).inOrder()
		assertThat(ColorTemplate.createColors(intArrayOf(Color.BLACK, Color.YELLOW, Color.BLACK)))
			.containsExactly(Color.BLACK, Color.YELLOW, Color.BLACK).inOrder()
	}
}
