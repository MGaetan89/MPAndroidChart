package com.github.mikephil.charting.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public class RadarEntry extends Entry {
    public RadarEntry(float y) {
        super(0f, y);
    }

    public RadarEntry(float y, @Nullable Object data) {
        super(0f, y, data);
    }

    /**
     * This is the same as getY(). Returns the value of the RadarEntry.
     *
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0. Use <code>getY()</code> instead.
     */
    @Deprecated
    public float getValue() {
        return getY();
    }

    @NonNull
    @Override
    public RadarEntry copy() {
        return new RadarEntry(getY(), getData());
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
}
