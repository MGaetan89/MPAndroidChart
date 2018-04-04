package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RadarDataSetTest : LineRadarDataSetTest<RadarEntry, RadarDataSet>() {
	@Before
	fun before() {
		this.dataSet = RadarDataSet(mutableListOf(), "RadarDataSet")
		this.values = mutableListOf(RadarEntry(1f), RadarEntry(2f), RadarEntry(3f))
		this.entry = RadarEntry(4f)
	}

	@Test
	override fun calcMinMax() {
		this.dataSet.calcMinMax()

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)
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

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)

		this.dataSet.calcMinMaxY(1f, 3f)

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(3f)
	}

	@Test
	override fun calcMinMax_entry() {
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)
	}

	@Test
	override fun calcMinMaxX_entry() {
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)
	}

	@Test
	override fun calcMinMaxY_entry() {
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
	}

	@Test
	override fun testToString() {
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 0\n")

		this.dataSet.values = this.values
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 3\nEntry, x: 0.0 y: 1.0 Entry, x: 0.0 y: 2.0 Entry, x: 0.0 y: 3.0 ")
	}

	@Test
	override fun addEntryOrdered() {
		this.dataSet.addEntryOrdered(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.addEntryOrdered(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.addEntryOrdered(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2]).inOrder()
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.clear()
		assertThat(this.dataSet.entryCount).isEqualTo(0)
		assertThat(this.dataSet.values).isEmpty()
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)
	}

	@Test
	override fun addEntry() {
		this.dataSet.addEntry(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.addEntry(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		this.dataSet.addEntry(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		assertThat(this.dataSet.removeEntry(null)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		assertThat(this.dataSet.removeEntry(this.values[0])).isTrue()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)

		assertThat(this.dataSet.removeEntry(this.entry)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(0f)
		assertThat(this.dataSet.xMin).isEqualTo(0f)
	}

	@Test
	override fun getEntryForXValue() {
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST)).isNull()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
	}

	@Test
	override fun getEntriesForXValue() {
		assertThat(this.dataSet.getEntriesForXValue(0f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(1f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(3f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(5f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(7f)).isEmpty()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntriesForXValue(0f))
			.containsExactlyElementsIn(this.values).inOrder()
		assertThat(this.dataSet.getEntriesForXValue(1f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(3f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(5f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(7f)).isEmpty()
	}

	@Test
	override fun getIndexInEntries() {
		assertThat(this.dataSet.getIndexInEntries(-1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(3)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(5)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(7)).isEqualTo(-1)

		this.dataSet.values = this.values
		assertThat(this.dataSet.getIndexInEntries(-1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(3)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(5)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(7)).isEqualTo(-1)
	}

	@Test
	fun setDrawHighlightCircleEnabled() {
		assertThat(this.dataSet.isDrawHighlightCircleEnabled).isFalse()

		this.dataSet.isDrawHighlightCircleEnabled = true
		assertThat(this.dataSet.isDrawHighlightCircleEnabled).isTrue()

		this.dataSet.isDrawHighlightCircleEnabled = false
		assertThat(this.dataSet.isDrawHighlightCircleEnabled).isFalse()
	}

	@Test
	fun setHighlightCircleFillColor() {
		assertThat(this.dataSet.highlightCircleFillColor).isEqualTo(Color.WHITE)

		this.dataSet.highlightCircleFillColor = Color.RED
		assertThat(this.dataSet.highlightCircleFillColor).isEqualTo(Color.RED)
	}

	@Test
	fun setHighlightCircleStrokeColor() {
		assertThat(this.dataSet.highlightCircleStrokeColor).isEqualTo(ColorTemplate.COLOR_NONE)

		this.dataSet.highlightCircleStrokeColor = Color.RED
		assertThat(this.dataSet.highlightCircleStrokeColor).isEqualTo(Color.RED)
	}

	@Test
	fun setHighlightCircleStrokeAlpha() {
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(76)

		this.dataSet.highlightCircleStrokeAlpha = -50
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(-50)

		this.dataSet.highlightCircleStrokeAlpha = 0
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(0)

		this.dataSet.highlightCircleStrokeAlpha = 50
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(50)

		this.dataSet.highlightCircleStrokeAlpha = 100
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(100)

		this.dataSet.highlightCircleStrokeAlpha = 150
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(150)

		this.dataSet.highlightCircleStrokeAlpha = 200
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(200)

		this.dataSet.highlightCircleStrokeAlpha = 250
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(250)

		this.dataSet.highlightCircleStrokeAlpha = 300
		assertThat(this.dataSet.highlightCircleStrokeAlpha).isEqualTo(300)
	}

	@Test
	fun setHighlightCircleInnerRadius() {
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(3f)

		this.dataSet.highlightCircleInnerRadius = -5f
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(-5f)

		this.dataSet.highlightCircleInnerRadius = -2.5f
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(-2.5f)

		this.dataSet.highlightCircleInnerRadius = 0f
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(0f)

		this.dataSet.highlightCircleInnerRadius = 2.5f
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(2.5f)

		this.dataSet.highlightCircleInnerRadius = 5f
		assertThat(this.dataSet.highlightCircleInnerRadius).isEqualTo(5f)
	}

	@Test
	fun setHighlightCircleOuterRadius() {
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(4f)

		this.dataSet.highlightCircleOuterRadius = -5f
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(-5f)

		this.dataSet.highlightCircleOuterRadius = -2.5f
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(-2.5f)

		this.dataSet.highlightCircleOuterRadius = 0f
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(0f)

		this.dataSet.highlightCircleOuterRadius = 2.5f
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(2.5f)

		this.dataSet.highlightCircleOuterRadius = 5f
		assertThat(this.dataSet.highlightCircleOuterRadius).isEqualTo(5f)
	}

	@Test
	fun setHighlightCircleStrokeWidth() {
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(2f)

		this.dataSet.highlightCircleStrokeWidth = -5f
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(-5f)

		this.dataSet.highlightCircleStrokeWidth = -2.5f
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(-2.5f)

		this.dataSet.highlightCircleStrokeWidth = 0f
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(0f)

		this.dataSet.highlightCircleStrokeWidth = 2.5f
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(2.5f)

		this.dataSet.highlightCircleStrokeWidth = 5f
		assertThat(this.dataSet.highlightCircleStrokeWidth).isEqualTo(5f)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as RadarDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as RadarDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
		}
	}
}
