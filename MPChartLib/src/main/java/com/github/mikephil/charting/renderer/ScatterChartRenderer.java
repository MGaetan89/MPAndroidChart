package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

import androidx.annotation.NonNull;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
    protected ScatterDataProvider mChart;

    float[] mPixelBuffer = new float[2];

    public ScatterChartRenderer(ScatterDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void initBuffers() {
        // Unused
    }

    @Override
    public void drawData(Canvas canvas) {
        ScatterData scatterData = mChart.getScatterData();
        if (scatterData == null) {
            return;
        }

        for (IScatterDataSet set : scatterData.getDataSets()) {
            if (set.isVisible()) {
                drawDataSet(canvas, set);
            }
        }
    }

    protected void drawDataSet(Canvas canvas, @NonNull IScatterDataSet dataSet) {
        if (dataSet.getEntryCount() < 1) {
            return;
        }

        ViewPortHandler viewPortHandler = mViewPortHandler;
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        IShapeRenderer renderer = dataSet.getShapeRenderer();

        float phaseY = mAnimator.getPhaseY();

        int max = (int) (Math.min(Math.ceil((float) dataSet.getEntryCount() * mAnimator.getPhaseX()), (float) dataSet.getEntryCount()));

        for (int i = 0; i < max; i++) {
            Entry entry = dataSet.getEntryForIndex(i);

            mPixelBuffer[0] = entry.getX();
            mPixelBuffer[1] = entry.getY() * phaseY;

            trans.pointValuesToPixel(mPixelBuffer);

            if (!viewPortHandler.isInBoundsRight(mPixelBuffer[0])) {
                break;
            }

            if (!viewPortHandler.isInBoundsLeft(mPixelBuffer[0]) || !viewPortHandler.isInBoundsY(mPixelBuffer[1])) {
                continue;
            }

            mRenderPaint.setColor(dataSet.getColor(i / 2));
            renderer.renderShape(canvas, dataSet, mViewPortHandler, mPixelBuffer[0], mPixelBuffer[1], mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas canvas) {
        // If values are drawn
        if (isDrawingValuesAllowed(mChart)) {
            ScatterData scatterData = mChart.getScatterData();
            if (scatterData == null) {
                return;
            }

            List<IScatterDataSet> dataSets = scatterData.getDataSets();
            for (int i = 0; i < scatterData.getDataSetCount(); i++) {
                IScatterDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1) {
                    continue;
                }

                // Apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                mXBounds.set(mChart, dataSet);

                float[] positions = mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesScatter(dataSet, mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

                float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {
                    if (!mViewPortHandler.isInBoundsRight(positions[j])) {
                        break;
                    }

                    // Make sure the lines don't do shitty things outside bounds
                    if ((!mViewPortHandler.isInBoundsLeft(positions[j]) || !mViewPortHandler.isInBoundsY(positions[j + 1]))) {
                        continue;
                    }

                    Entry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(
                                canvas, dataSet.getValueFormatter(), entry.getY(), entry,
                                i, positions[j], positions[j + 1] - shapeSize, dataSet.getValueTextColor(j / 2 + mXBounds.min)
                        );
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                canvas, icon, (int) (positions[j] + iconsOffset.x), (int) (positions[j + 1] + iconsOffset.y),
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
    public void drawHighlighted(Canvas canvas, @NonNull Highlight[] highlights) {
        ScatterData scatterData = mChart.getScatterData();
        if (scatterData == null) {
            return;
        }

        for (Highlight highlight : highlights) {
            IScatterDataSet set = scatterData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            final Entry entry = set.getEntryForXValue(highlight.getX(), highlight.getY());
            if (!isInBoundsX(entry, set)) {
                continue;
            }

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entry.getX(), entry.getY() * mAnimator.getPhaseY());

            highlight.setDraw((float) pix.x, (float) pix.y);

            // Draw the lines
            drawHighlightLines(canvas, (float) pix.x, (float) pix.y, set);
        }
    }
}
