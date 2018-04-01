package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.utils.Utils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

abstract class ChartDataTest<E : Entry, D : IDataSet<E>, T : ChartData<D>> {
	protected abstract val chartType: String
	protected lateinit var data: T
	protected lateinit var dataSets: List<D>
	protected lateinit var entry: E

	protected val chartTypeLower: String
		get() = this.chartType.toLowerCase()

	@Test
	fun calcMinMax() {
		this.data.calcMinMax()

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
	}

	@Test
	fun getDataSetCount() {
		assertThat(this.data.dataSetCount).isEqualTo(0)

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
	}

	@Test
	fun getYMin() {
		assertThat(this.data.getYMin(YAxis.AxisDependency.LEFT)).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.getYMin(YAxis.AxisDependency.RIGHT)).isEqualTo(java.lang.Float.MAX_VALUE)
	}

	@Test
	fun getYMax() {
		assertThat(this.data.getYMax(YAxis.AxisDependency.LEFT)).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.getYMax(YAxis.AxisDependency.RIGHT)).isEqualTo(-java.lang.Float.MAX_VALUE)
	}

	@Test
	fun getDataSets() {
		assertThat(this.data.dataSets).isEmpty()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
	}

	@Test
	fun getDataSetIndexByLabel() {
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartType 2", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartType 2", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartTypeLower 2", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartTypeLower 2", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartType 4", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartType 4", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartTypeLower 4", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(emptyList(), "$chartTypeLower 4", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartType 2", false))
			.isEqualTo(1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartType 2", true))
			.isEqualTo(1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartTypeLower 2", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartTypeLower 2", true))
			.isEqualTo(1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartType 4", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartType 4", true))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartTypeLower 4", false))
			.isEqualTo(-1)
		assertThat(this.data.getDataSetIndexByLabel(this.dataSets, "$chartTypeLower 4", true))
			.isEqualTo(-1)
	}

	@Test
	fun getDataSetLabels() {
		assertThat(this.data.dataSetLabels).isEmpty()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")
	}

	@Test
	open fun getEntryForHighlight() {
		assertThat(this.data.getEntryForHighlight(Highlight(0f, 1f, 0))).isNull()
	}

	@Test
	open fun getDataSetByLabel() {
		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", true)).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getDataSetByLabel("", false)).isNull()
		assertThat(this.data.getDataSetByLabel("", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 1", false))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartType 1", true))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 1", true))
			.isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByLabel("$chartType 2", false))
			.isEqualTo(this.dataSets[1])
		assertThat(this.data.getDataSetByLabel("$chartType 2", true))
			.isEqualTo(this.dataSets[1])
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 2", true))
			.isEqualTo(this.dataSets[1])
		assertThat(this.data.getDataSetByLabel("$chartType 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartType 4", true)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", false)).isNull()
		assertThat(this.data.getDataSetByLabel("$chartTypeLower 4", true)).isNull()
	}

	@Test
	open fun getDataSetByIndex() {
		assertThat(this.data.getDataSetByIndex(-1)).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isNull()
		assertThat(this.data.getDataSetByIndex(1)).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getDataSetByIndex(-1)).isNull()
		assertThat(this.data.getDataSetByIndex(0)).isEqualTo(this.dataSets[0])
		assertThat(this.data.getDataSetByIndex(1)).isEqualTo(this.dataSets[1])
		assertThat(this.data.getDataSetByIndex(2)).isEqualTo(this.dataSets[2])
		assertThat(this.data.getDataSetByIndex(3)).isNull()
	}

	@Test
	open fun addDataSet() {
		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")

		assertThat(this.data.yMax).isEqualTo(6f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(5f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(6f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.dataSetCount).isEqualTo(this.dataSets.size)
		assertThat(this.data.dataSets).containsExactlyElementsIn(this.dataSets)
		assertThat(this.data.dataSetLabels).asList()
			.containsExactly("$chartType 1", "$chartType 2", "$chartType 3")
	}

	@Test
	open fun addDataSet_axisLeft() {
		this.data.addDataSet(this.dataSets[2])

		assertThat(this.data.yMax).isEqualTo(6f)
		assertThat(this.data.yMin).isEqualTo(4f)
		assertThat(this.data.xMax).isEqualTo(5f)
		assertThat(this.data.xMin).isEqualTo(3f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(6f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[2])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 3")
	}

	@Test
	open fun addDataSet_axisRight() {
		this.data.addDataSet(this.dataSets[1])

		assertThat(this.data.yMax).isEqualTo(2f)
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(1f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(2f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)

		assertThat(this.data.dataSetCount).isEqualTo(1)
		assertThat(this.data.dataSets).containsExactly(this.dataSets[1])
		assertThat(this.data.dataSetLabels).asList().containsExactly("$chartType 2")
	}

	@Test
	fun addDataSet_null() {
		this.data.addDataSet(null)

		assertThat(this.data.yMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.yMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.xMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.mLeftAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mLeftAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMax).isEqualTo(-java.lang.Float.MAX_VALUE)
		assertThat(this.data.mRightAxisMin).isEqualTo(java.lang.Float.MAX_VALUE)

		assertThat(this.data.dataSetCount).isEqualTo(0)
		assertThat(this.data.dataSets).isEmpty()
	}

	@Test
	open fun removeDataSet() {
		assertThat(this.data.removeDataSet(null)).isFalse()
		assertThat(this.data.removeDataSet(this.dataSets[1])).isFalse()

		this.dataSets.forEach(this.data::addDataSet)

		assertThat(this.data.removeDataSet(this.dataSets[1])).isTrue()

		assertThat(this.data.yMax).isEqualTo(6f)
		assertThat(this.data.yMin).isEqualTo(4f)
		assertThat(this.data.xMax).isEqualTo(5f)
		assertThat(this.data.xMin).isEqualTo(3f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(6f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
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
	open fun addEntry() {
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
		assertThat(this.data.yMin).isEqualTo(2f)
		assertThat(this.data.xMax).isEqualTo(7f)
		assertThat(this.data.xMin).isEqualTo(1f)

		assertThat(this.data.mLeftAxisMax).isEqualTo(8f)
		assertThat(this.data.mLeftAxisMin).isEqualTo(4f)
		assertThat(this.data.mRightAxisMax).isEqualTo(8f)
		assertThat(this.data.mRightAxisMin).isEqualTo(2f)
	}

	@Test
	open fun getDataSetForEntry() {
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isNull()

		this.dataSets.forEach(this.data::addDataSet)

		this.data.addEntry(this.entry, 0)
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isEqualTo(this.dataSets[0])

		this.data.removeEntry(this.entry, 0)
		this.data.addEntry(this.entry, 1)
		assertThat(this.data.getDataSetForEntry(null)).isNull()
		assertThat(this.data.getDataSetForEntry(this.entry)).isEqualTo(this.dataSets[1])
	}

	@Test
	fun getColors() {
		assertThat(this.data.colors).isEmpty()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.colors).asList().containsExactly(0x8CEAFF, 0x8CEAFF, 0x8CEAFF)
	}

	@Test
	fun getIndexOfDataSet() {
		assertThat(this.data.getIndexOfDataSet(null)).isEqualTo(-1)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[0])).isEqualTo(-1)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[1])).isEqualTo(-1)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[2])).isEqualTo(-1)

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.getIndexOfDataSet(null)).isEqualTo(-1)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[0])).isEqualTo(0)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[1])).isEqualTo(1)
		assertThat(this.data.getIndexOfDataSet(this.dataSets[2])).isEqualTo(2)
	}

	@Test
	fun getFirstLeft() {
		assertThat(this.data.getFirstLeft(emptyList())).isNull()
		assertThat(this.data.getFirstLeft(this.dataSets)).isEqualTo(this.dataSets[0])
	}

	@Test
	fun getFirstRight() {
		assertThat(this.data.getFirstRight(emptyList())).isNull()
		assertThat(this.data.getFirstRight(this.dataSets)).isEqualTo(this.dataSets[1])
	}

	@Test
	fun setValueFormatter() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.valueFormatter).isEqualTo(Utils.getDefaultValueFormatter())
		}

		val formatter = PercentFormatter()
		this.data.setValueFormatter(formatter)
		this.dataSets.forEach {
			assertThat(it.valueFormatter).isEqualTo(formatter)
		}

		this.data.setValueFormatter(null)
		this.dataSets.forEach {
			assertThat(it.valueFormatter).isEqualTo(formatter)
		}
	}

	@Test
	fun setValueTextColor() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.valueTextColor).isEqualTo(Color.BLACK)
		}

		val color = Color.RED
		this.data.setValueTextColor(color)
		this.dataSets.forEach {
			assertThat(it.valueTextColor).isEqualTo(color)
		}

		val colors = listOf(Color.GREEN, Color.YELLOW)
		this.data.setValueTextColors(colors)
		this.dataSets.forEach {
			assertThat((it as BaseDataSet<*>).valueColors).containsExactlyElementsIn(colors)
		}
	}

	@Test
	fun setValueTypeface() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.valueTypeface).isNull()
		}

		val typeface = Typeface.MONOSPACE
		this.data.setValueTypeface(typeface)
		this.dataSets.forEach {
			assertThat(it.valueTypeface).isEqualTo(typeface)
		}
	}

	@Test
	fun setValueTextSize() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(17f)
		}

		this.data.setValueTextSize(-5f)
		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(-5f)
		}

		this.data.setValueTextSize(-2.5f)
		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(-2.5f)
		}

		this.data.setValueTextSize(0f)
		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(0f)
		}

		this.data.setValueTextSize(2.5f)
		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(2.5f)
		}

		this.data.setValueTextSize(5f)
		this.dataSets.forEach {
			assertThat(it.valueTextSize).isEqualTo(5f)
		}
	}

	@Test
	fun setDrawValues() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.isDrawValuesEnabled).isTrue()
		}

		this.data.setDrawValues(false)
		this.dataSets.forEach {
			assertThat(it.isDrawValuesEnabled).isFalse()
		}

		this.data.setDrawValues(true)
		this.dataSets.forEach {
			assertThat(it.isDrawValuesEnabled).isTrue()
		}
	}

	@Test
	fun setHighlightEnabled() {
		this.dataSets.forEach(this.data::addDataSet)

		this.dataSets.forEach {
			assertThat(it.isHighlightEnabled).isTrue()
		}

		this.data.isHighlightEnabled = false
		this.dataSets.forEach {
			assertThat(it.isHighlightEnabled).isFalse()
		}

		this.data.isHighlightEnabled = true
		this.dataSets.forEach {
			assertThat(it.isHighlightEnabled).isTrue()
		}
	}

	@Test
	fun clearValues() {
		this.dataSets.forEach(this.data::addDataSet)
		this.data.clearValues()

		assertThat(this.data.dataSetCount).isEqualTo(0)
	}

	@Test
	fun contains() {
		assertThat(this.data.contains(null)).isFalse()
		assertThat(this.data.contains(this.dataSets[0])).isFalse()
		assertThat(this.data.contains(this.dataSets[1])).isFalse()
		assertThat(this.data.contains(this.dataSets[2])).isFalse()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.contains(null)).isFalse()
		assertThat(this.data.contains(this.dataSets[0])).isTrue()
		assertThat(this.data.contains(this.dataSets[1])).isTrue()
		assertThat(this.data.contains(this.dataSets[2])).isTrue()
	}

	@Test
	fun getEntryCount() {
		assertThat(this.data.entryCount).isEqualTo(0)

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.entryCount).isEqualTo(3)
	}

	@Test
	fun getMaxEntryCountSet() {
		assertThat(this.data.maxEntryCountSet).isNull()

		this.dataSets.forEach(this.data::addDataSet)
		assertThat(this.data.maxEntryCountSet).isEqualTo(this.dataSets[2])
	}
}
