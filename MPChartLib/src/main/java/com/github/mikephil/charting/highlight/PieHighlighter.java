package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import androidx.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public class PieHighlighter extends PieRadarHighlighter<PieChart> {
    public PieHighlighter(PieChart chart) {
        super(chart);
    }

    @Nullable
    @Override
    protected Highlight getClosestHighlight(int index, float x, float y) {
        PieData data = mChart.getData();
        if (data == null) {
            return null;
        }

        IPieDataSet set = data.getDataSet();
        final Entry entry = set.getEntryForIndex(index);

        return new Highlight(index, entry.getY(), x, y, 0, set.getAxisDependency());
    }
}
