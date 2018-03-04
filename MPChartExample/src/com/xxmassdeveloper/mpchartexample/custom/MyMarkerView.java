package com.xxmassdeveloper.mpchartexample.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.xxmassdeveloper.mpchartexample.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(@NonNull Entry entry, @NonNull Highlight highlight) {

        if (entry instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) entry;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(Utils.formatNumber(entry.getY(), 0, true));
        }

        super.refreshContent(entry, highlight);
    }

    @NonNull
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
