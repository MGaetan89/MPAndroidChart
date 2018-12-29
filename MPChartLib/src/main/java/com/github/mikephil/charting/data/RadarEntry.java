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
