package com.github.mikephil.charting.buffer

import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test

class HorizontalBarBufferTest {
	@Test
	fun barBuffer_empty_noStacks_notInverted() {
		val buffer = HorizontalBarBuffer(0, false)
		buffer.setInverted(false)

		// size()
		assertThat(buffer.size()).isEqualTo(0)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		try {
			buffer.addBar(1f, 2f, 3f, 4f)
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		try {
			buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}
	}

	@Test
	fun barBuffer_empty_stacks_notInverted() {
		val buffer = HorizontalBarBuffer(0, true)
		buffer.setInverted(false)

		// size()
		assertThat(buffer.size()).isEqualTo(0)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		try {
			buffer.addBar(1f, 2f, 3f, 4f)
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		try {
			buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}
	}

	@Test
	fun barBuffer_notEmpty_noStacks_notInverted() {
		val buffer = HorizontalBarBuffer(12, false)
		buffer.setInverted(false)

		// size()
		assertThat(buffer.size()).isEqualTo(12)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		buffer.addBar(1f, 2f, 3f, 4f)
		assertThat(buffer.index).isEqualTo(4)
		assertThat(buffer.buffer[0]).isEqualTo(1f)
		assertThat(buffer.buffer[1]).isEqualTo(2f)
		assertThat(buffer.buffer[2]).isEqualTo(3f)
		assertThat(buffer.buffer[3]).isEqualTo(4f)

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
		assertThat(buffer.index).isEqualTo(0)
		assertThat(buffer.buffer[0]).isEqualTo(0f)
		assertThat(buffer.buffer[1]).isEqualTo(5.5f)
		assertThat(buffer.buffer[2]).isEqualTo(5.25f)
		assertThat(buffer.buffer[3]).isEqualTo(4.5f)
	}

	@Test
	fun barBuffer_notEmpty_stacks_notInverted() {
		val buffer = HorizontalBarBuffer(12, true)
		buffer.setInverted(false)

		// size()
		assertThat(buffer.size()).isEqualTo(12)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		buffer.addBar(1f, 2f, 3f, 4f)
		assertThat(buffer.index).isEqualTo(4)
		assertThat(buffer.buffer[0]).isEqualTo(1f)
		assertThat(buffer.buffer[1]).isEqualTo(2f)
		assertThat(buffer.buffer[2]).isEqualTo(3f)
		assertThat(buffer.buffer[3]).isEqualTo(4f)

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
		assertThat(buffer.index).isEqualTo(0)
		assertThat(buffer.buffer[0]).isEqualTo(0f)
		assertThat(buffer.buffer[1]).isEqualTo(5.5f)
		assertThat(buffer.buffer[2]).isEqualTo(1.5f)
		assertThat(buffer.buffer[3]).isEqualTo(4.5f)
	}

	@Test
	fun barBuffer_empty_noStacks_inverted() {
		val buffer = HorizontalBarBuffer(0, false)
		buffer.setInverted(true)

		// size()
		assertThat(buffer.size()).isEqualTo(0)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		try {
			buffer.addBar(1f, 2f, 3f, 4f)
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		try {
			buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}
	}

	@Test
	fun barBuffer_empty_stacks_inverted() {
		val buffer = HorizontalBarBuffer(0, true)
		buffer.setInverted(true)

		// size()
		assertThat(buffer.size()).isEqualTo(0)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		try {
			buffer.addBar(1f, 2f, 3f, 4f)
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		try {
			buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
			fail()
		} catch (exception: IndexOutOfBoundsException) {
			assertThat(buffer.index).isEqualTo(1)
		}
	}

	@Test
	fun barBuffer_notEmpty_noStacks_inverted() {
		val buffer = HorizontalBarBuffer(12, false)
		buffer.setInverted(true)

		// size()
		assertThat(buffer.size()).isEqualTo(12)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		buffer.addBar(1f, 2f, 3f, 4f)
		assertThat(buffer.index).isEqualTo(4)
		assertThat(buffer.buffer[0]).isEqualTo(1f)
		assertThat(buffer.buffer[1]).isEqualTo(2f)
		assertThat(buffer.buffer[2]).isEqualTo(3f)
		assertThat(buffer.buffer[3]).isEqualTo(4f)

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
		assertThat(buffer.index).isEqualTo(0)
		assertThat(buffer.buffer[0]).isEqualTo(5.25f)
		assertThat(buffer.buffer[1]).isEqualTo(5.5f)
		assertThat(buffer.buffer[2]).isEqualTo(0f)
		assertThat(buffer.buffer[3]).isEqualTo(4.5f)
	}

	@Test
	fun barBuffer_notEmpty_stacks_inverted() {
		val buffer = HorizontalBarBuffer(12, true)
		buffer.setInverted(true)

		// size()
		assertThat(buffer.size()).isEqualTo(12)

		// phaseX & phaseY
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(-1f, -1f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(2f, 2f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0f, 0f)
		assertThat(buffer.phaseX).isEqualTo(0f)
		assertThat(buffer.phaseY).isEqualTo(0f)

		buffer.setPhases(1f, 1f)
		assertThat(buffer.phaseX).isEqualTo(1f)
		assertThat(buffer.phaseY).isEqualTo(1f)

		buffer.setPhases(0.75f, 0.25f)
		assertThat(buffer.phaseX).isEqualTo(0.75f)
		assertThat(buffer.phaseY).isEqualTo(0.25f)

		// addBar()
		buffer.addBar(1f, 2f, 3f, 4f)
		assertThat(buffer.index).isEqualTo(4)
		assertThat(buffer.buffer[0]).isEqualTo(1f)
		assertThat(buffer.buffer[1]).isEqualTo(2f)
		assertThat(buffer.buffer[2]).isEqualTo(3f)
		assertThat(buffer.buffer[3]).isEqualTo(4f)

		// reset()
		buffer.reset()
		assertThat(buffer.index).isEqualTo(0)

		// feed()
		buffer.feed(BarDataSet(emptyList(), ""))
		assertThat(buffer.index).isEqualTo(0)

		buffer.feed(BarDataSet(listOf(BarEntry(5f, floatArrayOf(6f, 7f, 8f))), "Foo"))
		assertThat(buffer.index).isEqualTo(0)
		assertThat(buffer.buffer[0]).isEqualTo(1.5f)
		assertThat(buffer.buffer[1]).isEqualTo(5.5f)
		assertThat(buffer.buffer[2]).isEqualTo(0f)
		assertThat(buffer.buffer[3]).isEqualTo(4.5f)
	}
}
