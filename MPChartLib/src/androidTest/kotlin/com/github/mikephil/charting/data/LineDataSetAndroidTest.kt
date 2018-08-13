package com.github.mikephil.charting.data

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.TestActivity
import com.github.mikephil.charting.test.R
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LineDataSetAndroidTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

	private lateinit var dataSet: LineDataSet

	@Before
	fun before() {
		this.dataSet = LineDataSet(emptyList(), "LineDataSet")
	}

	@Test
	fun setCircleColors() {
		assertThat(this.dataSet.circleColors).containsExactly(0x8CEAFF)
		assertThat(this.dataSet.getCircleColor(-1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getCircleColor(0)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.getCircleColor(1)).isEqualTo(0x8CEAFF)
		assertThat(this.dataSet.circleColorCount).isEqualTo(1)

		val context = this.activityRule.activity
		val colors = intArrayOf(R.color.blue, R.color.green, R.color.red)
		val expectedColors = colors.map(context.resources::getColor)
		this.dataSet.setCircleColors(colors, context)
		assertThat(this.dataSet.circleColors).containsExactlyElementsIn(expectedColors).inOrder()
		assertThat(this.dataSet.circleColorCount).isEqualTo(3)

		try {
			this.dataSet.getCircleColor(-1)
			fail("Should have failed")
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(exception).hasMessageThat()
				.endsWith("index=-1")
		}

		assertThat(this.dataSet.getCircleColor(0)).isEqualTo(expectedColors[0])
		assertThat(this.dataSet.getCircleColor(1)).isEqualTo(expectedColors[1])
		assertThat(this.dataSet.getCircleColor(2)).isEqualTo(expectedColors[2])
		assertThat(this.dataSet.getCircleColor(3)).isEqualTo(expectedColors[0])
	}
}
