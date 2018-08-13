package com.github.mikephil.charting.animation

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import androidx.test.annotation.UiThreadTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.TestActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChartAnimatorTest {
	private val linearEasing = TimeInterpolator { input -> input }

	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	@Test
	@UiThreadTest
	fun animateX_noListener_noInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateX_noListener_noInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateX_noListener_withInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(0, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateX_noListener_withInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(100, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateX_withListener_noInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateX_withListener_noInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateX_withListener_withInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(0, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateX_withListener_withInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateX(100, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_noInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(0, 0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_noInterpolator_withDifferentDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(200, 100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_noInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(100, 100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_withInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(0, 0, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_withInterpolator_withDifferentDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(200, 100, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateXY_noListener_withInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(100, 100, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_noInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(0, 0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_noInterpolator_withDifferentDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(200, 100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_noInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(100, 100)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_withInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(0, 0, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_withInterpolator_withDifferentDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(200, 100, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateXY_withListener_withInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateXY(100, 100, linearEasing, linearEasing)
		assertThat(animator.phaseX).isEqualTo(0f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateY_noListener_noInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateY_noListener_noInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(100)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateY_noListener_withInterpolator_noDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(0, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)
	}

	@Test
	@UiThreadTest
	fun animateY_noListener_withInterpolator_withDuration() {
		val animator = ChartAnimator()
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(100, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(0f)
	}

	@Test
	@UiThreadTest
	fun animateY_withListener_noInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(0)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateY_withListener_noInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(100)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateY_withListener_withInterpolator_noDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(0, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		assertThat(called).isTrue()
	}

	@Test
	@UiThreadTest
	fun animateY_withListener_withInterpolator_withDuration() {
		var called = false
		val animator = ChartAnimator(ValueAnimator.AnimatorUpdateListener {
			called = true
		})
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(1f)

		animator.animateY(100, linearEasing)
		assertThat(animator.phaseX).isEqualTo(1f)
		assertThat(animator.phaseY).isEqualTo(0f)

		assertThat(called).isTrue()
	}
}
