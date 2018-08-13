package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public class CombinedHighlighter extends ChartHighlighter<CombinedDataProvider> implements IHighlighter {
    /**
     * Bar highlighter for supporting stacked highlighting.
     */
    @Nullable
    protected BarHighlighter barHighlighter;

    public CombinedHighlighter(CombinedDataProvider chart, BarDataProvider barChart) {
        super(chart);

        // If there is BarData, create a BarHighlighter
        barHighlighter = barChart.getBarData() == null ? null : new BarHighlighter(barChart);
    }

    @NonNull
    @Override
    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {
        mHighlightBuffer.clear();

        CombinedData data = mChart.getCombinedData();
        if (data == null) {
            return mHighlightBuffer;
        }

        List<BarLineScatterCandleBubbleData> dataObjects = data.getAllData();
        for (int i = 0; i < dataObjects.size(); i++) {
            ChartData dataObject = dataObjects.get(i);

            // In case of BarData, let the BarHighlighter take over
            if (barHighlighter != null && dataObject instanceof BarData) {
                Highlight highlight = barHighlighter.getHighlight(x, y);
                if (highlight != null) {
                    highlight.setDataIndex(i);
                    mHighlightBuffer.add(highlight);
                }
            } else {
                for (int j = 0, dataSetCount = dataObject.getDataSetCount(); j < dataSetCount; j++) {
                    IDataSet dataSet = dataObjects.get(i).getDataSetByIndex(j);

                    // Con't include datasets that cannot be highlighted
                    if (dataSet == null || !dataSet.isHighlightEnabled()) {
                        continue;
                    }

                    List<Highlight> highlights = buildHighlights(dataSet, j, xVal, DataSet.Rounding.CLOSEST);
                    for (Highlight highlight : highlights) {
                        highlight.setDataIndex(i);
                        mHighlightBuffer.add(highlight);
                    }
                }
            }
        }

        return mHighlightBuffer;
    }
}
