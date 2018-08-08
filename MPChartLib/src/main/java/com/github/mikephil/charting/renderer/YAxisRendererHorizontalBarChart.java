package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class YAxisRendererHorizontalBarChart extends YAxisRenderer {
    @NonNull
    protected Path mDrawZeroLinePathBuffer = new Path();

    @NonNull
    protected Path mRenderLimitLinesPathBuffer = new Path();
    protected float[] mRenderLimitLinesBuffer = new float[4];

    public YAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);

        mLimitLinePaint.setTextAlign(Align.LEFT);
    }

    /**
     * Computes the axis values.
     *
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    @Override
    public void computeAxis(float yMin, float yMax, boolean inverted) {
        // Calculate the starting and entry point of the y-labels (depending on zoom/contentrect bounds)
        if (mViewPortHandler.contentHeight() > 10f && !mViewPortHandler.isFullyZoomedOutX()) {
            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentRight(), mViewPortHandler.contentTop());

            if (!inverted) {
                yMin = (float) p1.x;
                yMax = (float) p2.x;
            } else {
                yMin = (float) p2.x;
                yMax = (float) p1.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        computeAxisValues(yMin, yMax);
    }

    /**
     * Draws the y-axis labels to the screen.
     */
    @Override
    public void renderAxisLabels(Canvas canvas) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled()) {
            return;
        }

        float[] positions = getTransformedPositions();

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());
        mAxisLabelPaint.setTextAlign(Align.CENTER);

        float baseYOffset = Utils.convertDpToPixel(2.5f);

        float yPos;
        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            yPos = mViewPortHandler.contentTop() - baseYOffset;
        } else {
            float textHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");

            yPos = mViewPortHandler.contentBottom() + textHeight + baseYOffset;
        }

        drawYLabels(canvas, yPos, positions, mYAxis.getYOffset());
    }

    @Override
    public void renderAxisLine(@NonNull Canvas canvas) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled()) {
            return;
        }

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            canvas.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mAxisLinePaint);
        } else {
            canvas.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * Draws the y-labels on the specified x-position.
     *
     * @param fixedPosition
     * @param positions
     */
    @Override
    protected void drawYLabels(@NonNull Canvas canvas, float fixedPosition, float[] positions, float offset) {
        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled() ? mYAxis.mEntryCount : (mYAxis.mEntryCount - 1);

        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);

            canvas.drawText(text, positions[i * 2], fixedPosition - offset, mAxisLabelPaint);
        }
    }

    @Override
    protected float[] getTransformedPositions() {
        if (mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }

        float[] positions = mGetTransformedPositionsBuffer;
        for (int i = 0; i < positions.length; i += 2) {
            // Only fill x values, y values are not needed for x-labels
            positions[i] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

    @NonNull
    @Override
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }

    @NonNull
    @Override
    protected Path linePath(@NonNull Path path, int i, float[] positions) {
        path.moveTo(positions[i], mViewPortHandler.contentTop());
        path.lineTo(positions[i], mViewPortHandler.contentBottom());
        return path;
    }

    @Override
    protected void drawZeroLine(@NonNull Canvas canvas) {
        int clipRestoreCount = canvas.save();
        mZeroLineClippingRect.set(mViewPortHandler.getContentRect());
        mZeroLineClippingRect.inset(-mYAxis.getZeroLineWidth(), 0f);
        canvas.clipRect(mLimitLineClippingRect);

        // Draw zero line
        MPPointD pos = mTrans.getPixelForValues(0f, 0f);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());

        Path zeroLinePath = mDrawZeroLinePathBuffer;
        zeroLinePath.reset();

        zeroLinePath.moveTo((float) pos.x - 1, mViewPortHandler.contentTop());
        zeroLinePath.lineTo((float) pos.x - 1, mViewPortHandler.contentBottom());

        // Draw a path because lines don't support dashing on lower android versions
        canvas.drawPath(zeroLinePath, mZeroLinePaint);
        canvas.restoreToCount(clipRestoreCount);
    }

    /**
     * Draws the LimitLines associated with this axis to the screen. This is the standard XAxis
     * renderer using the YAxis limit lines.
     *
     * @param canvas
     */
    @Override
    public void renderLimitLines(@NonNull Canvas canvas) {
        List<LimitLine> limitLines = mYAxis.getLimitLines();
        if (limitLines.isEmpty()) {
            return;
        }

        float[] points = mRenderLimitLinesBuffer;
        points[0] = 0;
        points[1] = 0;
        points[2] = 0;
        points[3] = 0;
        Path limitLinePath = mRenderLimitLinesPathBuffer;
        limitLinePath.reset();

        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine limitLine = limitLines.get(i);
            if (!limitLine.isEnabled()) {
                continue;
            }

            int clipRestoreCount = canvas.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(-limitLine.getLineWidth(), 0f);
            canvas.clipRect(mLimitLineClippingRect);

            points[0] = limitLine.getLimit();
            points[2] = limitLine.getLimit();

            mTrans.pointValuesToPixel(points);

            points[1] = mViewPortHandler.contentTop();
            points[3] = mViewPortHandler.contentBottom();

            limitLinePath.moveTo(points[0], points[1]);
            limitLinePath.lineTo(points[2], points[3]);

            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(limitLine.getLineColor());
            mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
            mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());

            canvas.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();

            String label = limitLine.getLabel();

            // If drawing the limit-value label is enabled
            if (!label.isEmpty()) {
                mLimitLinePaint.setStyle(limitLine.getTextStyle());
                mLimitLinePaint.setPathEffect(null);
                mLimitLinePaint.setColor(limitLine.getTextColor());
                mLimitLinePaint.setTypeface(limitLine.getTypeface());
                mLimitLinePaint.setStrokeWidth(0.5f);
                mLimitLinePaint.setTextSize(limitLine.getTextSize());

                float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();
                float yOffset = Utils.convertDpToPixel(2f) + limitLine.getYOffset();

                final LimitLine.LimitLabelPosition position = limitLine.getLabelPosition();

                if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                    final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    canvas.drawText(label, points[0] + xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint.setTextAlign(Align.LEFT);
                    canvas.drawText(label, points[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
                } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                    canvas.drawText(label, points[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
                } else {
                    mLimitLinePaint.setTextAlign(Align.RIGHT);
                    canvas.drawText(label, points[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
                }
            }

            canvas.restoreToCount(clipRestoreCount);
        }
    }
}
