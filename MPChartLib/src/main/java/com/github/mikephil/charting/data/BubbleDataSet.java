package com.github.mikephil.charting.data;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet {
    protected float mMaxSize;
    protected boolean mNormalizeSize = true;

    private float mHighlightCircleWidth = 2.5f;

    public BubbleDataSet(List<BubbleEntry> yValues, String label) {
        super(yValues, label);
    }

    @Override
    public void setHighlightCircleWidth(float width) {
        mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public float getHighlightCircleWidth() {
        return mHighlightCircleWidth;
    }

    @Override
    protected void calcMinMax(@NonNull BubbleEntry entry) {
        super.calcMinMax(entry);

        final float size = entry.getSize();

        if (size > mMaxSize) {
            mMaxSize = size;
        }
    }

    @NonNull
    @Override
    public DataSet<BubbleEntry> copy() {
        List<BubbleEntry> yValues = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            yValues.add(mValues.get(i).copy());
        }

        BubbleDataSet copied = new BubbleDataSet(yValues, getLabel());
        copied.mColors = mColors;
        copied.mHighLightColor = mHighLightColor;

        return copied;
    }

    @Override
    public float getMaxSize() {
        return mMaxSize;
    }

    @Override
    public boolean isNormalizeSizeEnabled() {
        return mNormalizeSize;
    }

    public void setNormalizeSizeEnabled(boolean normalizeSize) {
        mNormalizeSize = normalizeSize;
    }
}
