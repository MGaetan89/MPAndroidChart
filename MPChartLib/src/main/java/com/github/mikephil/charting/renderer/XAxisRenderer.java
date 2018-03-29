package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

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

public class XAxisRenderer extends AxisRenderer {
    protected XAxis mXAxis;

    @NonNull
    protected Path mRenderGridLinesPath = new Path();
    protected float[] mRenderGridLinesBuffer = new float[2];

    @NonNull
    protected RectF mGridClippingRect = new RectF();

    protected float[] mRenderLimitLinesBuffer = new float[2];

    @NonNull
    protected RectF mLimitLineClippingRect = new RectF();

    float[] mLimitLineSegmentsBuffer = new float[4];

    @NonNull
    private final Path mLimitLinePath = new Path();

    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, trans, xAxis);

        this.mXAxis = xAxis;

        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextAlign(Align.CENTER);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }

    protected void setupGridPaint() {
        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());
    }

    @Override
    public void computeAxis(float min, float max, boolean inverted) {
        // Calculate the starting and entry point of the y-labels (depending on zoom/contentrect bounds)
        if (mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutX()) {
            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentRight(), mViewPortHandler.contentTop());

            if (inverted) {
                min = (float) p2.x;
                max = (float) p1.x;
            } else {
                min = (float) p1.x;
                max = (float) p2.x;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }

        computeAxisValues(min, max);
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);

        computeSize();
    }

    protected void computeSize() {
        String longest = mXAxis.getLongestLabel();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, longest);

        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");

        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelWidth, labelHeight, mXAxis.getLabelRotationAngle());

        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        FSize.recycleInstance(labelRotatedSize);
        FSize.recycleInstance(labelSize);
    }

    @Override
    public void renderAxisLabels(Canvas canvas) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled()) {
            return;
        }

        float yOffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0f, 0f);
        if (mXAxis.getPosition() == XAxisPosition.TOP) {
            pointF.x = 0.5f;
            pointF.y = 1f;
            drawLabels(canvas, mViewPortHandler.contentTop() - yOffset, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 1f;
            drawLabels(canvas, mViewPortHandler.contentTop() + yOffset + mXAxis.mLabelRotatedHeight, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM) {
            pointF.x = 0.5f;
            pointF.y = 0f;
            drawLabels(canvas, mViewPortHandler.contentBottom() + yOffset, pointF);
        } else if (mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 0f;
            drawLabels(canvas, mViewPortHandler.contentBottom() - yOffset - mXAxis.mLabelRotatedHeight, pointF);
        } else { // Both sided
            pointF.x = 0.5f;
            pointF.y = 1f;
            drawLabels(canvas, mViewPortHandler.contentTop() - yOffset, pointF);
            pointF.x = 0.5f;
            pointF.y = 0f;
            drawLabels(canvas, mViewPortHandler.contentBottom() + yOffset, pointF);
        }

        MPPointF.recycleInstance(pointF);
    }

    @Override
    public void renderAxisLine(@NonNull Canvas canvas) {
        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled()) {
            return;
        }

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        if (mXAxis.getPosition() == XAxisPosition.TOP || mXAxis.getPosition() == XAxisPosition.TOP_INSIDE || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            canvas.drawLine(
                    mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(),
                    mViewPortHandler.contentRight(), mViewPortHandler.contentTop(), mAxisLinePaint
            );
        }

        if (mXAxis.getPosition() == XAxisPosition.BOTTOM || mXAxis.getPosition() == XAxisPosition.BOTTOM_INSIDE || mXAxis.getPosition() == XAxisPosition.BOTH_SIDED) {
            canvas.drawLine(
                    mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(),
                    mViewPortHandler.contentRight(), mViewPortHandler.contentBottom(), mAxisLinePaint
            );
        }
    }

    /**
     * Draws the x-labels on the specified y-position.
     *
     * @param pos
     */
    protected void drawLabels(Canvas canvas, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {
            // Only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }

        mTrans.pointValuesToPixel(positions);

        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPortHandler.isInBoundsX(x)) {
                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
                    // Avoid clipping of the last
                    if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                        if (width > mViewPortHandler.offsetRight() * 2 && x + width > mViewPortHandler.getChartWidth()) {
                            x -= width / 2;
                        }

                        // Avoid clipping of the first
                    } else if (i == 0) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        x += width / 2;
                    }
                }

                drawLabel(canvas, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
    }

    protected void drawLabel(Canvas canvas, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        Utils.drawXAxisValue(canvas, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }

    @Override
    public void renderGridLines(@NonNull Canvas canvas) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled()) {
            return;
        }

        int clipRestoreCount = canvas.save();
        canvas.clipRect(getGridClippingRect());

        if (mRenderGridLinesBuffer.length != mAxis.mEntryCount * 2) {
            mRenderGridLinesBuffer = new float[mXAxis.mEntryCount * 2];
        }

        float[] positions = mRenderGridLinesBuffer;
        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);

        setupGridPaint();

        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        for (int i = 0; i < positions.length; i += 2) {
            drawGridLine(canvas, positions[i], positions[i + 1], gridLinePath);
        }

        canvas.restoreToCount(clipRestoreCount);
    }

    @NonNull
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }

    /**
     * Draws the grid line at the specified position using the provided path.
     *
     * @param canvas
     * @param x
     * @param y
     * @param gridLinePath
     */
    protected void drawGridLine(@NonNull Canvas canvas, float x, float y, @NonNull Path gridLinePath) {
        gridLinePath.moveTo(x, mViewPortHandler.contentBottom());
        gridLinePath.lineTo(x, mViewPortHandler.contentTop());

        // Draw a path because lines don't support dashing on lower android versions
        canvas.drawPath(gridLinePath, mGridPaint);

        gridLinePath.reset();
    }

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param canvas
     */
    @Override
    public void renderLimitLines(@NonNull Canvas canvas) {
        List<LimitLine> limitLines = mXAxis.getLimitLines();
        if (limitLines.isEmpty()) {
            return;
        }

        float[] position = mRenderLimitLinesBuffer;
        position[0] = 0;
        position[1] = 0;

        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine limitLine = limitLines.get(i);
            if (!limitLine.isEnabled()) {
                continue;
            }

            int clipRestoreCount = canvas.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(-limitLine.getLineWidth(), 0.f);
            canvas.clipRect(mLimitLineClippingRect);

            position[0] = limitLine.getLimit();
            position[1] = 0f;

            mTrans.pointValuesToPixel(position);

            renderLimitLineLine(canvas, limitLine, position);
            renderLimitLineLabel(canvas, limitLine, position, 2.f + limitLine.getYOffset());

            canvas.restoreToCount(clipRestoreCount);
        }
    }

    public void renderLimitLineLine(@NonNull Canvas canvas, @NonNull LimitLine limitLine, float[] position) {
        mLimitLineSegmentsBuffer[0] = position[0];
        mLimitLineSegmentsBuffer[1] = mViewPortHandler.contentTop();
        mLimitLineSegmentsBuffer[2] = position[0];
        mLimitLineSegmentsBuffer[3] = mViewPortHandler.contentBottom();

        mLimitLinePath.reset();
        mLimitLinePath.moveTo(mLimitLineSegmentsBuffer[0], mLimitLineSegmentsBuffer[1]);
        mLimitLinePath.lineTo(mLimitLineSegmentsBuffer[2], mLimitLineSegmentsBuffer[3]);

        mLimitLinePaint.setStyle(Paint.Style.STROKE);
        mLimitLinePaint.setColor(limitLine.getLineColor());
        mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
        mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());

        canvas.drawPath(mLimitLinePath, mLimitLinePaint);
    }

    public void renderLimitLineLabel(@NonNull Canvas canvas, @NonNull LimitLine limitLine, float[] position, float yOffset) {
        String label = limitLine.getLabel();

        // If drawing the limit-value label is enabled
        if (!label.isEmpty()) {
            mLimitLinePaint.setStyle(limitLine.getTextStyle());
            mLimitLinePaint.setPathEffect(null);
            mLimitLinePaint.setColor(limitLine.getTextColor());
            mLimitLinePaint.setStrokeWidth(0.5f);
            mLimitLinePaint.setTextSize(limitLine.getTextSize());

            float xOffset = limitLine.getLineWidth() + limitLine.getXOffset();

            final LimitLine.LimitLabelPosition labelPosition = limitLine.getLabelPosition();

            if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                mLimitLinePaint.setTextAlign(Align.LEFT);
                canvas.drawText(label, position[0] + xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                mLimitLinePaint.setTextAlign(Align.LEFT);
                canvas.drawText(label, position[0] + xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            } else if (labelPosition == LimitLine.LimitLabelPosition.LEFT_TOP) {
                mLimitLinePaint.setTextAlign(Align.RIGHT);
                final float labelLineHeight = Utils.calcTextHeight(mLimitLinePaint, label);
                canvas.drawText(label, position[0] - xOffset, mViewPortHandler.contentTop() + yOffset + labelLineHeight, mLimitLinePaint);
            } else {
                mLimitLinePaint.setTextAlign(Align.RIGHT);
                canvas.drawText(label, position[0] - xOffset, mViewPortHandler.contentBottom() - yOffset, mLimitLinePaint);
            }
        }
    }
}
