package com.github.mikephil.charting.jobs;

import android.support.annotation.NonNull;
import android.view.View;

import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Philipp Jahoda
 */
public class MoveViewJob extends ViewPortJob {
    private static ObjectPool<MoveViewJob> pool;

    static {
        pool = ObjectPool.create(2, new MoveViewJob(null, 0f, 0f, null, null));
        pool.setReplenishPercentage(0.5f);
    }

    @NonNull
    public static MoveViewJob getInstance(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer transformer, View view) {
        MoveViewJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = xValue;
        result.yValue = yValue;
        result.mTrans = transformer;
        result.view = view;
        return result;
    }

    public static void recycleInstance(MoveViewJob instance) {
        pool.recycle(instance);
    }

    public MoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer transformer, View view) {
        super(viewPortHandler, xValue, yValue, transformer, view);
    }

    @Override
    public void run() {
        pts[0] = xValue;
        pts[1] = yValue;

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);

        recycleInstance(this);
    }

    @NonNull
    @Override
    protected ObjectPool.Poolable instantiate() {
        return new MoveViewJob(mViewPortHandler, xValue, yValue, mTrans, view);
    }
}
