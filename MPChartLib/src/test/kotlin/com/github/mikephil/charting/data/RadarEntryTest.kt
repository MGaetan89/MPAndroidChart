package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RadarEntryTest : BaseEntryTest<RadarEntry>() {
	@Before
	fun before() {
		this.entry = RadarEntry(2f)
	}

	@Test
	fun constructorY() {
		this.entry = RadarEntry(2f)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

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

		this.entry = RadarEntry(2f, data)

		assertThat(this.entry.x).isEqualTo(0f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

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
