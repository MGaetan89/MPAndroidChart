package com.github.mikephil.charting.jobs;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Philipp Jahoda
 */
public class ZoomJob extends ViewPortJob {
    private static ObjectPool<ZoomJob> pool;

    protected float scaleX;
    protected float scaleY;
    protected YAxis.AxisDependency axisDependency;
    @NonNull
    protected Matrix mRunMatrixBuffer = new Matrix();

    static {
        pool = ObjectPool.create(1, new ZoomJob(null, 0f, 0f, 0f, 0f, null, null, null));
        pool.setReplenishPercentage(0.5f);
    }

    @NonNull
    public static ZoomJob getInstance(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue, Transformer transformer, YAxis.AxisDependency axis, View view) {
        ZoomJob result = pool.get();
        result.xValue = xValue;
        result.yValue = yValue;
        result.scaleX = scaleX;
        result.scaleY = scaleY;
        result.mViewPortHandler = viewPortHandler;
        result.mTrans = transformer;
        result.axisDependency = axis;
        result.view = view;
        return result;
    }

    public ZoomJob(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue, Transformer transformer, YAxis.AxisDependency axis, View view) {
        super(viewPortHandler, xValue, yValue, transformer, view);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.axisDependency = axis;
    }

    public static void recycleInstance(ZoomJob instance) {
        pool.recycle(instance);
    }

    @Override
    public void run() {
        Matrix save = mRunMatrixBuffer;
        mViewPortHandler.zoom(scaleX, scaleY, save);
        mViewPortHandler.refresh(save, view, false);

        float yValsInView = ((BarLineChartBase) view).getAxis(axisDependency).mAxisRange / mViewPortHandler.getScaleY();
        float xValsInView = ((BarLineChartBase) view).getXAxis().mAxisRange / mViewPortHandler.getScaleX();

        pts[0] = xValue - xValsInView / 2f;
        pts[1] = yValue + yValsInView / 2f;

        mTrans.pointValuesToPixel(pts);

        mViewPortHandler.translate(pts, save);
        mViewPortHandler.refresh(save, view, false);

        ((BarLineChartBase) view).calculateOffsets();
        view.postInvalidate();

        recycleInstance(this);
    }

    @NonNull
    @Override
    protected ObjectPool.Poolable instantiate() {
        return new ZoomJob(null, 0f, 0f, 0f, 0f, null, null, null);
    }
}
