package com.github.mikephil.charting.components

import android.support.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XAxisTest : AxisBaseTest<XAxis>() {
	@Before
	fun before() {
		this.component = XAxis()
	}

	@Test
	override fun yOffset() {
		assertThat(this.component.yOffset).isEqualTo(4f)

		this.component.yOffset = 10f
		assertThat(this.component.yOffset).isEqualTo(10f)
	}

	@Test
	fun setPosition() {
		assertThat(this.component.position).isEqualTo(XAxis.XAxisPosition.TOP)

		XAxis.XAxisPosition.values().forEach {
			this.component.position = it
			assertThat(this.component.position).isEqualTo(it)
		}
	}

	@Test
	fun setLabelRotationAngle() {
		assertThat(this.component.labelRotationAngle).isEqualTo(0f)

		this.component.labelRotationAngle = -5f
		assertThat(this.component.labelRotationAngle).isEqualTo(-5f)

		this.component.labelRotationAngle = -2.5f
		assertThat(this.component.labelRotationAngle).isEqualTo(-2.5f)

		this.component.labelRotationAngle = 0f
		assertThat(this.component.labelRotationAngle).isEqualTo(0f)

		this.component.labelRotationAngle = 2.5f
		assertThat(this.component.labelRotationAngle).isEqualTo(2.5f)

		this.component.labelRotationAngle = 5f
		assertThat(this.component.labelRotationAngle).isEqualTo(5f)
	}

	@Test
	fun setAvoidFirstLastClipping() {
		assertThat(this.component.isAvoidFirstLastClippingEnabled).isFalse()

		this.component.setAvoidFirstLastClipping(true)
		assertThat(this.component.isAvoidFirstLastClippingEnabled).isTrue()

		this.component.setAvoidFirstLastClipping(false)
		assertThat(this.component.isAvoidFirstLastClippingEnabled).isFalse()
	}
}
