package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Bubble chart implementation: Copyright 2015 Pierre-Marc Airoldi Licensed
 * under Apache License 2.0 Ported by Daniel Cohen Gindi
 */
public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer {
    private final float[] sizeBuffer = new float[4];
    private final float[] pointBuffer = new float[2];
    private final float[] hsvBuffer = new float[3];

    protected BubbleDataProvider mChart;

    public BubbleChartRenderer(BubbleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);

        mChart = chart;

        mRenderPaint.setStyle(Style.FILL);

        mHighlightPaint.setStyle(Style.STROKE);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
    }

    @Override
    public void initBuffers() {
        // Unused
    }

    @Override
    public void drawData(Canvas canvas) {
        BubbleData bubbleData = mChart.getBubbleData();
        if (bubbleData == null) {
            return;
        }

        for (IBubbleDataSet set : bubbleData.getDataSets()) {
            if (set.isVisible()) {
                drawDataSet(canvas, set);
            }
        }
    }

    protected float getShapeSize(float entrySize, float maxSize, float reference, boolean normalizeSize) {
        final float factor = normalizeSize ? (maxSize == 0f ? 1f : (float) Math.sqrt(entrySize / maxSize)) : entrySize;
        return reference * factor;
    }

    protected void drawDataSet(@NonNull Canvas canvas, @NonNull IBubbleDataSet dataSet) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0f;
        sizeBuffer[2] = 1f;

        trans.pointValuesToPixel(sizeBuffer);

        boolean normalizeSize = dataSet.isNormalizeSizeEnabled();

        // Calculate the full width of 1 step on the x-axis
        final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
            final BubbleEntry entry = dataSet.getEntryForIndex(j);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;

            trans.pointValuesToPixel(pointBuffer);

            float shapeHalf = getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize, normalizeSize) / 2f;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf) || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf)) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf)) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf)) {
                break;
            }

            final int color = dataSet.getColor((int) entry.getX());

            mRenderPaint.setColor(color);
            canvas.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas canvas) {
        BubbleData bubbleData = mChart.getBubbleData();
        if (bubbleData == null) {
            return;
        }

        // If values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();

            float lineHeight = Utils.calcTextHeight(mValuePaint, "1");

            for (int i = 0; i < dataSets.size(); i++) {
                IBubbleDataSet dataSet = dataSets.get(i);
                if (!shouldDrawValues(dataSet)) {
                    continue;
                }

                // Apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                final float phaseX = Math.max(0f, Math.min(1f, mAnimator.getPhaseX()));
                final float phaseY = mAnimator.getPhaseY();

                mXBounds.set(mChart, dataSet);

                final float[] positions = mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max);
                final float alpha = phaseX == 1f ? phaseY : phaseX;

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {
                    int valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min);
                    valueTextColor = Color.argb(Math.round(255f * alpha), Color.red(valueTextColor), Color.green(valueTextColor), Color.blue(valueTextColor));

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x)) {
                        break;
                    }

                    if ((!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y))) {
                        continue;
                    }

                    BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(canvas, dataSet.getValueFormatter(), entry.getSize(), entry, i, x, y + (0.5f * lineHeight), valueTextColor);
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
    public void drawExtras(Canvas canvas) {
        // Unused
    }

    @Override
    public void drawHighlighted(@NonNull Canvas canvas, @NonNull Highlight[] highlights) {
        BubbleData bubbleData = mChart.getBubbleData();
        if (bubbleData == null) {
            return;
        }

        float phaseY = mAnimator.getPhaseY();
        for (Highlight highlight : highlights) {
            IBubbleDataSet set = bubbleData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            final BubbleEntry entry = set.getEntryForXValue(highlight.getX(), highlight.getY());
            if (entry == null || entry.getY() != highlight.getY() || !isInBoundsX(entry, set)) {
                continue;
            }

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            sizeBuffer[0] = 0f;
            sizeBuffer[2] = 1f;

            trans.pointValuesToPixel(sizeBuffer);

            boolean normalizeSize = set.isNormalizeSizeEnabled();

            // Calculate the full width of 1 step on the x-axis
            final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
            final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
            final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            highlight.setDraw(pointBuffer[0], pointBuffer[1]);

            float shapeHalf = getShapeSize(entry.getSize(), set.getMaxSize(), referenceSize, normalizeSize) / 2f;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf) || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf)) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf)) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf)) {
                break;
            }

            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor), Color.blue(originalColor), hsvBuffer);
            hsvBuffer[2] *= 0.5f;
            final int color = Color.HSVToColor(Color.alpha(originalColor), hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
            canvas.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint);
        }
    }
}
