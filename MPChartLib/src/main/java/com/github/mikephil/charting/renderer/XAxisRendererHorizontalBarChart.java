package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

public class XAxisRendererHorizontalBarChart extends XAxisRenderer {
    protected BarChart mChart;

    @NonNull
    protected Path mRenderLimitLinesPathBuffer = new Path();

    public XAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, BarChart chart) {
        super(viewPortHandler, xAxis, trans);

        this.mChart = chart;
    }

    @Override
    public void computeAxis(float min, float max, boolean inverted) {
        // Calculate the starting and entry point of the y-labels (depending on  zoom/contentrect bounds)
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {
            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());

            if (inverted) {
                min = (float) p2.y;
                max = (float) p1.y;
            } else {
                min = (float) p1.y;
                max = (float) p2.y;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        computeAxisValues(min, max);
    }

    @Override
    protected void computeSize() {
        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        String longest = mXAxis.getLongestLabel();

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, longest);

        final float labelWidth = (int) (labelSize.width + mXAxis.getXOffset() * 3.5f);
        final float labelHeight = labelSize.height;

        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelSize.width, labelHeight, mXAxis.getLabelRotationAngle());

        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = (int) (labelRotatedSize.width + mXAxis.getXOffset() * 3.5f);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        FSize.recycleInstance(labelRotatedSize);
    }

    @Override
    public void renderAxisLabels(Canvas canvas) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled()) {
            return;
        }

        float xOffset = mXAxis.getXOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0f, 0f);

        if (mXAxis.getPosition() == XAxisPosition.TOP) {
            pointF.x = 0f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentRight() + xOffset, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.TOP_INSIDE) {
            pointF.x = 1f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentRight() - xOffset, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {
            pointF.x = 1f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentLeft() - xOffset, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 1f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentLeft() + xOffset, pointF);
        } else { // Both sided
            pointF.x = 0f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentRight() + xOffset, pointF);
            pointF.x = 1f;
            pointF.y = 0.5f;
            drawLabels(canvas, mViewPortHandler.contentLeft() - xOffset, pointF);
        }

        MPPointF.recycleInstance(pointF);
    }

    @Override
    protected void drawLabels(Canvas canvas, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {
            // Only fill x values
            if (centeringEnabled) {
                positions[i + 1] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i + 1] = mXAxis.mEntries[i / 2];
            }
        }

        mTrans.pointValuesToPixel(positions);

        for (int i = 0; i < positions.length; i += 2) {
            float y = positions[i + 1];
            if (mViewPortHandler.isInBoundsY(y)) {
                String label = mXAxis.getValueFormatter().getAxisLabel(mXAxis.mEntries[i / 2], mXAxis);
                drawLabel(canvas, label, pos, y, anchor, labelRotationAngleDegrees);
            }
        }
    }

    @NonNull
    @Override
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(0f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }

    @Override
    protected void drawGridLine(@NonNull Canvas canvas, float x, float y, @NonNull Path gridLinePath) {
        gridLinePath.moveTo(mViewPortHandler.contentRight(), y);
        gridLinePath.lineTo(mViewPortHandler.contentLeft(), y);

        // Draw a path because lines don't support dashing on lower android versions
        canvas.drawPath(gridLinePath, mGridPaint);

        gridLinePath.reset();
    }

    @Override
    public void renderAxisLine(@NonNull Canvas canvas) {
        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled()) {
            return;
        }

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());

        if (mXAxis.getPosition() == XAxisPosition.TOP || mXAxis.getPosition() == XAxisPosition.TOP_INSIDE || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            canvas.drawLine(
                    mViewPortHandler.contentRight(), mViewPortHandler.contentTop(),
                    mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(), mAxisLinePaint
            );
        }

        if (mXAxis.getPosition() == XAxisPosition.BOTTOM || mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            canvas.drawLine(
                    mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(),
                    mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), mAxisLinePaint
            );
        }
    }

    /**
     * Draws the LimitLines associated with this axis to the screen. This is the standard YAxis
     * renderer using the XAxis limit lines.
     *
     * @param canvas
     */
    @Override
    public void renderLimitLines(@NonNull Canvas canvas) {
        List<LimitLine> limitLines = mXAxis.getLimitLines();
        if (limitLines.isEmpty()) {
            return;
        }

        float[] points = mRenderLimitLinesBuffer;
        points[0] = 0;
        points[1] = 0;

        Path limitLinePath = mRenderLimitLinesPathBuffer;
        limitLinePath.reset();

        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine limitLine = limitLines.get(i);
            if (!limitLine.isEnabled()) {
                continue;
            }

            int clipRestoreCount = canvas.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(0f, -limitLine.getLineWidth());
            canvas.clipRect(mLimitLineClippingRect);

            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(limitLine.getLineColor());
            mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
            mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());

            points[1] = limitLine.getLimit();

            mTrans.pointValuesToPixel(points);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), points[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), points[1]);

            canvas.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();

            String label = limitLine.getLabel();

            // If drawing the limit-value label is enabled
            if (!label.isEmpty()) {
                mLimitLinePaint.setStyle(limitLine.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(limitLine.getTextColor());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(limitLine.getTextSize());

                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                float xOffset = Utils.convertDpToPixel(4f) + limitLine.getXOffset();
                float yOffset = limitLine.getLineWidth() + labelLineHeight + limitLine.getYOffset();

                final LimitLine.LimitLabelPosition position = limitLine.getLabelPosition();

                if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    canvas.drawText(label, mViewPortHandler.contentRight() - xOffset, points[1] - yOffset + labelLineHeight, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    canvas.drawText(label, mViewPortHandler.contentRight() - xOffset, points[1] + yOffset, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    canvas.drawText(label, mViewPortHandler.contentLeft() + xOffset, points[1] - yOffset + labelLineHeight, mLimitLinePaint);
                } else {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    canvas.drawText(label, mViewPortHandler.offsetLeft() + xOffset, points[1] + yOffset, mLimitLinePaint);
                }
            }

            canvas.restoreToCount(clipRestoreCount);
        }
    }
}
