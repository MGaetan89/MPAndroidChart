package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BubbleEntryTest : BaseEntryTest<BubbleEntry>() {
	@Before
	fun before() {
		this.entry = BubbleEntry(1f, 2f, 3f)
	}

	@Test
	fun constructorXYSize() {
		this.entry = BubbleEntry(1f, 2f, 3f)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.size).isEqualTo(3f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.size = -5f
		assertThat(this.entry.size).isEqualTo(-5f)

		this.entry.size = -2.5f
		assertThat(this.entry.size).isEqualTo(-2.5f)

		this.entry.size = 0f
		assertThat(this.entry.size).isEqualTo(0f)

		this.entry.size = 2.5f
		assertThat(this.entry.size).isEqualTo(2.5f)

		this.entry.size = 5f
		assertThat(this.entry.size).isEqualTo(5f)
	}

	@Test
	fun constructorXYSizeData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = BubbleEntry(1f, 2f, 3f, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.size).isEqualTo(3f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.size = -5f
		assertThat(this.entry.size).isEqualTo(-5f)

		this.entry.size = -2.5f
		assertThat(this.entry.size).isEqualTo(-2.5f)

		this.entry.size = 0f
		assertThat(this.entry.size).isEqualTo(0f)

		this.entry.size = 2.5f
		assertThat(this.entry.size).isEqualTo(2.5f)

		this.entry.size = 5f
		assertThat(this.entry.size).isEqualTo(5f)
	}

	@Test
	fun constructorXYSizeIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = BubbleEntry(1f, 2f, 3f, icon)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.size).isEqualTo(3f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.size = -5f
		assertThat(this.entry.size).isEqualTo(-5f)

		this.entry.size = -2.5f
		assertThat(this.entry.size).isEqualTo(-2.5f)

		this.entry.size = 0f
		assertThat(this.entry.size).isEqualTo(0f)

		this.entry.size = 2.5f
		assertThat(this.entry.size).isEqualTo(2.5f)

		this.entry.size = 5f
		assertThat(this.entry.size).isEqualTo(5f)
	}

	@Test
	fun constructorXYSizeIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = BubbleEntry(1f, 2f, 3f, icon, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.size).isEqualTo(3f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.size = -5f
		assertThat(this.entry.size).isEqualTo(-5f)

		this.entry.size = -2.5f
		assertThat(this.entry.size).isEqualTo(-2.5f)

		this.entry.size = 0f
		assertThat(this.entry.size).isEqualTo(0f)

		this.entry.size = 2.5f
		assertThat(this.entry.size).isEqualTo(2.5f)

		this.entry.size = 5f
		assertThat(this.entry.size).isEqualTo(5f)
	}
}
