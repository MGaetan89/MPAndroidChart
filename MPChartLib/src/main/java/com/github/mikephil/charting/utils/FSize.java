package com.github.mikephil.charting.utils;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Class for describing width and height dimensions in some arbitrary unit.
 * Replacement for the {@link android.util.SizeF}, which is only available on API >= 21.
 */
public final class FSize extends ObjectPool.Poolable {
    // TODO : Encapsulate width & height
    public float width;
    public float height;

    private static ObjectPool<FSize> pool;

    static {
        pool = ObjectPool.create(256, new FSize(0f, 0f));
        pool.setReplenishPercentage(0.5f);
    }

    public FSize() {
    }

    public FSize(final float width, final float height) {
        this.width = width;
        this.height = height;
    }

    @NonNull
    public static FSize getInstance(final float width, final float height) {
        FSize result = pool.get();
        result.width = width;
        result.height = height;
        return result;
    }

    public static void recycleInstance(FSize instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<FSize> instances) {
        pool.recycle(instances);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof FSize) {
            final FSize other = (FSize) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(width) ^ Float.floatToIntBits(height);
    }

    @NonNull
    protected ObjectPool.Poolable instantiate() {
        return new FSize(0, 0);
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
