package com.github.mikephil.charting.data

import org.junit.Before
import org.junit.Test

class CandleDataTest : ChartDataTest<CandleData>() {
	@Before
	fun before() {
		this.data = CandleData()
	}

	@Test
	fun constructorList() {
		this.data = CandleData(emptyList())
	}

	@Test
	fun constructorVarargs() {
		this.data = CandleData(CandleDataSet(emptyList(), "Candle"))
	}

	@Test
	fun emptyConstructor() {
		this.data = CandleData()
	}
}
