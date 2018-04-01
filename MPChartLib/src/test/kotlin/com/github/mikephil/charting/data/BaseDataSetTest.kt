package com.github.mikephil.charting.data

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

abstract class BaseDataSetTest<E : Entry, T : BaseDataSet<E>> {
	protected lateinit var dataSet: T

	@Test
	fun setColors() {
		assertThat(this.dataSet.colors).containsExactly(0x8CEAFF)
		assertThat(this.dataSet.color).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(0)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(1)).isEqualTo(0x8CEAFF)

		this.dataSet.colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
		assertThat(this.dataSet.colors).containsExactly(Color.RED, Color.GREEN, Color.BLUE)
		assertThat(this.dataSet.color).isEqualTo(Color.RED)

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("-1")
		}

		assertThat(this.dataSet.getColor(0)).isEqualTo(Color.RED)
		assertThat(this.dataSet.getColor(1)).isEqualTo(Color.GREEN)
		assertThat(this.dataSet.getColor(2)).isEqualTo(Color.BLUE)
		assertThat(this.dataSet.getColor(3)).isEqualTo(Color.RED)

		this.dataSet.setColors(Color.YELLOW, Color.CYAN)
		assertThat(this.dataSet.colors).containsExactly(Color.YELLOW, Color.CYAN)
		assertThat(this.dataSet.color).isEqualTo(Color.YELLOW)

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("-1")
		}

		assertThat(this.dataSet.getColor(0)).isEqualTo(Color.YELLOW)
		assertThat(this.dataSet.getColor(1)).isEqualTo(Color.CYAN)
		assertThat(this.dataSet.getColor(2)).isEqualTo(Color.YELLOW)

		this.dataSet.addColor(Color.RED)
		assertThat(this.dataSet.colors).containsExactly(Color.YELLOW, Color.CYAN, Color.RED)
		assertThat(this.dataSet.color).isEqualTo(Color.YELLOW)

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("-1")
		}

		assertThat(this.dataSet.getColor(0)).isEqualTo(Color.YELLOW)
		assertThat(this.dataSet.getColor(1)).isEqualTo(Color.CYAN)
		assertThat(this.dataSet.getColor(2)).isEqualTo(Color.RED)
		assertThat(this.dataSet.getColor(3)).isEqualTo(Color.YELLOW)

		this.dataSet.color = Color.MAGENTA
		assertThat(this.dataSet.colors).containsExactly(Color.MAGENTA)
		assertThat(this.dataSet.color).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getColor(0)).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getColor(1)).isEqualTo(Color.MAGENTA)

		this.dataSet.resetColors()
		assertThat(this.dataSet.colors).isEmpty()

		try {
			this.dataSet.color
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("Index: 0, Size: 0")
		}

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: ArithmeticException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("/ by zero")
		}

		try {
			this.dataSet.getColor(0)
			fail("Should have failed")
		} catch (exception: ArithmeticException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("/ by zero")
		}
	}

	@Test
	fun setLabel() {
		assertThat(this.dataSet.label).endsWith("DataSet")

		this.dataSet.label = ""
		assertThat(this.dataSet.label).isEmpty()

		this.dataSet.label = "New label"
		assertThat(this.dataSet.label).isEqualTo("New label")
	}

	@Test
	fun setHighlightEnabled() {
		assertThat(this.dataSet.isHighlightEnabled).isTrue()

		this.dataSet.isHighlightEnabled = false
		assertThat(this.dataSet.isHighlightEnabled).isFalse()

		this.dataSet.isHighlightEnabled = true
		assertThat(this.dataSet.isHighlightEnabled).isTrue()
	}

	@Test
	fun setValueFormatter() {
		assertThat(this.dataSet.valueFormatter).isEqualTo(Utils.getDefaultValueFormatter())
		assertThat(this.dataSet.needsFormatter()).isFalse()

		val formatter = PercentFormatter()
		this.dataSet.valueFormatter = formatter
		assertThat(this.dataSet.valueFormatter).isEqualTo(formatter)
		assertThat(this.dataSet.needsFormatter()).isFalse()
	}

	@Test
	fun setValueTextColors() {
		assertThat(this.dataSet.valueColors).containsExactly(Color.BLACK)
		assertThat(this.dataSet.valueTextColor).isEqualTo(Color.BLACK)
		assertThat(this.dataSet.getValueTextColor(-1)).isEqualTo(Color.BLACK)
		assertThat(this.dataSet.getValueTextColor(0)).isEqualTo(Color.BLACK)
		assertThat(this.dataSet.getValueTextColor(1)).isEqualTo(Color.BLACK)

		this.dataSet.setValueTextColors(listOf(Color.RED, Color.GREEN, Color.BLUE))
		assertThat(this.dataSet.valueColors).containsExactly(Color.RED, Color.GREEN, Color.BLUE)
		assertThat(this.dataSet.valueTextColor).isEqualTo(Color.RED)

		try {
			this.dataSet.getValueTextColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("-1")
		}

		assertThat(this.dataSet.getValueTextColor(0)).isEqualTo(Color.RED)
		assertThat(this.dataSet.getValueTextColor(1)).isEqualTo(Color.GREEN)
		assertThat(this.dataSet.getValueTextColor(2)).isEqualTo(Color.BLUE)
		assertThat(this.dataSet.getValueTextColor(3)).isEqualTo(Color.RED)

		this.dataSet.valueTextColor = Color.MAGENTA
		assertThat(this.dataSet.valueColors).containsExactly(Color.MAGENTA)
		assertThat(this.dataSet.valueTextColor).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getValueTextColor(-1)).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getValueTextColor(0)).isEqualTo(Color.MAGENTA)
		assertThat(this.dataSet.getValueTextColor(1)).isEqualTo(Color.MAGENTA)
	}

	@Test
	fun setValueTypeface() {
		assertThat(this.dataSet.valueTypeface).isNull()

		this.dataSet.valueTypeface = Typeface.MONOSPACE
		assertThat(this.dataSet.valueTypeface).isEqualTo(Typeface.MONOSPACE)

		this.dataSet.valueTypeface = null
		assertThat(this.dataSet.valueTypeface as Any?).isNull()
	}

	@Test
	fun setValueTextSize() {
		assertThat(this.dataSet.valueTextSize).isEqualTo(17f)

		this.dataSet.valueTextSize = -5f
		assertThat(this.dataSet.valueTextSize).isEqualTo(-5f)

		this.dataSet.valueTextSize = -2.5f
		assertThat(this.dataSet.valueTextSize).isEqualTo(-2.5f)

		this.dataSet.valueTextSize = 0f
		assertThat(this.dataSet.valueTextSize).isEqualTo(0f)

		this.dataSet.valueTextSize = 2.5f
		assertThat(this.dataSet.valueTextSize).isEqualTo(2.5f)

		this.dataSet.valueTextSize = 5f
		assertThat(this.dataSet.valueTextSize).isEqualTo(5f)
	}

	@Test
	fun setForm() {
		assertThat(this.dataSet.form).isEqualTo(Legend.LegendForm.DEFAULT)

		Legend.LegendForm.values().forEach {
			this.dataSet.form = it
			assertThat(this.dataSet.form).isEqualTo(it)
		}
	}

	@Test
	fun setFormSize() {
		assertThat(this.dataSet.formSize).isEqualTo(java.lang.Float.NaN)

		this.dataSet.formSize = -5f
		assertThat(this.dataSet.formSize).isEqualTo(-5f)

		this.dataSet.formSize = -2.5f
		assertThat(this.dataSet.formSize).isEqualTo(-2.5f)

		this.dataSet.formSize = 0f
		assertThat(this.dataSet.formSize).isEqualTo(0f)

		this.dataSet.formSize = 2.5f
		assertThat(this.dataSet.formSize).isEqualTo(2.5f)

		this.dataSet.formSize = 5f
		assertThat(this.dataSet.formSize).isEqualTo(5f)
	}

	@Test
	fun setFormLineWidth() {
		assertThat(this.dataSet.formLineWidth).isEqualTo(java.lang.Float.NaN)

		this.dataSet.formLineWidth = -5f
		assertThat(this.dataSet.formLineWidth).isEqualTo(-5f)

		this.dataSet.formLineWidth = -2.5f
		assertThat(this.dataSet.formLineWidth).isEqualTo(-2.5f)

		this.dataSet.formLineWidth = 0f
		assertThat(this.dataSet.formLineWidth).isEqualTo(0f)

		this.dataSet.formLineWidth = 2.5f
		assertThat(this.dataSet.formLineWidth).isEqualTo(2.5f)

		this.dataSet.formLineWidth = 5f
		assertThat(this.dataSet.formLineWidth).isEqualTo(5f)
	}

	@Test
	fun setFormLineDashEffect() {
		assertThat(this.dataSet.formLineDashEffect).isNull()

		val effect = DashPathEffect(floatArrayOf(1f, 2f), 3f)
		this.dataSet.formLineDashEffect = effect
		assertThat(this.dataSet.formLineDashEffect).isEqualTo(effect)

		this.dataSet.formLineDashEffect = null
		assertThat(this.dataSet.formLineDashEffect as Any?).isNull()
	}

	@Test
	fun setDrawValues() {
		assertThat(this.dataSet.isDrawValuesEnabled).isTrue()

		this.dataSet.setDrawValues(false)
		assertThat(this.dataSet.isDrawValuesEnabled).isFalse()

		this.dataSet.setDrawValues(true)
		assertThat(this.dataSet.isDrawValuesEnabled).isTrue()
	}

	@Test
	fun setDrawIcons() {
		assertThat(this.dataSet.isDrawIconsEnabled).isTrue()

		this.dataSet.setDrawIcons(false)
		assertThat(this.dataSet.isDrawIconsEnabled).isFalse()

		this.dataSet.setDrawIcons(true)
		assertThat(this.dataSet.isDrawIconsEnabled).isTrue()
	}

	@Test
	fun setIconsOffset() {
		assertThat(this.dataSet.iconsOffset).isEqualTo(MPPointF(0f, 0f))

		this.dataSet.iconsOffset = MPPointF(1f, 2f)
		assertThat(this.dataSet.iconsOffset).isEqualTo(MPPointF(1f, 2f))
	}

	@Test
	fun setVisible() {
		assertThat(this.dataSet.isVisible).isTrue()

		this.dataSet.isVisible = false
		assertThat(this.dataSet.isVisible).isFalse()

		this.dataSet.isVisible = true
		assertThat(this.dataSet.isVisible).isTrue()
	}

	@Test
	fun setAxisDependency() {
		assertThat(this.dataSet.axisDependency).isEqualTo(YAxis.AxisDependency.LEFT)

		this.dataSet.axisDependency = YAxis.AxisDependency.RIGHT
		assertThat(this.dataSet.axisDependency).isEqualTo(YAxis.AxisDependency.RIGHT)

		this.dataSet.axisDependency = YAxis.AxisDependency.LEFT
		assertThat(this.dataSet.axisDependency).isEqualTo(YAxis.AxisDependency.LEFT)
	}

	@Test
	open fun getIndexInEntries() {
		assertThat(this.dataSet.getIndexInEntries(-1)).isEqualTo(-1)
		assertThat(this.dataSet.getIndexInEntries(0)).isEqualTo(-1)
	}

	@Test
	open fun removeFirst() {
		assertThat(this.dataSet.removeFirst()).isFalse()
	}

	@Test
	open fun removeLast() {
		assertThat(this.dataSet.removeLast()).isFalse()
	}

	@Test
	fun removeEntryByXValue() {
		assertThat(this.dataSet.removeEntryByXValue(0f)).isFalse()
		assertThat(this.dataSet.removeEntryByXValue(1f)).isFalse()
	}

	@Test
	fun removeEntry() {
		assertThat(this.dataSet.removeEntry(-1)).isFalse()
		assertThat(this.dataSet.removeEntry(0)).isFalse()
	}

	@Test
	open fun contains() {
		assertThat(this.dataSet.contains(null)).isFalse()
	}
}
