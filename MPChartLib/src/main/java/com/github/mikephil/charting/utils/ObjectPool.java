package com.github.mikephil.charting.utils;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * An object pool for recycling of object instances extending Poolable.
 * <p>
 * Costs/Benefits:
 * Benefit - The pool can very quickly determine if an object is eligible for storage without iteration.
 * Benefit - The pool can also know if an instance of Poolable is already stored in a different pool instance.
 * Benefit - The pool can grow as needed, if it is empty.
 * Cost - The pool can only contain objects extending Poolable.
 * Cost - However, refilling the pool when it is empty might incur a time cost with sufficiently large capacity. Set the replenishPercentage to a lower number if this is a concern.
 * <p>
 * Created by Tony Patino on 6/20/16.
 */
public class ObjectPool<T extends ObjectPool.Poolable> {
    private static int ids = 0;

    private int poolId;
    private int desiredCapacity;
    private Object[] objects;
    private int objectsPointer;
    private T modelObject;
    private float replenishPercentage;

    /**
     * Returns the id of the given pool instance.
     *
     * @return an integer ID belonging to this pool instance.
     */
    public int getPoolId() {
        return poolId;
    }

    /**
     * Returns an ObjectPool instance, of a given starting capacity, that recycles instances of a
     * given Poolable object.
     *
     * @param withCapacity a positive integer value
     * @param object       an instance of the object that the pool should recycle
     */
    @NonNull
    public static synchronized <T extends ObjectPool.Poolable> ObjectPool<T> create(int withCapacity, T object) {
        ObjectPool<T> result = new ObjectPool<>(withCapacity, object);
        result.poolId = ids++;

        return result;
    }

    private ObjectPool(int withCapacity, T object) {
        if (withCapacity <= 0) {
            throw new IllegalArgumentException("ObjectPool's capacity must be greater than 0, got '" + withCapacity + "'.");
        }

        this.desiredCapacity = withCapacity;
        this.objects = new Object[this.desiredCapacity];
        this.objectsPointer = 0;
        this.modelObject = object;

        this.setReplenishPercentage(1f);
        this.refillPool();
    }

    /**
     * Set the percentage of the pool to replenish on empty, between 0 and 1 included.
     *
     * @param percentage a value between 0 and 1, representing the percentage of the pool to replenish
     */
    public void setReplenishPercentage(@FloatRange(from = 0f, to = 1f) float percentage) {
        this.replenishPercentage = Math.max(0f, Math.min(percentage, 1f));
    }

    public float getReplenishPercentage() {
        return replenishPercentage;
    }

    private void refillPool() {
        this.refillPool(this.replenishPercentage);
    }

    private void refillPool(float percentage) {
        int portionOfCapacity = (int) (desiredCapacity * percentage);
        portionOfCapacity = Math.max(1, Math.min(portionOfCapacity, desiredCapacity));

        for (int i = 0; i < portionOfCapacity; i++) {
            this.objects[i] = modelObject.instantiate();
        }

        objectsPointer = portionOfCapacity - 1;
    }

    /**
     * Returns an instance of Poolable. If get() is called with an empty pool, the pool will be
     * replenished. If the pool capacity is sufficiently large, this could come at a performance
     * cost.
     *
     * @return An instance of Poolable object T
     */
    @NonNull
    public synchronized T get() {
        if (this.objectsPointer == -1) {
            this.refillPool();
        }

        T result = (T) objects[this.objectsPointer];
        result.currentOwnerId = Poolable.NO_OWNER;
        this.objectsPointer--;

        return result;
    }

    /**
     * Recycle an instance of Poolable that this pool is capable of generating. The T instance
     * passed must not already exist inside this or any other ObjectPool instance.
     *
     * @param object An object of type T to recycle
     */
    public synchronized void recycle(@NonNull T object) {
        checkObjectOwnership(object);

        this.objectsPointer++;
        if (this.objectsPointer >= objects.length) {
            this.resizePool();
        }

        object.currentOwnerId = this.poolId;
        objects[this.objectsPointer] = object;
    }

    /**
     * Recycle a List of Poolables that this pool is capable of generating. The T instances passed
     * must not already exist inside this or any other ObjectPool instance.
     *
     * @param objects A list of objects of type T to recycle
     */
    public synchronized void recycle(@NonNull List<T> objects) {
        while (objects.size() + this.objectsPointer + 1 > this.desiredCapacity) {
            this.resizePool();
        }

        final int objectsListSize = objects.size();

        // Not relying on recycle(T object) because this is more efficient.
        for (int i = 0; i < objectsListSize; i++) {
            T object = objects.get(i);
            checkObjectOwnership(object);

            object.currentOwnerId = this.poolId;
            this.objects[this.objectsPointer + 1 + i] = object;
        }

        this.objectsPointer += objectsListSize;
    }

    private void checkObjectOwnership(@NonNull T object) {
        if (object.currentOwnerId != Poolable.NO_OWNER) {
            if (object.currentOwnerId == this.poolId) {
                throw new IllegalArgumentException("The object passed is already stored in this pool.");
            } else {
                throw new IllegalArgumentException("The object to recycle already belongs to poolId '" + object.currentOwnerId + "'. Object cannot belong to different pools instances simultaneously.");
            }
        }
    }

    private void resizePool() {
        final int oldCapacity = this.desiredCapacity;
        this.desiredCapacity *= 2;
        Object[] temp = new Object[this.desiredCapacity];
        System.arraycopy(this.objects, 0, temp, 0, oldCapacity);
        this.objects = temp;
    }

    /**
     * Returns the capacity of this object pool.
     * Note: The pool will automatically resize to contain additional objects if the user tries to
     * add more objects than the pool's capacity allows, but this comes at a performance cost.
     *
     * @return The capacity of the pool.
     */
    public int getPoolCapacity() {
        return this.objects.length;
    }

    /**
     * Returns the number of objects remaining in the pool.
     *
     * @return The number of objects remaining in the pool.
     */
    public int getPoolCount() {
        return this.objectsPointer + 1;
    }

    public abstract static class Poolable {
        public static final int NO_OWNER = -1;
        int currentOwnerId = NO_OWNER;

        protected abstract Poolable instantiate();
    }
}
