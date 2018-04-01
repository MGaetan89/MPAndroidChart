package com.github.mikephil.charting.data

import org.junit.Before

class BarDataSetTest : BaseDataSetTest<BarEntry, BarDataSet>() {
	@Before
	fun before() {
		this.dataSet = BarDataSet(emptyList(), "BarDataSet")
	}
}
