package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.List;

public class PieChartRenderer extends DataRenderer {
    protected PieChart mChart;

    /**
     * Paint for the hole in the center of the pie chart and the transparent circle.
     */
    protected Paint mHolePaint;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;

    /**
     * Paint object for the text that can be displayed in the center of the chart.
     */
    private TextPaint mCenterTextPaint;

    /**
     * Paint object used for drawing the slice-text.
     */
    private Paint mEntryLabelsPaint;

    private StaticLayout mCenterTextLayout;
    private CharSequence mCenterTextLastValue;

    @NonNull
    private final RectF mCenterTextLastBounds = new RectF();

    @NonNull
    private final RectF[] mRectBuffer = {new RectF(), new RectF(), new RectF()};

    /**
     * Bitmap for drawing the center hole.
     */
    protected WeakReference<Bitmap> mDrawBitmap;

    protected Canvas mBitmapCanvas;

    @NonNull
    protected Path mDrawCenterTextPathBuffer = new Path();

    @NonNull
    protected RectF mDrawHighlightedRectF = new RectF();

    @NonNull
    private final Path mPathBuffer = new Path();

    @NonNull
    private final RectF mInnerRectBuffer = new RectF();

    @NonNull
    private final Path mHoleCirclePath = new Path();

    public PieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);

        mChart = chart;

        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setColor(Color.WHITE);
        mHolePaint.setStyle(Style.FILL);

        mTransparentCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTransparentCirclePaint.setColor(Color.WHITE);
        mTransparentCirclePaint.setStyle(Style.FILL);
        mTransparentCirclePaint.setAlpha(105);

        mCenterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setColor(Color.BLACK);
        mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12f));

        mValuePaint.setTextSize(Utils.convertDpToPixel(13f));
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Align.CENTER);

        mEntryLabelsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEntryLabelsPaint.setColor(Color.WHITE);
        mEntryLabelsPaint.setTextAlign(Align.CENTER);
        mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13f));

        mValueLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueLinePaint.setStyle(Style.STROKE);
    }

    public Paint getPaintHole() {
        return mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return mEntryLabelsPaint;
    }

    @Override
    public void initBuffers() {
        // Unused
    }

    @Override
    public void drawData(Canvas canvas) {
        int width = (int) mViewPortHandler.getChartWidth();
        int height = (int) mViewPortHandler.getChartHeight();

        Bitmap drawBitmap = mDrawBitmap == null ? null : mDrawBitmap.get();
        if (drawBitmap == null || drawBitmap.getWidth() != width || drawBitmap.getHeight() != height) {
            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                mDrawBitmap = new WeakReference<>(drawBitmap);
                mBitmapCanvas = new Canvas(drawBitmap);
            } else {
                return;
            }
        }

        drawBitmap.eraseColor(Color.TRANSPARENT);

        PieData pieData = mChart.getData();
        if (pieData != null) {
            for (IPieDataSet set : pieData.getDataSets()) {
                if (set.isVisible() && set.getEntryCount() > 0) {
                    drawDataSet(canvas, set);
                }
            }
        }
    }

    protected float calculateMinimumRadiusForSpacedSlice(
            @NonNull MPPointF center, float radius, float angle, float arcStartPointX,
            float arcStartPointY, float startAngle, float sweepAngle
    ) {
        final float angleMiddle = startAngle + sweepAngle / 2f;

        // Other point of the arc
        float arcEndPointX = center.x + radius * (float) Math.cos((startAngle + sweepAngle) * Utils.FDEG2RAD);
        float arcEndPointY = center.y + radius * (float) Math.sin((startAngle + sweepAngle) * Utils.FDEG2RAD);

        // Middle point on the arc
        float arcMidPointX = center.x + radius * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
        float arcMidPointY = center.y + radius * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);

        // This is the base of the contained triangle
        double basePointsDistance = Math.sqrt(Math.pow(arcEndPointX - arcStartPointX, 2) + Math.pow(arcEndPointY - arcStartPointY, 2));

        // After reducing space from both sides of the "slice", the angle of the contained triangle
        // should stay the same. So let's find out the height of that triangle.
        float containedTriangleHeight = (float) (basePointsDistance / 2f * Math.tan((180f - angle) / 2f * Utils.DEG2RAD));

        // Now we subtract that from the radius
        float spacedRadius = radius - containedTriangleHeight;

        // And now subtract the height of the arc that's between the triangle and the outer circle
        spacedRadius -= Math.sqrt(Math.pow(arcMidPointX - (arcEndPointX + arcStartPointX) / 2f, 2d) + Math.pow(arcMidPointY - (arcEndPointY + arcStartPointY) / 2f, 2d));

        return spacedRadius;
    }

    /**
     * Calculates the sliceSpace to use based on visible values and their size compared to the set
     * sliceSpace.
     *
     * @param dataSet
     * @return
     */
    protected float getSliceSpace(@NonNull IPieDataSet dataSet) {
        if (!dataSet.isAutomaticallyDisableSliceSpacingEnabled()) {
            return dataSet.getSliceSpace();
        }

        float spaceSizeRatio = dataSet.getSliceSpace() / mViewPortHandler.getSmallestContentExtension();
        float minValueRatio = dataSet.getYMin() / mChart.getData().getYValueSum() * 2;

        return spaceSizeRatio > minValueRatio ? 0f : dataSet.getSliceSpace();
    }

    protected void drawDataSet(Canvas canvas, @NonNull IPieDataSet dataSet) {
        float angle = 0;
        float rotationAngle = mChart.getRotationAngle();
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        final RectF circleBox = mChart.getCircleBox();

        final int entryCount = dataSet.getEntryCount();
        final float[] drawAngles = mChart.getDrawAngles();
        final MPPointF center = mChart.getCenterCircleBox();
        final float radius = mChart.getRadius();
        final boolean drawInnerArc = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawInnerArc ? radius * (mChart.getHoleRadius() / 100f) : 0f;

        int visibleAngleCount = 0;
        for (int j = 0; j < entryCount; j++) {
            // Draw only if the value is greater than zero
            if ((Math.abs(dataSet.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON)) {
                visibleAngleCount++;
            }
        }

        final float sliceSpace = visibleAngleCount <= 1 ? 0f : getSliceSpace(dataSet);
        for (int j = 0; j < entryCount; j++) {
            float sliceAngle = drawAngles[j];
            float innerRadius = userInnerRadius;

            Entry entry = dataSet.getEntryForIndex(j);

            // Draw only if the value is greater than zero
            if (Math.abs(entry.getY()) > Utils.FLOAT_EPSILON) {
                if (!mChart.needsHighlight(j)) {
                    final boolean accountForSliceSpacing = sliceSpace > 0f && sliceAngle <= 180f;

                    mRenderPaint.setColor(dataSet.getColor(j));

                    final float sliceSpaceAngleOuter = visibleAngleCount == 1 ? 0f : sliceSpace / (Utils.FDEG2RAD * radius);
                    final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.f) * phaseY;
                    float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                    if (sweepAngleOuter < 0f) {
                        sweepAngleOuter = 0f;
                    }

                    mPathBuffer.reset();

                    float arcStartPointX = center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD);
                    float arcStartPointY = center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD);

                    if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                        // Android is doing "mod 360"
                        mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
                    } else {
                        mPathBuffer.moveTo(arcStartPointX, arcStartPointY);
                        mPathBuffer.arcTo(circleBox, startAngleOuter, sweepAngleOuter);
                    }

                    // API < 21 does not receive floats in addArc, but a RectF
                    mInnerRectBuffer.set(
                            center.x - innerRadius,
                            center.y - innerRadius,
                            center.x + innerRadius,
                            center.y + innerRadius
                    );

                    if (drawInnerArc && (innerRadius > 0f || accountForSliceSpacing)) {
                        if (accountForSliceSpacing) {
                            float minSpacedRadius = calculateMinimumRadiusForSpacedSlice(
                                    center, radius, sliceAngle * phaseY,
                                    arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter
                            );

                            if (minSpacedRadius < 0f) {
                                minSpacedRadius = -minSpacedRadius;
                            }

                            innerRadius = Math.max(innerRadius, minSpacedRadius);
                        }

                        final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0f ? 0f : sliceSpace / (Utils.FDEG2RAD * innerRadius);
                        final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2f) * phaseY;
                        float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                        if (sweepAngleInner < 0f) {
                            sweepAngleInner = 0f;
                        }

                        final float endAngleInner = startAngleInner + sweepAngleInner;
                        if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                            // Android is doing "mod 360"
                            mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                        } else {
                            mPathBuffer.lineTo(
                                    center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                                    center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD)
                            );

                            mPathBuffer.arcTo(mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                        }
                    } else {
                        if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                            if (accountForSliceSpacing) {
                                float angleMiddle = startAngleOuter + sweepAngleOuter / 2f;

                                float sliceSpaceOffset = calculateMinimumRadiusForSpacedSlice(
                                        center, radius, sliceAngle * phaseY, arcStartPointX,
                                        arcStartPointY, startAngleOuter, sweepAngleOuter
                                );

                                float arcEndPointX = center.x + sliceSpaceOffset * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                                float arcEndPointY = center.y + sliceSpaceOffset * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);

                                mPathBuffer.lineTo(arcEndPointX, arcEndPointY);
                            } else {
                                mPathBuffer.lineTo(center.x, center.y);
                            }
                        }
                    }

                    mPathBuffer.close();

                    mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint);
                }
            }

            angle += sliceAngle * phaseX;
        }

        MPPointF.recycleInstance(center);
    }

    @Override
    public void drawValues(@NonNull Canvas canvas) {
        MPPointF center = mChart.getCenterCircleBox();

        // Get whole the radius
        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        final float holeRadiusPercent = mChart.getHoleRadius() / 100f;
        float labelRadiusOffset = radius / 10f * 3.6f;

        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
        }

        final float labelRadius = radius - labelRadiusOffset;

        PieData data = mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();

        float yValueSum = data.getYValueSum();

        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();

        float angle;
        int xIndex = 0;

        canvas.save();

        float offset = Utils.convertDpToPixel(5f);

        for (int i = 0; i < dataSets.size(); i++) {
            IPieDataSet dataSet = dataSets.get(i);
            final boolean drawValues = dataSet.isDrawValuesEnabled();
            if (!drawValues && !drawEntryLabels) {
                continue;
            }

            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();

            // Apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q") + Utils.convertDpToPixel(4f);

            IValueFormatter formatter = dataSet.getValueFormatter();

            int entryCount = dataSet.getEntryCount();

            mValueLinePaint.setColor(dataSet.getValueLineColor());
            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));

            final float sliceSpace = getSliceSpace(dataSet);

            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

            for (int j = 0; j < entryCount; j++) {
                PieEntry entry = dataSet.getEntryForIndex(j);

                if (xIndex == 0) {
                    angle = 0f;
                } else {
                    angle = absoluteAngles[xIndex - 1] * phaseX;
                }

                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);

                // Offset needed to center the drawn text in the slice
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f;

                angle = angle + angleOffset;

                final float transformedAngle = rotationAngle + angle * phaseY;

                float value = mChart.isUsePercentValuesEnabled() ? entry.getY() / yValueSum * 100f : entry.getY();

                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);

                final boolean drawXOutside = drawEntryLabels && xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues && yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawXInside = drawEntryLabels && xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                final boolean drawYInside = drawValues && yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;

                if (drawXOutside || drawYOutside) {
                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100f;

                    float pt2x;
                    float pt2y;
                    float labelPtx;
                    float labelPty;
                    float line1Radius;

                    if (mChart.isDrawHoleEnabled()) {
                        line1Radius = (radius - (radius * holeRadiusPercent)) * valueLinePart1OffsetPercentage + (radius * holeRadiusPercent);
                    } else {
                        line1Radius = radius * valueLinePart1OffsetPercentage;
                    }

                    final float polyline2Width = dataSet.isValueLineVariableLength()
                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(transformedAngle * Utils.FDEG2RAD))
                            : labelRadius * valueLineLength2;

                    final float pt0x = line1Radius * sliceXBase + center.x;
                    final float pt0y = line1Radius * sliceYBase + center.y;
                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;

                    if (transformedAngle % 360f >= 90f && transformedAngle % 360f <= 270f) {
                        pt2x = pt1x - polyline2Width;
                        pt2y = pt1y;

                        mValuePaint.setTextAlign(Align.RIGHT);

                        if (drawXOutside) {
                            mEntryLabelsPaint.setTextAlign(Align.RIGHT);
                        }

                        labelPtx = pt2x - offset;
                        labelPty = pt2y;
                    } else {
                        pt2x = pt1x + polyline2Width;
                        pt2y = pt1y;
                        mValuePaint.setTextAlign(Align.LEFT);

                        if (drawXOutside) {
                            mEntryLabelsPaint.setTextAlign(Align.LEFT);
                        }

                        labelPtx = pt2x + offset;
                        labelPty = pt2y;
                    }

                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
                        if (dataSet.isUsingSliceColorAsValueLineColor()) {
                            mValueLinePaint.setColor(dataSet.getColor(j));
                        }

                        canvas.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                        canvas.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                    }

                    // Draw everything, depending on settings
                    if (drawXOutside && drawYOutside) {
                        drawValue(
                                canvas, formatter, value, entry, 0,
                                labelPtx, labelPty, dataSet.getValueTextColor(j)
                        );

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(canvas, entry.getLabel(), labelPtx, labelPty + lineHeight);
                        }
                    } else if (drawXOutside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(canvas, entry.getLabel(), labelPtx, labelPty + lineHeight / 2f);
                        }
                    } else if (drawYOutside) {
                        drawValue(canvas, formatter, value, entry, 0, labelPtx, labelPty + lineHeight / 2f, dataSet.getValueTextColor(j));
                    }
                }

                if (drawXInside || drawYInside) {
                    // Calculate the text position
                    float x = labelRadius * sliceXBase + center.x;
                    float y = labelRadius * sliceYBase + center.y;

                    mValuePaint.setTextAlign(Align.CENTER);

                    // Draw everything, depending on settings
                    if (drawXInside && drawYInside) {
                        drawValue(canvas, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(canvas, entry.getLabel(), x, y + lineHeight);
                        }
                    } else if (drawXInside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(canvas, entry.getLabel(), x, y + lineHeight / 2f);
                        }
                    } else if (drawYInside) {

                        drawValue(canvas, formatter, value, entry, 0, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
                    }
                }

                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                    Drawable icon = entry.getIcon();

                    float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                    float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                    y += iconsOffset.x;

                    Utils.drawImage(
                            canvas, icon, (int) x, (int) y,
                            icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                    );
                }

                xIndex++;
            }

            MPPointF.recycleInstance(iconsOffset);
        }

        MPPointF.recycleInstance(center);
        canvas.restore();
    }

    /**
     * Draws an entry label at the specified position.
     *
     * @param canvas
     * @param label
     * @param x
     * @param y
     */
    protected void drawEntryLabel(@NonNull Canvas canvas, String label, float x, float y) {
        canvas.drawText(label, x, y, mEntryLabelsPaint);
    }

    @Override
    public void drawExtras(@NonNull Canvas canvas) {
        drawHole(canvas);
        canvas.drawBitmap(mDrawBitmap.get(), 0, 0, null);
        drawCenterText(canvas);
    }

    /**
     * Draws the hole in the center of the chart and the transparent circle/hole.
     */
    protected void drawHole(Canvas canvas) {
        if (mChart.isDrawHoleEnabled() && mBitmapCanvas != null) {
            float radius = mChart.getRadius();
            float holeRadius = radius * (mChart.getHoleRadius() / 100);
            MPPointF center = mChart.getCenterCircleBox();

            if (Color.alpha(mHolePaint.getColor()) > 0) {
                // Draw the hole-circle
                mBitmapCanvas.drawCircle(center.x, center.y, holeRadius, mHolePaint);
            }

            // Only draw the circle if it can be seen (not covered by the hole)
            if (Color.alpha(mTransparentCirclePaint.getColor()) > 0 && mChart.getTransparentCircleRadius() > mChart.getHoleRadius()) {
                int alpha = mTransparentCirclePaint.getAlpha();
                float secondHoleRadius = radius * (mChart.getTransparentCircleRadius() / 100);

                mTransparentCirclePaint.setAlpha((int) ((float) alpha * mAnimator.getPhaseX() * mAnimator.getPhaseY()));

                // Draw the transparent-circle
                mHoleCirclePath.reset();
                mHoleCirclePath.addCircle(center.x, center.y, secondHoleRadius, Path.Direction.CW);
                mHoleCirclePath.addCircle(center.x, center.y, holeRadius, Path.Direction.CCW);
                mBitmapCanvas.drawPath(mHoleCirclePath, mTransparentCirclePaint);

                // Reset alpha
                mTransparentCirclePaint.setAlpha(alpha);
            }

            MPPointF.recycleInstance(center);
        }
    }

    /**
     * Draws the description text in the center of the pie chart makes most sense when center-hole
     * is enabled.
     */
    protected void drawCenterText(@NonNull Canvas canvas) {
        CharSequence centerText = mChart.getCenterText();
        if (mChart.isDrawCenterTextEnabled() && centerText != null) {
            MPPointF center = mChart.getCenterCircleBox();
            MPPointF offset = mChart.getCenterTextOffset();

            float x = center.x + offset.x;
            float y = center.y + offset.y;

            float innerRadius = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled()
                    ? mChart.getRadius() * (mChart.getHoleRadius() / 100f)
                    : mChart.getRadius();

            RectF holeRect = mRectBuffer[0];
            holeRect.left = x - innerRadius;
            holeRect.top = y - innerRadius;
            holeRect.right = x + innerRadius;
            holeRect.bottom = y + innerRadius;
            RectF boundingRect = mRectBuffer[1];
            boundingRect.set(holeRect);

            float radiusPercent = mChart.getCenterTextRadiusPercent() / 100f;
            if (radiusPercent > 0f) {
                boundingRect.inset(
                        (boundingRect.width() - boundingRect.width() * radiusPercent) / 2f,
                        (boundingRect.height() - boundingRect.height() * radiusPercent) / 2f
                );
            }

            if (!centerText.equals(mCenterTextLastValue) || !boundingRect.equals(mCenterTextLastBounds)) {
                // Next time we won't recalculate StaticLayout...
                mCenterTextLastBounds.set(boundingRect);
                mCenterTextLastValue = centerText;

                float width = mCenterTextLastBounds.width();

                // If width is 0, it will crash. Always have a minimum of 1
                mCenterTextLayout = new StaticLayout(
                        centerText, 0, centerText.length(), mCenterTextPaint,
                        (int) Math.max(Math.ceil(width), 1d), Layout.Alignment.ALIGN_CENTER, 1f,
                        0f, false
                );
            }

            float layoutHeight = mCenterTextLayout.getHeight();

            canvas.save();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Path path = mDrawCenterTextPathBuffer;
                path.reset();
                path.addOval(holeRect, Path.Direction.CW);
                canvas.clipPath(path);
            }

            canvas.translate(boundingRect.left, boundingRect.top + (boundingRect.height() - layoutHeight) / 2f);
            mCenterTextLayout.draw(canvas);

            canvas.restore();

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(offset);
        }
    }

    @Override
    public void drawHighlighted(Canvas canvas, @NonNull Highlight[] highlights) {
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float angle;
        float rotationAngle = mChart.getRotationAngle();

        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();
        final MPPointF center = mChart.getCenterCircleBox();
        final float radius = mChart.getRadius();
        final boolean drawInnerArc = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawInnerArc ? radius * (mChart.getHoleRadius() / 100f) : 0f;

        final RectF highlightedCircleBox = mDrawHighlightedRectF;
        highlightedCircleBox.set(0, 0, 0, 0);

        for (Highlight highlight : highlights) {
            // Get the index to highlight
            int index = (int) highlight.getX();
            if (index >= drawAngles.length) {
                continue;
            }

            IPieDataSet set = mChart.getData().getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            final int entryCount = set.getEntryCount();
            int visibleAngleCount = 0;
            for (int j = 0; j < entryCount; j++) {
                // Draw only if the value is greater than zero
                if (Math.abs(set.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON) {
                    visibleAngleCount++;
                }
            }

            if (index == 0) {
                angle = 0f;
            } else {
                angle = absoluteAngles[index - 1] * phaseX;
            }

            final float sliceSpace = visibleAngleCount <= 1 ? 0f : set.getSliceSpace();

            float sliceAngle = drawAngles[index];
            float innerRadius = userInnerRadius;

            float shift = set.getSelectionShift();
            final float highlightedRadius = radius + shift;
            highlightedCircleBox.set(mChart.getCircleBox());
            highlightedCircleBox.inset(-shift, -shift);

            final boolean accountForSliceSpacing = sliceSpace > 0f && sliceAngle <= 180f;

            mRenderPaint.setColor(set.getColor(index));

            final float sliceSpaceAngleOuter = visibleAngleCount == 1 ? 0f : sliceSpace / (Utils.FDEG2RAD * radius);
            final float sliceSpaceAngleShifted = visibleAngleCount == 1 ? 0f : sliceSpace / (Utils.FDEG2RAD * highlightedRadius);

            final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2f) * phaseY;
            float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
            if (sweepAngleOuter < 0f) {
                sweepAngleOuter = 0f;
            }

            final float startAngleShifted = rotationAngle + (angle + sliceSpaceAngleShifted / 2f) * phaseY;
            float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
            if (sweepAngleShifted < 0f) {
                sweepAngleShifted = 0f;
            }

            mPathBuffer.reset();

            if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                // Android is doing "mod 360"
                mPathBuffer.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
            } else {
                mPathBuffer.moveTo(
                        center.x + highlightedRadius * (float) Math.cos(startAngleShifted * Utils.FDEG2RAD),
                        center.y + highlightedRadius * (float) Math.sin(startAngleShifted * Utils.FDEG2RAD)
                );

                mPathBuffer.arcTo(highlightedCircleBox, startAngleShifted, sweepAngleShifted);
            }

            float sliceSpaceRadius = 0f;
            if (accountForSliceSpacing) {
                sliceSpaceRadius = calculateMinimumRadiusForSpacedSlice(
                        center, radius, sliceAngle * phaseY,
                        center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD),
                        center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD),
                        startAngleOuter, sweepAngleOuter
                );
            }

            // API < 21 does not receive floats in addArc, but a RectF
            mInnerRectBuffer.set(
                    center.x - innerRadius, center.y - innerRadius,
                    center.x + innerRadius, center.y + innerRadius
            );

            if (drawInnerArc && (innerRadius > 0f || accountForSliceSpacing)) {
                if (accountForSliceSpacing) {
                    float minSpacedRadius = sliceSpaceRadius;
                    if (minSpacedRadius < 0f) {
                        minSpacedRadius = -minSpacedRadius;
                    }

                    innerRadius = Math.max(innerRadius, minSpacedRadius);
                }

                final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0f ? 0f : sliceSpace / (Utils.FDEG2RAD * innerRadius);
                final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2f) * phaseY;
                float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                if (sweepAngleInner < 0f) {
                    sweepAngleInner = 0f;
                }

                final float endAngleInner = startAngleInner + sweepAngleInner;
                if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                    // Android is doing "mod 360"
                    mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                } else {
                    mPathBuffer.lineTo(
                            center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                            center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD)
                    );

                    mPathBuffer.arcTo(mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                }
            } else {
                if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                    if (accountForSliceSpacing) {
                        final float angleMiddle = startAngleOuter + sweepAngleOuter / 2f;
                        final float arcEndPointX = center.x + sliceSpaceRadius * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                        final float arcEndPointY = center.y + sliceSpaceRadius * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);

                        mPathBuffer.lineTo(arcEndPointX, arcEndPointY);
                    } else {
                        mPathBuffer.lineTo(center.x, center.y);
                    }
                }
            }

            mPathBuffer.close();

            mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint);
        }

        MPPointF.recycleInstance(center);
    }

    /**
     * This gives all pie-slices a rounded edge.
     *
     * @param canvas
     */
    protected void drawRoundedSlices(Canvas canvas) {
        if (!mChart.isDrawRoundedSlicesEnabled()) {
            return;
        }

        IPieDataSet dataSet = mChart.getData().getDataSet();
        if (!dataSet.isVisible()) {
            return;
        }

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        MPPointF center = mChart.getCenterCircleBox();
        float r = mChart.getRadius();

        // Calculate the radius of the "slice-circle"
        float circleRadius = (r - (r * mChart.getHoleRadius() / 100f)) / 2f;

        float[] drawAngles = mChart.getDrawAngles();
        float angle = mChart.getRotationAngle();

        for (int j = 0; j < dataSet.getEntryCount(); j++) {
            float sliceAngle = drawAngles[j];

            Entry entry = dataSet.getEntryForIndex(j);

            // Draw only if the value is greater than zero
            if (Math.abs(entry.getY()) > Utils.FLOAT_EPSILON) {
                float x = (float) ((r - circleRadius) * Math.cos(Math.toRadians((angle + sliceAngle) * phaseY)) + center.x);
                float y = (float) ((r - circleRadius) * Math.sin(Math.toRadians((angle + sliceAngle) * phaseY)) + center.y);

                mRenderPaint.setColor(dataSet.getColor(j));
                mBitmapCanvas.drawCircle(x, y, circleRadius, mRenderPaint);
            }

            angle += sliceAngle * phaseX;
        }

        MPPointF.recycleInstance(center);
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
}
