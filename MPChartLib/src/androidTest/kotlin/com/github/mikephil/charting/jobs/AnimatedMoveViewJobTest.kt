package com.github.mikephil.charting.jobs

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.widget.FrameLayout
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimatedMoveViewJobTest : AnimatedViewPortJobTest<AnimatedMoveViewJob>() {
	@Before
	fun before() {
		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())

		this.job =
				AnimatedMoveViewJob(viewPortHandler, 2.5f, 10f, transformer, view, 5f, 7.5f, 1000L)
	}

	@Test
	override fun getXOrigin() {
		assertThat(this.job.getXOrigin()).isEqualTo(5f)
	}

	@Test
	override fun getYOrigin() {
		assertThat(this.job.getYOrigin()).isEqualTo(7.5f)
	}

	@Test
	override fun getXValue() {
		assertThat(this.job.getXValue()).isEqualTo(2.5f)
	}

	@Test
	override fun getYValue() {
		assertThat(this.job.getYValue()).isEqualTo(10f)
	}

	@Test
	fun getInstance() {
		with(AnimatedMoveViewJob(null, 0f, 0f, null, null, 0f, 0f, 0L)) {
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.mTrans).isNull()
			assertThat(this.view).isNull()
			assertThat(this.getXOrigin()).isEqualTo(0f)
			assertThat(this.getYOrigin()).isEqualTo(0f)
			assertThat(this.animator.duration).isEqualTo(0L)
		}

		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())

		with(AnimatedMoveViewJob(viewPortHandler, 2.5f, 10f, transformer, view, 5f, 7.5f, 1000L)) {
			assertThat(this.mViewPortHandler).isEqualTo(viewPortHandler)
			assertThat(this.getXValue()).isEqualTo(2.5f)
			assertThat(this.getYValue()).isEqualTo(10f)
			assertThat(this.mTrans).isEqualTo(transformer)
			assertThat(this.view).isEqualTo(view)
			assertThat(this.getXOrigin()).isEqualTo(5f)
			assertThat(this.getYOrigin()).isEqualTo(7.5f)
			assertThat(this.animator.duration).isEqualTo(1000L)
		}
	}

	@Test
	fun onAnimationUpdate() {
		assertThat(this.job.pts).isEqualTo(floatArrayOf(0f, 0f))

		this.job.phase = 2f
		this.job.onAnimationUpdate(null)
		assertThat(this.job.pts).isEqualTo(floatArrayOf(0f, 12.5f))
	}
}
