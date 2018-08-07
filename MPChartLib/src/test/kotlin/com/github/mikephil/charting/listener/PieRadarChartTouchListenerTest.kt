package com.github.mikephil.charting.listener

import android.view.MotionEvent
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.PieRadarChartBase
import com.github.mikephil.charting.data.PieData
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Before
import org.junit.Test
import org.mockito.internal.verification.Times

class PieRadarChartTouchListenerTest : ChartTouchListenerTest<PieRadarChartBase<*>, PieRadarChartTouchListener>() {
    @Before
    fun before() {
        this.chart = spy(PieChart(mock()).apply {
            this.data = PieData()
        })
        this.listener = PieRadarChartTouchListener(this.chart)
    }

    @Test
    fun onTouch() {
        // TODO
    }

    @Test
    fun onLongPress() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.onChartGestureListener = gestureListener
        this.listener.onLongPress(event)

        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.LONG_PRESS)
        verify(gestureListener).onChartLongPressed(event)
    }

    @Test
    fun onLongPress_noListener() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.listener.onLongPress(event)

        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.LONG_PRESS)
        verifyZeroInteractions(gestureListener)
    }

    @Test
    fun onSingleTapConfirmed() {
        val event = mock<MotionEvent>()

        assertThat(this.listener.onSingleTapConfirmed(event)).isTrue()
    }

    @Test
    fun onSingleTapUp_withHighlightPerTap() {
        val event = mock<MotionEvent> {
            on { x } doReturn 1f
            on { y } doReturn 2f
        }
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.isHighlightPerTapEnabled = true
        this.chart.onChartGestureListener = gestureListener

        assertThat(this.listener.onSingleTapUp(event)).isTrue()
        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.SINGLE_TAP)
        verify(gestureListener).onChartSingleTapped(event)
        verify(this.chart).getHighlightByTouchPoint(event.x, event.y)
    }

    @Test
    fun onSingleTapUp_withHighlightPerTap_noListener() {
        val event = mock<MotionEvent> {
            on { x } doReturn 1f
            on { y } doReturn 2f
        }
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.isHighlightPerTapEnabled = true

        assertThat(this.listener.onSingleTapUp(event)).isTrue()
        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.SINGLE_TAP)
        verifyZeroInteractions(gestureListener)
        verify(this.chart).getHighlightByTouchPoint(event.x, event.y)
    }

    @Test
    fun onSingleTapUp_withoutHighlightPerTap() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.isHighlightPerTapEnabled = false
        this.chart.onChartGestureListener = gestureListener

        assertThat(this.listener.onSingleTapUp(event)).isFalse()
        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.SINGLE_TAP)
        verify(gestureListener).onChartSingleTapped(event)
        verify(this.chart, Times(0)).getHighlightByTouchPoint(any(), any())
    }

    @Test
    fun onSingleTapUp_withoutHighlightPerTap_noListener() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.isHighlightPerTapEnabled = false

        assertThat(this.listener.onSingleTapUp(event)).isFalse()
        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.SINGLE_TAP)
        verifyZeroInteractions(gestureListener)
        verify(this.chart, Times(0)).getHighlightByTouchPoint(any(), any())
    }

    @Test
    fun setGestureStartAngle_customAngle() {
        this.chart.rotationAngle = 45f

        assertThat(this.listener.gestureStartAngle).isEqualTo(0f)

        this.listener.setGestureStartAngle(-5f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(168.4248f)

        this.listener.setGestureStartAngle(-5f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(135f)

        this.listener.setGestureStartAngle(-5f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(101.575195f)

        this.listener.setGestureStartAngle(0f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(225f)

        this.listener.setGestureStartAngle(0f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(Float.NaN)

        this.listener.setGestureStartAngle(0f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(45f)

        this.listener.setGestureStartAngle(5f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(281.5752f)

        this.listener.setGestureStartAngle(5f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(315f)

        this.listener.setGestureStartAngle(5f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-11.575195f)
    }

    @Test
    fun setGestureStartAngle_defaultAngle() {
        assertThat(this.listener.gestureStartAngle).isEqualTo(0f)

        this.listener.setGestureStartAngle(-5f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-56.575195f)

        this.listener.setGestureStartAngle(-5f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-90f)

        this.listener.setGestureStartAngle(-5f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-123.424805f)

        this.listener.setGestureStartAngle(0f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(0f)

        this.listener.setGestureStartAngle(0f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(Float.NaN)

        this.listener.setGestureStartAngle(0f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-180f)

        this.listener.setGestureStartAngle(5f, -3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(56.575195f)

        this.listener.setGestureStartAngle(5f, 0f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(90f)

        this.listener.setGestureStartAngle(5f, 3.3f)
        assertThat(this.listener.gestureStartAngle).isEqualTo(-236.5752f)
    }

    @Test
    fun updateGestureRotation() {
        this.listener.updateGestureRotation(-5f, -3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(213.4248f)
        assertThat(this.chart.rotationAngle).isEqualTo(213.4248f)

        this.listener.updateGestureRotation(-5f, 0f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(180f)
        assertThat(this.chart.rotationAngle).isEqualTo(180f)

        this.listener.updateGestureRotation(-5f, 3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(146.5752f)
        assertThat(this.chart.rotationAngle).isEqualTo(146.5752f)

        this.listener.updateGestureRotation(0f, -3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(270f)
        assertThat(this.chart.rotationAngle).isEqualTo(270f)

        this.listener.updateGestureRotation(0f, 0f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(Float.NaN)
        assertThat(this.chart.rotationAngle).isEqualTo(Float.NaN)

        this.listener.updateGestureRotation(0f, 3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(90f)
        assertThat(this.chart.rotationAngle).isEqualTo(90f)

        this.listener.updateGestureRotation(5f, -3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(326.5752f)
        assertThat(this.chart.rotationAngle).isEqualTo(326.5752f)

        this.listener.updateGestureRotation(5f, 0f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(360f)
        assertThat(this.chart.rotationAngle).isEqualTo(0f)

        this.listener.updateGestureRotation(5f, 3.3f)
        assertThat(this.chart.rawRotationAngle).isEqualTo(33.424805f)
        assertThat(this.chart.rotationAngle).isEqualTo(33.424805f)
    }
}
