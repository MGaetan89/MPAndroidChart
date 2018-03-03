package com.github.mikephil.charting.utils

import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsUnitTest {
	@Test
	fun getDefaultValueFormatter() {
		val formatter = Utils.getDefaultValueFormatter()

		assertThat((formatter as DefaultValueFormatter).decimalDigits).isEqualTo(1)
	}

	@Test
	fun formatNumber() {
		assertThat(Utils.formatNumber(-1234.56f, 0, false)).isEqualTo("-1235")
		assertThat(Utils.formatNumber(-1234.56f, 0, true)).isEqualTo("-1.235")
		assertThat(Utils.formatNumber(-1234.56f, 2, false)).isEqualTo("-1234,56")
		assertThat(Utils.formatNumber(-1234.56f, 2, true)).isEqualTo("-1.234,56")
		assertThat(Utils.formatNumber(-1234.56f, 11, false)).isEqualTo("-2,147483647")
		assertThat(Utils.formatNumber(-1234.56f, 11, true)).isEqualTo("-2,147483647")
		assertThat(Utils.formatNumber(-0.5f, 0, false)).isEqualTo("-01")
		assertThat(Utils.formatNumber(-0.5f, 0, true)).isEqualTo("-01")
		assertThat(Utils.formatNumber(-0.5f, 2, false)).isEqualTo("-0,50")
		assertThat(Utils.formatNumber(-0.5f, 2, true)).isEqualTo("-0,50")
		assertThat(Utils.formatNumber(-0.5f, 11, false)).isEqualTo("-0,500000000")
		assertThat(Utils.formatNumber(-0.5f, 11, true)).isEqualTo("-0,500000000")
		assertThat(Utils.formatNumber(0f, 0, false)).isEqualTo("0")
		assertThat(Utils.formatNumber(0f, 0, true)).isEqualTo("0")
		assertThat(Utils.formatNumber(0f, 2, false)).isEqualTo("0")
		assertThat(Utils.formatNumber(0f, 2, true)).isEqualTo("0")
		assertThat(Utils.formatNumber(0f, 11, false)).isEqualTo("0")
		assertThat(Utils.formatNumber(0f, 11, true)).isEqualTo("0")
		assertThat(Utils.formatNumber(0.5f, 0, false)).isEqualTo("01")
		assertThat(Utils.formatNumber(0.5f, 0, true)).isEqualTo("01")
		assertThat(Utils.formatNumber(0.5f, 2, false)).isEqualTo("0,50")
		assertThat(Utils.formatNumber(0.5f, 2, true)).isEqualTo("0,50")
		assertThat(Utils.formatNumber(0.5f, 11, false)).isEqualTo("0,500000000")
		assertThat(Utils.formatNumber(0.5f, 11, true)).isEqualTo("0,500000000")
		assertThat(Utils.formatNumber(1234.56f, 0, false)).isEqualTo("1235")
		assertThat(Utils.formatNumber(1234.56f, 0, true)).isEqualTo("1.235")
		assertThat(Utils.formatNumber(1234.56f, 2, false)).isEqualTo("1234,56")
		assertThat(Utils.formatNumber(1234.56f, 2, true)).isEqualTo("1.234,56")
		assertThat(Utils.formatNumber(1234.56f, 11, false)).isEqualTo("2,147483647")
		assertThat(Utils.formatNumber(1234.56f, 11, true)).isEqualTo("2,147483647")
	}

	@Test
	fun roundToNextSignificant() {
		assertThat(Utils.roundToNextSignificant(java.lang.Double.NEGATIVE_INFINITY)).isEqualTo(0f)
		assertThat(Utils.roundToNextSignificant(java.lang.Double.POSITIVE_INFINITY)).isEqualTo(0f)
		assertThat(Utils.roundToNextSignificant(java.lang.Double.NaN)).isEqualTo(0f)

		assertThat(Utils.roundToNextSignificant(java.lang.Double.MIN_VALUE)).isEqualTo(0f)
		assertThat(Utils.roundToNextSignificant(-100.0)).isEqualTo(-100f)
		assertThat(Utils.roundToNextSignificant(-99.0)).isEqualTo(-100f)
		assertThat(Utils.roundToNextSignificant(-50.0)).isEqualTo(-50f)
		assertThat(Utils.roundToNextSignificant(-25.0)).isEqualTo(-30f)
		assertThat(Utils.roundToNextSignificant(-10.0)).isEqualTo(-10f)
		assertThat(Utils.roundToNextSignificant(-5.5)).isEqualTo(-5f)
		assertThat(Utils.roundToNextSignificant(0.0)).isEqualTo(0f)
		assertThat(Utils.roundToNextSignificant(5.5)).isEqualTo(6f)
		assertThat(Utils.roundToNextSignificant(10.0)).isEqualTo(10f)
		assertThat(Utils.roundToNextSignificant(25.0)).isEqualTo(30f)
		assertThat(Utils.roundToNextSignificant(50.0)).isEqualTo(50f)
		assertThat(Utils.roundToNextSignificant(99.0)).isEqualTo(100f)
		assertThat(Utils.roundToNextSignificant(100.0)).isEqualTo(100f)
		assertThat(Utils.roundToNextSignificant(java.lang.Double.MAX_VALUE)).isEqualTo(java.lang.Float.NaN)
	}

	@Test
	fun getDecimals() {
		assertThat(Utils.getDecimals(java.lang.Float.NEGATIVE_INFINITY)).isEqualTo(java.lang.Integer.MIN_VALUE + 1)
		assertThat(Utils.getDecimals(java.lang.Float.POSITIVE_INFINITY)).isEqualTo(java.lang.Integer.MIN_VALUE + 1)
		assertThat(Utils.getDecimals(java.lang.Float.NaN)).isEqualTo(java.lang.Integer.MIN_VALUE + 1)

		assertThat(Utils.getDecimals(java.lang.Float.MIN_VALUE)).isEqualTo(java.lang.Integer.MIN_VALUE + 1)
		assertThat(Utils.getDecimals(-10f)).isEqualTo(2)
		assertThat(Utils.getDecimals(-5.5f)).isEqualTo(2)
		assertThat(Utils.getDecimals(0f)).isEqualTo(java.lang.Integer.MIN_VALUE + 1)
		assertThat(Utils.getDecimals(5.5f)).isEqualTo(2)
		assertThat(Utils.getDecimals(10f)).isEqualTo(1)
		assertThat(Utils.getDecimals(java.lang.Float.MAX_VALUE)).isEqualTo(-36)
	}

	@Test
	fun getPosition() {
		assertThat(Utils.getPosition(MPPointF(0f, 0f), 0f, 0f)).isEqualTo(MPPointF(0f, 0f))
		assertThat(Utils.getPosition(MPPointF(123f, 456f), 78f, 45f))
			.isEqualTo(MPPointF(178.15433f, 511.15433f))
	}

	@Test
	fun getNormalizedAngle() {
		assertThat(Utils.getNormalizedAngle(-720f)).isEqualTo(0f)
		assertThat(Utils.getNormalizedAngle(-540f)).isEqualTo(180f)
		assertThat(Utils.getNormalizedAngle(-360f)).isEqualTo(0f)
		assertThat(Utils.getNormalizedAngle(-180f)).isEqualTo(180f)
		assertThat(Utils.getNormalizedAngle(-45f)).isEqualTo(315f)
		assertThat(Utils.getNormalizedAngle(0f)).isEqualTo(0f)
		assertThat(Utils.getNormalizedAngle(45f)).isEqualTo(45f)
		assertThat(Utils.getNormalizedAngle(180f)).isEqualTo(180f)
		assertThat(Utils.getNormalizedAngle(360f)).isEqualTo(0f)
		assertThat(Utils.getNormalizedAngle(540f)).isEqualTo(180f)
		assertThat(Utils.getNormalizedAngle(720f)).isEqualTo(0f)
	}

	@Test
	fun getSizeOfRotatedRectangleByDegrees() {
		assertThat(Utils.getSizeOfRotatedRectangleByDegrees(25f, 10f, 45f))
			.isEqualTo(FSize(24.748737f, 24.748737f))
	}
}
