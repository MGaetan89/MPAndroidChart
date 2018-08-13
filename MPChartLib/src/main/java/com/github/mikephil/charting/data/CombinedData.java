package com.github.mikephil.charting.data;

import android.util.Log;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Data object that allows the combination of Line-, Bar-, Scatter-, Bubble- and CandleData. Used in
 * the CombinedChart class.
 *
 * @author Philipp Jahoda
 */
public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<? extends Entry>> {
    @Nullable
    private LineData mLineData;

    @Nullable
    private BarData mBarData;

    @Nullable
    private ScatterData mScatterData;

    @Nullable
    private CandleData mCandleData;

    @Nullable
    private BubbleData mBubbleData;

    public CombinedData() {
        super();
    }

    public void setData(@Nullable LineData data) {
        mLineData = data;
        notifyDataChanged();
    }

    public void setData(@Nullable BarData data) {
        mBarData = data;
        notifyDataChanged();
    }

    public void setData(@Nullable ScatterData data) {
        mScatterData = data;
        notifyDataChanged();
    }

    public void setData(@Nullable CandleData data) {
        mCandleData = data;
        notifyDataChanged();
    }

    public void setData(@Nullable BubbleData data) {
        mBubbleData = data;
        notifyDataChanged();
    }

    @Override
    public void calcMinMax() {
        mDataSets.clear();

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        mLeftAxisMax = -Float.MAX_VALUE;
        mLeftAxisMin = Float.MAX_VALUE;
        mRightAxisMax = -Float.MAX_VALUE;
        mRightAxisMin = Float.MAX_VALUE;

        List<BarLineScatterCandleBubbleData> allData = getAllData();

        for (ChartData data : allData) {
            data.calcMinMax();

            List<IBarLineScatterCandleBubbleDataSet<? extends Entry>> sets = data.getDataSets();
            mDataSets.addAll(sets);

            if (data.getYMax() > mYMax) {
                mYMax = data.getYMax();
            }

            if (data.getYMin() < mYMin) {
                mYMin = data.getYMin();
            }

            if (data.getXMax() > mXMax) {
                mXMax = data.getXMax();
            }

            if (data.getXMin() < mXMin) {
                mXMin = data.getXMin();
            }

            if (data.mLeftAxisMax > mLeftAxisMax) {
                mLeftAxisMax = data.mLeftAxisMax;
            }

            if (data.mLeftAxisMin < mLeftAxisMin) {
                mLeftAxisMin = data.mLeftAxisMin;
            }

            if (data.mRightAxisMax > mRightAxisMax) {
                mRightAxisMax = data.mRightAxisMax;
            }

            if (data.mRightAxisMin < mRightAxisMin) {
                mRightAxisMin = data.mRightAxisMin;
            }
        }
    }

    @Nullable
    public BubbleData getBubbleData() {
        return mBubbleData;
    }

    @Nullable
    public LineData getLineData() {
        return mLineData;
    }

    @Nullable
    public BarData getBarData() {
        return mBarData;
    }

    @Nullable
    public ScatterData getScatterData() {
        return mScatterData;
    }

    @Nullable
    public CandleData getCandleData() {
        return mCandleData;
    }

    /**
     * Returns all data objects in row: line-bar-scatter-candle-bubble if not null.
     */
    @NonNull
    public List<BarLineScatterCandleBubbleData> getAllData() {
        List<BarLineScatterCandleBubbleData> data = new ArrayList<>();
        if (mLineData != null) {
            data.add(mLineData);
        }

        if (mBarData != null) {
            data.add(mBarData);
        }

        if (mScatterData != null) {
            data.add(mScatterData);
        }

        if (mCandleData != null) {
            data.add(mCandleData);
        }

        if (mBubbleData != null) {
            data.add(mBubbleData);
        }

        return data;
    }

    public BarLineScatterCandleBubbleData getDataByIndex(int index) {
        if (index < 0 || index >= getAllData().size()) {
            return null;
        } else {
            return getAllData().get(index);
        }
    }

    @Override
    public void notifyDataChanged() {
        if (mLineData != null) {
            mLineData.notifyDataChanged();
        }

        if (mBarData != null) {
            mBarData.notifyDataChanged();
        }

        if (mCandleData != null) {
            mCandleData.notifyDataChanged();
        }

        if (mScatterData != null) {
            mScatterData.notifyDataChanged();
        }

        if (mBubbleData != null) {
            mBubbleData.notifyDataChanged();
        }

        calcMinMax(); // Recalculate everything
    }

    /**
     * Returns the Entry for a corresponding highlight object.
     *
     * @param highlight
     */
    @Nullable
    @Override
    public Entry getEntryForHighlight(@NonNull Highlight highlight) {
        if (highlight.getDataIndex() == -1 || highlight.getDataIndex() >= getAllData().size()) {
            return null;
        }

        ChartData data = getDataByIndex(highlight.getDataIndex());
        if (highlight.getDataSetIndex() >= data.getDataSetCount()) {
            return null;
        }

        // The value of the highlighted entry could be NaN, if we are not interested in highlighting
        // a specific value.

        List<Entry> entries = data.getDataSetByIndex(highlight.getDataSetIndex()).getEntriesForXValue(highlight.getX());
        for (Entry entry : entries) {
            if (entry.getY() == highlight.getY() || Float.isNaN(highlight.getY())) {
                return entry;
            }
        }

        return null;
    }

    /**
     * Returns the data set for highlight.
     *
     * @param highlight current highlight
     */
    @Nullable
    public IBarLineScatterCandleBubbleDataSet<? extends Entry> getDataSetByHighlight(@NonNull Highlight highlight) {
        if (highlight.getDataIndex() == -1 || highlight.getDataIndex() >= getAllData().size()) {
            return null;
        }

        BarLineScatterCandleBubbleData data = getDataByIndex(highlight.getDataIndex());
        if (highlight.getDataSetIndex() >= data.getDataSetCount()) {
            return null;
        }

        return (IBarLineScatterCandleBubbleDataSet<? extends Entry>) data.getDataSetByIndex(highlight.getDataSetIndex());
    }

    public int getDataIndex(ChartData data) {
        return getAllData().indexOf(data);
    }

    @Override
    public boolean removeDataSet(@Nullable IBarLineScatterCandleBubbleDataSet<? extends Entry> dataSet) {
        List<BarLineScatterCandleBubbleData> datas = getAllData();

        boolean success = false;
        for (ChartData data : datas) {
            success = data.removeDataSet(dataSet);
            if (success) {
                break;
            }
        }

        return success;
    }

    @Deprecated
    @Override
    public boolean removeDataSet(int index) {
        Log.e("MPAndroidChart", "removeDataSet(int) is not supported for CombinedData");
        return false;
    }

    @Deprecated
    @Override
    public boolean removeEntry(Entry entry, int dataSetIndex) {
        Log.e("MPAndroidChart", "removeEntry(Entry, int) is not supported for CombinedData");
        return false;
    }

    @Deprecated
    @Override
    public boolean removeEntry(float xValue, int dataSetIndex) {
        Log.e("MPAndroidChart", "removeEntry(float, int) is not supported for CombinedData");
        return false;
    }
}
