package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Renderer for the HorizontalBarChart.
 *
 * @author Philipp Jahoda
 */
public class HorizontalBarChartRenderer extends BarChartRenderer {
    @NonNull
    private RectF mBarShadowRectBuffer = new RectF();

    public HorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);

        mValuePaint.setTextAlign(Align.LEFT);
    }

    @Override
    public void initBuffers() {
        BarData barData = mChart.getBarData();
        if (barData == null) {
            return;
        }

        mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            if (set != null) {
                mBarBuffers[i] = new HorizontalBarBuffer(
                        set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                        set.isStacked()
                );
            }
        }
    }

    @Override
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

                mBarShadowRectBuffer.top = x - barWidthHalf;
                mBarShadowRectBuffer.bottom = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsTop(mBarShadowRectBuffer.bottom)) {
                    continue;
                }

                if (!mViewPortHandler.isInBoundsBottom(mBarShadowRectBuffer.top)) {
                    break;
                }

                mBarShadowRectBuffer.left = mViewPortHandler.contentLeft();
                mBarShadowRectBuffer.right = mViewPortHandler.contentRight();

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

        for (int j = 0; j < buffer.size(); j += 4) {
            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3])) {
                break;
            }

            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                continue;
            }

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index is out of bounds, reuse
                // colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            canvas.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], mRenderPaint);

            if (drawBorder) {
                canvas.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], mBarBorderPaint);
            }
        }
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

            final float valueOffsetPlus = Utils.convertDpToPixel(5f);
            float posOffset;
            float negOffset;
            final boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < barData.getDataSetCount(); i++) {
                IBarDataSet dataSet = dataSets.get(i);
                if (!shouldDrawValues(dataSet)) {
                    continue;
                }

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // Apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);
                final float halfTextHeight = Utils.calcTextHeight(mValuePaint, "10") / 2f;

                ValueFormatter formatter = dataSet.getValueFormatter();

                // Get the buffer
                BarBuffer buffer = mBarBuffers[i];

                final float phaseY = mAnimator.getPhaseY();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                // If only single values are drawn (sum)
                if (!dataSet.isStacked()) {
                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                        float y = (buffer.buffer[j + 1] + buffer.buffer[j + 3]) / 2f;

                        if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 1])) {
                            break;
                        }

                        if (!mViewPortHandler.isInBoundsX(buffer.buffer[j])) {
                            continue;
                        }

                        if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                            continue;
                        }

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);
                        float val = entry.getY();
                        String formattedValue = formatter.getBarLabel(entry);

                        // Calculate the correct offset depending on the draw position of the value
                        float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                        posOffset = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus);
                        negOffset = drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus;

                        if (isInverted) {
                            posOffset = -posOffset - valueTextWidth;
                            negOffset = -negOffset - valueTextWidth;
                        }

                        if (dataSet.isDrawValuesEnabled()) {
                            drawValue(
                                    canvas, formattedValue, buffer.buffer[j + 2] + (val >= 0 ? posOffset : negOffset),
                                    y + halfTextHeight, dataSet.getValueTextColor(j / 2)
                            );
                        }

                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                            Drawable icon = entry.getIcon();

                            float px = buffer.buffer[j + 2] + (val >= 0 ? posOffset : negOffset);
                            float py = y;

                            px += iconsOffset.x;
                            py += iconsOffset.y;

                            Utils.drawImage(
                                    canvas, icon, (int) px, (int) py,
                                    icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                            );
                        }
                    }

                    // If each value of a potential stack should be drawn
                } else {
                    Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                    int bufferIndex = 0;
                    int index = 0;
                    while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {
                        BarEntry entry = dataSet.getEntryForIndex(index);

                        int color = dataSet.getValueTextColor(index);
                        float[] values = entry.getYVals();

                        // We still draw stacked bars, but there is one non-stacked in between
                        if (values == null) {
                            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[bufferIndex + 1])) {
                                break;
                            }

                            if (!mViewPortHandler.isInBoundsX(buffer.buffer[bufferIndex])) {
                                continue;
                            }

                            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[bufferIndex + 1])) {
                                continue;
                            }

                            String formattedValue = formatter.getBarLabel(entry);

                            // Calculate the correct offset depending on the draw position of the value
                            float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                            posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                            negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                            if (isInverted) {
                                posOffset = -posOffset - valueTextWidth;
                                negOffset = -negOffset - valueTextWidth;
                            }

                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(
                                        canvas, formattedValue, buffer.buffer[bufferIndex + 2] + (entry.getY() >= 0 ? posOffset : negOffset),
                                        buffer.buffer[bufferIndex + 1] + halfTextHeight, color
                                );
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                Drawable icon = entry.getIcon();

                                float px = buffer.buffer[bufferIndex + 2] + (entry.getY() >= 0 ? posOffset : negOffset);
                                float py = buffer.buffer[bufferIndex + 1];

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        canvas, icon, (int) px, (int) py,
                                        icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                                );
                            }
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

                                transformed[k] = y * phaseY;
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {
                                final float val = values[k / 2];
                                String formattedValue = formatter.getBarStackedLabel(val, entry);

                                // Calculate the correct offset depending on the draw position of the value
                                float valueTextWidth = Utils.calcTextWidth(mValuePaint, formattedValue);
                                posOffset = (drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus));
                                negOffset = (drawValueAboveBar ? -(valueTextWidth + valueOffsetPlus) : valueOffsetPlus);

                                if (isInverted) {
                                    posOffset = -posOffset - valueTextWidth;
                                    negOffset = -negOffset - valueTextWidth;
                                }

                                final boolean drawBelow = (val == 0f && negY == 0f && posY > 0f) || val < 0f;

                                float x = transformed[k] + (drawBelow ? negOffset : posOffset);
                                float y = (buffer.buffer[bufferIndex + 1] + buffer.buffer[bufferIndex + 3]) / 2f;

                                if (!mViewPortHandler.isInBoundsTop(y)) {
                                    break;
                                }

                                if (!mViewPortHandler.isInBoundsX(x)) {
                                    continue;
                                }

                                if (!mViewPortHandler.isInBoundsBottom(y)) {
                                    continue;
                                }

                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(canvas, formattedValue, x, y + halfTextHeight, color);
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
    public void drawValue(@NonNull Canvas canvas, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        canvas.drawText(valueText, x, y, mValuePaint);
    }

    @Override
    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, @NonNull Transformer trans) {
        float top = x - barWidthHalf;
        float bottom = x + barWidthHalf;
        float left = y1;
        float right = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhaseHorizontal(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    protected void setHighlightDrawPos(@NonNull Highlight highlight, @NonNull RectF bar) {
        highlight.setDraw(bar.centerY(), bar.right);
    }

    @Override
    protected boolean isDrawingValuesAllowed(@NonNull ChartInterface chart) {
        ChartData data = chart.getData();
        return data != null && data.getEntryCount() < chart.getMaxVisibleCount() * mViewPortHandler.getScaleY();
    }
}
