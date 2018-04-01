package com.github.mikephil.charting.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class DataSetTest<E : Entry, T : DataSet<E>> : BaseDataSetTest<E, T>() {
	protected lateinit var values: List<E>

	@Test
	fun calcMinMax() {
		this.dataSet.calcMinMax()

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values

		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	fun calcMinMaxY() {
		this.dataSet.calcMinMaxY(0f, 2f)

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.calcMinMaxY(1f, 3f)

		assertThat(this.dataSet.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.dataSet.yMin).isEqualTo(java.lang.Float.MAX_VALUE)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxY(0f, 2f)

		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)

		this.dataSet.calcMinMaxY(1f, 3f)

		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
	}

	@Test
	fun calcMinMax_entry() {
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMax(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	fun calcMinMaxX_entry() {
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxX(this.values[1])

		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	fun calcMinMaxY_entry() {
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)

		this.dataSet.values = this.values
		this.dataSet.calcMinMaxY(this.values[1])

		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
	}

	@Test
	fun getEntryCount() {
		assertThat(this.dataSet.entryCount).isEqualTo(0)

		this.dataSet.values = this.values
		assertThat(this.dataSet.entryCount).isEqualTo(this.values.size)
	}

	@Test
	fun setValues() {
		assertThat(this.dataSet.values).isEmpty()

		this.dataSet.values = this.values
		assertThat(this.dataSet.values).containsExactlyElementsIn(this.values).inOrder()

		this.dataSet.values = emptyList()
		assertThat(this.dataSet.values).isEmpty()
	}

	@Test
	fun testToString() {
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 0\n")

		this.dataSet.values = this.values
		assertThat(this.dataSet.toString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 3\nEntry, x: 1.0 y: 2.0 Entry, x: 3.0 y: 4.0 Entry, x: 5.0 y: 6.0 ")
	}

	@Test
	fun toSimpleString() {
		assertThat(this.dataSet.toSimpleString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 0\n")

		this.dataSet.values = this.values
		assertThat(this.dataSet.toSimpleString()).isEqualTo("DataSet, label: ${this.dataSet.label}, entries: 3\n")
	}

	@Test
	fun addEntryOrdered() {
		this.dataSet.addEntryOrdered(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)

		this.dataSet.addEntryOrdered(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.addEntryOrdered(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values).containsExactlyElementsIn(this.values).inOrder()
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.clear()
		assertThat(this.dataSet.entryCount).isEqualTo(0)
		assertThat(this.dataSet.values).isEmpty()
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)
	}

	@Test
	fun addEntry() {
		this.dataSet.addEntry(this.values[1])
		assertThat(this.dataSet.entryCount).isEqualTo(1)
		assertThat(this.dataSet.values).containsExactly(this.values[1])
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)

		this.dataSet.addEntry(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values).containsExactly(this.values[1], this.values[0])
		assertThat(this.dataSet.yMax).isEqualTo(4f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(3f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.addEntry(this.values[2])
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.removeEntry(null)
		assertThat(this.dataSet.entryCount).isEqualTo(3)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[0], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(2f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(1f)

		this.dataSet.removeEntry(this.values[0])
		assertThat(this.dataSet.entryCount).isEqualTo(2)
		assertThat(this.dataSet.values)
			.containsExactly(this.values[1], this.values[2])
		assertThat(this.dataSet.yMax).isEqualTo(6f)
		assertThat(this.dataSet.yMin).isEqualTo(4f)
		assertThat(this.dataSet.xMax).isEqualTo(5f)
		assertThat(this.dataSet.xMin).isEqualTo(3f)
	}

	@Test
	fun getEntryIndex() {
		assertThat(this.dataSet.getEntryIndex(null)).isEqualTo(-1)
		assertThat(this.dataSet.getEntryIndex(this.values[0])).isEqualTo(-1)
		assertThat(this.dataSet.getEntryIndex(this.values[1])).isEqualTo(-1)
		assertThat(this.dataSet.getEntryIndex(this.values[2])).isEqualTo(-1)

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntryIndex(null)).isEqualTo(-1)
		assertThat(this.dataSet.getEntryIndex(this.values[0])).isEqualTo(0)
		assertThat(this.dataSet.getEntryIndex(this.values[1])).isEqualTo(1)
		assertThat(this.dataSet.getEntryIndex(this.values[2])).isEqualTo(2)
	}

	@Test
	fun getEntryForXValue() {
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN)).isNull()
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST)).isNull()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.UP))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.DOWN))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(1f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForXValue(3f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForXValue(5f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForXValue(7f, 2f, DataSet.Rounding.CLOSEST))
			.isEqualTo(this.values[2])
	}

	@Test
	fun getEntryForIndex() {
		assertThat(this.dataSet.getEntryForIndex(-1)).isNull()
		assertThat(this.dataSet.getEntryForIndex(0)).isNull()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntryForIndex(-1)).isNull()
		assertThat(this.dataSet.getEntryForIndex(0)).isEqualTo(this.values[0])
		assertThat(this.dataSet.getEntryForIndex(1)).isEqualTo(this.values[1])
		assertThat(this.dataSet.getEntryForIndex(2)).isEqualTo(this.values[2])
		assertThat(this.dataSet.getEntryForIndex(3)).isNull()
	}

	@Test
	fun getEntriesForXValue() {
		assertThat(this.dataSet.getEntriesForXValue(0f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(1f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(3f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(5f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(7f)).isEmpty()

		this.dataSet.values = this.values
		assertThat(this.dataSet.getEntriesForXValue(0f)).isEmpty()
		assertThat(this.dataSet.getEntriesForXValue(1f)).containsExactly(this.values[0])
		assertThat(this.dataSet.getEntriesForXValue(3f)).containsExactly(this.values[1])
		assertThat(this.dataSet.getEntriesForXValue(5f)).containsExactly(this.values[2])
		assertThat(this.dataSet.getEntriesForXValue(7f)).isEmpty()
	}
}
