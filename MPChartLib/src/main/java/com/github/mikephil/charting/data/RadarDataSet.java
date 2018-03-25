package com.github.mikephil.charting.data;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class RadarDataSet extends LineRadarDataSet<RadarEntry> implements IRadarDataSet {
    /**
     * Flag indicating whether highlight circle should be drawn or not.
     */
    protected boolean mDrawHighlightCircleEnabled = false;

    @ColorInt
    protected int mHighlightCircleFillColor = Color.WHITE;

    /**
     * The stroke color for highlight circle. If Utils.COLOR_NONE, the color of the data set is
     * taken.
     */
    @ColorInt
    protected int mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE;

    protected int mHighlightCircleStrokeAlpha = (int) (0.3f * 255f);
    protected float mHighlightCircleInnerRadius = 3f;
    protected float mHighlightCircleOuterRadius = 4f;
    protected float mHighlightCircleStrokeWidth = 2f;

    public RadarDataSet(List<RadarEntry> yValues, String label) {
        super(yValues, label);
    }

    /**
     * Returns true if highlight circle should be drawn, false if not.
     */
    @Override
    public boolean isDrawHighlightCircleEnabled() {
        return mDrawHighlightCircleEnabled;
    }

    /**
     * Sets whether highlight circle should be drawn or not.
     */
    @Override
    public void setDrawHighlightCircleEnabled(boolean enabled) {
        mDrawHighlightCircleEnabled = enabled;
    }

    @ColorInt
    @Override
    public int getHighlightCircleFillColor() {
        return mHighlightCircleFillColor;
    }

    public void setHighlightCircleFillColor(@ColorInt int color) {
        mHighlightCircleFillColor = color;
    }

    /**
     * Returns the stroke color for highlight circle.  If Utils.COLOR_NONE, the color of the data
     * set is taken.
     */
    @ColorInt
    @Override
    public int getHighlightCircleStrokeColor() {
        return mHighlightCircleStrokeColor;
    }

    /**
     * Sets the stroke color for highlight circle. Set to Utils.COLOR_NONE in order to use the color
     * of the data set.
     */
    public void setHighlightCircleStrokeColor(@ColorInt int color) {
        mHighlightCircleStrokeColor = color;
    }

    @Override
    public int getHighlightCircleStrokeAlpha() {
        return mHighlightCircleStrokeAlpha;
    }

    public void setHighlightCircleStrokeAlpha(int alpha) {
        mHighlightCircleStrokeAlpha = alpha;
    }

    @Override
    public float getHighlightCircleInnerRadius() {
        return mHighlightCircleInnerRadius;
    }

    public void setHighlightCircleInnerRadius(float radius) {
        mHighlightCircleInnerRadius = radius;
    }

    @Override
    public float getHighlightCircleOuterRadius() {
        return mHighlightCircleOuterRadius;
    }

    public void setHighlightCircleOuterRadius(float radius) {
        mHighlightCircleOuterRadius = radius;
    }

    @Override
    public float getHighlightCircleStrokeWidth() {
        return mHighlightCircleStrokeWidth;
    }

    public void setHighlightCircleStrokeWidth(float strokeWidth) {
        mHighlightCircleStrokeWidth = strokeWidth;
    }

    @NonNull
    @Override
    public DataSet<RadarEntry> copy() {
        List<RadarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            yValues.add(mValues.get(i).copy());
        }

        RadarDataSet copied = new RadarDataSet(yValues, getLabel());
        copied.mColors = mColors;
        copied.mHighLightColor = mHighLightColor;

        return copied;
    }
}
