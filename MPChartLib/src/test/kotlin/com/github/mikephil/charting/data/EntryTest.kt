package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class EntryTest : BaseEntryTest<Entry>() {
	@Before
	fun before() {
		this.entry = Entry()
	}

	@Test
	fun constructorXY() {
		this.entry = Entry(1f, 2f)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

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

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		assertThat(this.entry.toString()).isEqualTo("Entry, x: 5.0 y: 2.0")
		assertThat(this.entry.describeContents()).isEqualTo(0)
	}

	@Test
	fun constructorXYData() {
		val data = System.currentTimeMillis()

		this.entry = Entry(1f, 2f, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isEqualTo(data)

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

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		assertThat(this.entry.toString()).isEqualTo("Entry, x: 5.0 y: 2.0")
		assertThat(this.entry.describeContents()).isEqualTo(0)
	}

	@Test
	fun constructorXYIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = Entry(1f, 2f, icon)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

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

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		assertThat(this.entry.toString()).isEqualTo("Entry, x: 5.0 y: 2.0")
		assertThat(this.entry.describeContents()).isEqualTo(0)
	}

	@Test
	fun emptyConstructor() {
		this.entry = Entry()

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(0f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

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

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		assertThat(this.entry.toString()).isEqualTo("Entry, x: 5.0 y: 0.0")
		assertThat(this.entry.describeContents()).isEqualTo(0)
	}

	@Test
	fun equalTo() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)
		val entry1 = Entry(1f, 2f, icon, data)
		val entry2 = Entry(1f, 2f, icon, data)

		assertThat(entry1.equalTo(null)).isFalse()
		assertThat(entry1.equalTo(entry2)).isTrue()

		entry2.y = 3f
		assertThat(entry1.equalTo(entry2)).isFalse()

		entry2.x = 2f
		assertThat(entry1.equalTo(entry2)).isFalse()

		entry2.data = null
		assertThat(entry1.equalTo(entry2)).isFalse()
	}

	@Test
	fun fullConstructor() {
		val data = System.currentTimeMillis()
		val icon = ColorDrawable(Color.RED)

		this.entry = Entry(1f, 2f, icon, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

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

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		assertThat(this.entry.toString()).isEqualTo("Entry, x: 5.0 y: 2.0")
		assertThat(this.entry.describeContents()).isEqualTo(0)
	}

	@Test
	fun creator() {
		assertThat(Entry.CREATOR.newArray(0)).isEmpty()
		assertThat(Entry.CREATOR.newArray(1)).asList().containsExactly(null)
		assertThat(Entry.CREATOR.newArray(5)).asList().containsExactly(null, null, null, null, null)
	}
}
