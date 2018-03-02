package com.github.mikephil.charting.animation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EasingTest {
	@Test
	fun easeInBack() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInBack)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-380.237f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-52.847065f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(31.577312f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(295.15802f)
	}

	@Test
	fun easeInBounce() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInBounce)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-192.5f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-48.984375f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-16.015625f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-120f)
	}

	@Test
	fun easeInCirc() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInCirc)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(5f)).isEqualTo(java.lang.Float.NaN)
	}

	@Test
	fun easeInCubic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInCubic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-125f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-15.625f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(15.625f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(125f)
	}

	@Test
	fun easeInElastic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInElastic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(8.6736174E-19f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-1.4551966E-11f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(1f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(32768f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-5.49751751E11f)
	}

	@Test
	fun easeInExpo() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInExpo)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(8.6736174E-19f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(2.910383E-11f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(32768f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1.09951163E12f)
	}

	@Test
	fun easeInOutBack() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutBack)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-1927.2001f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-257.1182f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(61.20837f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1004.3339f)
	}

	@Test
	fun easeInOutBounce() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutBounce)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-381.56253f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-96.25f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(36.0625f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(245.75003f)
	}

	@Test
	fun easeInOutCirc() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutCirc)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(5f)).isEqualTo(java.lang.Float.NaN)
	}

	@Test
	fun easeInOutCubic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutCubic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-500f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-62.5f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(14.5f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(257f)
	}

	@Test
	fun easeInOutElastic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutElastic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-3.6195597E-34f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-2.1684016E-19f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(1f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun easeInOutExpo() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutExpo)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(3.85186E-34f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(4.3368087E-19f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(1f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun easeInOutQuad() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(50f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(12.5f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-3.5f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-31f)
	}

	@Test
	fun easeInOutQuart() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuart)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(5000f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(312.5f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-39.5f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-2047f)
	}

	@Test
	fun easeInOutSine() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutSine)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(0.5f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(0.5f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun easeInQuad() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInQuad)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(25f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(6.25f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(6.25f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(25f)
	}

	@Test
	fun easeInQuart() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInQuart)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(625f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(39.0625f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(39.0625f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(625f)
	}

	@Test
	fun easeInSine() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInSine)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(1.7071068f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(1.7071068f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun easeOutBack() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutBack)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-521.2844f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-93.985886f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(13.946388f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(201.1264f)
	}

	@Test
	fun easeOutBounce() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutBounce)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(189.0625f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(47.265625f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(0.5f)).isEqualTo(0.765625f)
		assertThat(easing.getInterpolation(0.75f)).isEqualTo(0.97265625f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(19.046875f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(124.75f)
	}

	@Test
	fun easeOutCirc() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutCirc)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(java.lang.Float.NaN)
		assertThat(easing.getInterpolation(5f)).isEqualTo(java.lang.Float.NaN)
	}

	@Test
	fun easeOutCubic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutCubic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-215f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-41.875f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(4.375f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(65f)
	}

	@Test
	fun easeOutElastic() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutElastic)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(5.6295794E14f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(1.6777184E7f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(1f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun easeOutExpo() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutExpo)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-1.09951163E12f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-32768f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-9.765625E-4f)
		assertThat(easing.getInterpolation(1f)).isEqualTo(1f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-2.910383E-11f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-8.6736174E-19f)
	}

	@Test
	fun easeOutQuad() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutQuad)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-35f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-11.25f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-1.25f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-15f)
	}

	@Test
	fun easeOutQuart() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutQuart)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-1295f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-149.0625f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(-0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-4.0625f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(-255f)
	}

	@Test
	fun easeOutSine() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseOutSine)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-1f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(0.70710677f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(-0.70710677f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(1f)
	}

	@Test
	fun linear() {
		val easing = Easing.getEasingFunctionFromOption(Easing.EasingOption.Linear)

		assertThat(easing.getInterpolation(-5f)).isEqualTo(-5f)
		assertThat(easing.getInterpolation(-2.5f)).isEqualTo(-2.5f)
		assertThat(easing.getInterpolation(0f)).isEqualTo(0f)
		assertThat(easing.getInterpolation(2.5f)).isEqualTo(2.5f)
		assertThat(easing.getInterpolation(5f)).isEqualTo(5f)
	}
}
