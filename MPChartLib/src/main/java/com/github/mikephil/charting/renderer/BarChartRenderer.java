package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {
    protected BarDataProvider mChart;

    /**
     * The rect object that is used for drawing the bars.
     */
    protected RectF mBarRect = new RectF();

    protected BarBuffer[] mBarBuffers;

    protected Paint mShadowPaint;
    protected Paint mBarBorderPaint;

    @NonNull
    private RectF mBarShadowRectBuffer = new RectF();

    public BarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);

        this.mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        mHighlightPaint.setAlpha(120);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);

        mBarBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void initBuffers() {
        BarData barData = mChart.getBarData();
        if (barData == null) {
            return;
        }

        mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (set != null) {
                mBarBuffers[i] = new BarBuffer(
                        set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                        set.isStacked()
                );
            }
        }
    }

    @Override
    public void drawData(@NonNull Canvas canvas) {
        BarData barData = mChart.getBarData();
        if (barData == null) {
            return;
        }

        for (int i = 0; i < barData.getDataSetCount(); i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (set != null && set.isVisible()) {
                drawDataSet(canvas, set, i);
            }
        }
    }

    protected void drawDataSet(@NonNull Canvas canvas, @NonNull IBarDataSet dataSet, int index) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // Draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();
            if (barData == null) {
                return;
            }

            final float barWidth = barData.getBarWidth();
            final float barWidthHalf = barWidth / 2f;
            float x;
            for (int i = 0, count = Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount()); i < count; i++) {
                BarEntry entry = dataSet.getEntryForIndex(i);

                x = entry.getX();

                mBarShadowRectBuffer.left = x - barWidthHalf;
                mBarShadowRectBuffer.right = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    continue;
                }

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) {
                    break;
                }

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop();
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom();

                canvas.drawRect(mBarShadowRectBuffer, mShadowPaint);
            }
        }

        // Initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int i = 0; i < buffer.size(); i += 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[i + 2])) {
                continue;
            }

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[i])) {
                break;
            }

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index is out of bounds,
                // reuse colors.
                mRenderPaint.setColor(dataSet.getColor(i / 4));
            }

            canvas.drawRect(
                    buffer.buffer[i], buffer.buffer[i + 1], buffer.buffer[i + 2],
                    buffer.buffer[i + 3], mRenderPaint
            );

            if (drawBorder) {
                canvas.drawRect(
                        buffer.buffer[i], buffer.buffer[i + 1], buffer.buffer[i + 2],
                        buffer.buffer[i + 3], mBarBorderPaint
                );
            }
        }
    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {
        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public void drawValues(Canvas canvas) {
        // If values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            BarData barData = mChart.getBarData();
            if (barData == null) {
                return;
            }

            List<IBarDataSet> dataSets = barData.getDataSets();
            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset;
            float negOffset;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();
            for (int i = 0; i < barData.getDataSetCount(); i++) {
                IBarDataSet dataSet = dataSets.get(i);
                if (!shouldDrawValues(dataSet)) {
                    continue;
                }

                // Apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // Calculate the correct offset depending on the draw position of the value
                float valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
                posOffset = drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus;
                negOffset = drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus;

                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight;
                    negOffset = -negOffset - valueTextHeight;
                }

                // Get the buffer
                BarBuffer buffer = mBarBuffers[i];
                final float phaseY = mAnimator.getPhaseY();
                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                // If only single values are drawn (sum)
                if (!dataSet.isStacked()) {
                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                        float x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f;
                        if (!mViewPortHandler.isInBoundsRight(x)) {
                            break;
                        }

                        if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1]) || !mViewPortHandler.isInBoundsLeft(x)) {
                            continue;
                        }

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);
                        float val = entry.getY();
                        if (dataSet.isDrawValuesEnabled()) {
                            drawValue(
                                    canvas, dataSet.getValueFormatter(), val, entry, i, x,
                                    val >= 0 ? (buffer.buffer[j + 1] + posOffset) : (buffer.buffer[j + 3] + negOffset),
                                    dataSet.getValueTextColor(j / 4)
                            );
                        }

                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                            Drawable icon = entry.getIcon();

                            float px = x;
                            float py = val >= 0 ? (buffer.buffer[j + 1] + posOffset) : (buffer.buffer[j + 3] + negOffset);

                            px += iconsOffset.x;
                            py += iconsOffset.y;

                            Utils.drawImage(
                                    canvas, icon, (int) px, (int) py,
                                    icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                            );
                        }
                    }

                    // If we have stacks
                } else {
                    Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                    int bufferIndex = 0;
                    int index = 0;
                    while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {
                        BarEntry entry = dataSet.getEntryForIndex(index);

                        float[] values = entry.getYVals();
                        float x = (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f;

                        int color = dataSet.getValueTextColor(index);

                        // We still draw stacked bars, but there is one non-stacked in between
                        if (values == null) {
                            if (!mViewPortHandler.isInBoundsRight(x)) {
                                break;
                            }

                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1]) || !mViewPortHandler.isInBoundsLeft(x)) {
                                continue;
                            }

                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(
                                        canvas, dataSet.getValueFormatter(), entry.getY(), entry, i, x,
                                        buffer.buffer[bufferIndex + 1] + (entry.getY() >= 0 ? posOffset : negOffset),
                                        color
                                );
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                Drawable icon = entry.getIcon();

                                float px = x;
                                float py = buffer.buffer[bufferIndex + 1] + (entry.getY() >= 0 ? posOffset : negOffset);

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        canvas, icon, (int) px, (int) py,
                                        icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                                );
                            }

                            // Draw stack values
                        } else {
                            float[] transformed = new float[values.length * 2];

                            float posY = 0f;
                            float negY = -entry.getNegativeSum();

                            for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {
                                float value = values[idx];
                                float y;

                                if (value == 0f && (posY == 0f || negY == 0f)) {
                                    // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                    y = value;
                                } else if (value >= 0f) {
                                    posY += value;
                                    y = posY;
                                } else {
                                    y = negY;
                                    negY -= value;
                                }

                                transformed[k + 1] = y * phaseY;
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {
                                final float val = values[k / 2];
                                final boolean drawBelow = (val == 0.0f && negY == 0.0f && posY > 0.0f) || val < 0.0f;
                                float y = transformed[k + 1] + (drawBelow ? negOffset : posOffset);

                                if (!mViewPortHandler.isInBoundsRight(x)) {
                                    break;
                                }

                                if (!mViewPortHandler.isInBoundsY(y) || !mViewPortHandler.isInBoundsLeft(x)) {
                                    continue;
                                }

                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(
                                            canvas, dataSet.getValueFormatter(), values[k / 2],
                                            entry, i, x, y, color
                                    );
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                    Drawable icon = entry.getIcon();

                                    Utils.drawImage(
                                            canvas, icon, (int) (x + iconsOffset.x), (int) (y + iconsOffset.y),
                                            icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                                    );
                                }
                            }
                        }

                        bufferIndex = values == null ? bufferIndex + 4 : bufferIndex + 4 * values.length;
                        index++;
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawHighlighted(@NonNull Canvas canvas, @NonNull Highlight[] highlights) {
        BarData barData = mChart.getBarData();
        if (barData == null) {
            return;
        }

        for (Highlight highlight : highlights) {
            IBarDataSet set = barData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            BarEntry entry = set.getEntryForXValue(highlight.getX(), highlight.getY());
            if (!isInBoundsX(entry, set)) {
                continue;
            }

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = highlight.getStackIndex() >= 0 && entry.isStacked();

            final float y1;
            final float y2;

            if (isStack) {
                if (mChart.isHighlightFullBarEnabled()) {
                    y1 = entry.getPositiveSum();
                    y2 = -entry.getNegativeSum();
                } else {
                    Range range = entry.getRanges()[highlight.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }
            } else {
                y1 = entry.getY();
                y2 = 0f;
            }

            prepareBarHighlight(entry.getX(), y1, y2, barData.getBarWidth() / 2f, trans);

            setHighlightDrawPos(highlight, mBarRect);

            canvas.drawRect(mBarRect, mHighlightPaint);
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     *
     * @param highlight
     */
    protected void setHighlightDrawPos(@NonNull Highlight highlight, @NonNull RectF bar) {
        highlight.setDraw(bar.centerX(), bar.top);
    }

    @Override
    public void drawExtras(Canvas canvas) {
    }
}
