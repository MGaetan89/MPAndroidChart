package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.Typeface
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class ComponentBaseTest<T : ComponentBase> {
	protected lateinit var component: T

	@Test
	fun xOffset() {
		assertThat(this.component.xOffset).isEqualTo(5f)

		this.component.xOffset = 10f
		assertThat(this.component.xOffset).isEqualTo(10f)
	}

	@Test
	open fun yOffset() {
		assertThat(this.component.yOffset).isEqualTo(5f)

		this.component.yOffset = 10f
		assertThat(this.component.yOffset).isEqualTo(10f)
	}

	@Test
	fun typeface() {
		assertThat(this.component.typeface).isNull()

		this.component.typeface = Typeface.DEFAULT_BOLD
		assertThat(this.component.typeface).isEqualTo(Typeface.DEFAULT_BOLD)
	}

	@Test
	open fun textSize() {
		assertThat(this.component.textSize).isEqualTo(10f)

		this.component.textSize = 5f
		assertThat(this.component.textSize).isEqualTo(6f)

		this.component.textSize = 12f
		assertThat(this.component.textSize).isEqualTo(12f)

		this.component.textSize = 25f
		assertThat(this.component.textSize).isEqualTo(24f)
	}

	@Test
	fun textColor() {
		assertThat(this.component.textColor).isEqualTo(Color.BLACK)

		this.component.textColor = Color.YELLOW
		assertThat(this.component.textColor).isEqualTo(Color.YELLOW)
	}

	@Test
	fun enabled() {
		assertThat(this.component.isEnabled).isTrue()

		this.component.isEnabled = false
		assertThat(this.component.isEnabled).isFalse()

		this.component.isEnabled = true
		assertThat(this.component.isEnabled).isTrue()
	}
}
