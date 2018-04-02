package com.github.mikephil.charting.data

import android.graphics.Color
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.renderer.scatter.ChevronDownShapeRenderer
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer
import com.github.mikephil.charting.renderer.scatter.CircleShapeRenderer
import com.github.mikephil.charting.renderer.scatter.CrossShapeRenderer
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer
import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer
import com.github.mikephil.charting.renderer.scatter.TriangleShapeRenderer
import com.github.mikephil.charting.renderer.scatter.XShapeRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ScatterDataSetTest : LineScatterCandleRadarDataSetTest<Entry, ScatterDataSet>() {
	@Before
	fun before() {
		this.dataSet = ScatterDataSet(mutableListOf(), "ScatterDataSet")
		this.values = mutableListOf(Entry(1f, 2f), Entry(3f, 4f), Entry(5f, 6f))
		this.entry = Entry(7f, 8f)
	}

	@Test
	fun copy() {
		with(this.dataSet.copy() as ScatterDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).isEmpty()
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.isDrawValuesEnabled).isEqualTo(dataSet.isDrawValuesEnabled)
			assertThat(this.valueColors).containsExactlyElementsIn(dataSet.valueColors).inOrder()
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.scatterShapeSize).isEqualTo(dataSet.scatterShapeSize)
			assertThat(this.shapeRenderer).isEqualTo(dataSet.shapeRenderer)
			assertThat(this.scatterShapeHoleRadius).isEqualTo(dataSet.scatterShapeHoleRadius)
			assertThat(this.scatterShapeHoleColor).isEqualTo(dataSet.scatterShapeHoleColor)
			assertThat(this.highlightLineWidth).isEqualTo(dataSet.highlightLineWidth)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.dashPathEffectHighlight).isEqualTo(dataSet.dashPathEffectHighlight)
		}

		this.dataSet.values = this.values
		with(this.dataSet.copy() as ScatterDataSet) {
			assertThat(this).isNotSameAs(dataSet)
			assertThat(this.values).hasSize(dataSet.values.size)
			assertThat(this.label).isEqualTo(dataSet.label)
			assertThat(this.isDrawValuesEnabled).isEqualTo(dataSet.isDrawValuesEnabled)
			assertThat(this.valueColors).containsExactlyElementsIn(dataSet.valueColors).inOrder()
			assertThat(this.colors).containsExactlyElementsIn(dataSet.colors).inOrder()
			assertThat(this.scatterShapeSize).isEqualTo(dataSet.scatterShapeSize)
			assertThat(this.shapeRenderer).isEqualTo(dataSet.shapeRenderer)
			assertThat(this.scatterShapeHoleRadius).isEqualTo(dataSet.scatterShapeHoleRadius)
			assertThat(this.scatterShapeHoleColor).isEqualTo(dataSet.scatterShapeHoleColor)
			assertThat(this.highlightLineWidth).isEqualTo(dataSet.highlightLineWidth)
			assertThat(this.highLightColor).isEqualTo(dataSet.highLightColor)
			assertThat(this.dashPathEffectHighlight).isEqualTo(dataSet.dashPathEffectHighlight)
		}
	}

	@Test
	fun setScatterShapeSize() {
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(15f)

		this.dataSet.scatterShapeSize = -5f
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(-5f)

		this.dataSet.scatterShapeSize = -2.5f
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(-2.5f)

		this.dataSet.scatterShapeSize = 0f
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(0f)

		this.dataSet.scatterShapeSize = 2.5f
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(2.5f)

		this.dataSet.scatterShapeSize = 5f
		assertThat(this.dataSet.scatterShapeSize).isEqualTo(5f)
	}

	@Test
	fun setScatterShape() {
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(SquareShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(CircleShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.TRIANGLE)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(TriangleShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.CROSS)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(CrossShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.X)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(XShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.CHEVRON_UP)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(ChevronUpShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.CHEVRON_DOWN)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(ChevronDownShapeRenderer::class.java)

		this.dataSet.setScatterShape(ScatterChart.ScatterShape.SQUARE)
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(SquareShapeRenderer::class.java)
	}

	@Test
	fun setShapeRenderer() {
		assertThat(this.dataSet.shapeRenderer).isInstanceOf(SquareShapeRenderer::class.java)

		var renderer: IShapeRenderer = CircleShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = TriangleShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = CrossShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = XShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = ChevronUpShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = ChevronDownShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)

		renderer = SquareShapeRenderer()
		this.dataSet.shapeRenderer = renderer
		assertThat(this.dataSet.shapeRenderer).isEqualTo(renderer)
	}

	@Test
	fun setScatterShapeHoleRadius() {
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(0f)

		this.dataSet.scatterShapeHoleRadius = -5f
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(-5f)

		this.dataSet.scatterShapeHoleRadius = -2.5f
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(-2.5f)

		this.dataSet.scatterShapeHoleRadius = 0f
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(0f)

		this.dataSet.scatterShapeHoleRadius = 2.5f
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(2.5f)

		this.dataSet.scatterShapeHoleRadius = 5f
		assertThat(this.dataSet.scatterShapeHoleRadius).isEqualTo(5f)
	}

	@Test
	fun setScatterShapeHoleColor() {
		assertThat(this.dataSet.scatterShapeHoleColor).isEqualTo(ColorTemplate.COLOR_NONE)

		this.dataSet.scatterShapeHoleColor = Color.RED
		assertThat(this.dataSet.scatterShapeHoleColor).isEqualTo(Color.RED)
	}
}
