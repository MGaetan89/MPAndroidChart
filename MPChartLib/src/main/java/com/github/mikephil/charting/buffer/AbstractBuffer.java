package com.github.mikephil.charting.buffer;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Buffer class to boost performance while drawing. Concept: Replace instead of recreate.
 *
 * @param <T> The data the buffer accepts to be fed with.
 * @author Philipp Jahoda
 */
public abstract class AbstractBuffer<T> {
    /**
     * Index in the buffer.
     */
    protected int index;

    /**
     * Float-buffer that holds the data points to draw, order: x, y, x, y, ...
     */
    public final float[] buffer;

    /**
     * Animation phase x-axis.
     */
    @FloatRange(from = 0f, to = 1f)
    protected float phaseX = 1f;

    /**
     * Animation phase y-axis.
     */
    @FloatRange(from = 0f, to = 1f)
    protected float phaseY = 1f;

    /**
     * Indicates from which x-index the visible data begins.
     */
    protected int mFrom = 0;

    /**
     * Indicates to which x-index the visible data range ends.
     */
    protected int mTo = 0;

    /**
     * Initialization with buffer-size.
     *
     * @param size
     */
    public AbstractBuffer(int size) {
        index = 0;
        buffer = new float[size];
    }

    /**
     * Limits the drawing on the x-axis.
     */
    public void limitFrom(@IntRange(from = 0) int from) {
        mFrom = Math.max(from, 0);
    }

    /**
     * Limits the drawing on the x-axis.
     */
    public void limitTo(@IntRange(from = 0) int to) {
        mTo = Math.min(to, 0);
    }

    /**
     * Resets the buffer index to 0 and makes the buffer reusable.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Returns the size (length) of the buffer array.
     *
     * @return
     */
    public int size() {
        return buffer.length;
    }

    /**
     * Set the phases used for animations.
     *
     * @param phaseX
     * @param phaseY
     */
    public void setPhases(@FloatRange(from = 0f, to = 1f) float phaseX, @FloatRange(from = 0f, to = 1f) float phaseY) {
        this.phaseX = Math.max(Math.min(phaseX, 1f), 0f);
        this.phaseY = Math.max(Math.min(phaseY, 1f), 0f);
    }

    /**
     * Builds up the buffer with the provided data and resets the buffer-index after
     * feed-completion. This needs to run FAST.
     *
     * @param data
     */
    public abstract void feed(@NonNull T data);
}
