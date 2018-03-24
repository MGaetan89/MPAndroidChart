package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Listener for callbacks when selecting values inside the chart by touch-gesture.
 *
 * @author Philipp Jahoda
 */
public interface OnChartValueSelectedListener {
    /**
     * Called when a value has been selected inside the chart.
     *
     * @param entry     The selected Entry
     * @param highlight The corresponding highlight object that contains information about the
     *                  highlighted position such as dataSetIndex, ...
     */
    void onValueSelected(Entry entry, Highlight highlight);

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    void onNothingSelected();
}
