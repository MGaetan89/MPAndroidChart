package com.github.mikephil.charting.data;

import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * DataSet for the CandleStickChart.
 *
 * @author Philipp Jahoda
 */
public class CandleDataSet extends LineScatterCandleRadarDataSet<CandleEntry> implements ICandleDataSet {
    /**
     * The width of the shadow of the candle.
     */
    private float mShadowWidth = 3f;

    /**
     * Should the candle bars show? when false, only "ticks" will show
     */
    private boolean mShowCandleBar = true;

    /**
     * The space between the candle entries.
     */
    private float mBarSpace = 0.1f;

    /**
     * Use candle color for the shadow.
     */
    private boolean mShadowColorSameAsCandle = false;

    /**
     * Paint style when open < close. Increasing candlesticks are traditionally hollow.
     */
    protected Paint.Style mIncreasingPaintStyle = Paint.Style.STROKE;

    /**
     * Paint style when open > close. Decreasing candlesticks are traditionally filled.
     */
    protected Paint.Style mDecreasingPaintStyle = Paint.Style.FILL;

    /**
     * Color for open == close.
     */
    @ColorInt
    protected int mNeutralColor = ColorTemplate.COLOR_SKIP;

    /**
     * Color for open < close.
     */
    @ColorInt
    protected int mIncreasingColor = ColorTemplate.COLOR_SKIP;

    /**
     * Color for open > close.
     */
    @ColorInt
    protected int mDecreasingColor = ColorTemplate.COLOR_SKIP;

    /**
     * Shadow line color, set -1 for backward compatibility and uses default color.
     */
    @ColorInt
    protected int mShadowColor = ColorTemplate.COLOR_SKIP;

    public CandleDataSet(List<CandleEntry> yValues, String label) {
        super(yValues, label);
    }

    @NonNull
    @Override
    public DataSet<CandleEntry> copy() {
        List<CandleEntry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        CandleDataSet copied = new CandleDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(CandleDataSet candleDataSet) {
        super.copy(candleDataSet);
        candleDataSet.mShadowWidth = mShadowWidth;
        candleDataSet.mShowCandleBar = mShowCandleBar;
        candleDataSet.mBarSpace = mBarSpace;
        candleDataSet.mShadowColorSameAsCandle = mShadowColorSameAsCandle;
        candleDataSet.mHighLightColor = mHighLightColor;
        candleDataSet.mIncreasingPaintStyle = mIncreasingPaintStyle;
        candleDataSet.mDecreasingPaintStyle = mDecreasingPaintStyle;
        candleDataSet.mNeutralColor = mNeutralColor;
        candleDataSet.mIncreasingColor = mIncreasingColor;
        candleDataSet.mDecreasingColor = mDecreasingColor;
        candleDataSet.mShadowColor = mShadowColor;
    }

    @Override
    protected void calcMinMax(@NonNull CandleEntry entry) {
        if (entry.getLow() < mYMin) {
            mYMin = entry.getLow();
        }

        if (entry.getHigh() > mYMax) {
            mYMax = entry.getHigh();
        }

        calcMinMaxX(entry);
    }

    @Override
    protected void calcMinMaxY(@NonNull CandleEntry entry) {
        if (entry.getHigh() < mYMin) {
            mYMin = entry.getHigh();
        }

        if (entry.getHigh() > mYMax) {
            mYMax = entry.getHigh();
        }

        if (entry.getLow() < mYMin) {
            mYMin = entry.getLow();
        }

        if (entry.getLow() > mYMax) {
            mYMax = entry.getLow();
        }
    }

    /**
     * Sets the space that is left out on the left and right side of each candle. Max 0.45f, min 0f.
     *
     * @param space
     */
    public void setBarSpace(float space) {
        if (space < 0f) {
            space = 0f;
        }

        if (space > 0.45f) {
            space = 0.45f;
        }

        mBarSpace = space;
    }

    @Override
    public float getBarSpace() {
        return mBarSpace;
    }

    /**
     * Sets the width of the candle-shadow-line in pixels.
     *
     * @param width
     */
    public void setShadowWidth(float width) {
        mShadowWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public float getShadowWidth() {
        return mShadowWidth;
    }

    /**
     * Sets whether the candle bars should show?
     *
     * @param showCandleBar
     */
    public void setShowCandleBar(boolean showCandleBar) {
        mShowCandleBar = showCandleBar;
    }

    @Override
    public boolean getShowCandleBar() {
        return mShowCandleBar;
    }

    // TODO
    /**
     * It is necessary to implement ColorsList class that will encapsulate colors list functionality,
     * because it's wrong to copy paste setColor, addColor, ... resetColors each time when we want to
     * add a coloring options for one of objects.
     *
     * @author Mesrop
     */

    /**
     * Sets the one and ONLY color that should be used for this DataSet when open == close.
     *
     * @param color
     */
    public void setNeutralColor(@ColorInt int color) {
        mNeutralColor = color;
    }

    @ColorInt
    @Override
    public int getNeutralColor() {
        return mNeutralColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when open <= close.
     *
     * @param color
     */
    public void setIncreasingColor(@ColorInt int color) {
        mIncreasingColor = color;
    }

    @ColorInt
    @Override
    public int getIncreasingColor() {
        return mIncreasingColor;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet when open > close.
     *
     * @param color
     */
    public void setDecreasingColor(@ColorInt int color) {
        mDecreasingColor = color;
    }

    @ColorInt
    @Override
    public int getDecreasingColor() {
        return mDecreasingColor;
    }

    @Override
    public Paint.Style getIncreasingPaintStyle() {
        return mIncreasingPaintStyle;
    }

    /**
     * Sets paint style when open < close.
     *
     * @param paintStyle
     */
    public void setIncreasingPaintStyle(Paint.Style paintStyle) {
        this.mIncreasingPaintStyle = paintStyle;
    }

    @Override
    public Paint.Style getDecreasingPaintStyle() {
        return mDecreasingPaintStyle;
    }

    /**
     * Sets paint style when open > close.
     *
     * @param decreasingPaintStyle
     */
    public void setDecreasingPaintStyle(Paint.Style decreasingPaintStyle) {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }

    @ColorInt
    @Override
    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Sets shadow color for all entries.
     *
     * @param shadowColor
     */
    public void setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    @Override
    public boolean getShadowColorSameAsCandle() {
        return mShadowColorSameAsCandle;
    }

    /**
     * Sets shadow color to be the same color as the candle color.
     *
     * @param shadowColorSameAsCandle
     */
    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
