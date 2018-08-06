package com.github.mikephil.charting.highlight;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Jahoda
 */
public class HorizontalBarHighlighter extends BarHighlighter {
    public HorizontalBarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    @Nullable
    @Override
    public Highlight getHighlight(float x, float y) {
        MPPointD pos = getValsForTouch(y, x);
        Highlight highlight = getHighlightForX((float) pos.y, y, x);
        if (highlight == null) {
            MPPointD.recycleInstance(pos);
            return null;
        }

        BarData barData = mChart.getBarData();
        if (barData == null) {
            MPPointD.recycleInstance(pos);
            return null;
        }

        IBarDataSet set = barData.getDataSetByIndex(highlight.getDataSetIndex());
        if (set == null) {
            MPPointD.recycleInstance(pos);
            return null;
        }

        if (set.isStacked()) {
            Highlight stackedHighlight = getStackedHighlight(highlight, set, (float) pos.y, (float) pos.x);
            MPPointD.recycleInstance(pos);
            return stackedHighlight;
        }

        return highlight;
    }

    @NonNull
    @Override
    protected List<Highlight> buildHighlights(@NonNull IDataSet set, int dataSetIndex, float xVal, @NonNull DataSet.Rounding rounding) {
        ArrayList<Highlight> highlights = new ArrayList<>();
        List<Entry> entries = set.getEntriesForXValue(xVal);
        if (entries.isEmpty()) {
            // Try to find closest x-value and take all entries for that x-value
            final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);
            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX());
            }
        }

        if (entries.isEmpty()) {
            return highlights;
        }

        for (Entry entry : entries) {
            MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(entry.getY(), entry.getX());

            highlights.add(new Highlight(entry.getX(), entry.getY(), (float) pixels.x, (float) pixels.y, dataSetIndex, set.getAxisDependency()));
        }

        return highlights;
    }

    @Override
    protected float getDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(y1 - y2);
    }
}
