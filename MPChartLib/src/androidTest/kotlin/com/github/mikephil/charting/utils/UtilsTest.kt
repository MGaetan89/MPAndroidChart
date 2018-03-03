package com.github.mikephil.charting.utils

import android.graphics.Paint
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilsTest {
	@Test
	fun init() {
		Utils.init(InstrumentationRegistry.getTargetContext())

		assertThat(Utils.convertDpToPixel(0f)).isEqualTo(0f)
		assertThat(Utils.convertDpToPixel(5.5f)).isEqualTo(19.25f)
		assertThat(Utils.convertDpToPixel(10f)).isEqualTo(35f)
		assertThat(Utils.getMinimumFlingVelocity()).isEqualTo(175)
		assertThat(Utils.getMaximumFlingVelocity()).isEqualTo(28000)

		Utils.init(null)

		assertThat(Utils.convertDpToPixel(0f)).isEqualTo(0f)
		assertThat(Utils.convertDpToPixel(5.5f)).isEqualTo(5.5f)
		assertThat(Utils.convertDpToPixel(10f)).isEqualTo(10f)
		assertThat(Utils.getMinimumFlingVelocity()).isEqualTo(50)
		assertThat(Utils.getMaximumFlingVelocity()).isEqualTo(8000)
	}

	@Test
	fun calcTextWidth() {
		val paint = Paint()

		assertThat(Utils.calcTextWidth(paint, "")).isEqualTo(0)
		assertThat(Utils.calcTextWidth(paint, "hello")).isEqualTo(28)
		assertThat(Utils.calcTextWidth(paint, "gif^")).isEqualTo(20)
	}

	@Test
	fun calcTextHeight() {
		val paint = Paint()

		assertThat(Utils.calcTextHeight(paint, "")).isEqualTo(0)
		assertThat(Utils.calcTextHeight(paint, "hello")).isEqualTo(10)
		assertThat(Utils.calcTextHeight(paint, "gif^")).isEqualTo(13)
	}

	@Test
	fun getLineHeight() {
		val paint = Paint()

		assertThat(Utils.getLineHeight(paint)).isEqualTo(14.0625f)
	}

	@Test
	fun getLineSpacing() {
		val paint = Paint()

		assertThat(Utils.getLineSpacing(paint)).isEqualTo(4.7929688f)
	}

	@Test
	fun calcTextSize() {
		val paint = Paint()

		assertThat(Utils.calcTextSize(paint, "")).isEqualTo(FSize(0f, 0f))
		assertThat(Utils.calcTextSize(paint, "hello")).isEqualTo(FSize(26f, 10f))
		assertThat(Utils.calcTextSize(paint, "gif^")).isEqualTo(FSize(19f, 13f))
	}

	@Test
	fun velocityTrackerPointerUpCleanUpIfNecessary() {
		// TODO
	}

	@Test
	fun drawImage() {
		// TODO
	}

	@Test
	fun drawXAxisValue() {
		// TODO
	}
}
