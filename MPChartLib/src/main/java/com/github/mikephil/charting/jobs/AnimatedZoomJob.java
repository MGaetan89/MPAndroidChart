package com.github.mikephil.charting.jobs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;

/**
 * @author Philipp Jahoda
 */
public class AnimatedZoomJob extends AnimatedViewPortJob implements Animator.AnimatorListener {
    private static ObjectPool<AnimatedZoomJob> pool;

    protected float zoomOriginX;
    protected float zoomOriginY;
    protected float zoomCenterX;
    protected float zoomCenterY;
    protected YAxis yAxis;
    protected float xAxisRange;
    @NonNull
    protected Matrix mOnAnimationUpdateMatrixBuffer = new Matrix();

    static {
        pool = ObjectPool.create(8, new AnimatedZoomJob(null, null, null, null, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L));
    }

    @NonNull
    public static AnimatedZoomJob getInstance(ViewPortHandler viewPortHandler, View view, Transformer transformer, YAxis axis, float xAxisRange, float scaleX, float scaleY, float xOrigin, float yOrigin, float zoomCenterX, float zoomCenterY, float zoomOriginX, float zoomOriginY, long duration) {
        AnimatedZoomJob result = pool.get();
        result.mViewPortHandler = viewPortHandler;
        result.xValue = scaleX;
        result.yValue = scaleY;
        result.mTrans = transformer;
        result.yAxis = axis;
        result.xAxisRange = xAxisRange;
        result.view = view;
        result.xOrigin = xOrigin;
        result.yOrigin = yOrigin;
        result.xAxisRange = xAxisRange;
        result.resetAnimator();
        result.zoomCenterX = zoomCenterX;
        result.zoomCenterY = zoomCenterY;
        result.zoomOriginX = zoomOriginX;
        result.zoomOriginY = zoomOriginY;
        result.animator.setDuration(duration);
        return result;
    }

    public AnimatedZoomJob(ViewPortHandler viewPortHandler, View view, Transformer transformer, YAxis axis, float xAxisRange, float scaleX, float scaleY, float xOrigin, float yOrigin, float zoomCenterX, float zoomCenterY, float zoomOriginX, float zoomOriginY, long duration) {
        super(viewPortHandler, scaleX, scaleY, transformer, view, xOrigin, yOrigin, duration);

        this.zoomCenterX = zoomCenterX;
        this.zoomCenterY = zoomCenterY;
        this.zoomOriginX = zoomOriginX;
        this.zoomOriginY = zoomOriginY;
        this.animator.addListener(this);
        this.yAxis = axis;
        this.xAxisRange = xAxisRange;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float scaleX = xOrigin + (xValue - xOrigin) * phase;
        float scaleY = yOrigin + (yValue - yOrigin) * phase;

        Matrix save = mOnAnimationUpdateMatrixBuffer;
        mViewPortHandler.setZoom(scaleX, scaleY, save);
        mViewPortHandler.refresh(save, view, false);

        float valsInView = yAxis.mAxisRange / mViewPortHandler.getScaleY();
        float xsInView = xAxisRange / mViewPortHandler.getScaleX();

        pts[0] = zoomOriginX + ((zoomCenterX - xsInView / 2f) - zoomOriginX) * phase;
        pts[1] = zoomOriginY + ((zoomCenterY + valsInView / 2f) - zoomOriginY) * phase;

        mTrans.pointValuesToPixel(pts);

        mViewPortHandler.translate(pts, save);
        mViewPortHandler.refresh(save, view, true);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        ((BarLineChartBase) view).calculateOffsets();
        view.postInvalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        // Unused
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        // Unused
    }

    @Override
    public void recycleSelf() {
        // Unused
    }

    @Override
    public void onAnimationStart(Animator animation) {
        // Unused
    }

    @NonNull
    @Override
    protected ObjectPool.Poolable instantiate() {
        return new AnimatedZoomJob(null, null, null, null, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0L);
    }
}
