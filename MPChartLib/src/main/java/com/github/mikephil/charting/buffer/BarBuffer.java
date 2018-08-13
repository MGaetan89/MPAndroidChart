package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

public class BarBuffer extends AbstractBuffer<IBarDataSet> {
    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    protected int mDataSetIndex = 0;

    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    protected int mDataSetCount;
    protected boolean mContainsStacks;
    protected boolean mInverted = false;

    /**
     * Width of the bar on the x-axis, in values (not pixels).
     */
    protected float mBarWidth = 1f;

    /**
     * @param size
     * @param dataSetCount
     * @param containsStacks
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0. Use <code>BarBuffer(int, boolean)</code> instead.
     */
    @Deprecated
    public BarBuffer(@Size(multiple = 4L) int size, @SuppressWarnings("unused") int dataSetCount, boolean containsStacks) {
        this(size, containsStacks);
    }

    public BarBuffer(@Size(multiple = 4L) int size, boolean containsStacks) {
        super(size);

        this.mContainsStacks = containsStacks;
    }

    public void setBarWidth(float barWidth) {
        this.mBarWidth = Math.max(barWidth, 1f);
    }

    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    public void setInverted(boolean inverted) {
        this.mInverted = inverted;
    }

    protected void addBar(float left, float top, float right, float bottom) {
        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;
    }

    @Override
    public void feed(@NonNull IBarDataSet data) {
        float size = data.getEntryCount() * phaseX;
        float barWidthHalf = mBarWidth / 2f;

        for (int i = 0; i < size; i++) {
            BarEntry entry = data.getEntryForIndex(i);
            float x = entry.getX();
            float y = entry.getY();
            float[] values = entry.getYVals();
            if (mContainsStacks && values != null) {
                float posY = 0f;
                float negY = -entry.getNegativeSum();
                float yStart;

                // fill the stack
                for (float value : values) {
                    if (value == 0f && (posY == 0f || negY == 0f)) {
                        // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                        y = value;
                        yStart = y;
                    } else if (value >= 0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }

                    float left = x - barWidthHalf;
                    float right = x + barWidthHalf;
                    float bottom;
                    float top;
                    if (mInverted) {
                        bottom = y >= yStart ? y : yStart;
                        top = y <= yStart ? y : yStart;
                    } else {
                        top = y >= yStart ? y : yStart;
                        bottom = y <= yStart ? y : yStart;
                    }

                    // multiply the height of the rect with the phase
                    top *= phaseY;
                    bottom *= phaseY;

                    addBar(left, top, right, bottom);
                }
            } else {
                float left = x - barWidthHalf;
                float right = x + barWidthHalf;
                float bottom;
                float top;
                if (mInverted) {
                    bottom = y >= 0f ? y : 0f;
                    top = y <= 0f ? y : 0f;
                } else {
                    top = y >= 0f ? y : 0f;
                    bottom = y <= 0f ? y : 0f;
                }

                // multiply the height of the rect with the phase
                if (top > 0f) {
                    top *= phaseY;
                } else {
                    bottom *= phaseY;
                }

                addBar(left, top, right, bottom);
            }
        }

        reset();
    }
}
