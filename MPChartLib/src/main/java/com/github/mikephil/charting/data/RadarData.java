package com.github.mikephil.charting.data;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Data container for the RadarChart.
 *
 * @author Philipp Jahoda
 */
public class RadarData extends ChartData<IRadarDataSet> {
    @NonNull
    private List<String> mLabels = Collections.emptyList();

    public RadarData() {
        super();
    }

    public RadarData(List<IRadarDataSet> dataSets) {
        super(dataSets);
    }

    public RadarData(IRadarDataSet... dataSets) {
        super(dataSets);
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(@NonNull List<String> labels) {
        this.mLabels = labels;
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(String... labels) {
        this.mLabels = Arrays.asList(labels);
    }

    @NonNull
    public List<String> getLabels() {
        return mLabels;
    }

    @Nullable
    @Override
    public Entry getEntryForHighlight(@NonNull Highlight highlight) {
        IRadarDataSet dataSet = getDataSetByIndex(highlight.getDataSetIndex());
        if (dataSet == null) {
            return null;
        }

        return dataSet.getEntryForIndex((int) highlight.getX());
    }
}
