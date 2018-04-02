package com.github.mikephil.charting.data

import android.graphics.Color
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.mikephil.charting.TestActivity
import com.github.mikephil.charting.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaseDataSetAndroidTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	private lateinit var dataSet: BaseDataSet<out Entry>

	@Before
	fun before() {
		this.dataSet = PieDataSet(emptyList(), "PieDataSet")
	}

	@Test
	fun setColors() {
		assertThat(this.dataSet.colors).containsExactly(0x8CEAFF)
		assertThat(this.dataSet.color).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(0)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(1)).isEqualTo(0x8CEAFF)

		val context = this.activityRule.activity
		val colors = intArrayOf(R.color.blue, R.color.green, R.color.red)
		val expectedColors = colors.map(context::getColor)
		this.dataSet.setColors(colors, context)
		assertThat(this.dataSet.colors).containsExactlyElementsIn(expectedColors).inOrder()
		assertThat(this.dataSet.color).isEqualTo(expectedColors[0])

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("length=10; index=-1")
		}

		assertThat(this.dataSet.getColor(0)).isEqualTo(expectedColors[0])
		assertThat(this.dataSet.getColor(1)).isEqualTo(expectedColors[1])
		assertThat(this.dataSet.getColor(2)).isEqualTo(expectedColors[2])
		assertThat(this.dataSet.getColor(3)).isEqualTo(expectedColors[0])
	}

	@Test
	fun setColor() {
		assertThat(this.dataSet.colors).containsExactly(0x8CEAFF)
		assertThat(this.dataSet.color).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(0)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(1)).isEqualTo(0x8CEAFF)

		val alpha = 128
		val color = Color.RED
		val expectedColor = (color and 0x00FFFFFF) or (alpha shl 24)
		this.dataSet.setColor(color, alpha)
		assertThat(this.dataSet.colors).containsExactly(expectedColor)
		assertThat(this.dataSet.color).isEqualTo(expectedColor)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(expectedColor)
		assertThat(this.dataSet.getColor(0)).isEqualTo(expectedColor)
		assertThat(this.dataSet.getColor(1)).isEqualTo(expectedColor)
	}

	@Test
	fun setColors_alpha() {
		assertThat(this.dataSet.colors).containsExactly(0x8CEAFF)
		assertThat(this.dataSet.color).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(-1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(0)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getColor(1)).isEqualTo(0x8CEAFF)

		val alpha = 128
		val colors = intArrayOf(Color.BLUE, Color.GREEN, Color.RED)
		val expectedColors = colors.map { (it and 0x00FFFFFF) or (alpha shl 24) }
		this.dataSet.setColors(colors, alpha)
		assertThat(this.dataSet.colors).containsExactlyElementsIn(expectedColors).inOrder()
		assertThat(this.dataSet.color).isEqualTo(expectedColors[0])

		try {
			this.dataSet.getColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.isEqualTo("length=10; index=-1")
		}

		assertThat(this.dataSet.getColor(0)).isEqualTo(expectedColors[0])
		assertThat(this.dataSet.getColor(1)).isEqualTo(expectedColors[1])
		assertThat(this.dataSet.getColor(2)).isEqualTo(expectedColors[2])
		assertThat(this.dataSet.getColor(3)).isEqualTo(expectedColors[0])
	}
}
