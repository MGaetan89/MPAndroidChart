package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by Philipp Jahoda on 02/06/16.
 */
public abstract class BaseEntry {

    /** the y value */
    private float y = 0f;

    /** optional spot for additional data this Entry represents */
    @Nullable
    private Object mData = null;

    /** optional icon image */
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
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;
    }

    /**
     * Returns the icon of this Entry.
     *
     * @return
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
     * Returns the data, additional information that this Entry represents, or
     * null, if no data has been specified.
     *
     * @return
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
