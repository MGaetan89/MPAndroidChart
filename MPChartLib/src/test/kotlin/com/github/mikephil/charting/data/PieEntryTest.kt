package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PieEntryTest : BaseEntryTest<PieEntry>() {
	@Before
	fun before() {
		this.entry = PieEntry(2f)
	}

	@Test
	fun constructorY() {
		this.entry = PieEntry(2f)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isNull()
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = PieEntry(2f, data)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isNull()
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = PieEntry(2f, icon)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isNull()
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = PieEntry(2f, icon, data)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isNull()
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYLabel() {
		this.entry = PieEntry(2f, "Hello")

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isEqualTo("Hello")
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYLabelData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = PieEntry(2f, "Hello", data)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isEqualTo("Hello")
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYLabelIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = PieEntry(2f, "Hello", icon)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isEqualTo("Hello")
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}

	@Test
	fun constructorYLabelIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = PieEntry(2f, "Hello", icon, data)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.label).isEqualTo("Hello")
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.value).isEqualTo(2f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.label = ""
		assertThat(this.entry.label).isEmpty()

		this.entry.label = "Hello, World!"
		assertThat(this.entry.label).isEqualTo("Hello, World!")

		this.entry.label = null
		assertThat(this.entry.label as Any?).isNull()

		this.entry.x = -5f
		assertThat(this.entry.x).isEqualTo(-5f)

		this.entry.x = -2.5f
		assertThat(this.entry.x).isEqualTo(-2.5f)

		this.entry.x = 0f
		assertThat(this.entry.x).isEqualTo(0f)

		this.entry.x = 2.5f
		assertThat(this.entry.x).isEqualTo(2.5f)

		this.entry.x = 5f
		assertThat(this.entry.x).isEqualTo(5f)
	}
}
