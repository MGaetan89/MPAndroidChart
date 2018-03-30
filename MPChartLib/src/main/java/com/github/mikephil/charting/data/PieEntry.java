package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public class PieEntry extends Entry {
    @Nullable
    private String label;

    public PieEntry(float y) {
        super(0f, y);
    }

    public PieEntry(float y, @Nullable Object data) {
        super(0f, y, data);
    }

    public PieEntry(float y, @Nullable Drawable icon) {
        super(0f, y, icon);
    }

    public PieEntry(float y, @Nullable Drawable icon, @Nullable Object data) {
        super(0f, y, icon, data);
    }

    public PieEntry(float y, @Nullable String label) {
        super(0f, y);
        this.label = label;
    }

    public PieEntry(float y, @Nullable String label, @Nullable Object data) {
        super(0f, y, data);
        this.label = label;
    }

    public PieEntry(float y, @Nullable String label, @Nullable Drawable icon) {
        super(0f, y, icon);
        this.label = label;
    }

    public PieEntry(float y, @Nullable String label, @Nullable Drawable icon, @Nullable Object data) {
        super(0f, y, icon, data);
        this.label = label;
    }

    /**
     * This is the same as getY(). Returns the value of the PieEntry.
     *
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0. Use <code>getY()</code> instead.
     */
    @Deprecated
    public float getValue() {
        return getY();
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }

    @Deprecated
    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Deprecated
    @Override
    public float getX() {
        return super.getX();
    }

    @NonNull
    public PieEntry copy() {
        return new PieEntry(getY(), label, getData());
    }
}
