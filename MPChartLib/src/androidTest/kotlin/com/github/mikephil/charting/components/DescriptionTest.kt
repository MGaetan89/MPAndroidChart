package com.github.mikephil.charting.components

import android.graphics.Paint
import androidx.test.runner.AndroidJUnit4
import com.github.mikephil.charting.utils.MPPointF
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DescriptionTest : ComponentBaseTest<Description>() {
	@Before
	fun before() {
		this.component = Description()
	}

	@Test
	override fun textSize() {
		assertThat(this.component.textSize).isEqualTo(8f)

		this.component.textSize = 5f
		assertThat(this.component.textSize).isEqualTo(6f)

		this.component.textSize = 12f
		assertThat(this.component.textSize).isEqualTo(12f)

		this.component.textSize = 25f
		assertThat(this.component.textSize).isEqualTo(24f)
	}

	@Test
	fun text() {
		assertThat(this.component.text).isEqualTo("Description Label")

		this.component.setText(null)
		assertThat(this.component.text).isEmpty()

		this.component.setText("Hello, World!")
		assertThat(this.component.text).isEqualTo("Hello, World!")

		this.component.setText("")
		assertThat(this.component.text).isEmpty()
	}

	@Test
	fun position() {
		assertThat(this.component.position).isNull()

		this.component.setPosition(1f, 2f)
		assertThat(this.component.position).isEqualTo(MPPointF(1f, 2f))

		val position = this.component.position

		this.component.setPosition(2f, 3f)
		assertThat(this.component.position).isSameAs(position)
		assertThat(this.component.position).isEqualTo(MPPointF(2f, 3f))
	}

	@Test
	fun textAlign() {
		assertThat(this.component.textAlign).isEqualTo(Paint.Align.RIGHT)

		this.component.textAlign = Paint.Align.CENTER
		assertThat(this.component.textAlign).isEqualTo(Paint.Align.CENTER)

		this.component.textAlign = Paint.Align.LEFT
		assertThat(this.component.textAlign).isEqualTo(Paint.Align.LEFT)

		this.component.textAlign = Paint.Align.RIGHT
		assertThat(this.component.textAlign).isEqualTo(Paint.Align.RIGHT)
	}
}
