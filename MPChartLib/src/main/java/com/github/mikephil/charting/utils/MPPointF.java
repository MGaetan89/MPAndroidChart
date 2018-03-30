package com.github.mikephil.charting.utils;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Tony Patino on 6/24/16.
 */
public class MPPointF extends ObjectPool.Poolable {
    private static ObjectPool<MPPointF> pool;

    static {
        pool = ObjectPool.create(32, new MPPointF(0f, 0f));
        pool.setReplenishPercentage(0.5f);
    }

    public float x;
    public float y;

    public MPPointF() {
    }

    public MPPointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    public static MPPointF getInstance(float x, float y) {
        MPPointF result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    @NonNull
    public static MPPointF getInstance() {
        return getInstance(0f, 0f);
    }

    @NonNull
    public static MPPointF getInstance(MPPointF copy) {
        return getInstance(copy.x, copy.y);
    }

    public static void recycleInstance(MPPointF instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<MPPointF> instances) {
        pool.recycle(instances);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @NonNull
    @Override
    protected ObjectPool.Poolable instantiate() {
        return new MPPointF(0f, 0f);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MPPointF)) {
            return false;
        }

        MPPointF other = (MPPointF) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return "MPPointF, x: " + x + ", y: " + y;
    }
}
