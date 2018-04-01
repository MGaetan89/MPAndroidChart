package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class BarDataTest : BarLineScatterCandleBubbleDataTest<BarEntry, IBarDataSet, BarData>() {
	override val chartType = "Bar"

	@Before
	fun before() {
		this.data = BarData()
		this.dataSets = mutableListOf(
			BarDataSet(mutableListOf(), "$chartType 1"),
			BarDataSet(mutableListOf(BarEntry(1f, 2f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(mutableListOf(BarEntry(3f, 4f), BarEntry(5f, 6f)), "$chartType 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = BarEntry(7f, 8f)
	}

	@Test
	override fun addEntry() {
		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 1)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 4)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 4)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 0)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.removeEntry(0f, 4)).isFalse()
		assertThat(this.data.removeEntry(1f, 1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(4f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(3f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(8f)
	}

	@Test
	fun constructorList() {
		this.data = BarData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = BarData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = BarData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun setBarWidth() {
		assertThat(this.data.barWidth).isEqualTo(0.85f)

		this.data.barWidth = -5f
		assertThat(this.data.barWidth).isEqualTo(-5f)

		this.data.barWidth = -2.5f
		assertThat(this.data.barWidth).isEqualTo(-2.5f)

		this.data.barWidth = 0f
		assertThat(this.data.barWidth).isEqualTo(0f)

		this.data.barWidth = 2.5f
		assertThat(this.data.barWidth).isEqualTo(2.5f)

		this.data.barWidth = 5f
		assertThat(this.data.barWidth).isEqualTo(5f)
	}

	@Test
	fun groupBars() {
		try {
			this.data.groupBars(5f, 0.8f, 0.5f)
			fail("Should have failed")
		} catch (exception: RuntimeException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("BarData needs to hold at least 2 IBarDataSet to allow grouping.")
		}

		this.dataSets.forEach(this.data::addDataSet)

		this.data.barWidth = 0.85f
		this.data.groupBars(5f, 0.8f, 0.5f)
		assertThat(this.dataSets[0].entryCount).isEqualTo(0)
		assertThat(this.dataSets[1].entryCount).isEqualTo(1)
		assertThat(this.dataSets[1].getEntryForIndex(0)!!.x).isEqualTo(7.4250007f)
		assertThat(this.dataSets[2].entryCount).isEqualTo(2)
		assertThat(this.dataSets[2].getEntryForIndex(0)!!.x).isEqualTo(8.775001f)
		assertThat(this.dataSets[2].getEntryForIndex(1)!!.x).isEqualTo(13.625001f)

		this.data.barWidth = 2f
		this.data.groupBars(5f, 0.8f, 0.5f)
		assertThat(this.dataSets[0].entryCount).isEqualTo(0)
		assertThat(this.dataSets[1].entryCount).isEqualTo(1)
		assertThat(this.dataSets[1].getEntryForIndex(0)!!.x).isEqualTo(9.15f)
		assertThat(this.dataSets[2].entryCount).isEqualTo(2)
		assertThat(this.dataSets[2].getEntryForIndex(0)!!.x).isEqualTo(11.65f)
		assertThat(this.dataSets[2].getEntryForIndex(1)!!.x).isEqualTo(19.95f)

		this.data.barWidth = 0.85f
		this.data.groupBars(2f, 4f, 1f)
		assertThat(this.dataSets[0].entryCount).isEqualTo(0)
		assertThat(this.dataSets[1].entryCount).isEqualTo(1)
		assertThat(this.dataSets[1].getEntryForIndex(0)!!.x).isEqualTo(6.7750006f)
		assertThat(this.dataSets[2].entryCount).isEqualTo(2)
		assertThat(this.dataSets[2].getEntryForIndex(0)!!.x).isEqualTo(8.625001f)
		assertThat(this.dataSets[2].getEntryForIndex(1)!!.x).isEqualTo(18.175f)

		this.data.barWidth = 2f
		this.data.groupBars(2f, 4f, 1f)
		assertThat(this.dataSets[0].entryCount).isEqualTo(0)
		assertThat(this.dataSets[1].entryCount).isEqualTo(1)
		assertThat(this.dataSets[1].getEntryForIndex(0)!!.x).isEqualTo(8.5f)
		assertThat(this.dataSets[2].entryCount).isEqualTo(2)
		assertThat(this.dataSets[2].getEntryForIndex(0)!!.x).isEqualTo(11.5f)
		assertThat(this.dataSets[2].getEntryForIndex(1)!!.x).isEqualTo(24.5f)
	}

	@Test
	fun getGroupWidth() {
		this.data.barWidth = 0.85f
		assertThat(this.data.getGroupWidth(5f, 1f)).isEqualTo(5f)

		this.data.barWidth = 2f
		assertThat(this.data.getGroupWidth(5f, 1f)).isEqualTo(5f)

		this.dataSets.forEach(this.data::addDataSet)
		this.data.barWidth = 0.85f
		assertThat(this.data.getGroupWidth(5f, 1f)).isEqualTo(10.55f)

		this.data.barWidth = 2f
		assertThat(this.data.getGroupWidth(5f, 1f)).isEqualTo(14f)
	}
}
