package com.github.mikephil.charting.components

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LegendTest : ComponentBaseTest<Legend>() {
	private val entries = listOf(
		LegendEntry(),
		LegendEntry(null, Legend.LegendForm.DEFAULT, Float.NaN, 5f, null, Color.RED),
		LegendEntry("", Legend.LegendForm.CIRCLE, 10f, 10f, null, Color.GREEN),
		LegendEntry("Hello, World!", Legend.LegendForm.EMPTY, 15f, 15f, null, Color.TRANSPARENT)
	)

	@Before
	fun before() {
		this.component = Legend(emptyArray())

		assertThat(this.component.textSize).isEqualTo(10f)
		assertThat(this.component.xOffset).isEqualTo(5f)
	}

	@Test
	override fun yOffset() {
		assertThat(this.component.yOffset).isEqualTo(3f)

		this.component.yOffset = 10f
		assertThat(this.component.yOffset).isEqualTo(10f)
	}

	@Test
	fun setEntries() {
		assertThat(this.component.entries).isEmpty()

		this.component.setEntries(this.entries)
		assertThat(this.component.entries).asList().containsExactlyElementsIn(this.entries)

		this.component.setEntries(emptyList())
		assertThat(this.component.entries).isEmpty()
	}

	@Test
	fun getMaximumEntryWidth() {
		val paint = Paint()

		assertThat(this.component.getMaximumEntryWidth(paint)).isEqualTo(5f)

		this.component.setEntries(this.entries)
		assertThat(this.component.getMaximumEntryWidth(paint)).isEqualTo(92f)
	}

	@Test
	fun getMaximumEntryHeight() {
		val paint = Paint()

		assertThat(this.component.getMaximumEntryHeight(paint)).isEqualTo(0f)

		this.component.setEntries(this.entries)
		assertThat(this.component.getMaximumEntryHeight(paint)).isEqualTo(13f)
	}

	@Test
	fun setExtra() {
		assertThat(this.component.extraEntries).isNull()

		this.component.setExtra(this.entries)
		assertThat(this.component.extraEntries).asList().containsExactlyElementsIn(this.entries)

		this.component.setExtra(emptyList())
		assertThat(this.component.extraEntries).isEmpty()

		this.component.setExtra(this.entries.toTypedArray())
		assertThat(this.component.extraEntries).asList().containsExactlyElementsIn(this.entries)

		this.component.setExtra(emptyArray())
		assertThat(this.component.extraEntries).isEmpty()

		this.component.setExtra(null)
		assertThat(this.component.extraEntries).isNull()

		this.component.setExtra(
			intArrayOf(ColorTemplate.COLOR_NONE, 0, Color.GREEN, ColorTemplate.COLOR_SKIP),
			arrayOf(null, "", "MPAndroidChart", "Hello, World!")
		)
		val extra = this.component.extraEntries
		assertThat(extra).isNotNull()
		assertThat(extra).asList().hasSize(4)

		with(extra!![0]) {
			assertThat(this).isNotNull()
			assertThat(this.formColor).isEqualTo(ColorTemplate.COLOR_NONE)
			assertThat(this.form).isEqualTo(Legend.LegendForm.EMPTY)
			assertThat(this.label).isNull()
		}

		with(extra[1]) {
			assertThat(this).isNotNull()
			assertThat(this.formColor).isEqualTo(0)
			assertThat(this.form).isEqualTo(Legend.LegendForm.NONE)
			assertThat(this.label).isEmpty()
		}

		with(extra[2]) {
			assertThat(this).isNotNull()
			assertThat(this.formColor).isEqualTo(Color.GREEN)
			assertThat(this.form).isEqualTo(Legend.LegendForm.DEFAULT)
			assertThat(this.label).isEqualTo("MPAndroidChart")
		}

		with(extra[3]) {
			assertThat(this).isNotNull()
			assertThat(this.formColor).isEqualTo(ColorTemplate.COLOR_SKIP)
			assertThat(this.form).isEqualTo(Legend.LegendForm.NONE)
			assertThat(this.label).isEqualTo("Hello, World!")
		}
	}

	@Test
	fun setCustom() {
		assertThat(this.component.entries).isEmpty()
		assertThat(this.component.isLegendCustom).isFalse()

		this.component.setCustom(this.entries)
		assertThat(this.component.entries).asList().containsExactlyElementsIn(this.entries)
		assertThat(this.component.isLegendCustom).isTrue()

		this.component.setCustom(emptyList())
		assertThat(this.component.entries).isEmpty()
		assertThat(this.component.isLegendCustom).isTrue()

		this.component.setCustom(this.entries.toTypedArray())
		assertThat(this.component.entries).asList().containsExactlyElementsIn(this.entries)
		assertThat(this.component.isLegendCustom).isTrue()

		this.component.setCustom(emptyArray())
		assertThat(this.component.entries).isEmpty()
		assertThat(this.component.isLegendCustom).isTrue()

		this.component.resetCustom()
		assertThat(this.component.isLegendCustom).isFalse()
	}

	@Test
	fun setHorizontalAlignment() {
		assertThat(this.component.horizontalAlignment).isEqualTo(Legend.LegendHorizontalAlignment.LEFT)

		Legend.LegendHorizontalAlignment.values().forEach {
			this.component.horizontalAlignment = it
			assertThat(this.component.horizontalAlignment).isEqualTo(it)
		}
	}

	@Test
	fun setVerticalAlignment() {
		assertThat(this.component.verticalAlignment).isEqualTo(Legend.LegendVerticalAlignment.BOTTOM)

		Legend.LegendVerticalAlignment.values().forEach {
			this.component.verticalAlignment = it
			assertThat(this.component.verticalAlignment).isEqualTo(it)
		}
	}

	@Test
	fun setOrientation() {
		assertThat(this.component.orientation).isEqualTo(Legend.LegendOrientation.HORIZONTAL)

		Legend.LegendOrientation.values().forEach {
			this.component.orientation = it
			assertThat(this.component.orientation).isEqualTo(it)
		}
	}

	@Test
	fun setDrawInside() {
		assertThat(this.component.isDrawInsideEnabled).isFalse()

		this.component.setDrawInside(true)
		assertThat(this.component.isDrawInsideEnabled).isTrue()

		this.component.setDrawInside(false)
		assertThat(this.component.isDrawInsideEnabled).isFalse()
	}

	@Test
	fun setDirection() {
		assertThat(this.component.direction).isEqualTo(Legend.LegendDirection.LEFT_TO_RIGHT)

		Legend.LegendDirection.values().forEach {
			this.component.direction = it
			assertThat(this.component.direction).isEqualTo(it)
		}
	}

	@Test
	fun setForm() {
		assertThat(this.component.form).isEqualTo(Legend.LegendForm.SQUARE)

		Legend.LegendForm.values().forEach {
			this.component.form = it
			assertThat(this.component.form).isEqualTo(it)
		}
	}

	@Test
	fun setFormSize() {
		assertThat(this.component.formSize).isEqualTo(8f)

		this.component.formSize = -5f
		assertThat(this.component.formSize).isEqualTo(-5f)

		this.component.formSize = -2.5f
		assertThat(this.component.formSize).isEqualTo(-2.5f)

		this.component.formSize = 0f
		assertThat(this.component.formSize).isEqualTo(0f)

		this.component.formSize = 2.5f
		assertThat(this.component.formSize).isEqualTo(2.5f)

		this.component.formSize = 5f
		assertThat(this.component.formSize).isEqualTo(5f)
	}

	@Test
	fun setFormLineWidth() {
		assertThat(this.component.formLineWidth).isEqualTo(3f)

		this.component.formLineWidth = -5f
		assertThat(this.component.formLineWidth).isEqualTo(-5f)

		this.component.formLineWidth = -2.5f
		assertThat(this.component.formLineWidth).isEqualTo(-2.5f)

		this.component.formLineWidth = 0f
		assertThat(this.component.formLineWidth).isEqualTo(0f)

		this.component.formLineWidth = 2.5f
		assertThat(this.component.formLineWidth).isEqualTo(2.5f)

		this.component.formLineWidth = 5f
		assertThat(this.component.formLineWidth).isEqualTo(5f)
	}

	@Test
	fun setFormLineDashEffect() {
		assertThat(this.component.formLineDashEffect).isNull()

		this.component.formLineDashEffect = DashPathEffect(floatArrayOf(0f, 1f), 0f)
		assertThat(this.component.formLineDashEffect).isNotNull()

		this.component.formLineDashEffect = null
		assertThat(this.component.formLineDashEffect as Any?).isNull()
	}

	@Test
	fun setXEntrySpace() {
		assertThat(this.component.xEntrySpace).isEqualTo(6f)

		this.component.xEntrySpace = -5f
		assertThat(this.component.xEntrySpace).isEqualTo(-5f)

		this.component.xEntrySpace = -2.5f
		assertThat(this.component.xEntrySpace).isEqualTo(-2.5f)

		this.component.xEntrySpace = 0f
		assertThat(this.component.xEntrySpace).isEqualTo(0f)

		this.component.xEntrySpace = 2.5f
		assertThat(this.component.xEntrySpace).isEqualTo(2.5f)

		this.component.xEntrySpace = 5f
		assertThat(this.component.xEntrySpace).isEqualTo(5f)
	}

	@Test
	fun setYEntrySpace() {
		assertThat(this.component.yEntrySpace).isEqualTo(0f)

		this.component.yEntrySpace = -5f
		assertThat(this.component.yEntrySpace).isEqualTo(-5f)

		this.component.yEntrySpace = -2.5f
		assertThat(this.component.yEntrySpace).isEqualTo(-2.5f)

		this.component.yEntrySpace = 0f
		assertThat(this.component.yEntrySpace).isEqualTo(0f)

		this.component.yEntrySpace = 2.5f
		assertThat(this.component.yEntrySpace).isEqualTo(2.5f)

		this.component.yEntrySpace = 5f
		assertThat(this.component.yEntrySpace).isEqualTo(5f)
	}

	@Test
	fun setFormToTextSpace() {
		assertThat(this.component.formToTextSpace).isEqualTo(5f)

		this.component.formToTextSpace = -5f
		assertThat(this.component.formToTextSpace).isEqualTo(-5f)

		this.component.formToTextSpace = -2.5f
		assertThat(this.component.formToTextSpace).isEqualTo(-2.5f)

		this.component.formToTextSpace = 0f
		assertThat(this.component.formToTextSpace).isEqualTo(0f)

		this.component.formToTextSpace = 2.5f
		assertThat(this.component.formToTextSpace).isEqualTo(2.5f)

		this.component.formToTextSpace = 5f
		assertThat(this.component.formToTextSpace).isEqualTo(5f)
	}

	@Test
	fun setStackSpace() {
		assertThat(this.component.stackSpace).isEqualTo(3f)

		this.component.stackSpace = -5f
		assertThat(this.component.stackSpace).isEqualTo(-5f)

		this.component.stackSpace = -2.5f
		assertThat(this.component.stackSpace).isEqualTo(-2.5f)

		this.component.stackSpace = 0f
		assertThat(this.component.stackSpace).isEqualTo(0f)

		this.component.stackSpace = 2.5f
		assertThat(this.component.stackSpace).isEqualTo(2.5f)

		this.component.stackSpace = 5f
		assertThat(this.component.stackSpace).isEqualTo(5f)
	}

	@Test
	fun setWordWrapEnabled() {
		assertThat(this.component.isWordWrapEnabled).isFalse()

		this.component.isWordWrapEnabled = true
		assertThat(this.component.isWordWrapEnabled).isTrue()

		this.component.isWordWrapEnabled = false
		assertThat(this.component.isWordWrapEnabled).isFalse()
	}

	@Test
	fun setMaxSizePercent() {
		assertThat(this.component.maxSizePercent).isEqualTo(0.95f)

		this.component.maxSizePercent = -5f
		assertThat(this.component.maxSizePercent).isEqualTo(-5f)

		this.component.maxSizePercent = -2.5f
		assertThat(this.component.maxSizePercent).isEqualTo(-2.5f)

		this.component.maxSizePercent = 0f
		assertThat(this.component.maxSizePercent).isEqualTo(0f)

		this.component.maxSizePercent = 2.5f
		assertThat(this.component.maxSizePercent).isEqualTo(2.5f)

		this.component.maxSizePercent = 5f
		assertThat(this.component.maxSizePercent).isEqualTo(5f)
	}

	@Test
	fun getCalculatedLabelSizes() {
		assertThat(this.component.calculatedLabelSizes).isEmpty()
	}

	@Test
	fun getCalculatedLabelBreakPoints() {
		assertThat(this.component.calculatedLabelBreakPoints).isEmpty()
	}

	@Test
	fun getCalculatedLineSizes() {
		assertThat(this.component.calculatedLineSizes).isEmpty()
	}
}
