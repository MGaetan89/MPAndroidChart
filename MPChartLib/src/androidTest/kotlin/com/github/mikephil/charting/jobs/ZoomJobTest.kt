package com.github.mikephil.charting.jobs

import android.support.test.InstrumentationRegistry
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
class ZoomJobTest : ViewPortJobTest<ZoomJob>() {
	@Before
	fun before() {
		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val axisDependency = YAxis.AxisDependency.LEFT
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())

		this.job = ZoomJob(viewPortHandler, 2.5f, 10f, 5f, 7.5f, transformer, axisDependency, view)
	}

	@Test
	override fun getXValue() {
		assertThat(this.job.getXValue()).isEqualTo(5f)
	}

	@Test
	override fun getYValue() {
		assertThat(this.job.getYValue()).isEqualTo(7.5f)
	}

	@Test
	fun getInstance() {
		with(ZoomJob.getInstance(null, 0f, 0f, 0f, 0f, null, null, null)) {
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.scaleX).isEqualTo(0f)
			assertThat(this.scaleY).isEqualTo(0f)
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.mTrans).isNull()
			assertThat(this.axisDependency).isNull()
			assertThat(this.view).isNull()
		}

		val viewPortHandler = ViewPortHandler()
		val transformer = Transformer(viewPortHandler)
		val axisDependency = YAxis.AxisDependency.LEFT
		val view = FrameLayout(InstrumentationRegistry.getTargetContext())
		with(
			ZoomJob.getInstance(
				viewPortHandler, 2.5f, 10f, 5f, 7.5f, transformer, axisDependency, view
			)
		) {
			assertThat(this.getXValue()).isEqualTo(5f)
			assertThat(this.getYValue()).isEqualTo(7.5f)
			assertThat(this.scaleX).isEqualTo(2.5f)
			assertThat(this.scaleY).isEqualTo(10f)
			assertThat(this.mViewPortHandler).isEqualTo(viewPortHandler)
			assertThat(this.mTrans).isEqualTo(transformer)
			assertThat(this.axisDependency).isEqualTo(axisDependency)
			assertThat(this.view).isEqualTo(view)
		}
	}

	@Test
	fun instantiate() {
		with(this.job.instantiate() as ZoomJob) {
			assertThat(this.getXValue()).isEqualTo(0f)
			assertThat(this.getYValue()).isEqualTo(0f)
			assertThat(this.scaleX).isEqualTo(0f)
			assertThat(this.scaleY).isEqualTo(0f)
			assertThat(this.mViewPortHandler).isNull()
			assertThat(this.mTrans).isNull()
			assertThat(this.axisDependency).isNull()
			assertThat(this.view).isNull()
		}
	}
}
