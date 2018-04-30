package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Class that contains information about the charts current viewport settings, including offsets,
 * scale & translation levels, ...
 *
 * @author Philipp Jahoda
 */
public class ViewPortHandler {
    /**
     * Matrix used for touch events.
     */
    @NonNull
    protected final Matrix mMatrixTouch = new Matrix();

    /**
     * This rectangle defines the area in which graph values can be drawn.
     */
    protected RectF mContentRect = new RectF();

    protected float mChartWidth = 0f;
    protected float mChartHeight = 0f;

    protected float[] valsBufferForFitScreen = new float[9];

    protected Matrix mCenterViewPortMatrixBuffer = new Matrix();

    /**
     * Buffer for storing the 9 matrix values of a 3x3 matrix.
     */
    protected final float[] matrixBuffer = new float[9];

    /**
     * Minimum scale value on the y-axis.
     */
    private float mMinScaleY = 1f;

    /**
     * Maximum scale value on the y-axis.
     */
    private float mMaxScaleY = Float.MAX_VALUE;

    /**
     * Minimum scale value on the x-axis.
     */
    private float mMinScaleX = 1f;

    /**
     * Maximum scale value on the x-axis.
     */
    private float mMaxScaleX = Float.MAX_VALUE;

    /**
     * Contains the current scale factor of the x-axis.
     */
    private float mScaleX = 1f;

    /**
     * Contains the current scale factor of the y-axis.
     */
    private float mScaleY = 1f;

    /**
     * Current translation (drag distance) on the x-axis.
     */
    private float mTransX = 0f;

    /**
     * Current translation (drag distance) on the y-axis.
     */
    private float mTransY = 0f;

    /**
     * Offset that allows the chart to be dragged over its bounds on the x-axis.
     */
    private float mTransOffsetX = 0f;

    /**
     * Offset that allows the chart to be dragged over its bounds on the x-axis.
     */
    private float mTransOffsetY = 0f;

    /**
     * Constructor - don't forget calling setChartDimens(...)
     */
    public ViewPortHandler() {
    }

    /**
     * Sets the width and height of the chart.
     *
     * @param width
     * @param height
     */
    public void setChartDimens(float width, float height) {
        float offsetLeft = this.offsetLeft();
        float offsetTop = this.offsetTop();
        float offsetRight = this.offsetRight();
        float offsetBottom = this.offsetBottom();

        mChartHeight = height;
        mChartWidth = width;

        restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public boolean hasChartDimens() {
        return mChartHeight > 0f && mChartWidth > 0f;
    }

    public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight, float offsetBottom) {
        mContentRect.set(offsetLeft, offsetTop, mChartWidth - offsetRight, mChartHeight - offsetBottom);
    }

    public float offsetLeft() {
        return mContentRect.left;
    }

    public float offsetRight() {
        return mChartWidth - mContentRect.right;
    }

    public float offsetTop() {
        return mContentRect.top;
    }

    public float offsetBottom() {
        return mChartHeight - mContentRect.bottom;
    }

    public float contentTop() {
        return mContentRect.top;
    }

    public float contentLeft() {
        return mContentRect.left;
    }

    public float contentRight() {
        return mContentRect.right;
    }

    public float contentBottom() {
        return mContentRect.bottom;
    }

    public float contentWidth() {
        return mContentRect.width();
    }

    public float contentHeight() {
        return mContentRect.height();
    }

    @NonNull
    public RectF getContentRect() {
        return mContentRect;
    }

    @NonNull
    public MPPointF getContentCenter() {
        return MPPointF.getInstance(mContentRect.centerX(), mContentRect.centerY());
    }

    public float getChartHeight() {
        return mChartHeight;
    }

    public float getChartWidth() {
        return mChartWidth;
    }

    /**
     * Returns the smallest extension of the content rect (width or height).
     */
    public float getSmallestContentExtension() {
        return Math.min(mContentRect.width(), mContentRect.height());
    }

