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

    public BubbleDataSet(@NonNull List<BubbleEntry> yValues, @NonNull String label) {
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
        List<BubbleEntry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }

        BubbleDataSet copied = new BubbleDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(BubbleDataSet bubbleDataSet) {
        bubbleDataSet.mHighlightCircleWidth = mHighlightCircleWidth;
        bubbleDataSet.mNormalizeSize = mNormalizeSize;
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
