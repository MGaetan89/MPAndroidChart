package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BubbleDataTest : ChartDataTest<BubbleEntry, IBubbleDataSet, BubbleData>() {
	override val chartType = "Bubble"

	@Before
	fun before() {
		this.data = BubbleData()
		this.dataSets = mutableListOf(
			BubbleDataSet(mutableListOf(), "$chartType 1"),
			BubbleDataSet(mutableListOf(BubbleEntry(1f, 2f, 3f)), "$chartType 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BubbleDataSet(
				mutableListOf(BubbleEntry(4f, 5f, 6f), BubbleEntry(7f, 8f, 9f)),
				"$chartType 3"
			).also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		)
		this.entry = BubbleEntry(10f, 11f, 12f)
	}

	@Test
	override fun addDataSet() {
		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")
	}

	@Test
	override fun addDataSet_axisLeft() {
		this.data.addDataSet(this.dataSets[2])

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(5f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(4f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[2])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 3")
	}

	@Test
	override fun removeDataSet() {
		assertThat(this.data.removeDataSet(null)).isFalse()
		assertThat(this.data.removeDataSet(this.dataSets[1])).isFalse()

		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.removeDataSet(this.dataSets[1])).isTrue()

		assertThat(this.data.yMax).isEqualTo(8f)
		assertThat(this.data.yMin).isEqualTo(5f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(4f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(2)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[0], this.dataSets[2])

		assertThat(this.data.removeDataSet(2)).isFalse()
		assertThat(this.data.removeDataSet(1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[0])
	}

	@Test
	override fun addEntry() {
		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 1)

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(11f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.addEntry(this.entry, 4)

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(11f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 4)

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(11f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		this.data.removeEntry(null, 0)

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(11f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.removeEntry(0f, 4)).isFalse()
		assertThat(this.data.removeEntry(1f, 1)).isTrue()

		assertThat(this.data.yMax).isEqualTo(11f)
		assertThat(this.data.yMin).isEqualTo(5f)
		assertThat(this.data.xMax).isEqualTo(10f)
		assertThat(this.data.xMin).isEqualTo(4f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(11f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(5f)
		assertThat(this.data.mRightAxisMax).isEqualTo(11f)
		assertThat(this.data.mRightAxisMin).isEqualTo(11f)
	}

	@Test
	fun constructorList() {
		this.data = BubbleData(this.dataSets)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun constructorVarargs() {
		this.data = BubbleData(*this.dataSets.toTypedArray())

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun emptyConstructor() {
		this.data = BubbleData()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun setHighlightCircleWidth() {
		this.dataSets.forEach(this.data::addDataSet)

		this.data.setHighlightCircleWidth(-5f)
		this.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-5f)
		}

		this.data.setHighlightCircleWidth(-2.5f)
		this.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(-2.5f)
		}

		this.data.setHighlightCircleWidth(0f)
		this.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(0f)
		}

		this.data.setHighlightCircleWidth(2.5f)
		this.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(2.5f)
		}

		this.data.setHighlightCircleWidth(5f)
		this.dataSets.forEach {
			assertThat(it.highlightCircleWidth).isEqualTo(5f)
		}
	}
}
