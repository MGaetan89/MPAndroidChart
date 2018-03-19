package com.github.mikephil.charting.jobs

import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class AnimatedViewPortJobTest<T : AnimatedViewPortJob> : ViewPortJobTest<T>() {
	abstract fun getXOrigin()

	abstract fun getYOrigin()

	@Test
	fun setPhase() {
		assertThat(this.job.getPhase()).isEqualTo(0f)

		this.job.setPhase(-5f)
		assertThat(this.job.getPhase()).isEqualTo(-5f)

		this.job.setPhase(-2.5f)
		assertThat(this.job.getPhase()).isEqualTo(-2.5f)

		this.job.setPhase(0f)
		assertThat(this.job.getPhase()).isEqualTo(0f)

		this.job.setPhase(2.5f)
		assertThat(this.job.getPhase()).isEqualTo(2.5f)

		this.job.setPhase(5f)
		assertThat(this.job.getPhase()).isEqualTo(5f)
	}
}
