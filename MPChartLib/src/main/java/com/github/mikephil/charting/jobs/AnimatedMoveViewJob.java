package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Philipp Jahoda
 */
public class AnimatedMoveViewJob extends AnimatedViewPortJob {
    private static ObjectPool<AnimatedMoveViewJob> pool;

    static {
        pool = ObjectPool.create(4, new AnimatedMoveViewJob(null, 0f, 0f, null, null, 0f, 0f, 0L));
        pool.setReplenishPercentage(0.5f);
    }

    @NonNull
    public static AnimatedMoveViewJob getInstance(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer transformer, View view, float xOrigin, float yOrigin, long duration) {
        AnimatedMoveViewJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = xValue;
        result.yValue = yValue;
        result.mTrans = transformer;
        result.view = view;
        result.xOrigin = xOrigin;
        result.yOrigin = yOrigin;
        result.animator.setDuration(duration);
        return result;
    }

    public static void recycleInstance(AnimatedMoveViewJob instance) {
        pool.recycle(instance);
    }

    public AnimatedMoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer transformer, View view, float xOrigin, float yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, transformer, view, xOrigin, yOrigin, duration);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        pts[0] = xOrigin + (xValue - xOrigin) * phase;
        pts[1] = yOrigin + (yValue - yOrigin) * phase;

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);
    }

    public void recycleSelf() {
        recycleInstance(this);
    }

    @NonNull
    @Override
    protected ObjectPool.Poolable instantiate() {
        return new AnimatedMoveViewJob(null, 0f, 0f, null, null, 0f, 0f, 0L);
    }
}
