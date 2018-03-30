package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CandleEntryTest : BaseEntryTest<CandleEntry>() {
	@Before
	fun before() {
		this.entry = CandleEntry(1f, 3f, 2f, 4f, 5f)
	}

	@Test
	fun constructorXHighLowOpenClose() {
		this.entry = CandleEntry(1f, 3f, 2f, 4f, 5f)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2.5f)
		assertThat(this.entry.high).isEqualTo(3f)
		assertThat(this.entry.low).isEqualTo(2f)
		assertThat(this.entry.open).isEqualTo(4f)
		assertThat(this.entry.close).isEqualTo(5f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.shadowRange).isEqualTo(1f)
		assertThat(this.entry.bodyRange).isEqualTo(1f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.high = -5f
		assertThat(this.entry.high).isEqualTo(-5f)

		this.entry.high = -2.5f
		assertThat(this.entry.high).isEqualTo(-2.5f)

		this.entry.high = 0f
		assertThat(this.entry.high).isEqualTo(0f)

		this.entry.high = 2.5f
		assertThat(this.entry.high).isEqualTo(2.5f)

		this.entry.high = 5f
		assertThat(this.entry.high).isEqualTo(5f)

		this.entry.low = -5f
		assertThat(this.entry.low).isEqualTo(-5f)

		this.entry.low = -2.5f
		assertThat(this.entry.low).isEqualTo(-2.5f)

		this.entry.low = 0f
		assertThat(this.entry.low).isEqualTo(0f)

		this.entry.low = 2.5f
		assertThat(this.entry.low).isEqualTo(2.5f)

		this.entry.low = 5f
		assertThat(this.entry.low).isEqualTo(5f)

		this.entry.open = -5f
		assertThat(this.entry.open).isEqualTo(-5f)

		this.entry.open = -2.5f
		assertThat(this.entry.open).isEqualTo(-2.5f)

		this.entry.open = 0f
		assertThat(this.entry.open).isEqualTo(0f)

		this.entry.open = 2.5f
		assertThat(this.entry.open).isEqualTo(2.5f)

		this.entry.open = 5f
		assertThat(this.entry.open).isEqualTo(5f)

		this.entry.close = -5f
		assertThat(this.entry.close).isEqualTo(-5f)

		this.entry.close = -2.5f
		assertThat(this.entry.close).isEqualTo(-2.5f)

		this.entry.close = 0f
		assertThat(this.entry.close).isEqualTo(0f)

		this.entry.close = 2.5f
		assertThat(this.entry.close).isEqualTo(2.5f)

		this.entry.close = 5f
		assertThat(this.entry.close).isEqualTo(5f)
	}

	@Test
	fun constructorXHighLowOpenCloseData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = CandleEntry(1f, 3f, 2f, 4f, 5f, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2.5f)
		assertThat(this.entry.high).isEqualTo(3f)
		assertThat(this.entry.low).isEqualTo(2f)
		assertThat(this.entry.open).isEqualTo(4f)
		assertThat(this.entry.close).isEqualTo(5f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		assertThat(this.entry.shadowRange).isEqualTo(1f)
		assertThat(this.entry.bodyRange).isEqualTo(1f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.high = -5f
		assertThat(this.entry.high).isEqualTo(-5f)

		this.entry.high = -2.5f
		assertThat(this.entry.high).isEqualTo(-2.5f)

		this.entry.high = 0f
		assertThat(this.entry.high).isEqualTo(0f)

		this.entry.high = 2.5f
		assertThat(this.entry.high).isEqualTo(2.5f)

		this.entry.high = 5f
		assertThat(this.entry.high).isEqualTo(5f)

		this.entry.low = -5f
		assertThat(this.entry.low).isEqualTo(-5f)

		this.entry.low = -2.5f
		assertThat(this.entry.low).isEqualTo(-2.5f)

		this.entry.low = 0f
		assertThat(this.entry.low).isEqualTo(0f)

		this.entry.low = 2.5f
		assertThat(this.entry.low).isEqualTo(2.5f)

		this.entry.low = 5f
		assertThat(this.entry.low).isEqualTo(5f)

		this.entry.open = -5f
		assertThat(this.entry.open).isEqualTo(-5f)

		this.entry.open = -2.5f
		assertThat(this.entry.open).isEqualTo(-2.5f)

		this.entry.open = 0f
		assertThat(this.entry.open).isEqualTo(0f)

		this.entry.open = 2.5f
		assertThat(this.entry.open).isEqualTo(2.5f)

		this.entry.open = 5f
		assertThat(this.entry.open).isEqualTo(5f)

		this.entry.close = -5f
		assertThat(this.entry.close).isEqualTo(-5f)

		this.entry.close = -2.5f
		assertThat(this.entry.close).isEqualTo(-2.5f)

		this.entry.close = 0f
		assertThat(this.entry.close).isEqualTo(0f)

		this.entry.close = 2.5f
		assertThat(this.entry.close).isEqualTo(2.5f)

		this.entry.close = 5f
		assertThat(this.entry.close).isEqualTo(5f)
	}

	@Test
	fun constructorXHighLowOpenCloseIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = CandleEntry(1f, 3f, 2f, 4f, 5f, icon)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2.5f)
		assertThat(this.entry.high).isEqualTo(3f)
		assertThat(this.entry.low).isEqualTo(2f)
		assertThat(this.entry.open).isEqualTo(4f)
		assertThat(this.entry.close).isEqualTo(5f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.shadowRange).isEqualTo(1f)
		assertThat(this.entry.bodyRange).isEqualTo(1f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.high = -5f
		assertThat(this.entry.high).isEqualTo(-5f)

		this.entry.high = -2.5f
		assertThat(this.entry.high).isEqualTo(-2.5f)

		this.entry.high = 0f
		assertThat(this.entry.high).isEqualTo(0f)

		this.entry.high = 2.5f
		assertThat(this.entry.high).isEqualTo(2.5f)

		this.entry.high = 5f
		assertThat(this.entry.high).isEqualTo(5f)

		this.entry.low = -5f
		assertThat(this.entry.low).isEqualTo(-5f)

		this.entry.low = -2.5f
		assertThat(this.entry.low).isEqualTo(-2.5f)

		this.entry.low = 0f
		assertThat(this.entry.low).isEqualTo(0f)

		this.entry.low = 2.5f
		assertThat(this.entry.low).isEqualTo(2.5f)

		this.entry.low = 5f
		assertThat(this.entry.low).isEqualTo(5f)

		this.entry.open = -5f
		assertThat(this.entry.open).isEqualTo(-5f)

		this.entry.open = -2.5f
		assertThat(this.entry.open).isEqualTo(-2.5f)

		this.entry.open = 0f
		assertThat(this.entry.open).isEqualTo(0f)

		this.entry.open = 2.5f
		assertThat(this.entry.open).isEqualTo(2.5f)

		this.entry.open = 5f
		assertThat(this.entry.open).isEqualTo(5f)

		this.entry.close = -5f
		assertThat(this.entry.close).isEqualTo(-5f)

		this.entry.close = -2.5f
		assertThat(this.entry.close).isEqualTo(-2.5f)

		this.entry.close = 0f
		assertThat(this.entry.close).isEqualTo(0f)

		this.entry.close = 2.5f
		assertThat(this.entry.close).isEqualTo(2.5f)

		this.entry.close = 5f
		assertThat(this.entry.close).isEqualTo(5f)
	}

	@Test
	fun constructorXHighLowOpenCloseIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = CandleEntry(1f, 3f, 2f, 4f, 5f, icon, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2.5f)
		assertThat(this.entry.high).isEqualTo(3f)
		assertThat(this.entry.low).isEqualTo(2f)
		assertThat(this.entry.open).isEqualTo(4f)
		assertThat(this.entry.close).isEqualTo(5f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		assertThat(this.entry.shadowRange).isEqualTo(1f)
		assertThat(this.entry.bodyRange).isEqualTo(1f)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.high = -5f
		assertThat(this.entry.high).isEqualTo(-5f)

		this.entry.high = -2.5f
		assertThat(this.entry.high).isEqualTo(-2.5f)

		this.entry.high = 0f
		assertThat(this.entry.high).isEqualTo(0f)

		this.entry.high = 2.5f
		assertThat(this.entry.high).isEqualTo(2.5f)

		this.entry.high = 5f
		assertThat(this.entry.high).isEqualTo(5f)

		this.entry.low = -5f
		assertThat(this.entry.low).isEqualTo(-5f)

		this.entry.low = -2.5f
		assertThat(this.entry.low).isEqualTo(-2.5f)

		this.entry.low = 0f
		assertThat(this.entry.low).isEqualTo(0f)

		this.entry.low = 2.5f
		assertThat(this.entry.low).isEqualTo(2.5f)

		this.entry.low = 5f
		assertThat(this.entry.low).isEqualTo(5f)

		this.entry.open = -5f
		assertThat(this.entry.open).isEqualTo(-5f)

		this.entry.open = -2.5f
		assertThat(this.entry.open).isEqualTo(-2.5f)

		this.entry.open = 0f
		assertThat(this.entry.open).isEqualTo(0f)

		this.entry.open = 2.5f
		assertThat(this.entry.open).isEqualTo(2.5f)

		this.entry.open = 5f
		assertThat(this.entry.open).isEqualTo(5f)

		this.entry.close = -5f
		assertThat(this.entry.close).isEqualTo(-5f)

		this.entry.close = -2.5f
		assertThat(this.entry.close).isEqualTo(-2.5f)

		this.entry.close = 0f
		assertThat(this.entry.close).isEqualTo(0f)

		this.entry.close = 2.5f
		assertThat(this.entry.close).isEqualTo(2.5f)

		this.entry.close = 5f
		assertThat(this.entry.close).isEqualTo(5f)
	}
}
