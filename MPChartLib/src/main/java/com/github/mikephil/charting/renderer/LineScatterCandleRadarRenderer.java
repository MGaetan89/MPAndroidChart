package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Philipp Jahoda
 */
public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {
    /**
     * Path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes).
     */
    @NonNull
    private final Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param canvas
     * @param x      x-position of the highlight line intersection
     * @param y      y-position of the highlight line intersection
     * @param set    the currently drawn dataset
     */
    protected void drawHighlightLines(@NonNull Canvas canvas, float x, float y, @NonNull ILineScatterCandleRadarDataSet set) {
        // Set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // Draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        // Draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {
            // Create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            canvas.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // Draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            // Create horizontal path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);

            canvas.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }
}
