package com.github.mikephil.charting.data

import org.junit.Before

class ScatterDataSetTest : LineScatterCandleRadarDataSetTest<Entry, ScatterDataSet>() {
	@Before
	fun before() {
		this.dataSet = ScatterDataSet(mutableListOf(), "ScatterDataSet")
		this.values = mutableListOf(Entry(1f, 2f), Entry(3f, 4f), Entry(5f, 6f))
		this.entry = Entry(7f, 8f)
	}
}
