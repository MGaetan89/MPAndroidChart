package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;

/**
 * Superclass of all render classes for the different data types (line, bar, ...).
 *
 * @author Philipp Jahoda
 */
public abstract class DataRenderer extends Renderer {
    /**
     * The animator object used to perform animations on the chart data.
     */
    protected ChartAnimator mAnimator;

    /**
     * Main paint object used for rendering.
     */
    protected Paint mRenderPaint;

    /**
     * Paint used for highlighting values.
     */
    protected Paint mHighlightPaint;

    protected Paint mDrawPaint;

    /**
     * Paint object for drawing values (text representing values of chart entries).
     */
    protected Paint mValuePaint;

    public DataRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);

        this.mAnimator = animator;

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Style.FILL);

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(0x3F3F3F);
        mValuePaint.setTextAlign(Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(0xFFBB73);
    }

    protected boolean isDrawingValuesAllowed(@NonNull ChartInterface chart) {
        ChartData data = chart.getData();
        return data != null && data.getEntryCount() < chart.getMaxVisibleCount() * mViewPortHandler.getScaleX();
    }

    /**
     * Returns the Paint object this renderer uses for drawing the values (value-text).
     */
    public Paint getPaintValues() {
        return mValuePaint;
    }

    /**
     * Returns the Paint object this renderer uses for drawing highlight indicators.
     */
    public Paint getPaintHighlight() {
        return mHighlightPaint;
    }

    /**
     * Returns the Paint object used for rendering.
     */
    public Paint getPaintRender() {
        return mRenderPaint;
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint object.
     *
     * @param set
     */
    protected void applyValueTextStyle(@NonNull IDataSet set) {
        mValuePaint.setTypeface(set.getValueTypeface());
        mValuePaint.setTextSize(set.getValueTextSize());
    }

    /**
     * Initializes the buffers used for rendering with a new size. Since this method performs memory
     * allocations, it should only be called if necessary.
     */
    public abstract void initBuffers();

    /**
     * Draws the actual data in form of lines, bars, ... depending on Renderer subclass.
     *
     * @param canvas
     */
    public abstract void drawData(Canvas canvas);

    /**
     * Loops over all Entrys and draws their values.
     *
     * @param canvas
     */
    public abstract void drawValues(Canvas canvas);

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param canvas       canvas
     * @param valueText
     * @param x            position
     * @param y            position
     * @param color
     */
    public abstract void drawValue(Canvas canvas, String valueText, float x, float y, int color);

    /**
     * Draws any kind of additional information (e.g. line-circles).
     *
     * @param canvas
     */
    public abstract void drawExtras(Canvas canvas);

    /**
     * Draws all highlight indicators for the values that are currently highlighted.
     *
     * @param canvas
     * @param highlights the highlighted values
     */
    public abstract void drawHighlighted(Canvas canvas, Highlight[] highlights);
}
