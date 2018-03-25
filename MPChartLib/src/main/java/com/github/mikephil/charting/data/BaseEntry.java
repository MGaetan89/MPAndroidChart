package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public abstract class BaseEntry {
    /**
     * The y value.
     */
    private float y = 0f;

    /**
     * Optional spot for additional data this Entry represents.
     */
    @Nullable
    private Object mData = null;

    /**
     * Optional icon image.
     */
    @Nullable
    private Drawable mIcon = null;

    public BaseEntry() {
    }

    public BaseEntry(float y) {
        this.y = y;
    }

    public BaseEntry(float y, @Nullable Object data) {
        this(y);
        this.mData = data;
    }

    public BaseEntry(float y, @Nullable Drawable icon) {
        this(y);
        this.mIcon = icon;
    }

    public BaseEntry(float y, @Nullable Drawable icon, @Nullable Object data) {
        this(y);
        this.mIcon = icon;
        this.mData = data;
    }

    /**
     * Returns the y value of this Entry.
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the icon drawable.
     *
     * @param icon
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;
    }

    /**
     * Returns the icon of this Entry.
     */
    @Nullable
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns the data, additional information that this Entry represents, or null, if no data has
     * been specified.
     */
    @Nullable
    public Object getData() {
        return mData;
    }

    /**
     * Sets additional data this Entry should represent.
     *
     * @param data
     */
    public void setData(@Nullable Object data) {
        this.mData = data;
    }
}
