package com.github.mikephil.charting.data

import android.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PieDataSetTest : DataSetTest<PieEntry, PieDataSet>() {
	@Before
	fun before() {
		this.dataSet = PieDataSet(mutableListOf(), "PieDataSet")
		this.values = mutableListOf(PieEntry(1f), PieEntry(2f), PieEntry(3f))
		this.entry = PieEntry(4f)
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
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)
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
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)
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
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.addEntryOrdered(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.addEntryOrdered(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2]).inOrder()
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.clear()
		assertThat(this.dataSet.entryCount).isEqualTo(0)
		assertThat(this.dataSet.values).isEmpty()
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)
	}

	@Test
	override fun addEntry() {
		this.dataSet.addEntry(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.addEntry(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(2f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.addEntry(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.dataSet.removeEntry(null)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(1f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.dataSet.removeEntry(this.values[0])).isTrue()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.dataSet.removeEntry(this.entry)).isFalse()
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(3f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)
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
	fun copy() {
		with(this.dataSet.copy() as PieDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.sliceSpace).isEqualTo(dataSet.sliceSpace)
			assertThat(this.selectionShift).isEqualTo(dataSet.selectionShift)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as PieDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.sliceSpace).isEqualTo(dataSet.sliceSpace)
			assertThat(this.selectionShift).isEqualTo(dataSet.selectionShift)
		}
	}

	@Test
	fun setSliceSpace() {
		assertThat(this.dataSet.sliceSpace).isEqualTo(0f)

		this.dataSet.sliceSpace = -5f
		assertThat(this.dataSet.sliceSpace).isEqualTo(0f)

		this.dataSet.sliceSpace = -2.5f
		assertThat(this.dataSet.sliceSpace).isEqualTo(0f)

		this.dataSet.sliceSpace = 0f
		assertThat(this.dataSet.sliceSpace).isEqualTo(0f)

		this.dataSet.sliceSpace = 2.5f
		assertThat(this.dataSet.sliceSpace).isEqualTo(2.5f)

		this.dataSet.sliceSpace = 5f
		assertThat(this.dataSet.sliceSpace).isEqualTo(5f)

		this.dataSet.sliceSpace = 25f
		assertThat(this.dataSet.sliceSpace).isEqualTo(20f)
	}

	@Test
	fun setAutomaticallyDisableSliceSpacing() {
		assertThat(this.dataSet.isAutomaticallyDisableSliceSpacingEnabled).isFalse()

		this.dataSet.setAutomaticallyDisableSliceSpacing(true)
		assertThat(this.dataSet.isAutomaticallyDisableSliceSpacingEnabled).isTrue()

		this.dataSet.setAutomaticallyDisableSliceSpacing(false)
		assertThat(this.dataSet.isAutomaticallyDisableSliceSpacingEnabled).isFalse()
	}

	@Test
	fun setSelectionShift() {
		assertThat(this.dataSet.selectionShift).isEqualTo(18f)

		this.dataSet.selectionShift = -5f
		assertThat(this.dataSet.selectionShift).isEqualTo(-5f)

		this.dataSet.selectionShift = -2.5f
		assertThat(this.dataSet.selectionShift).isEqualTo(-2.5f)

		this.dataSet.selectionShift = 0f
		assertThat(this.dataSet.selectionShift).isEqualTo(0f)

		this.dataSet.selectionShift = 2.5f
		assertThat(this.dataSet.selectionShift).isEqualTo(2.5f)

		this.dataSet.selectionShift = 5f
		assertThat(this.dataSet.selectionShift).isEqualTo(5f)
	}

	@Test
	fun setXValuePosition() {
		assertThat(this.dataSet.xValuePosition).isEqualTo(PieDataSet.ValuePosition.INSIDE_SLICE)

		PieDataSet.ValuePosition.values().forEach {
			this.dataSet.xValuePosition = it
			assertThat(this.dataSet.xValuePosition).isEqualTo(it)
		}
	}

	@Test
	fun setYValuePosition() {
		assertThat(this.dataSet.yValuePosition).isEqualTo(PieDataSet.ValuePosition.INSIDE_SLICE)

		PieDataSet.ValuePosition.values().forEach {
			this.dataSet.yValuePosition = it
			assertThat(this.dataSet.yValuePosition).isEqualTo(it)
		}
	}

	@Test
	fun setValueLineColor() {
		assertThat(this.dataSet.valueLineColor).isEqualTo(Color.BLACK)

		this.dataSet.valueLineColor = Color.BLUE
		assertThat(this.dataSet.valueLineColor).isEqualTo(Color.BLUE)
	}

	@Test
	fun setValueLineWidth() {
		assertThat(this.dataSet.valueLineWidth).isEqualTo(1f)

		this.dataSet.valueLineWidth = -5f
		assertThat(this.dataSet.valueLineWidth).isEqualTo(-5f)

		this.dataSet.valueLineWidth = -2.5f
		assertThat(this.dataSet.valueLineWidth).isEqualTo(-2.5f)

		this.dataSet.valueLineWidth = 0f
		assertThat(this.dataSet.valueLineWidth).isEqualTo(0f)

		this.dataSet.valueLineWidth = 2.5f
		assertThat(this.dataSet.valueLineWidth).isEqualTo(2.5f)

		this.dataSet.valueLineWidth = 5f
		assertThat(this.dataSet.valueLineWidth).isEqualTo(5f)
	}

	@Test
	fun setValueLinePart1OffsetPercentage() {
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(75f)

		this.dataSet.valueLinePart1OffsetPercentage = -5f
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(-5f)

		this.dataSet.valueLinePart1OffsetPercentage = -2.5f
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(-2.5f)

		this.dataSet.valueLinePart1OffsetPercentage = 0f
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(0f)

		this.dataSet.valueLinePart1OffsetPercentage = 2.5f
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(2.5f)

		this.dataSet.valueLinePart1OffsetPercentage = 5f
		assertThat(this.dataSet.valueLinePart1OffsetPercentage).isEqualTo(5f)
	}

	@Test
	fun setValueLinePart1Length() {
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(0.3f)

		this.dataSet.valueLinePart1Length = -5f
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(-5f)

		this.dataSet.valueLinePart1Length = -2.5f
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(-2.5f)

		this.dataSet.valueLinePart1Length = 0f
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(0f)

		this.dataSet.valueLinePart1Length = 2.5f
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(2.5f)

		this.dataSet.valueLinePart1Length = 5f
		assertThat(this.dataSet.valueLinePart1Length).isEqualTo(5f)
	}

	@Test
	fun setValueLinePart2Length() {
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(0.4f)

		this.dataSet.valueLinePart2Length = -5f
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(-5f)

		this.dataSet.valueLinePart2Length = -2.5f
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(-2.5f)

		this.dataSet.valueLinePart2Length = 0f
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(0f)

		this.dataSet.valueLinePart2Length = 2.5f
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(2.5f)

		this.dataSet.valueLinePart2Length = 5f
		assertThat(this.dataSet.valueLinePart2Length).isEqualTo(5f)
	}

	@Test
	fun setValueLineVariableLength() {
		assertThat(this.dataSet.isValueLineVariableLength).isTrue()

		this.dataSet.isValueLineVariableLength = false
		assertThat(this.dataSet.isValueLineVariableLength).isFalse()

		this.dataSet.isValueLineVariableLength = true
		assertThat(this.dataSet.isValueLineVariableLength).isTrue()
	}
}
