package com.xxmassdeveloper.mpchartexample.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.utils.Utils;

import androidx.annotation.NonNull;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressWarnings("unused")
@SuppressLint("ViewConstructor")
public class StackedBarsMarkerView extends MarkerView {
    private TextView tvContent;

    public StackedBarsMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(@NonNull Entry entry, @NonNull Highlight highlight) {
        float value = entry.getY();
        if (entry instanceof BarEntry) {
            BarEntry barEntry = (BarEntry) entry;
            if (barEntry.getYVals() != null) {
                value = barEntry.getYVals()[highlight.getStackIndex()];
            }
        }

        tvContent.setText(Utils.formatNumber(value, 0, true));

        super.refreshContent(entry, highlight);
    }

    @NonNull
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}
