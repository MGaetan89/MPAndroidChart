package com.github.mikephil.charting.listener

import android.view.MotionEvent
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.highlight.Highlight
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Test
import org.mockito.internal.verification.Only
import org.mockito.internal.verification.Times

abstract class ChartTouchListenerTest<C : Chart<*>, L : ChartTouchListener<C>> {
    protected lateinit var chart: C
    protected lateinit var listener: L

    @Test
    fun startAction() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.chart.onChartGestureListener = gestureListener
        this.listener.startAction(event)
        verify(gestureListener).onChartGestureStart(event, this.listener.lastGesture)
    }

    @Test
    fun startAction_noListener() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.listener.startAction(event)
        verifyZeroInteractions(gestureListener)
    }

    @Test
    fun endAction() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.listener.endAction(event)
        verifyZeroInteractions(gestureListener)

        this.chart.onChartGestureListener = gestureListener
        this.listener.endAction(event)
        verify(gestureListener).onChartGestureEnd(event, this.listener.lastGesture)
    }

    @Test
    fun endAction_noListener() {
        val event = mock<MotionEvent>()
        val gestureListener = mock<OnChartGestureListener>()

        this.listener.endAction(event)
        verifyZeroInteractions(gestureListener)
    }

    @Test
    fun setLastHighlighted() {
        assertThat(this.listener.mLastHighlighted).isNull()

        val highlight = Highlight(1f, 2.3f, 4)
        this.listener.setLastHighlighted(highlight)
        assertThat(this.listener.mLastHighlighted).isEqualTo(highlight)

        this.listener.setLastHighlighted(null)
        assertThat(this.listener.mLastHighlighted).isNull()
    }

    @Test
    fun getTouchMode() {
        assertThat(this.listener.touchMode).isEqualTo(ChartTouchListener.NONE)
    }

    @Test
    fun getLastGesture() {
        assertThat(this.listener.lastGesture).isEqualTo(ChartTouchListener.ChartGesture.NONE)
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
    fun performHighlight_duplicateHighlight() {
        val event = mock<MotionEvent>()
        val selectionListener = mock<OnChartValueSelectedListener>()
        val highlight = Highlight(5f, 6.7f, 8)

        this.chart.setOnChartValueSelectedListener(selectionListener)

        this.listener.performHighlight(highlight, event)
        verify(this.chart).highlightValue(highlight, true)
        verify(selectionListener).onNothingSelected()
        assertThat(this.listener.mLastHighlighted).isEqualTo(highlight)

        this.listener.performHighlight(highlight, event)
        verify(this.chart).highlightValue(null, true)
        verify(selectionListener, Times(2)).onNothingSelected()
        assertThat(this.listener.mLastHighlighted).isNull()
    }

    @Test
    fun performHighlight_noHighlight() {
        val event = mock<MotionEvent>()
        val selectionListener = mock<OnChartValueSelectedListener>()

        this.chart.setOnChartValueSelectedListener(selectionListener)

        this.listener.performHighlight(null, event)
        verify(this.chart).highlightValue(null, true)
        verify(selectionListener, Only()).onNothingSelected()
        assertThat(this.listener.mLastHighlighted).isNull()
    }

    @Test
    fun performHighlight_withHighlight() {
        val event = mock<MotionEvent>()
        val selectionListener = mock<OnChartValueSelectedListener>()
        val highlight = Highlight(1f, 2.3f, 4)

        this.chart.setOnChartValueSelectedListener(selectionListener)

        this.listener.performHighlight(highlight, event)
        verify(this.chart).highlightValue(highlight, true)
        verify(selectionListener, Only()).onNothingSelected()
        assertThat(this.listener.mLastHighlighted).isEqualTo(highlight)
    }

    @Test
    fun distance() {
        assertThat(ChartTouchListener.distance(0f, 0f, 0f, 0f)).isEqualTo(0f)
        assertThat(ChartTouchListener.distance(0f, 0f, 25f, 25f)).isEqualTo(0f)
        assertThat(ChartTouchListener.distance(0f, 0f, 25f, 50f)).isEqualTo(25f)
        assertThat(ChartTouchListener.distance(0f, 0f, 50f, 25f)).isEqualTo(25f)

        assertThat(ChartTouchListener.distance(50f, 50f, 0f, 0f)).isEqualTo(0f)
        assertThat(ChartTouchListener.distance(50f, 50f, 25f, 25f)).isEqualTo(0f)
        assertThat(ChartTouchListener.distance(50f, 50f, 25f, 50f)).isEqualTo(25f)
        assertThat(ChartTouchListener.distance(50f, 50f, 50f, 25f)).isEqualTo(25f)

        assertThat(ChartTouchListener.distance(50f, 100f, 0f, 0f)).isEqualTo(50f)
        assertThat(ChartTouchListener.distance(50f, 100f, 25f, 25f)).isEqualTo(50f)
        assertThat(ChartTouchListener.distance(50f, 100f, 25f, 50f)).isEqualTo(55.9017f)
        assertThat(ChartTouchListener.distance(50f, 100f, 50f, 25f)).isEqualTo(55.9017f)

        assertThat(ChartTouchListener.distance(100f, 50f, 0f, 0f)).isEqualTo(50f)
        assertThat(ChartTouchListener.distance(100f, 50f, 25f, 25f)).isEqualTo(50f)
        assertThat(ChartTouchListener.distance(100f, 50f, 25f, 50f)).isEqualTo(55.9017f)
        assertThat(ChartTouchListener.distance(100f, 50f, 50f, 25f)).isEqualTo(55.9017f)
    }
}
