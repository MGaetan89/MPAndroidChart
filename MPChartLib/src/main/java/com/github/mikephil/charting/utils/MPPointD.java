package com.github.mikephil.charting.utils;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Point encapsulating two double values.
 *
 * @author Philipp Jahoda
 */
public class MPPointD extends ObjectPool.Poolable {
    private static ObjectPool<MPPointD> pool;

    static {
        pool = ObjectPool.create(64, new MPPointD(0d, 0d));
        pool.setReplenishPercentage(0.5f);
    }

    public double x;
    public double y;

    private MPPointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    public static MPPointD getInstance(double x, double y) {
        MPPointD result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(MPPointD instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<MPPointD> instances) {
        pool.recycle(instances);
    }

    @NonNull
    protected ObjectPool.Poolable instantiate() {
        return new MPPointD(0d, 0d);
    }

    /**
     * Returns a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        return "MPPointD, x: " + x + ", y: " + y;
    }
}