package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CandleDataSetTest : LineScatterCandleRadarDataSetTest<CandleEntry, CandleDataSet>() {
	@Before
	fun before() {
		this.dataSet = CandleDataSet(mutableListOf(), "CandleDataSet")
		this.values = mutableListOf(
			CandleEntry(1f, 2f, 3f, 4f, 5f),
			CandleEntry(6f, 7f, 8f, 9f, 10f),
			CandleEntry(11f, 12f, 13f, 14f, 15f)
		)
		this.entry = CandleEntry(16f, 17f, 18f, 19f, 20f)
	}

	@Test
	override fun calcMinMax() {
		this.dataSet.calcMinMax()

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values

		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	override fun calcMinMaxY() {
		this.dataSet.calcMinMaxY(0f, 2f)

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.calcMinMaxY(1f, 3f)

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxY(0f, 2f)

		assertThat(this.dataSet.yMax).isEqualTo(8f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)

		this.dataSet.calcMinMaxY(1f, 3f)

		assertThat(this.dataSet.yMax).isEqualTo(8f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
	}

	@Test
	override fun calcMinMax_entry() {
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(7f)
		assertThat(this.dataSet.yMin).isEqualTo(8f)
		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	override fun calcMinMaxX_entry() {
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	override fun calcMinMaxY_entry() {
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(8f)
		assertThat(this.dataSet.yMin).isEqualTo(7f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
	}

	@Test
	override fun testToString() {
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 0\n")

		this.dataSet.values = this.values
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 3\nEntry, x: 1.0 y: 2.5 Entry, x: 6.0 y: 7.5 Entry, x: 11.0 y: 12.5 ")
	}

	@Test
	override fun addEntryOrdered() {
		this.dataSet.addEntryOrdered(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(7f)
		assertThat(this.dataSet.yMin).isEqualTo(8f)
		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)

		this.dataSet.addEntryOrdered(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(7f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.addEntryOrdered(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactlyElementsIn(this.values).inOrder()
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.clear()
		assertThat(this.dataSet.entryCount).isEqualTo(0)
		assertThat(this.dataSet.values).isEmpty()
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	override fun addEntry() {
		this.dataSet.addEntry(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(7f)
		assertThat(this.dataSet.yMin).isEqualTo(8f)
		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)

		this.dataSet.addEntry(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(7f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(6f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.addEntry(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		assertThat(this.dataSet.removeEntry(null)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		assertThat(this.dataSet.removeEntry(this.values[0])).isTrue()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(8f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)

		assertThat(this.dataSet.removeEntry(this.entry)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(12f)
		assertThat(this.dataSet.yMin).isEqualTo(8f)
		assertThat(this.dataSet.xMax).isEqualTo(11f)
		assertThat(this.dataSet.xMin).isEqualTo(6f)
	}

	@Test
	override fun getEntryForXValue() {
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST)).isNull()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(6f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(11f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(15f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(6f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(11f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(15f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(6f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(11f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(15f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[2])
	}

	@Test
	override fun getEntriesForXValue() {
		assertThat(this.dataSet.getEntriesForXValue(0f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(1f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(6f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(11f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(15f)).isEmpty()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntriesForXValue(0f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(1f)).containsExactly(this.values[0])
		assertThat(this.dataSet.getEntriesForXValue(6f)).containsExactly(this.values[1])
		assertThat(this.dataSet.getEntriesForXValue(11f)).containsExactly(this.values[2])
		assertThat(this.dataSet.getEntriesForXValue(15f)).isEmpty()
	}

	@Test
	override fun getIndexInEntries() {
		assertThat(this.dataSet.getIndexInEntries(-1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(6)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(11)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(15)).isEqualTo(-1)

		this.dataSet.values = this.values
		assertThat(this.dataSet.getIndexInEntries(-1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(1)).isEqualTo(0)
		assertThat(this.dataSet.getIndexInEntries(6)).isEqualTo(1)
		assertThat(this.dataSet.getIndexInEntries(11)).isEqualTo(2)
		assertThat(this.dataSet.getIndexInEntries(16)).isEqualTo(-1)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as CandleDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.shadowWidth).isEqualTo(dataSet.shadowWidth)
			assertThat(this.showCandleBar).isEqualTo(dataSet.showCandleBar)
			assertThat(this.barSpace).isEqualTo(dataSet.barSpace)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.increasingPaintStyle).isEqualTo(dataSet.increasingPaintStyle)
			assertThat(this.decreasingPaintStyle).isEqualTo(dataSet.decreasingPaintStyle)
			assertThat(this.shadowColor).isEqualTo(dataSet.shadowColor)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as CandleDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.shadowWidth).isEqualTo(dataSet.shadowWidth)
			assertThat(this.showCandleBar).isEqualTo(dataSet.showCandleBar)
			assertThat(this.barSpace).isEqualTo(dataSet.barSpace)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.increasingPaintStyle).isEqualTo(dataSet.increasingPaintStyle)
			assertThat(this.decreasingPaintStyle).isEqualTo(dataSet.decreasingPaintStyle)
			assertThat(this.shadowColor).isEqualTo(dataSet.shadowColor)
		}
	}

	@Test
	fun setBarSpace() {
		assertThat(this.dataSet.barSpace).isEqualTo(0.1f)

		this.dataSet.barSpace = -0.5f
		assertThat(this.dataSet.barSpace).isEqualTo(0f)

		this.dataSet.barSpace = 0f
		assertThat(this.dataSet.barSpace).isEqualTo(0f)

		this.dataSet.barSpace = 0.1f
		assertThat(this.dataSet.barSpace).isEqualTo(0.1f)

		this.dataSet.barSpace = 0.25f
		assertThat(this.dataSet.barSpace).isEqualTo(0.25f)

		this.dataSet.barSpace = 0.45f
		assertThat(this.dataSet.barSpace).isEqualTo(0.45f)

		this.dataSet.barSpace = 0.5f
		assertThat(this.dataSet.barSpace).isEqualTo(0.45f)
	}

	@Test
	fun setShadowWidth() {
		assertThat(this.dataSet.shadowWidth).isEqualTo(3f)

		this.dataSet.shadowWidth = -5f
		assertThat(this.dataSet.shadowWidth).isEqualTo(-5f)

		this.dataSet.shadowWidth = -2.5f
		assertThat(this.dataSet.shadowWidth).isEqualTo(-2.5f)

		this.dataSet.shadowWidth = 0f
		assertThat(this.dataSet.shadowWidth).isEqualTo(0f)

		this.dataSet.shadowWidth = 2.5f
		assertThat(this.dataSet.shadowWidth).isEqualTo(2.5f)

		this.dataSet.shadowWidth = 5f
		assertThat(this.dataSet.shadowWidth).isEqualTo(5f)
	}

	@Test
	fun setShowCandleBar() {
		assertThat(this.dataSet.showCandleBar).isTrue()

		this.dataSet.showCandleBar = false
		assertThat(this.dataSet.showCandleBar).isFalse()

		this.dataSet.showCandleBar = true
		assertThat(this.dataSet.showCandleBar).isTrue()
	}

	@Test
	fun setNeutralColor() {
		assertThat(this.dataSet.neutralColor).isEqualTo(ColorTemplate.COLOR_SKIP)

		this.dataSet.neutralColor = Color.RED
		assertThat(this.dataSet.neutralColor).isEqualTo(Color.RED)
	}

	@Test
	fun setIncreasingColor() {
		assertThat(this.dataSet.increasingColor).isEqualTo(ColorTemplate.COLOR_SKIP)

		this.dataSet.increasingColor = Color.RED
		assertThat(this.dataSet.increasingColor).isEqualTo(Color.RED)
	}

	@Test
	fun setDecreasingColor() {
		assertThat(this.dataSet.decreasingColor).isEqualTo(ColorTemplate.COLOR_SKIP)

		this.dataSet.decreasingColor = Color.RED
		assertThat(this.dataSet.decreasingColor).isEqualTo(Color.RED)
	}

	@Test
	fun setIncreasingPaintStyle() {
		assertThat(this.dataSet.increasingPaintStyle).isEqualTo(Paint.Style.STROKE)

		Paint.Style.values().forEach {
			this.dataSet.increasingPaintStyle = it
			assertThat(this.dataSet.increasingPaintStyle).isEqualTo(it)
		}
	}

	@Test
	fun setDecreasingPaintStyle() {
		assertThat(this.dataSet.decreasingPaintStyle).isEqualTo(Paint.Style.FILL)

		Paint.Style.values().forEach {
			this.dataSet.decreasingPaintStyle = it
			assertThat(this.dataSet.decreasingPaintStyle).isEqualTo(it)
		}
	}

	@Test
	fun setShadowColor() {
		assertThat(this.dataSet.shadowColor).isEqualTo(ColorTemplate.COLOR_SKIP)

		this.dataSet.shadowColor = Color.RED
		assertThat(this.dataSet.shadowColor).isEqualTo(Color.RED)
	}

	@Test
	fun setShadowColorSameAsCandle() {
		assertThat(this.dataSet.shadowColorSameAsCandle).isFalse()

		this.dataSet.shadowColorSameAsCandle = true
		assertThat(this.dataSet.shadowColorSameAsCandle).isTrue()

		this.dataSet.shadowColorSameAsCandle = false
		assertThat(this.dataSet.shadowColorSameAsCandle).isFalse()
	}
}
