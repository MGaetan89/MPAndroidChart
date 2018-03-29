package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

/**
 * Transformer class that contains all matrices and is responsible for transforming values into
 * pixels on the screen and backwards.
 *
 * @author Philipp Jahoda
 */
public class Transformer {
    /**
     * Matrix to map the values to the screen pixels.
     */
    protected Matrix mMatrixValueToPx = new Matrix();

    /**
     * Matrix for handling the different offsets of the chart.
     */
    protected Matrix mMatrixOffset = new Matrix();

    protected ViewPortHandler mViewPortHandler;

    protected float[] valuePointsForGenerateTransformedValuesScatter = new float[1];

    protected float[] valuePointsForGenerateTransformedValuesBubble = new float[1];

    protected float[] valuePointsForGenerateTransformedValuesLine = new float[1];

    protected float[] valuePointsForGenerateTransformedValuesCandle = new float[1];

    protected Matrix mPixelToValueMatrixBuffer = new Matrix();

    @NonNull
    private final Matrix mMBuffer1 = new Matrix();

    @NonNull
    private final Matrix mMBuffer2 = new Matrix();

    /**
     * Buffer for performance.
     */
    float[] ptsBuffer = new float[2];

    public Transformer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    /**
     * Prepares the matrix that transforms values to pixels. Calculates the scale factors from the
     * charts size and offsets.
     *
     * @param xChartMin
     * @param deltaX
     * @param deltaY
     * @param yChartMin
     */
    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {
        float scaleX = mViewPortHandler.contentWidth() / deltaX;
        float scaleY = mViewPortHandler.contentHeight() / deltaY;

        if (Float.isInfinite(scaleX)) {
            scaleX = 0f;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0f;
        }

        // Setup all matrices
        mMatrixValueToPx.reset();
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    /**
     * Prepares the matrix that contains all offsets.
     *
     * @param inverted
     */
    public void prepareMatrixOffset(boolean inverted) {
        mMatrixOffset.reset();

        if (!inverted) {
            mMatrixOffset.postTranslate(
                    mViewPortHandler.offsetLeft(),
                    mViewPortHandler.getChartHeight() - mViewPortHandler.offsetBottom()
            );
        } else {
            mMatrixOffset.setTranslate(mViewPortHandler.offsetLeft(), -mViewPortHandler.offsetTop());
            mMatrixOffset.postScale(1f, -1f);
        }
    }

    /**
     * Transforms an List of Entry into a float array containing the x and y values transformed with
     * all matrices for the SCATTERCHART.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesScatter(
            @NonNull IScatterDataSet data, float phaseX, float phaseY, int from, int to
    ) {
        final int count = (int) ((to - from) * phaseX + 1) * 2;
        if (valuePointsForGenerateTransformedValuesScatter.length != count) {
            valuePointsForGenerateTransformedValuesScatter = new float[count];
        }

        float[] valuePoints = valuePointsForGenerateTransformedValuesScatter;
        for (int i = 0; i < count; i += 2) {
            Entry entry = data.getEntryForIndex(i / 2 + from);
            if (entry != null) {
                valuePoints[i] = entry.getX();
                valuePoints[i + 1] = entry.getY() * phaseY;
            } else {
                valuePoints[i] = 0f;
                valuePoints[i + 1] = 0f;
            }
        }

        getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }

    /**
     * Transforms an List of Entry into a float array containing the x and y values transformed with
     * all matrices for the BUBBLECHART.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesBubble(
            @NonNull IBubbleDataSet data, float phaseY, int from, int to
    ) {
        final int count = (to - from + 1) * 2;
        if (valuePointsForGenerateTransformedValuesBubble.length != count) {
            valuePointsForGenerateTransformedValuesBubble = new float[count];
        }

        float[] valuePoints = valuePointsForGenerateTransformedValuesBubble;
        for (int i = 0; i < count; i += 2) {
            Entry entry = data.getEntryForIndex(i / 2 + from);
            if (entry != null) {
                valuePoints[i] = entry.getX();
                valuePoints[i + 1] = entry.getY() * phaseY;
            } else {
                valuePoints[i] = 0f;
                valuePoints[i + 1] = 0f;
            }
        }

        getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }

    /**
     * Transforms an List of Entry into a float array containing the x and y values transformed with
     * all matrices for the LINECHART.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesLine(
            @NonNull ILineDataSet data, float phaseX, float phaseY, int min, int max
    ) {
        final int count = ((int) ((max - min) * phaseX) + 1) * 2;
        if (valuePointsForGenerateTransformedValuesLine.length != count) {
            valuePointsForGenerateTransformedValuesLine = new float[count];
        }

        float[] valuePoints = valuePointsForGenerateTransformedValuesLine;
        for (int i = 0; i < count; i += 2) {
            Entry entry = data.getEntryForIndex(i / 2 + min);
            if (entry != null) {
                valuePoints[i] = entry.getX();
                valuePoints[i + 1] = entry.getY() * phaseY;
            } else {
                valuePoints[i] = 0f;
                valuePoints[i + 1] = 0f;
            }
        }

        getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }

    /**
     * Transforms an List of Entry into a float array containing the x and y values transformed with
     * all matrices for the CANDLESTICKCHART.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesCandle(
            @NonNull ICandleDataSet data, float phaseX, float phaseY, int from, int to
    ) {
        final int count = (int) ((to - from) * phaseX + 1) * 2;
        if (valuePointsForGenerateTransformedValuesCandle.length != count) {
            valuePointsForGenerateTransformedValuesCandle = new float[count];
        }

        float[] valuePoints = valuePointsForGenerateTransformedValuesCandle;
        for (int i = 0; i < count; i += 2) {
            CandleEntry entry = data.getEntryForIndex(i / 2 + from);
            if (entry != null) {
                valuePoints[i] = entry.getX();
                valuePoints[i + 1] = entry.getHigh() * phaseY;
            } else {
                valuePoints[i] = 0f;
                valuePoints[i + 1] = 0f;
            }
        }

        getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }

    /**
     * Transform a path with all the given matrices.
     * VERY IMPORTANT: keep order to value-touch-offset
     *
     * @param path
     */
    public void pathValueToPixel(@NonNull Path path) {
        path.transform(mMatrixValueToPx);
        path.transform(mViewPortHandler.getMatrixTouch());
        path.transform(mMatrixOffset);
    }

    /**
     * Transforms multiple paths will all matrices.
     *
     * @param paths
     */
    public void pathValuesToPixel(@NonNull List<Path> paths) {
        for (int i = 0; i < paths.size(); i++) {
            pathValueToPixel(paths.get(i));
        }
    }

    /**
     * Transform an array of points with all matrices.
     * VERY IMPORTANT: Keep matrix order "value-touch-offset" when transforming.
     *
     * @param points
     */
    public void pointValuesToPixel(float[] points) {
        mMatrixValueToPx.mapPoints(points);
        mViewPortHandler.getMatrixTouch().mapPoints(points);
        mMatrixOffset.mapPoints(points);
    }

    /**
     * Transform a rectangle with all matrices.
     *
     * @param rect
     */
    public void rectValueToPixel(RectF rect) {
        mMatrixValueToPx.mapRect(rect);
        mViewPortHandler.getMatrixTouch().mapRect(rect);
        mMatrixOffset.mapRect(rect);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param rect
     * @param phaseY
     */
    public void rectToPixelPhase(@NonNull RectF rect, float phaseY) {
        // Multiply the height of the rect with the phase
        rect.top *= phaseY;
        rect.bottom *= phaseY;

        mMatrixValueToPx.mapRect(rect);
        mViewPortHandler.getMatrixTouch().mapRect(rect);
        mMatrixOffset.mapRect(rect);
    }

    public void rectToPixelPhaseHorizontal(@NonNull RectF rect, float phaseY) {
        // Multiply the height of the rect with the phase
        rect.left *= phaseY;
        rect.right *= phaseY;

        mMatrixValueToPx.mapRect(rect);
        mViewPortHandler.getMatrixTouch().mapRect(rect);
        mMatrixOffset.mapRect(rect);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param rect
     */
    public void rectValueToPixelHorizontal(RectF rect) {
        mMatrixValueToPx.mapRect(rect);
        mViewPortHandler.getMatrixTouch().mapRect(rect);
        mMatrixOffset.mapRect(rect);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param rect
     * @param phaseY
     */
    public void rectValueToPixelHorizontal(@NonNull RectF rect, float phaseY) {
        // Multiply the height of the rect with the phase
        rect.left *= phaseY;
        rect.right *= phaseY;

        mMatrixValueToPx.mapRect(rect);
        mViewPortHandler.getMatrixTouch().mapRect(rect);
        mMatrixOffset.mapRect(rect);
    }

    /**
     * Transforms multiple rects with all matrices.
     *
     * @param rects
     */
    public void rectValuesToPixel(@NonNull List<RectF> rects) {
        Matrix matrix = getValueToPixelMatrix();
        for (int i = 0; i < rects.size(); i++) {
            matrix.mapRect(rects.get(i));
        }
    }

    /**
     * Transforms the given array of touch positions (pixels) (x, y, x, y, ...) into values on the
     * chart.
     *
     * @param pixels
     */
    public void pixelsToValue(float[] pixels) {
        Matrix matrix = mPixelToValueMatrixBuffer;
        matrix.reset();

        // Invert all matrices to convert back to the original value
        mMatrixOffset.invert(matrix);
        matrix.mapPoints(pixels);

        mViewPortHandler.getMatrixTouch().invert(matrix);
        matrix.mapPoints(pixels);

        mMatrixValueToPx.invert(matrix);
        matrix.mapPoints(pixels);
    }

    /**
     * Returns a recyclable MPPointD instance. Returns the x and y values in the chart at the given
     * touch point (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates/values in the chart. This is the opposite method to getPixelForValues(...).
     *
     * @param x
     * @param y
     */
    @NonNull
    public MPPointD getValuesByTouchPoint(float x, float y) {
        MPPointD result = MPPointD.getInstance(0d, 0d);
        getValuesByTouchPoint(x, y, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, @NonNull MPPointD outputPoint) {
        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pixelsToValue(ptsBuffer);

        outputPoint.x = ptsBuffer[0];
        outputPoint.y = ptsBuffer[1];
    }

    /**
     * Returns a recyclable MPPointD instance. Returns the x and y coordinates (pixels) for a given
     * x and y value in the chart.
     *
     * @param x
     * @param y
     */
    @NonNull
    public MPPointD getPixelForValues(float x, float y) {
        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pointValuesToPixel(ptsBuffer);

        double xPx = ptsBuffer[0];
        double yPx = ptsBuffer[1];

        return MPPointD.getInstance(xPx, yPx);
    }

    public Matrix getValueMatrix() {
        return mMatrixValueToPx;
    }

    public Matrix getOffsetMatrix() {
        return mMatrixOffset;
    }

    @NonNull
    public Matrix getValueToPixelMatrix() {
        mMBuffer1.set(mMatrixValueToPx);
        mMBuffer1.postConcat(mViewPortHandler.mMatrixTouch);
        mMBuffer1.postConcat(mMatrixOffset);
        return mMBuffer1;
    }

    @NonNull
    public Matrix getPixelToValueMatrix() {
        getValueToPixelMatrix().invert(mMBuffer2);
        return mMBuffer2;
    }
}
