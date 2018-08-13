package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

public class YAxisRenderer extends AxisRenderer {
    protected YAxis mYAxis;
    protected Paint mZeroLinePaint;

    @NonNull
    protected Path mRenderGridLinesPath = new Path();

    @NonNull
    protected RectF mGridClippingRect = new RectF();

    @NonNull
    protected float[] mGetTransformedPositionsBuffer = new float[2];

    @NonNull
    protected Path mDrawZeroLinePath = new Path();

    @NonNull
    protected RectF mZeroLineClippingRect = new RectF();

    @NonNull
    protected Path mRenderLimitLines = new Path();
    protected float[] mRenderLimitLinesBuffer = new float[2];

    @NonNull
    protected RectF mLimitLineClippingRect = new RectF();

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans, yAxis);

        this.mYAxis = yAxis;

        if (mViewPortHandler != null) {
            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));

            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }
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

        float xOffset = mYAxis.getXOffset();
        float yOffset = Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();

        AxisDependency dependency = mYAxis.getAxisDependency();
        YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float xPos;
        if (dependency == AxisDependency.LEFT) {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.offsetLeft() - xOffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.offsetLeft() + xOffset;
            }
        } else {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mViewPortHandler.contentRight() + xOffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mViewPortHandler.contentRight() - xOffset;
            }
        }

        drawYLabels(canvas, xPos, positions, yOffset);
    }

    @Override
    public void renderAxisLine(@NonNull Canvas canvas) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled()) {
            return;
        }

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == AxisDependency.LEFT) {
            canvas.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), mAxisLinePaint);
        } else {
            canvas.drawLine(mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * Draws the y-labels on the specified x-position.
     *
     * @param fixedPosition
     * @param positions
     */
    protected void drawYLabels(@NonNull Canvas canvas, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled() ? mYAxis.mEntryCount : (mYAxis.mEntryCount - 1);

        // Draw
        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);

            canvas.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
    }

    @Override
    public void renderGridLines(@NonNull Canvas canvas) {
        if (!mYAxis.isEnabled()) {
            return;
        }

        if (mYAxis.isDrawGridLinesEnabled()) {
            int clipRestoreCount = canvas.save();
            canvas.clipRect(getGridClippingRect());

            float[] positions = getTransformedPositions();

            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();

            // Draw the grid
            for (int i = 0; i < positions.length; i += 2) {
                // Draw a path because lines don't support dashing on lower android versions
                canvas.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                gridLinePath.reset();
            }

            canvas.restoreToCount(clipRestoreCount);
        }

        if (mYAxis.isDrawZeroLineEnabled()) {
            drawZeroLine(canvas);
        }
    }

    @NonNull
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(0.f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }

    /**
     * Calculates the path for a grid line.
     *
     * @param path
     * @param i
     * @param positions
     * @return
     */
    @NonNull
    protected Path linePath(@NonNull Path path, int i, float[] positions) {
        path.moveTo(mViewPortHandler.offsetLeft(), positions[i + 1]);
        path.lineTo(mViewPortHandler.contentRight(), positions[i + 1]);
        return path;
    }

    /**
     * Transforms the values contained in the axis entries to screen pixels and returns them in form
     * of a float array of x- and y-coordinates.
     *
     * @return
     */
    protected float[] getTransformedPositions() {
        if (mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }

        float[] positions = mGetTransformedPositionsBuffer;
        for (int i = 0; i < positions.length; i += 2) {
            // Only fill y values, x values are not needed for y-labels
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

    /**
     * Draws the zero line.
     */
    protected void drawZeroLine(@NonNull Canvas canvas) {
        int clipRestoreCount = canvas.save();
        mZeroLineClippingRect.set(mViewPortHandler.getContentRect());
        mZeroLineClippingRect.inset(0.f, -mYAxis.getZeroLineWidth());
        canvas.clipRect(mZeroLineClippingRect);

        // Draw zero line
        MPPointD pos = mTrans.getPixelForValues(0f, 0f);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());

        Path zeroLinePath = mDrawZeroLinePath;
        zeroLinePath.reset();

        zeroLinePath.moveTo(mViewPortHandler.contentLeft(), (float) pos.y);
        zeroLinePath.lineTo(mViewPortHandler.contentRight(), (float) pos.y);

        // Draw a path because lines don't support dashing on lower android versions
        canvas.drawPath(zeroLinePath, mZeroLinePaint);

        canvas.restoreToCount(clipRestoreCount);
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
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
        Path limitLinePath = mRenderLimitLines;
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
                mLimitLinePaint.setTypeface(limitLine.getTypeface());
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
