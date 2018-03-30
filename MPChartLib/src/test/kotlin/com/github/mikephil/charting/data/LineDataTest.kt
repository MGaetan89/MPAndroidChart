package com.github.mikephil.charting.data

import org.junit.Before
import org.junit.Test

class LineDataTest : ChartDataTest<LineData>() {
	@Before
	fun before() {
		this.data = LineData()
	}

	@Test
	fun constructorList() {
		this.data = LineData(emptyList())
	}

	@Test
	fun constructorVarargs() {
		this.data = LineData(LineDataSet(emptyList(), "Line"))
	}

	@Test
	fun emptyConstructor() {
		this.data = LineData()
	}
}
