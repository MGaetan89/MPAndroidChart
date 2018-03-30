package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BarEntryTest : BaseEntryTest<BarEntry>() {
	@Before
	fun before() {
		this.entry = BarEntry(1f, 2f)
	}

	@Test
	fun constructorXValues() {
		this.entry = BarEntry(1f, floatArrayOf(2f, -3f, 4f))

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(3f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(2f, -3f, 4f)
		with(this.entry.ranges) {
			assertThat(this).hasLength(3)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(2f)
			assertThat(this[1].from).isEqualTo(-3f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(2f)
			assertThat(this[2].to).isEqualTo(6f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(1f)
		assertThat(this.entry.positiveSum).isEqualTo(6f)
		assertThat(this.entry.negativeSum).isEqualTo(3f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		val values = floatArrayOf(1f, -2f, 3f, 4f)

		this.entry.setVals(values)
		assertThat(this.entry.y).isEqualTo(6f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(values)
		with(this.entry.ranges) {
			assertThat(this).hasLength(4)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(1f)
			assertThat(this[1].from).isEqualTo(-2f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(1f)
			assertThat(this[2].to).isEqualTo(4f)
			assertThat(this[3].from).isEqualTo(4f)
			assertThat(this[3].to).isEqualTo(8f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(5f)
		assertThat(this.entry.positiveSum).isEqualTo(8f)
		assertThat(this.entry.negativeSum).isEqualTo(2f)
	}

	@Test
	fun constructorXValuesData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = BarEntry(1f, floatArrayOf(2f, -3f, 4f), data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(3f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(2f, -3f, 4f)
		with(this.entry.ranges) {
			assertThat(this).hasLength(3)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(2f)
			assertThat(this[1].from).isEqualTo(-3f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(2f)
			assertThat(this[2].to).isEqualTo(6f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(1f)
		assertThat(this.entry.positiveSum).isEqualTo(6f)
		assertThat(this.entry.negativeSum).isEqualTo(3f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.setVals(null)
		assertThat(this.entry.y).isEqualTo(0f)
		assertThat(this.entry.yVals).isNull()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
	}

	@Test
	fun constructorXValuesIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = BarEntry(1f, floatArrayOf(2f, -3f, 4f), icon)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(3f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(2f, -3f, 4f)
		with(this.entry.ranges) {
			assertThat(this).hasLength(3)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(2f)
			assertThat(this[1].from).isEqualTo(-3f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(2f)
			assertThat(this[2].to).isEqualTo(6f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(1f)
		assertThat(this.entry.positiveSum).isEqualTo(6f)
		assertThat(this.entry.negativeSum).isEqualTo(3f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.setVals(floatArrayOf())
		assertThat(this.entry.y).isEqualTo(0f)
		assertThat(this.entry.yVals).isEmpty()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
	}

	@Test
	fun constructorXValuesIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = BarEntry(1f, floatArrayOf(2f, -3f, 4f), icon, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(3f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(2f, -3f, 4f)
		with(this.entry.ranges) {
			assertThat(this).hasLength(3)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(2f)
			assertThat(this[1].from).isEqualTo(-3f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(2f)
			assertThat(this[2].to).isEqualTo(6f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(1f)
		assertThat(this.entry.positiveSum).isEqualTo(6f)
		assertThat(this.entry.negativeSum).isEqualTo(3f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		this.entry.setVals(floatArrayOf())
		assertThat(this.entry.y).isEqualTo(0f)
		assertThat(this.entry.yVals).isEmpty()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
	}

	@Test
	fun constructorXY() {
		this.entry = BarEntry(1f, 2f)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.yVals).isNull()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		val values = floatArrayOf(1f, -2f, 3f, 4f)

		this.entry.setVals(values)
		assertThat(this.entry.y).isEqualTo(6f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(values)
		with(this.entry.ranges) {
			assertThat(this).hasLength(4)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(1f)
			assertThat(this[1].from).isEqualTo(-2f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(1f)
			assertThat(this[2].to).isEqualTo(4f)
			assertThat(this[3].from).isEqualTo(4f)
			assertThat(this[3].to).isEqualTo(8f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(5f)
		assertThat(this.entry.positiveSum).isEqualTo(8f)
		assertThat(this.entry.negativeSum).isEqualTo(2f)
	}

	@Test
	fun constructorXYData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long

		this.entry = BarEntry(1f, 2f, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.yVals).isNull()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isNull()

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		val values = floatArrayOf(1f, -2f, 3f, 4f)

		this.entry.setVals(values)
		assertThat(this.entry.y).isEqualTo(6f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(values)
		with(this.entry.ranges) {
			assertThat(this).hasLength(4)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(1f)
			assertThat(this[1].from).isEqualTo(-2f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(1f)
			assertThat(this[2].to).isEqualTo(4f)
			assertThat(this[3].from).isEqualTo(4f)
			assertThat(this[3].to).isEqualTo(8f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(5f)
		assertThat(this.entry.positiveSum).isEqualTo(8f)
		assertThat(this.entry.negativeSum).isEqualTo(2f)
	}

	@Test
	fun constructorXYIcon() {
		val icon = ColorDrawable(Color.RED)

		this.entry = BarEntry(1f, 2f, icon)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.yVals).isNull()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
		assertThat(this.entry.data).isNull()
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		val values = floatArrayOf(1f, -2f, 3f, 4f)

		this.entry.setVals(values)
		assertThat(this.entry.y).isEqualTo(6f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(values)
		with(this.entry.ranges) {
			assertThat(this).hasLength(4)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(1f)
			assertThat(this[1].from).isEqualTo(-2f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(1f)
			assertThat(this[2].to).isEqualTo(4f)
			assertThat(this[3].from).isEqualTo(4f)
			assertThat(this[3].to).isEqualTo(8f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(5f)
		assertThat(this.entry.positiveSum).isEqualTo(8f)
		assertThat(this.entry.negativeSum).isEqualTo(2f)
	}

	@Test
	fun constructorXYIconData() {
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		val data = System.currentTimeMillis() as java.lang.Long
		val icon = ColorDrawable(Color.RED)

		this.entry = BarEntry(1f, 2f, icon, data)

		assertThat(this.entry.x).isEqualTo(1f)
		assertThat(this.entry.y).isEqualTo(2f)
		assertThat(this.entry.yVals).isNull()
		assertThat(this.entry.ranges).isNull()
		assertThat(this.entry.isStacked).isFalse()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(0f)
		assertThat(this.entry.positiveSum).isEqualTo(0f)
		assertThat(this.entry.negativeSum).isEqualTo(0f)
		assertThat(this.entry.data).isEqualTo(data)
		assertThat(this.entry.icon).isEqualTo(icon)

		val copy = this.entry.copy()
		assertThat(copy).isNotSameAs(this.entry)
		assertThat(copy.equalTo(this.entry)).isTrue()

		val values = floatArrayOf(1f, -2f, 3f, 4f)

		this.entry.setVals(values)
		assertThat(this.entry.y).isEqualTo(6f)
		assertThat(this.entry.yVals).usingExactEquality().containsExactly(values)
		with(this.entry.ranges) {
			assertThat(this).hasLength(4)
			assertThat(this[0].from).isEqualTo(0f)
			assertThat(this[0].to).isEqualTo(1f)
			assertThat(this[1].from).isEqualTo(-2f)
			assertThat(this[1].to).isEqualTo(0f)
			assertThat(this[2].from).isEqualTo(1f)
			assertThat(this[2].to).isEqualTo(4f)
			assertThat(this[3].from).isEqualTo(4f)
			assertThat(this[3].to).isEqualTo(8f)
		}
		assertThat(this.entry.isStacked).isTrue()
		assertThat(this.entry.getSumBelow(0)).isEqualTo(5f)
		assertThat(this.entry.positiveSum).isEqualTo(8f)
		assertThat(this.entry.negativeSum).isEqualTo(2f)
	}
}
