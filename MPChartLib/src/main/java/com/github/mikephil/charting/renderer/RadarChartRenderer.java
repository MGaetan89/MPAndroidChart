package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;

public class RadarChartRenderer extends LineRadarRenderer {
    protected RadarChart mChart;

    /**
     * paint for drawing the web
     */
    protected Paint mWebPaint;
    protected Paint mHighlightCirclePaint;

    @NonNull
    protected Path mDrawDataSetSurfacePathBuffer = new Path();

    @NonNull
    protected Path mDrawHighlightCirclePathBuffer = new Path();

    public RadarChartRenderer(RadarChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);

        mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(0xFFBB73);

        mWebPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWebPaint.setStyle(Paint.Style.STROKE);

        mHighlightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Paint getWebPaint() {
        return mWebPaint;
    }

    @Override
    public void initBuffers() {
        // Unused
    }

    @Override
    public void drawData(Canvas canvas) {
        RadarData radarData = mChart.getData();
        if (radarData == null) {
            return;
        }

        IRadarDataSet maxEntryCountSet = radarData.getMaxEntryCountSet();
        if (maxEntryCountSet == null) {
            return;
        }

        int mostEntries = maxEntryCountSet.getEntryCount();
        for (IRadarDataSet set : radarData.getDataSets()) {
            if (set.isVisible()) {
                drawDataSet(canvas, set, mostEntries);
            }
        }
    }

    /**
     * Draws the RadarDataSet.
     *
     * @param canvas
     * @param dataSet
     * @param mostEntries the entry count of the dataset with the most entries
     */
    protected void drawDataSet(@NonNull Canvas canvas, @NonNull IRadarDataSet dataSet, int mostEntries) {
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0f, 0f);
        Path surface = mDrawDataSetSurfacePathBuffer;
        surface.reset();

        boolean hasMovedToPoint = false;
        for (int j = 0; j < dataSet.getEntryCount(); j++) {
            mRenderPaint.setColor(dataSet.getColor(j));

            RadarEntry entry = dataSet.getEntryForIndex(j);

            Utils.getPosition(
                    center, (entry.getY() - mChart.getYChartMin()) * factor * phaseY,
                    sliceAngle * j * phaseX + mChart.getRotationAngle(), pOut
            );

            if (Float.isNaN(pOut.x)) {
                continue;
            }

            if (!hasMovedToPoint) {
                surface.moveTo(pOut.x, pOut.y);
                hasMovedToPoint = true;
            } else {
                surface.lineTo(pOut.x, pOut.y);
            }
        }

        if (dataSet.getEntryCount() > mostEntries) {
            // If this is not the largest set, draw a line to the center before closing
            surface.lineTo(center.x, center.y);
        }

        surface.close();

        if (dataSet.isDrawFilledEnabled()) {
            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(canvas, surface, drawable);
            } else {
                drawFilledPath(canvas, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }

        mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        mRenderPaint.setStyle(Paint.Style.STROKE);

        // Draw the line (only if filled is disabled or alpha is below 255)
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255) {
            canvas.drawPath(surface, mRenderPaint);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    @Override
    public void drawValues(Canvas canvas) {
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0f, 0f);
        MPPointF pIcon = MPPointF.getInstance(0f, 0f);

        float yOffset = Utils.convertDpToPixel(5f);
        for (int i = 0; i < mChart.getData().getDataSetCount(); i++) {
            IRadarDataSet dataSet = mChart.getData().getDataSetByIndex(i);
            if (dataSet == null || !shouldDrawValues(dataSet)) {
                continue;
            }

            // Apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

            for (int j = 0; j < dataSet.getEntryCount(); j++) {
                RadarEntry entry = dataSet.getEntryForIndex(j);

                Utils.getPosition(
                        center, (entry.getY() - mChart.getYChartMin()) * factor * phaseY,
                        sliceAngle * j * phaseX + mChart.getRotationAngle(), pOut
                );

                if (dataSet.isDrawValuesEnabled()) {
                    drawValue(
                            canvas, dataSet.getValueFormatter(), entry.getY(), entry,
                            i, pOut.x, pOut.y - yOffset, dataSet.getValueTextColor(j)
                    );
                }

                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                    Drawable icon = entry.getIcon();

                    Utils.getPosition(
                            center, (entry.getY()) * factor * phaseY + iconsOffset.y,
                            sliceAngle * j * phaseX + mChart.getRotationAngle(), pIcon
                    );

                    pIcon.y += iconsOffset.x;

                    Utils.drawImage(
                            canvas, icon, (int) pIcon.x, (int) pIcon.y,
                            icon.getIntrinsicWidth(), icon.getIntrinsicHeight()
                    );
                }
            }

            MPPointF.recycleInstance(iconsOffset);
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(pIcon);
    }

    @Override
    public void drawExtras(@NonNull Canvas canvas) {
        drawWeb(canvas);
    }

