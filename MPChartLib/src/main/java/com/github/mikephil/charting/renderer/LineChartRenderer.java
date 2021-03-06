package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class LineChartRenderer extends LineRadarRenderer {
    protected LineDataProvider mChart;

    /**
     * Paint for the inner circle of the value indicators.
     */
    protected Paint mCirclePaintInner;

    /**
     * Bitmap object used for drawing the paths (otherwise they are too long if rendered directly on
     * the canvas).
     */
    protected WeakReference<Bitmap> mDrawBitmap;

    /**
     * On this canvas, the paths are rendered, it is initialized with the pathBitmap.
     */
    protected Canvas mBitmapCanvas;

    /**
     * The bitmap configuration to be used.
     */
    protected Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;

    protected Path cubicPath = new Path();
    protected Path cubicFillPath = new Path();

    @NonNull
    protected Path mGenerateFilledPathBuffer = new Path();

    private float[] mLineBuffer = new float[4];

    /**
     * Cache for the circle bitmaps of all datasets.
     */
    @NonNull
    private final HashMap<IDataSet, DataSetImageCache> mImageCaches = new HashMap<>();

    /**
     * Buffer for drawing the circles.
     */
    private final float[] mCirclesBuffer = new float[2];

    public LineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);

        mChart = chart;

        mCirclePaintInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaintInner.setStyle(Paint.Style.FILL);
        mCirclePaintInner.setColor(Color.WHITE);
    }

    @Override
    public void initBuffers() {
        // Unused
    }

    @Override
    public void drawData(@NonNull Canvas canvas) {
        int width = (int) mViewPortHandler.getChartWidth();
        int height = (int) mViewPortHandler.getChartHeight();

        Bitmap drawBitmap = mDrawBitmap == null ? null : mDrawBitmap.get();

        if (drawBitmap == null
                || (drawBitmap.getWidth() != width)
                || (drawBitmap.getHeight() != height)) {

            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, mBitmapConfig);
                mDrawBitmap = new WeakReference<>(drawBitmap);
                mBitmapCanvas = new Canvas(drawBitmap);
            } else {
                return;
            }
        }

        drawBitmap.eraseColor(Color.TRANSPARENT);

        LineData lineData = mChart.getLineData();
        if (lineData != null) {
            for (ILineDataSet set : lineData.getDataSets()) {
                if (set.isVisible()) {
                    drawDataSet(canvas, set);
                }
            }
        }

        canvas.drawBitmap(drawBitmap, 0, 0, mRenderPaint);
    }

    protected void drawDataSet(Canvas canvas, @NonNull ILineDataSet dataSet) {
        if (dataSet.getEntryCount() < 1) {
            return;
        }

        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setPathEffect(dataSet.getDashPathEffect());

        switch (dataSet.getMode()) {
            default:
            case LINEAR:
            case STEPPED:
                drawLinear(canvas, dataSet);
                break;

            case CUBIC_BEZIER:
                drawCubicBezier(dataSet);
                break;

            case HORIZONTAL_BEZIER:
                drawHorizontalBezier(dataSet);
                break;
        }

        mRenderPaint.setPathEffect(null);
    }

    protected void drawHorizontalBezier(@NonNull ILineDataSet dataSet) {
        float phaseY = mAnimator.getPhaseY();

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mXBounds.set(mChart, dataSet);

        cubicPath.reset();

        if (mXBounds.range >= 1) {
            Entry prev = dataSet.getEntryForIndex(mXBounds.min);
            Entry cur = prev;

            // Let the spline start
            cubicPath.moveTo(cur.getX(), cur.getY() * phaseY);

            for (int j = mXBounds.min + 1; j <= mXBounds.range + mXBounds.min; j++) {
                prev = cur;
                cur = dataSet.getEntryForIndex(j);

                final float cpx = (prev.getX()) + (cur.getX() - prev.getX()) / 2f;

                cubicPath.cubicTo(
                        cpx, prev.getY() * phaseY,
                        cpx, cur.getY() * phaseY,
                        cur.getX(), cur.getY() * phaseY
                );
            }
        }

        // If filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);

            // Create a new path, this is bad for performance
            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds);
        }

        mRenderPaint.setColor(dataSet.getColor());
        mRenderPaint.setStyle(Paint.Style.STROKE);

        trans.pathValueToPixel(cubicPath);

        mBitmapCanvas.drawPath(cubicPath, mRenderPaint);

        mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicBezier(@NonNull ILineDataSet dataSet) {
        float phaseY = mAnimator.getPhaseY();

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mXBounds.set(mChart, dataSet);

        float intensity = dataSet.getCubicIntensity();

        cubicPath.reset();

        if (mXBounds.range >= 1) {
            float prevDx;
            float prevDy;
            float curDx;
            float curDy;

            // Take an extra point from the left, and an extra from the right. That's because we
            // need 4 points for a cubic bezier (cubic=4), otherwise we get lines moving and doing
            // weird stuff on the edges of the chart. So in the starting `prev` and `cur`, go -2, -1
            // And in the `lastIndex`, add +1

            final int firstIndex = mXBounds.min + 1;

            Entry prevPrev;
            Entry prev = dataSet.getEntryForIndex(Math.max(firstIndex - 2, 0));
            Entry cur = dataSet.getEntryForIndex(Math.max(firstIndex - 1, 0));
            Entry next = cur;
            int nextIndex = -1;
            if (cur == null) {
                return;
            }

            // Let the spline start
            cubicPath.moveTo(cur.getX(), cur.getY() * phaseY);

            for (int j = mXBounds.min + 1; j <= mXBounds.range + mXBounds.min; j++) {
                prevPrev = prev;
                prev = cur;
                cur = nextIndex == j ? next : dataSet.getEntryForIndex(j);

                nextIndex = j + 1 < dataSet.getEntryCount() ? j + 1 : j;
                next = dataSet.getEntryForIndex(nextIndex);

                prevDx = (cur.getX() - prevPrev.getX()) * intensity;
                prevDy = (cur.getY() - prevPrev.getY()) * intensity;
                curDx = (next.getX() - prev.getX()) * intensity;
                curDy = (next.getY() - prev.getY()) * intensity;

                cubicPath.cubicTo(
                        prev.getX() + prevDx, (prev.getY() + prevDy) * phaseY,
                        cur.getX() - curDx, (cur.getY() - curDy) * phaseY, cur.getX(), cur.getY() * phaseY
                );
            }
        }

        // If filled is enabled, close the path
        if (dataSet.isDrawFilledEnabled()) {
            cubicFillPath.reset();
            cubicFillPath.addPath(cubicPath);

            drawCubicFill(mBitmapCanvas, dataSet, cubicFillPath, trans, mXBounds);
        }

        mRenderPaint.setColor(dataSet.getColor());
        mRenderPaint.setStyle(Paint.Style.STROKE);

        trans.pathValueToPixel(cubicPath);

        mBitmapCanvas.drawPath(cubicPath, mRenderPaint);

        mRenderPaint.setPathEffect(null);
    }

    protected void drawCubicFill(Canvas canvas, @NonNull ILineDataSet dataSet, @NonNull Path spline, @NonNull Transformer trans, @NonNull XBounds bounds) {
        float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, mChart);

        spline.lineTo(dataSet.getEntryForIndex(bounds.min + bounds.range).getX(), fillMin);
        spline.lineTo(dataSet.getEntryForIndex(bounds.min).getX(), fillMin);
        spline.close();

        trans.pathValueToPixel(spline);

        final Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            drawFilledPath(canvas, spline, drawable);
        } else {
            drawFilledPath(canvas, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
        }
    }

    /**
     * Draws a normal line.
     *
     * @param c
     * @param dataSet
     */
    protected void drawLinear(Canvas c, @NonNull ILineDataSet dataSet) {
        int entryCount = dataSet.getEntryCount();

        final boolean isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED;
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mRenderPaint.setStyle(Paint.Style.STROKE);

        Canvas canvas;

        // If the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }

        mXBounds.set(mChart, dataSet);

        // If drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds);
        }

        // More than 1 color
        if (dataSet.getColors().size() > 1) {
            if (mLineBuffer.length <= pointsPerEntryPair * 2) {
                mLineBuffer = new float[pointsPerEntryPair * 4];
            }

            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
                Entry entry = dataSet.getEntryForIndex(j);
                if (entry == null) {
                    continue;
                }

                mLineBuffer[0] = entry.getX();
                mLineBuffer[1] = entry.getY() * phaseY;

                if (j < mXBounds.max) {
                    entry = dataSet.getEntryForIndex(j + 1);

                    if (entry == null) {
                        break;
                    }

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = entry.getX();
                        mLineBuffer[3] = mLineBuffer[1];
                        mLineBuffer[4] = mLineBuffer[2];
                        mLineBuffer[5] = mLineBuffer[3];
                        mLineBuffer[6] = entry.getX();
                        mLineBuffer[7] = entry.getY() * phaseY;
                    } else {
                        mLineBuffer[2] = entry.getX();
                        mLineBuffer[3] = entry.getY() * phaseY;
                    }
                } else {
                    mLineBuffer[2] = mLineBuffer[0];
                    mLineBuffer[3] = mLineBuffer[1];
                }

                trans.pointValuesToPixel(mLineBuffer);

                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0])) {
                    break;
                }

                // Make sure the lines don't do shitty things outside bounds
                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2]) || (!mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler.isInBoundsBottom(mLineBuffer[3]))) {
                    continue;
                }

                // Get the color that is set for this line-segment
                mRenderPaint.setColor(dataSet.getColor(j));

                canvas.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint);
            }
        } else { // Only one color per dataset
            if (mLineBuffer.length < Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2) {
                mLineBuffer = new float[Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 4];
            }

            Entry entry1 = dataSet.getEntryForIndex(mXBounds.min);
            if (entry1 != null) {
                Entry entry2;
                int j = 0;
                for (int x = mXBounds.min; x <= mXBounds.range + mXBounds.min; x++) {
                    entry1 = dataSet.getEntryForIndex(x == 0 ? 0 : (x - 1));
                    entry2 = dataSet.getEntryForIndex(x);

                    if (entry1 == null || entry2 == null) {
                        continue;
                    }

                    mLineBuffer[j++] = entry1.getX();
                    mLineBuffer[j++] = entry1.getY() * phaseY;

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = entry2.getX();
                        mLineBuffer[j++] = entry1.getY() * phaseY;
                        mLineBuffer[j++] = entry2.getX();
                        mLineBuffer[j++] = entry1.getY() * phaseY;
                    }

                    mLineBuffer[j++] = entry2.getX();
                    mLineBuffer[j++] = entry2.getY() * phaseY;
                }

                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer);

                    final int size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;

                    mRenderPaint.setColor(dataSet.getColor());

                    canvas.drawLines(mLineBuffer, 0, size, mRenderPaint);
                }
            }
        }

        mRenderPaint.setPathEffect(null);
    }

    /**
     * Draws a filled linear path on the canvas.
     *
     * @param canvas
     * @param dataSet
     * @param trans
     * @param bounds
     */
    protected void drawLinearFill(Canvas canvas, @NonNull ILineDataSet dataSet, @NonNull Transformer trans, @NonNull XBounds bounds) {
        final Path filled = mGenerateFilledPathBuffer;

        final int startingIndex = bounds.min;
        final int endingIndex = bounds.range + bounds.min;
        final int indexInterval = 128;

        int currentStartIndex;
        int currentEndIndex;
        int iterations = 0;

        // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
        do {
            currentStartIndex = startingIndex + (iterations * indexInterval);
            currentEndIndex = currentStartIndex + indexInterval;
            currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;

            if (currentStartIndex <= currentEndIndex) {
                generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);

                trans.pathValueToPixel(filled);

                final Drawable drawable = dataSet.getFillDrawable();
                if (drawable != null) {
                    drawFilledPath(canvas, filled, drawable);
                } else {
                    drawFilledPath(canvas, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                }
            }

            iterations++;

        } while (currentStartIndex <= currentEndIndex);
    }

    /**
     * Generates a path that is used for filled drawing.
     *
     * @param dataSet    The dataset from which to read the entries.
     * @param startIndex The index from which to start reading the dataset
     * @param endIndex   The index from which to stop reading the dataset
     * @param outputPath The path object that will be assigned the chart data.
     * @return
     */
    private void generateFilledPath(@NonNull final ILineDataSet dataSet, final int startIndex, final int endIndex, @NonNull final Path outputPath) {
        final float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, mChart);
        final float phaseY = mAnimator.getPhaseY();
        final boolean isDrawSteppedEnabled = dataSet.getMode() == LineDataSet.Mode.STEPPED;

        outputPath.reset();

        final Entry entry = dataSet.getEntryForIndex(startIndex);

        outputPath.moveTo(entry.getX(), fillMin);
        outputPath.lineTo(entry.getX(), entry.getY() * phaseY);

        // Create a new path
        Entry currentEntry = null;
        Entry previousEntry = entry;
        for (int x = startIndex + 1; x <= endIndex; x++) {
            currentEntry = dataSet.getEntryForIndex(x);

            if (isDrawSteppedEnabled) {
                outputPath.lineTo(currentEntry.getX(), previousEntry.getY() * phaseY);
            }

            outputPath.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);

            previousEntry = currentEntry;
        }

        // Close up
        if (currentEntry != null) {
            outputPath.lineTo(currentEntry.getX(), fillMin);
        }

        outputPath.close();
    }

    @Override
    public void drawValues(Canvas canvas) {
        if (isDrawingValuesAllowed(mChart)) {
            LineData lineData = mChart.getLineData();
            if (lineData == null) {
                return;
            }

            List<ILineDataSet> dataSets = lineData.getDataSets();
            for (int i = 0; i < dataSets.size(); i++) {
                ILineDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1) {
                    continue;
                }

                // Apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                // mMke sure the values do not interfere with the circles
                int valOffset = (int) (dataSet.getCircleRadius() * 1.75f);

                if (!dataSet.isDrawCirclesEnabled()) {
                    valOffset = valOffset / 2;
                }

                mXBounds.set(mChart, dataSet);

                float[] positions = trans.generateTransformedValuesLine(dataSet, mAnimator.getPhaseX(), mAnimator
                        .getPhaseY(), mXBounds.min, mXBounds.max);
                ValueFormatter formatter = dataSet.getValueFormatter();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {
                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x)) {
                        break;
                    }

                    if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)) {
                        continue;
                    }

                    Entry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(canvas, formatter.getPointLabel(entry), x, y - valOffset, dataSet.getValueTextColor(j / 2));
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                canvas, icon, (int) (x + iconsOffset.x), (int) (y + iconsOffset.y),
                                icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                        );
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawValue(Canvas canvas, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        canvas.drawText(valueText, x, y, mValuePaint);
    }

    @Override
    public void drawExtras(Canvas canvas) {
        drawCircles(canvas);
    }

    protected void drawCircles(@NonNull Canvas canvas) {
        LineData lineData = mChart.getLineData();
        if (lineData == null) {
            return;
        }

        mRenderPaint.setStyle(Paint.Style.FILL);

        float phaseY = mAnimator.getPhaseY();

        mCirclesBuffer[0] = 0;
        mCirclesBuffer[1] = 0;

        List<ILineDataSet> dataSets = lineData.getDataSets();
        for (int i = 0; i < dataSets.size(); i++) {
            ILineDataSet dataSet = dataSets.get(i);
            if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled() || dataSet.getEntryCount() == 0) {
                continue;
            }

            mCirclePaintInner.setColor(dataSet.getCircleHoleColor());

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

            mXBounds.set(mChart, dataSet);

            float circleRadius = dataSet.getCircleRadius();
            float circleHoleRadius = dataSet.getCircleHoleRadius();
            boolean drawCircleHole = dataSet.isDrawCircleHoleEnabled() && circleHoleRadius < circleRadius && circleHoleRadius > 0f;
            boolean drawTransparentCircleHole = drawCircleHole && dataSet.getCircleHoleColor() == ColorTemplate.COLOR_NONE;

            DataSetImageCache imageCache;

            if (mImageCaches.containsKey(dataSet)) {
                imageCache = mImageCaches.get(dataSet);
            } else {
                imageCache = new DataSetImageCache();
                mImageCaches.put(dataSet, imageCache);
            }

            boolean changeRequired = imageCache.init(dataSet);

            // Only fill the cache with new bitmaps if a change is required
            if (changeRequired) {
                imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole);
            }

            int boundsRangeCount = mXBounds.range + mXBounds.min;
            for (int j = mXBounds.min; j <= boundsRangeCount; j++) {
                Entry entry = dataSet.getEntryForIndex(j);
                if (entry == null) {
                    break;
                }

                mCirclesBuffer[0] = entry.getX();
                mCirclesBuffer[1] = entry.getY() * phaseY;

                trans.pointValuesToPixel(mCirclesBuffer);

                if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0])) {
                    break;
                }

                if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) || !mViewPortHandler.isInBoundsY(mCirclesBuffer[1])) {
                    continue;
                }

                Bitmap circleBitmap = imageCache.getBitmap(j);
                if (circleBitmap != null) {
                    canvas.drawBitmap(circleBitmap, mCirclesBuffer[0] - circleRadius, mCirclesBuffer[1] - circleRadius, null);
                }
            }
        }
    }

    @Override
    public void drawHighlighted(Canvas canvas, @NonNull Highlight[] highlights) {
        LineData lineData = mChart.getLineData();
        if (lineData == null) {
            return;
        }

        for (Highlight highlight : highlights) {
            ILineDataSet set = lineData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            Entry entry = set.getEntryForXValue(highlight.getX(), highlight.getY());
            if (!isInBoundsX(entry, set)) {
                continue;
            }

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entry.getX(), entry.getY() * mAnimator.getPhaseY());

            highlight.setDraw((float) pix.x, (float) pix.y);

            // Draw the lines
            drawHighlightLines(canvas, (float) pix.x, (float) pix.y, set);
        }
    }

    /**
     * Sets the Bitmap.Config to be used by this renderer. Default: Bitmap.Config.ARGB_8888. Use
     * Bitmap.Config.ARGB_4444 to consume less memory.
     *
     * @param config
     */
    public void setBitmapConfig(Bitmap.Config config) {
        mBitmapConfig = config;
        releaseBitmap();
    }

    /**
     * Returns the Bitmap.Config that is used by this renderer.
     */
    public Bitmap.Config getBitmapConfig() {
        return mBitmapConfig;
    }

    /**
     * Releases the drawing bitmap. This should be called when {@link LineChart#onDetachedFromWindow()}.
     */
    public void releaseBitmap() {
        if (mBitmapCanvas != null) {
            mBitmapCanvas.setBitmap(null);
            mBitmapCanvas = null;
        }

        if (mDrawBitmap != null) {
            Bitmap drawBitmap = mDrawBitmap.get();
            if (drawBitmap != null) {
                drawBitmap.recycle();
            }
            mDrawBitmap.clear();
            mDrawBitmap = null;
        }
    }

    private class DataSetImageCache {
        @NonNull
        private Path mCirclePathBuffer = new Path();

        private Bitmap[] circleBitmaps;

        /**
         * Sets up the cache, returns true if a change of cache was required.
         *
         * @param set
         * @return
         */
        protected boolean init(@NonNull ILineDataSet set) {
            int size = set.getCircleColorCount();
            boolean changeRequired = false;

            if (circleBitmaps == null || circleBitmaps.length != size) {
                circleBitmaps = new Bitmap[size];
                changeRequired = true;
            }

            return changeRequired;
        }

        /**
         * Fills the cache with bitmaps for the given dataset.
         *
         * @param set
         * @param drawCircleHole
         * @param drawTransparentCircleHole
         */
        protected void fill(@NonNull ILineDataSet set, boolean drawCircleHole, boolean drawTransparentCircleHole) {
            int colorCount = set.getCircleColorCount();
            float circleRadius = set.getCircleRadius();
            float circleHoleRadius = set.getCircleHoleRadius();

            for (int i = 0; i < colorCount; i++) {
                Bitmap.Config conf = Bitmap.Config.ARGB_4444;
                Bitmap circleBitmap = Bitmap.createBitmap((int) (circleRadius * 2.1), (int) (circleRadius * 2.1), conf);

                Canvas canvas = new Canvas(circleBitmap);
                circleBitmaps[i] = circleBitmap;
                mRenderPaint.setColor(set.getCircleColor(i));

                if (drawTransparentCircleHole) {
                    // Begin path for circle with hole
                    mCirclePathBuffer.reset();

                    mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleRadius, Path.Direction.CW);

                    // Cut hole in path
                    mCirclePathBuffer.addCircle(circleRadius, circleRadius, circleHoleRadius, Path.Direction.CCW);

                    // Fill in-between
                    canvas.drawPath(mCirclePathBuffer, mRenderPaint);
                } else {
                    canvas.drawCircle(circleRadius, circleRadius, circleRadius, mRenderPaint);

                    if (drawCircleHole) {
                        canvas.drawCircle(circleRadius, circleRadius, circleHoleRadius, mCirclePaintInner);
                    }
                }
            }
        }

        /**
         * Returns the cached Bitmap at the given index.
         *
         * @param index
         */
        protected Bitmap getBitmap(int index) {
            return circleBitmaps[index % circleBitmaps.length];
        }
    }
}
