package com.github.mikephil.charting.highlight

import com.github.mikephil.charting.components.YAxis
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class HighlightTest {
	private lateinit var highlight1: Highlight
	private lateinit var highlight2: Highlight
	private lateinit var highlight3: Highlight
	private lateinit var highlight4: Highlight

	@Before
	fun before() {
		this.highlight1 = Highlight(1f, 2f, 3)
		this.highlight2 = Highlight(1f, 2, 3)
		this.highlight3 = Highlight(1f, 2f, 3f, 4f, 5, YAxis.AxisDependency.LEFT)
		this.highlight4 = Highlight(1f, 2f, 3f, 4f, 5, 6, YAxis.AxisDependency.LEFT)
	}

	@Test
	fun highlight1() {
		assertThat(this.highlight1.x).isEqualTo(1f)
		assertThat(this.highlight1.y).isEqualTo(2f)
		assertThat(this.highlight1.xPx).isEqualTo(0f)
		assertThat(this.highlight1.yPx).isEqualTo(0f)
		assertThat(this.highlight1.dataIndex).isEqualTo(-1)
		assertThat(this.highlight1.dataSetIndex).isEqualTo(3)
		assertThat(this.highlight1.stackIndex).isEqualTo(-1)
		assertThat(this.highlight1.isStacked).isFalse()
		assertThat(this.highlight1.axis).isNull()
		assertThat(this.highlight1.drawX).isEqualTo(0f)
		assertThat(this.highlight1.drawY).isEqualTo(0f)
		assertThat(this.highlight1 == this.highlight1).isTrue()
		assertThat(this.highlight1 == this.highlight2).isFalse()
		assertThat(this.highlight1 == this.highlight3).isFalse()
		assertThat(this.highlight1 == this.highlight4).isFalse()
		assertThat(this.highlight1.toString()).isEqualTo("Highlight, x: 1.0, y: 2.0, dataSetIndex: 3, stackIndex (only stacked barentry): -1")

		this.highlight1.dataIndex = 4
		assertThat(this.highlight1.dataIndex).isEqualTo(4)

		this.highlight1.setDraw(5f, 6f)
		assertThat(this.highlight1.drawX).isEqualTo(5f)
		assertThat(this.highlight1.drawY).isEqualTo(6f)
	}

	@Test
	fun highlight2() {
		assertThat(this.highlight2.x).isEqualTo(1f)
		assertThat(this.highlight2.y).isEqualTo(Float.NaN)
		assertThat(this.highlight2.xPx).isEqualTo(0f)
		assertThat(this.highlight2.yPx).isEqualTo(0f)
		assertThat(this.highlight2.dataIndex).isEqualTo(-1)
		assertThat(this.highlight2.dataSetIndex).isEqualTo(2)
		assertThat(this.highlight2.stackIndex).isEqualTo(3)
		assertThat(this.highlight2.isStacked).isTrue()
		assertThat(this.highlight2.axis).isNull()
		assertThat(this.highlight2.drawX).isEqualTo(0f)
		assertThat(this.highlight2.drawY).isEqualTo(0f)
		assertThat(this.highlight2 == this.highlight1).isFalse()
		assertThat(this.highlight2 == this.highlight2).isTrue()
		assertThat(this.highlight2 == this.highlight3).isFalse()
		assertThat(this.highlight2 == this.highlight4).isFalse()
		assertThat(this.highlight2.toString()).isEqualTo("Highlight, x: 1.0, y: NaN, dataSetIndex: 2, stackIndex (only stacked barentry): 3")

		this.highlight2.dataIndex = 4
		assertThat(this.highlight2.dataIndex).isEqualTo(4)

		this.highlight2.setDraw(5f, 6f)
		assertThat(this.highlight2.drawX).isEqualTo(5f)
		assertThat(this.highlight2.drawY).isEqualTo(6f)
	}

	@Test
	fun highlight3() {
		assertThat(this.highlight3.x).isEqualTo(1f)
		assertThat(this.highlight3.y).isEqualTo(2f)
		assertThat(this.highlight3.xPx).isEqualTo(3f)
		assertThat(this.highlight3.yPx).isEqualTo(4f)
		assertThat(this.highlight3.dataIndex).isEqualTo(-1)
		assertThat(this.highlight3.dataSetIndex).isEqualTo(5)
		assertThat(this.highlight3.stackIndex).isEqualTo(-1)
		assertThat(this.highlight3.isStacked).isFalse()
		assertThat(this.highlight3.axis).isEqualTo(YAxis.AxisDependency.LEFT)
		assertThat(this.highlight3.drawX).isEqualTo(0f)
		assertThat(this.highlight3.drawY).isEqualTo(0f)
		assertThat(this.highlight3 == this.highlight1).isFalse()
		assertThat(this.highlight3 == this.highlight2).isFalse()
		assertThat(this.highlight3 == this.highlight3).isTrue()
		assertThat(this.highlight3 == this.highlight4).isFalse()
		assertThat(this.highlight3.toString()).isEqualTo("Highlight, x: 1.0, y: 2.0, dataSetIndex: 5, stackIndex (only stacked barentry): -1")

		this.highlight3.dataIndex = 6
		assertThat(this.highlight3.dataIndex).isEqualTo(6)

		this.highlight3.setDraw(7f, 8f)
		assertThat(this.highlight3.drawX).isEqualTo(7f)
		assertThat(this.highlight3.drawY).isEqualTo(8f)
	}

	@Test
	fun highlight4() {
		assertThat(this.highlight4.x).isEqualTo(1f)
		assertThat(this.highlight4.y).isEqualTo(2f)
		assertThat(this.highlight4.xPx).isEqualTo(3f)
		assertThat(this.highlight4.yPx).isEqualTo(4f)
		assertThat(this.highlight4.dataIndex).isEqualTo(-1)
		assertThat(this.highlight4.dataSetIndex).isEqualTo(5)
		assertThat(this.highlight4.stackIndex).isEqualTo(6)
		assertThat(this.highlight4.isStacked).isTrue()
		assertThat(this.highlight4.axis).isEqualTo(YAxis.AxisDependency.LEFT)
		assertThat(this.highlight4.drawX).isEqualTo(0f)
		assertThat(this.highlight4.drawY).isEqualTo(0f)
		assertThat(this.highlight4 == this.highlight1).isFalse()
		assertThat(this.highlight4 == this.highlight2).isFalse()
		assertThat(this.highlight4 == this.highlight3).isFalse()
		assertThat(this.highlight4 == this.highlight4).isTrue()
		assertThat(this.highlight4.toString()).isEqualTo("Highlight, x: 1.0, y: 2.0, dataSetIndex: 5, stackIndex (only stacked barentry): 6")

		this.highlight4.dataIndex = 7
		assertThat(this.highlight4.dataIndex).isEqualTo(7)

		this.highlight4.setDraw(8f, 9f)
		assertThat(this.highlight4.drawX).isEqualTo(8f)
		assertThat(this.highlight4.drawY).isEqualTo(9f)
	}
}
