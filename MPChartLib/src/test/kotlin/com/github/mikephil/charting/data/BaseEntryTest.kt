package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class BaseEntryTest<T : BaseEntry> {
	protected lateinit var entry: T

	@Test
	fun setIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry.icon = icon
		assertThat(this.entry.icon).isEqualTo(icon)

		this.entry.icon = null
		assertThat(this.entry.icon as Any?).isNull()
	}

	@Test
	fun setY() {
		this.entry.y = -5f
		assertThat(this.entry.y).isEqualTo(-5f)

		this.entry.y = -2.5f
		assertThat(this.entry.y).isEqualTo(-2.5f)

		this.entry.y = 0f
		assertThat(this.entry.y).isEqualTo(0f)

		this.entry.y = 2.5f
		assertThat(this.entry.y).isEqualTo(2.5f)

		this.entry.y = 5f
		assertThat(this.entry.y).isEqualTo(5f)
	}

	@Test
	fun setData() {
		val data = System.currentTimeMillis()

		this.entry.data = data
		assertThat(this.entry.data!!).isEqualTo(data)

		this.entry.data = null
		assertThat(this.entry.data as Any?).isNull()
	}
}
