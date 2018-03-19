package com.github.mikephil.charting.jobs

import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import android.widget.FrameLayout
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnimatedZoomJobTest : AnimatedViewPortJobTest<AnimatedZoomJob>() {
	@Before
	fun before() {
		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())
		val axis = YAxis()

		this.job = AnimatedZoomJob(
			viewPortHandler, view, transformer, axis, 1.25f, 2.5f, 10f, 5f,
			7.5f, 12.5f, 110f, 15f, 17.5f, 1000L
		)
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
	@UiThreadTest
	fun getInstance() {
		with(
			AnimatedZoomJob.getInstance(
				null, null, null, null, 0f, 0f, 0f,
				0f, 0f, 0f, 0f, 0f, 0f, 0L
			)
		) {
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.mTrans).isNull()
			assertThat(this.yAxis).isNull()
			assertThat(this.xAxisRange).isEqualTo(0f)
			assertThat(this.view).isNull()
			assertThat(this.getXOrigin()).isEqualTo(0f)
			assertThat(this.getYOrigin()).isEqualTo(0f)
			assertThat(this.zoomCenterX).isEqualTo(0f)
			assertThat(this.zoomCenterY).isEqualTo(0f)
			assertThat(this.zoomOriginX).isEqualTo(0f)
			assertThat(this.zoomOriginY).isEqualTo(0f)
			assertThat(this.animator.duration).isEqualTo(0L)
		}

		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())
		val axis = YAxis()
		with(
			AnimatedZoomJob.getInstance(
				viewPortHandler, view, transformer, axis, 1.25f, 2.5f, 10f, 5f,
				7.5f, 12.5f, 110f, 15f, 17.5f, 1000L
			)
		) {
			assertThat(this.mViewPortHandler).isEqualTo(viewPortHandler)
			assertThat(this.getXValue()).isEqualTo(2.5f)
			assertThat(this.getYValue()).isEqualTo(10f)
			assertThat(this.mTrans).isEqualTo(transformer)
			assertThat(this.yAxis).isEqualTo(axis)
			assertThat(this.xAxisRange).isEqualTo(1.25f)
			assertThat(this.view).isEqualTo(view)
			assertThat(this.getXOrigin()).isEqualTo(5f)
			assertThat(this.getYOrigin()).isEqualTo(7.5f)
			assertThat(this.zoomCenterX).isEqualTo(12.5f)
			assertThat(this.zoomCenterY).isEqualTo(110f)
			assertThat(this.zoomOriginX).isEqualTo(15f)
			assertThat(this.zoomOriginY).isEqualTo(17.5f)
			assertThat(this.animator.duration).isEqualTo(1000L)
		}
	}

	@Test
	fun instantiate() {
		with(this.job.instantiate() as AnimatedZoomJob) {
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.mTrans).isNull()
			assertThat(this.yAxis).isNull()
			assertThat(this.xAxisRange).isEqualTo(0f)
			assertThat(this.view).isNull()
			assertThat(this.getXOrigin()).isEqualTo(0f)
			assertThat(this.getYOrigin()).isEqualTo(0f)
			assertThat(this.zoomCenterX).isEqualTo(0f)
			assertThat(this.zoomCenterY).isEqualTo(0f)
			assertThat(this.zoomOriginX).isEqualTo(0f)
			assertThat(this.zoomOriginY).isEqualTo(0f)
			assertThat(this.animator.duration).isEqualTo(0L)
		}
	}
}
