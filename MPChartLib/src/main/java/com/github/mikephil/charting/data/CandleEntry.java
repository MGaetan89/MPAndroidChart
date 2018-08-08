package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Subclass of Entry that holds all values for one entry in a CandleStickChart.
 *
 * @author Philipp Jahoda
 */
public class CandleEntry extends Entry {
    /**
     * Shadow-high value.
     */
    private float mShadowHigh;

    /**
     * Shadow-low value.
     */
    private float mShadowLow;

    /**
     * Close value.
     */
    private float mClose;

    /**
     * Open value.
     */
    private float mOpen;

    /**
     * Constructor.
     *
     * @param x       The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open    The open value
     * @param close   The close value
     */
    public CandleEntry(float x, float shadowH, float shadowL, float open, float close) {
        this(x, shadowH, shadowL, open, close, null, null);
    }

    /**
     * Constructor.
     *
     * @param x       The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param data    Spot for additional data this Entry represents
     */
    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, @Nullable Object data) {
        this(x, shadowH, shadowL, open, close, null, data);
    }

    /**
     * Constructor.
     *
     * @param x       The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon    Icon image
     */
    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, @Nullable Drawable icon) {
        this(x, shadowH, shadowL, open, close, icon, null);
    }

    /**
     * Constructor.
     *
     * @param x       The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon    Icon image
     * @param data    Spot for additional data this Entry represents
     */
    public CandleEntry(float x, float shadowH, float shadowL, float open, float close, @Nullable Drawable icon, @Nullable Object data) {
        super(x, (shadowH + shadowL) / 2f, icon, data);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    /**
     * Returns the overall range (difference) between shadow-high and shadow-low.
     */
    public float getShadowRange() {
        return Math.abs(mShadowHigh - mShadowLow);
    }

    /**
     * Returns the body size (difference between open and close).
     */
    public float getBodyRange() {
        return Math.abs(mOpen - mClose);
    }

    @NonNull
    @Override
    public CandleEntry copy() {
        return new CandleEntry(getX(), mShadowHigh, mShadowLow, mOpen, mClose, getData());
    }

    /**
     * Returns the upper shadows highest value.
     */
    public float getHigh() {
        return mShadowHigh;
    }

    public void setHigh(float mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }

    /**
     * Returns the lower shadows lowest value.
     */
    public float getLow() {
        return mShadowLow;
    }

    public void setLow(float mShadowLow) {
        this.mShadowLow = mShadowLow;
    }

    /**
     * Returns the body close value.
     */
    public float getClose() {
        return mClose;
    }

    public void setClose(float mClose) {
        this.mClose = mClose;
    }

    /**
     * Returns the body open value.
     */
    public float getOpen() {
        return mOpen;
    }

    public void setOpen(float mOpen) {
        this.mOpen = mOpen;
    }
}
