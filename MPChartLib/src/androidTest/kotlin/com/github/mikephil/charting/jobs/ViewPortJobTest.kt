package com.github.mikephil.charting.jobs

abstract class ViewPortJobTest<T : ViewPortJob> {
	protected lateinit var job: T

	abstract fun getXValue()

	abstract fun getYValue()
}
