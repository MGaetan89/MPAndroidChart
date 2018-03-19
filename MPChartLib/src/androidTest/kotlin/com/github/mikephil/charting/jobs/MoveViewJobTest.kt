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
class MoveViewJobTest : ViewPortJobTest<MoveViewJob>() {
	@Before
	fun before() {
		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())

		this.job = MoveViewJob(viewPortHandler, 2.5f, 10f, transformer, view)
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
		with(MoveViewJob.getInstance(null, 0f, 0f, null, null)) {
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.mTrans).isNull()
			assertThat(this.view).isNull()
		}

		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())
		with(MoveViewJob.getInstance(viewPortHandler, 2.5f, 10f, transformer, view)) {
			assertThat(this.mViewPortHandler).isEqualTo(viewPortHandler)
			assertThat(this.getXValue()).isEqualTo(2.5f)
			assertThat(this.getYValue()).isEqualTo(10f)
			assertThat(this.mTrans).isEqualTo(transformer)
			assertThat(this.view).isEqualTo(view)
		}
	}

	@Test
	fun run() {
		assertThat(this.job.pts).isEqualTo(floatArrayOf(0f, 0f))

		this.job.run()
		assertThat(this.job.pts).isEqualTo(floatArrayOf(2.5f, 10f))

		// TODO Add more checks
	}

	@Test
	fun instantiate() {
		with(this.job.instantiate() as MoveViewJob) {
			assertThat(this.getXValue()).isEqualTo(2.5f)
			assertThat(this.getYValue()).isEqualTo(10f)
			assertThat(this.mViewPortHandler).isEqualTo(this@MoveViewJobTest.job.mViewPortHandler)
			assertThat(this.mTrans).isEqualTo(this@MoveViewJobTest.job.mTrans)
			assertThat(this.view).isEqualTo(this@MoveViewJobTest.job.view)
		}
	}
}