    protected void drawWeb(@NonNull Canvas canvas) {
        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();
        float rotationAngle = mChart.getRotationAngle();

        MPPointF center = mChart.getCenterOffsets();

        // Draw the web lines that come from the center
        mWebPaint.setStrokeWidth(mChart.getWebLineWidth());
        mWebPaint.setColor(mChart.getWebColor());
        mWebPaint.setAlpha(mChart.getWebAlpha());

        final int xIncrements = 1 + mChart.getSkipWebLineCount();
        int maxEntryCount = mChart.getData().getMaxEntryCountSet().getEntryCount();

        MPPointF p = MPPointF.getInstance(0f, 0f);
        for (int i = 0; i < maxEntryCount; i += xIncrements) {
            Utils.getPosition(
                    center, mChart.getYRange() * factor,
                    sliceAngle * i + rotationAngle, p
            );

            canvas.drawLine(center.x, center.y, p.x, p.y, mWebPaint);
        }

        MPPointF.recycleInstance(p);

        // Draw the inner-web
        mWebPaint.setStrokeWidth(mChart.getWebLineWidthInner());
        mWebPaint.setColor(mChart.getWebColorInner());
        mWebPaint.setAlpha(mChart.getWebAlpha());

        int labelCount = mChart.getYAxis().mEntryCount;

        MPPointF p1out = MPPointF.getInstance(0f, 0f);
        MPPointF p2out = MPPointF.getInstance(0f, 0f);
        for (int j = 0; j < labelCount; j++) {
            for (int i = 0; i < mChart.getData().getEntryCount(); i++) {
                float r = (mChart.getYAxis().mEntries[j] - mChart.getYChartMin()) * factor;

                Utils.getPosition(center, r, sliceAngle * i + rotationAngle, p1out);
                Utils.getPosition(center, r, sliceAngle * (i + 1) + rotationAngle, p2out);

                canvas.drawLine(p1out.x, p1out.y, p2out.x, p2out.y, mWebPaint);
            }
        }

        MPPointF.recycleInstance(p1out);
        MPPointF.recycleInstance(p2out);
    }

    @Override
    public void drawHighlighted(Canvas canvas, @NonNull Highlight[] highlights) {
        float sliceAngle = mChart.getSliceAngle();

        // Calculate the factor that is needed for transforming the value to pixels
        float factor = mChart.getFactor();

        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0, 0);

        RadarData radarData = mChart.getData();

        for (Highlight highlight : highlights) {
            IRadarDataSet set = radarData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            RadarEntry entry = set.getEntryForIndex((int) highlight.getX());
            if (!isInBoundsX(entry, set)) {
                continue;
            }

            float y = (entry.getY() - mChart.getYChartMin());

            Utils.getPosition(center,
                    y * factor * mAnimator.getPhaseY(),
                    sliceAngle * highlight.getX() * mAnimator.getPhaseX() + mChart.getRotationAngle(),
                    pOut
            );

            highlight.setDraw(pOut.x, pOut.y);

            // Draw the lines
            drawHighlightLines(canvas, pOut.x, pOut.y, set);

            if (set.isDrawHighlightCircleEnabled()) {
                if (!Float.isNaN(pOut.x) && !Float.isNaN(pOut.y)) {
                    int strokeColor = set.getHighlightCircleStrokeColor();
                    if (strokeColor == ColorTemplate.COLOR_NONE) {
                        strokeColor = set.getColor(0);
                    }

                    if (set.getHighlightCircleStrokeAlpha() < 255) {
                        strokeColor = ColorTemplate.colorWithAlpha(strokeColor, set.getHighlightCircleStrokeAlpha());
                    }

                    drawHighlightCircle(canvas,
                            pOut, set.getHighlightCircleInnerRadius(), set.getHighlightCircleOuterRadius(),
                            set.getHighlightCircleFillColor(), strokeColor, set.getHighlightCircleStrokeWidth()
                    );
                }
            }
        }

        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
    }

    public void drawHighlightCircle(
            @NonNull Canvas canvas, @NonNull MPPointF point, float innerRadius, float outerRadius,
            int fillColor, int strokeColor, float strokeWidth
    ) {
        canvas.save();

        outerRadius = Utils.convertDpToPixel(outerRadius);
        innerRadius = Utils.convertDpToPixel(innerRadius);

        if (fillColor != ColorTemplate.COLOR_NONE) {
            Path path = mDrawHighlightCirclePathBuffer;
            path.reset();
            path.addCircle(point.x, point.y, outerRadius, Path.Direction.CW);
            if (innerRadius > 0f) {
                path.addCircle(point.x, point.y, innerRadius, Path.Direction.CCW);
            }

            mHighlightCirclePaint.setColor(fillColor);
            mHighlightCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, mHighlightCirclePaint);
        }

        if (strokeColor != ColorTemplate.COLOR_NONE) {
            mHighlightCirclePaint.setColor(strokeColor);
            mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
            mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
            canvas.drawCircle(point.x, point.y, outerRadius, mHighlightCirclePaint);
        }

        canvas.restore();
    }
}
