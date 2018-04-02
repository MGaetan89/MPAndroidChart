package com.github.mikephil.charting.data

import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CombinedDataTest {
	private lateinit var data: CombinedData
	private lateinit var lineData: LineData
	private lateinit var barData: BarData
	private lateinit var scatterData: ScatterData
	private lateinit var candleData: CandleData
	private lateinit var bubbleData: BubbleData

	@Before
	fun before() {
		this.data = CombinedData()
		this.lineData = LineData(mutableListOf<ILineDataSet>(
			LineDataSet(mutableListOf(), "Line 1"),
			LineDataSet(mutableListOf(Entry(1f, 2f)), "Line 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			LineDataSet(mutableListOf(Entry(3f, 4f), Entry(5f, 6f)), "Line 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		))
		this.barData = BarData(mutableListOf<IBarDataSet>(
			BarDataSet(mutableListOf(), "Bar 1"),
			BarDataSet(mutableListOf(BarEntry(1f, 2f)), "Bar 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BarDataSet(mutableListOf(BarEntry(3f, 4f), BarEntry(5f, 6f)), "Bar 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		))
		this.scatterData = ScatterData(mutableListOf<IScatterDataSet>(
			ScatterDataSet(mutableListOf(), "Scatter 1"),
			ScatterDataSet(mutableListOf(Entry(1f, 2f)), "Scatter 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			ScatterDataSet(mutableListOf(Entry(3f, 4f), Entry(5f, 6f)), "Scatter 3").also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		))
		this.candleData = CandleData(mutableListOf<ICandleDataSet>(
			CandleDataSet(mutableListOf(), "Candle 1"),
			CandleDataSet(mutableListOf(CandleEntry(1f, 2f, 3f, 4f, 5f)), "Candle 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			CandleDataSet(
				mutableListOf(
					CandleEntry(6f, 7f, 8f, 9f, 10f),
					CandleEntry(11f, 12f, 13f, 14f, 15f)
				), "Candle 3"
			).also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		))
		this.bubbleData = BubbleData(mutableListOf<IBubbleDataSet>(
			BubbleDataSet(mutableListOf(), "Bubble 1"),
			BubbleDataSet(mutableListOf(BubbleEntry(1f, 2f, 3f)), "Bubble 2").also {
				it.axisDependency = YAxis.AxisDependency.RIGHT
			},
			BubbleDataSet(
				mutableListOf(BubbleEntry(4f, 5f, 6f), BubbleEntry(7f, 8f, 9f)),
				"Bubble 3"
			).also {
				it.axisDependency = YAxis.AxisDependency.LEFT
			}
		))
	}

	@Test
	fun setData() {
		assertThat(this.data.allData).isEmpty()
		assertThat(this.data.bubbleData).isNull()
		assertThat(this.data.lineData).isNull()
		assertThat(this.data.barData).isNull()
		assertThat(this.data.scatterData).isNull()
		assertThat(this.data.candleData).isNull()
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isNull()
		assertThat(this.data.getDataByIndex(1)).isNull()
		assertThat(this.data.getDataByIndex(2)).isNull()
		assertThat(this.data.getDataByIndex(3)).isNull()
		assertThat(this.data.getDataByIndex(4)).isNull()
		assertThat(this.data.getDataByIndex(5)).isNull()

		this.data.setData(this.lineData)
		assertThat(this.data.allData).containsExactly(this.lineData)
		assertThat(this.data.bubbleData).isNull()
		assertThat(this.data.lineData).isEqualTo(this.lineData)
		assertThat(this.data.barData).isNull()
		assertThat(this.data.scatterData).isNull()
		assertThat(this.data.candleData).isNull()
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isEqualTo(this.lineData)
		assertThat(this.data.getDataByIndex(1)).isNull()
		assertThat(this.data.getDataByIndex(2)).isNull()
		assertThat(this.data.getDataByIndex(3)).isNull()
		assertThat(this.data.getDataByIndex(4)).isNull()
		assertThat(this.data.getDataByIndex(5)).isNull()

		this.data.setData(this.barData)
		assertThat(this.data.allData).containsExactly(this.lineData, this.barData)
		assertThat(this.data.bubbleData).isNull()
		assertThat(this.data.lineData).isEqualTo(this.lineData)
		assertThat(this.data.barData).isEqualTo(this.barData)
		assertThat(this.data.scatterData).isNull()
		assertThat(this.data.candleData).isNull()
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isEqualTo(this.lineData)
		assertThat(this.data.getDataByIndex(1)).isEqualTo(this.barData)
		assertThat(this.data.getDataByIndex(2)).isNull()
		assertThat(this.data.getDataByIndex(3)).isNull()
		assertThat(this.data.getDataByIndex(4)).isNull()
		assertThat(this.data.getDataByIndex(5)).isNull()

		this.data.setData(this.scatterData)
		assertThat(this.data.allData).containsExactly(this.lineData, this.barData, this.scatterData)
		assertThat(this.data.bubbleData).isNull()
		assertThat(this.data.lineData).isEqualTo(this.lineData)
		assertThat(this.data.barData).isEqualTo(this.barData)
		assertThat(this.data.scatterData).isEqualTo(this.scatterData)
		assertThat(this.data.candleData).isNull()
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isEqualTo(this.lineData)
		assertThat(this.data.getDataByIndex(1)).isEqualTo(this.barData)
		assertThat(this.data.getDataByIndex(2)).isEqualTo(this.scatterData)
		assertThat(this.data.getDataByIndex(3)).isNull()
		assertThat(this.data.getDataByIndex(4)).isNull()
		assertThat(this.data.getDataByIndex(5)).isNull()

		this.data.setData(this.candleData)
		assertThat(this.data.allData)
			.containsExactly(this.lineData, this.barData, this.scatterData, this.candleData)
		assertThat(this.data.bubbleData).isNull()
		assertThat(this.data.lineData).isEqualTo(this.lineData)
		assertThat(this.data.barData).isEqualTo(this.barData)
		assertThat(this.data.scatterData).isEqualTo(this.scatterData)
		assertThat(this.data.candleData).isEqualTo(this.candleData)
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isEqualTo(this.lineData)
		assertThat(this.data.getDataByIndex(1)).isEqualTo(this.barData)
		assertThat(this.data.getDataByIndex(2)).isEqualTo(this.scatterData)
		assertThat(this.data.getDataByIndex(3)).isEqualTo(this.candleData)
		assertThat(this.data.getDataByIndex(4)).isNull()
		assertThat(this.data.getDataByIndex(5)).isNull()

		this.data.setData(this.bubbleData)
		assertThat(this.data.allData).containsExactly(
			this.lineData, this.barData, this.scatterData, this.candleData, this.bubbleData
		)
		assertThat(this.data.bubbleData).isEqualTo(this.bubbleData)
		assertThat(this.data.lineData).isEqualTo(this.lineData)
		assertThat(this.data.barData).isEqualTo(this.barData)
		assertThat(this.data.scatterData).isEqualTo(this.scatterData)
		assertThat(this.data.candleData).isEqualTo(this.candleData)
		assertThat(this.data.getDataByIndex(-1)).isNull()
		assertThat(this.data.getDataByIndex(0)).isEqualTo(this.lineData)
		assertThat(this.data.getDataByIndex(1)).isEqualTo(this.barData)
		assertThat(this.data.getDataByIndex(2)).isEqualTo(this.scatterData)
		assertThat(this.data.getDataByIndex(3)).isEqualTo(this.candleData)
		assertThat(this.data.getDataByIndex(4)).isEqualTo(this.bubbleData)
		assertThat(this.data.getDataByIndex(5)).isNull()
	}

	@Test
	fun getEntryForHighlight() {
		var highlight = Highlight(0f, 0f, 0)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 0f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 4f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, java.lang.Float.NaN, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 0).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 4f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, java.lang.Float.NaN, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		highlight = Highlight(0f, 0f, 0)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 0f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 4f, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, java.lang.Float.NaN, 2)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4)
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 0).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()

		highlight = Highlight(3f, 4f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight))
			.isEqualTo(this.lineData.dataSets[2].getEntryForIndex(0))

		highlight = Highlight(3f, java.lang.Float.NaN, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight))
			.isEqualTo(this.lineData.dataSets[2].getEntryForIndex(0))

		highlight = Highlight(0f, 0f, 4).also { it.dataIndex = 0 }
		assertThat(this.data.getEntryForHighlight(highlight)).isNull()
	}

	@Test
	fun getDataSetByHighlight() {
		var highlight = Highlight(0f, 0f, 0)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 0).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		highlight = Highlight(0f, 0f, 0)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 2)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 4)
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()

		highlight = Highlight(0f, 0f, 0).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight))
			.isEqualTo(this.lineData.dataSets[0])

		highlight = Highlight(0f, 0f, 2).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight))
			.isEqualTo(this.lineData.dataSets[2])

		highlight = Highlight(0f, 0f, 4).also { it.dataIndex = 0 }
		assertThat(this.data.getDataSetByHighlight(highlight)).isNull()
	}

	@Test
	fun getDataIndex() {
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(-1)

		this.data.setData(this.lineData)
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(0)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(-1)

		this.data.setData(this.barData)
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(0)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(-1)

		this.data.setData(this.scatterData)
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(0)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(2)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(-1)

		this.data.setData(this.candleData)
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(0)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(2)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(3)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(-1)

		this.data.setData(this.bubbleData)
		assertThat(this.data.getDataIndex(null)).isEqualTo(-1)
		assertThat(this.data.getDataIndex(this.lineData)).isEqualTo(0)
		assertThat(this.data.getDataIndex(this.barData)).isEqualTo(1)
		assertThat(this.data.getDataIndex(this.scatterData)).isEqualTo(2)
		assertThat(this.data.getDataIndex(this.candleData)).isEqualTo(3)
		assertThat(this.data.getDataIndex(this.bubbleData)).isEqualTo(4)
	}

	@Test
	fun removeDataSet() {
		assertThat(this.data.removeDataSet(this.lineData.dataSets[1])).isFalse()
		assertThat(this.data.removeDataSet(this.barData.dataSets[1])).isFalse()
		assertThat(this.data.removeDataSet(this.scatterData.dataSets[1])).isFalse()
		assertThat(this.data.removeDataSet(this.candleData.dataSets[1])).isFalse()
		assertThat(this.data.removeDataSet(this.bubbleData.dataSets[1])).isFalse()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		assertThat(this.data.removeDataSet(this.lineData.dataSets[1])).isTrue()
		assertThat(this.data.removeDataSet(this.barData.dataSets[1])).isTrue()
		assertThat(this.data.removeDataSet(this.scatterData.dataSets[1])).isTrue()
		assertThat(this.data.removeDataSet(this.candleData.dataSets[1])).isTrue()
		assertThat(this.data.removeDataSet(this.bubbleData.dataSets[1])).isTrue()
	}

	@Test
	fun removeDataSet_int() {
		assertThat(this.data.removeDataSet(-1)).isFalse()
		assertThat(this.data.removeDataSet(0)).isFalse()
		assertThat(this.data.removeDataSet(1)).isFalse()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		assertThat(this.data.removeDataSet(-1)).isFalse()
		assertThat(this.data.removeDataSet(0)).isFalse()
		assertThat(this.data.removeDataSet(1)).isFalse()
	}

	@Test
	fun removeEntry_entry() {
		assertThat(this.data.removeEntry(null, 0)).isFalse()
		assertThat(this.data.removeEntry(this.lineData.dataSets[1]?.getEntryForIndex(0), 0))
			.isFalse()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		assertThat(this.data.removeEntry(null, 0)).isFalse()
		assertThat(this.data.removeEntry(this.lineData.dataSets[1]?.getEntryForIndex(0), 0))
			.isFalse()
	}

	@Test
	fun removeEntry_float() {
		assertThat(this.data.removeEntry(0f, 0)).isFalse()
		assertThat(this.data.removeEntry(1f, 0)).isFalse()

		this.data.setData(this.lineData)
		this.data.setData(this.barData)
		this.data.setData(this.scatterData)
		this.data.setData(this.candleData)
		this.data.setData(this.bubbleData)

		assertThat(this.data.removeEntry(0f, 0)).isFalse()
		assertThat(this.data.removeEntry(1f, 0)).isFalse()
	}
}
