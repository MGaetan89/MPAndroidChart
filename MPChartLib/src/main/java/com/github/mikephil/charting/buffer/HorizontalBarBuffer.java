package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

public class HorizontalBarBuffer extends BarBuffer {
    /**
     * @param size
     * @param dataSetCount
     * @param containsStacks
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0. Use <code>HorizontalBarBuffer(int, boolean)</code> instead.
     */
    @Deprecated
    public HorizontalBarBuffer(@Size(multiple = 4L) int size, @SuppressWarnings("unused") int dataSetCount, boolean containsStacks) {
        this(size, containsStacks);
    }

    public HorizontalBarBuffer(@Size(multiple = 4L) int size, boolean containsStacks) {
        super(size, containsStacks);
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
                    if (value >= 0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }

                    float bottom = x - barWidthHalf;
                    float top = x + barWidthHalf;
                    float left;
                    float right;
                    if (mInverted) {
                        left = y >= yStart ? y : yStart;
                        right = y <= yStart ? y : yStart;
                    } else {
                        right = y >= yStart ? y : yStart;
                        left = y <= yStart ? y : yStart;
                    }

                    // multiply the height of the rect with the phase
                    right *= phaseY;
                    left *= phaseY;

                    addBar(left, top, right, bottom);
                }
            } else {
                float bottom = x - barWidthHalf;
                float top = x + barWidthHalf;
                float left;
                float right;
                if (mInverted) {
                    left = y >= 0f ? y : 0f;
                    right = y <= 0f ? y : 0f;
                } else {
                    right = y >= 0f ? y : 0f;
                    left = y <= 0f ? y : 0f;
                }

                // multiply the height of the rect with the phase
                if (right > 0) {
                    right *= phaseY;
                } else {
                    left *= phaseY;
                }

                addBar(left, top, right, bottom);
            }
        }

        reset();
    }
}