    /**
     * Zooms in by 1.4f, x and y are the coordinates (in pixels) of the zoom center.
     *
     * @param x
     * @param y
     */
    @NonNull
    public Matrix zoomIn(float x, float y) {
        Matrix save = new Matrix();
        zoomIn(x, y, save);
        return save;
    }

    public void zoomIn(float x, float y, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1.4f, 1.4f, x, y);
    }

    /**
     * Zooms out by 0.7f, x and y are the coordinates (in pixels) of the zoom center.
     */
    @NonNull
    public Matrix zoomOut(float x, float y) {
        Matrix save = new Matrix();
        zoomOut(x, y, save);
        return save;
    }

    public void zoomOut(float x, float y, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(0.7f, 0.7f, x, y);
    }

    /**
     * Zooms out to original size.
     *
     * @param outputMatrix
     */
    public void resetZoom(@NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1.0f, 1.0f, 0.0f, 0.0f);
    }

    /**
     * Post-scales by the specified scale factors.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    @NonNull
    public Matrix zoom(float scaleX, float scaleY) {
        Matrix save = new Matrix();
        zoom(scaleX, scaleY, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY);
    }

    /**
     * Post-scales by the specified scale factors. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    @NonNull
    public Matrix zoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = new Matrix();
        zoom(scaleX, scaleY, x, y, save);
        return save;
    }

    public void zoom(float scaleX, float scaleY, float x, float y, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY, x, y);
    }

    /**
     * Sets the scale factor to the specified values.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    @NonNull
    public Matrix setZoom(float scaleX, float scaleY) {
        Matrix save = new Matrix();
        setZoom(scaleX, scaleY, save);
        return save;
    }

    public void setZoom(float scaleX, float scaleY, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.setScale(scaleX, scaleY);
    }

    /**
     * Sets the scale factor to the specified values. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    @NonNull
    public Matrix setZoom(float scaleX, float scaleY, float x, float y) {
        Matrix save = new Matrix();
        save.set(mMatrixTouch);
        save.setScale(scaleX, scaleY, x, y);
        return save;
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's bounds.
     */
    @NonNull
    public Matrix fitScreen() {
        Matrix save = new Matrix();
        fitScreen(save);
        return save;
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's bounds. Output Matrix is
     * available for those who wish to cache the object.
     */
    public void fitScreen(@NonNull Matrix outputMatrix) {
        mMinScaleX = 1f;
        mMinScaleY = 1f;

        outputMatrix.set(mMatrixTouch);

        float[] values = valsBufferForFitScreen;
        for (int i = 0; i < 9; i++) {
            values[i] = 0;
        }

        outputMatrix.getValues(values);

        // Reset all translations and scaling
        values[Matrix.MTRANS_X] = 0f;
        values[Matrix.MTRANS_Y] = 0f;
        values[Matrix.MSCALE_X] = 1f;
        values[Matrix.MSCALE_Y] = 1f;

        outputMatrix.setValues(values);
    }

    /**
     * Post-translates to the specified points. Less performant.
     *
     * @param transformedPts
     * @return
     */
    @NonNull
    public Matrix translate(final float[] transformedPts) {
        Matrix save = new Matrix();
        translate(transformedPts, save);
        return save;
    }

    /**
     * Post-translates to the specified points. Output matrix allows for caching objects.
     *
     * @param transformedPts
     * @return
     */
    public void translate(final float[] transformedPts, @NonNull Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        final float x = transformedPts[0] - offsetLeft();
        final float y = transformedPts[1] - offsetTop();
        outputMatrix.postTranslate(-x, -y);
    }

    /**
     * Centers the viewport around the specified position (x-index and y-value) in the chart.
     * Centering the viewport outside the bounds of the chart is not possible. Makes most sense in
     * combination with the setScaleMinima(...) method.
     *
     * @param transformedPts the position to center view viewport to
     * @param view
     * @return save
     */
    public void centerViewPort(final float[] transformedPts, final View view) {
        Matrix save = mCenterViewPortMatrixBuffer;
        save.reset();
        save.set(mMatrixTouch);
        final float x = transformedPts[0] - offsetLeft();
        final float y = transformedPts[1] - offsetTop();
        save.postTranslate(-x, -y);
        refresh(save, view, true);
    }

    /**
     * Call this method to refresh the graph with a given matrix.
     *
     * @param newMatrix
     * @return
     */
    @NonNull
    public Matrix refresh(@NonNull Matrix newMatrix, @NonNull View chart, boolean invalidate) {
        mMatrixTouch.set(newMatrix);

        // Make sure scale and translation are within their bounds
        limitTransAndScale(mMatrixTouch, mContentRect);

        if (invalidate) {
            chart.invalidate();
        }

        newMatrix.set(mMatrixTouch);
        return newMatrix;
    }

    /**
     * Limits the maximum scale and X translation of the given matrix.
     *
     * @param matrix
     */
    public void limitTransAndScale(@NonNull Matrix matrix, @NonNull RectF content) {
        matrix.getValues(matrixBuffer);

        float curTransX = matrixBuffer[Matrix.MTRANS_X];
        float curScaleX = matrixBuffer[Matrix.MSCALE_X];
        float curTransY = matrixBuffer[Matrix.MTRANS_Y];
        float curScaleY = matrixBuffer[Matrix.MSCALE_Y];

        // Min scale-x is 1f
        mScaleX = Math.min(Math.max(mMinScaleX, curScaleX), mMaxScaleX);

        // Min scale-y is 1f
        mScaleY = Math.min(Math.max(mMinScaleY, curScaleY), mMaxScaleY);

        float width = content.width();
        float height = content.height();

        float maxTransX = -width * (mScaleX - 1f);
        mTransX = Math.min(Math.max(curTransX, maxTransX - mTransOffsetX), mTransOffsetX);

        float maxTransY = height * (mScaleY - 1f);
        mTransY = Math.max(Math.min(curTransY, maxTransY + mTransOffsetY), -mTransOffsetY);

        matrixBuffer[Matrix.MTRANS_X] = mTransX;
        matrixBuffer[Matrix.MSCALE_X] = mScaleX;

        matrixBuffer[Matrix.MTRANS_Y] = mTransY;
        matrixBuffer[Matrix.MSCALE_Y] = mScaleY;

        matrix.setValues(matrixBuffer);
    }

    /**
     * Sets the minimum scale factor for the x-axis.
     *
     * @param xScale
     */
    public void setMinimumScaleX(@FloatRange(from = 1f) float xScale) {
        if (xScale < 1f) {
            mMinScaleX = 1f;
        } else {
            mMinScaleX = xScale;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the x-axis.
     *
     * @param xScale
     */
    public void setMaximumScaleX(@FloatRange(from = 0f, fromInclusive = false) float xScale) {
        if (xScale <= 0f) {
            mMaxScaleX = Float.MAX_VALUE;
        } else {
            mMaxScaleX = xScale;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum and maximum scale factors for the x-axis.
     *
     * @param minScaleX
     * @param maxScaleX
     */
    public void setMinMaxScaleX(@FloatRange(from = 1f) float minScaleX, @FloatRange(from = 0f, fromInclusive = false) float maxScaleX) {
        if (minScaleX < 1f) {
            mMinScaleX = 1f;
        } else {
            mMinScaleX = minScaleX;
        }

        if (maxScaleX <= 0f) {
            mMaxScaleX = Float.MAX_VALUE;
        } else {
            mMaxScaleX = maxScaleX;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum scale factor for the y-axis.
     *
     * @param yScale
     */
    public void setMinimumScaleY(@FloatRange(from = 1f) float yScale) {
        if (yScale < 1f) {
            mMinScaleY = 1f;
        } else {
            mMinScaleY = yScale;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the y-axis.
     *
     * @param yScale
     */
    public void setMaximumScaleY(@FloatRange(from = 0f, fromInclusive = false) float yScale) {
        if (yScale <= 0f) {
            mMaxScaleY = Float.MAX_VALUE;
        } else {
            mMaxScaleY = yScale;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    public void setMinMaxScaleY(@FloatRange(from = 1f) float minScaleY, @FloatRange(from = 0f, fromInclusive = false) float maxScaleY) {
        if (minScaleY < 1f) {
            mMinScaleY = 1f;
        } else {
            mMinScaleY = minScaleY;
        }

        if (maxScaleY <= 0f) {
            mMaxScaleY = Float.MAX_VALUE;
        } else {
            mMaxScaleY = maxScaleY;
        }

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Returns the charts-touch matrix used for translation and scale on touch.
     */
    public Matrix getMatrixTouch() {
        return mMatrixTouch;
    }

    public boolean isInBoundsX(float x) {
        return isInBoundsLeft(x) && isInBoundsRight(x);
    }

    public boolean isInBoundsY(float y) {
        return isInBoundsTop(y) && isInBoundsBottom(y);
    }

    public boolean isInBounds(float x, float y) {
        return isInBoundsX(x) && isInBoundsY(y);
    }

    public boolean isInBoundsLeft(float x) {
        return mContentRect.left <= x + 1f;
    }

    public boolean isInBoundsRight(float x) {
        x = ((int) (x * 100f)) / 100f;
        return mContentRect.right >= x - 1f;
    }

    public boolean isInBoundsTop(float y) {
        return mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(float y) {
        y = ((int) (y * 100f)) / 100f;
        return mContentRect.bottom >= y;
    }

    /**
     * Returns the current x-scale factor.
     */
    public float getScaleX() {
        return mScaleX;
    }

    /**
     * Returns the current y-scale factor.
     */
    public float getScaleY() {
        return mScaleY;
    }

    public float getMinScaleX() {
        return mMinScaleX;
    }

    public float getMaxScaleX() {
        return mMaxScaleX;
    }

    public float getMinScaleY() {
        return mMinScaleY;
    }

    public float getMaxScaleY() {
        return mMaxScaleY;
    }

    /**
     * Returns the translation (drag / pan) distance on the x-axis.
     */
    public float getTransX() {
        return mTransX;
    }

    /**
     * Returns the translation (drag / pan) distance on the y-axis.
     */
    public float getTransY() {
        return mTransY;
    }

    /**
     * If the chart is fully zoomed out, return true.
     */
    public boolean isFullyZoomedOut() {
        return isFullyZoomedOutX() && isFullyZoomedOutY();
    }

    /**
     * Returns true if the chart is fully zoomed out on it's y-axis (vertical).
     */
    public boolean isFullyZoomedOutY() {
        return !(mScaleY > mMinScaleY || mMinScaleY > 1f);
    }

    /**
     * Returns true if the chart is fully zoomed out on it's x-axis (horizontal).
     */
    public boolean isFullyZoomedOutX() {
        return !(mScaleX > mMinScaleX || mMinScaleX > 1f);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's bounds on the x-axis.
     *
     * @param offset
     */
    public void setDragOffsetX(float offset) {
        mTransOffsetX = Utils.convertDpToPixel(offset);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's bounds on the y-axis.
     *
     * @param offset
     */
    public void setDragOffsetY(float offset) {
        mTransOffsetY = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     */
    public boolean hasNoDragOffset() {
        return mTransOffsetX <= 0 && mTransOffsetY <= 0;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the x-axis.
     */
    public boolean canZoomOutMoreX() {
        return mScaleX > mMinScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the x-axis.
     */
    public boolean canZoomInMoreX() {
        return mScaleX < mMaxScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the y-axis.
     */
    public boolean canZoomOutMoreY() {
        return mScaleY > mMinScaleY;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the y-axis.
     */
    public boolean canZoomInMoreY() {
        return mScaleY < mMaxScaleY;
    }
}
