package com.github.mikephil.charting.components;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.lang.ref.WeakReference;

/**
 * View that can be displayed when selecting values in the chart. Extend this class to provide
 * custom layouts for your markers.
 *
 * @author Philipp Jahoda
 */
public class MarkerView extends RelativeLayout implements IMarker {
    @NonNull
    private MPPointF mOffset = MPPointF.getInstance();

    @NonNull
    private final MPPointF mOffset2 = MPPointF.getInstance();

    @Nullable
    private WeakReference<Chart> mWeakChart;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MarkerView(Context context, @LayoutRes int layoutResource) {
        super(context);

        if (layoutResource != 0) {
            setupLayoutResource(layoutResource);
        }
    }

    /**
     * Sets the layout resource for a custom MarkerView.
     *
     * @param layoutResource
     */
    private void setupLayoutResource(@LayoutRes int layoutResource) {
        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflated.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }

    public void setOffset(@Nullable MPPointF offset) {
        if (offset == null) {
            mOffset = MPPointF.getInstance();
        } else {
            mOffset = offset;
        }
    }

    public void setOffset(float offsetX, float offsetY) {
        mOffset.x = offsetX;
        mOffset.y = offsetY;
    }

    @NonNull
    @Override
    public MPPointF getOffset() {
        return mOffset;
    }

    public void setChartView(@Nullable Chart chart) {
        if (chart == null) {
            if (mWeakChart != null) {
                mWeakChart.clear();
                mWeakChart = null;
            }
        } else {
            mWeakChart = new WeakReference<>(chart);
        }
    }

    @Nullable
    public Chart getChartView() {
        return mWeakChart == null ? null : mWeakChart.get();
    }

    @NonNull
    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = getWidth();
        float height = getHeight();

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX;
        } else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
            mOffset2.x = chart.getWidth() - posX - width;
        }

        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY;
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height;
        }

        return mOffset2;
    }

    @Override
    public void refreshContent(@NonNull Entry entry, @NonNull Highlight highlight) {
        measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas, float posX, float posY) {
        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        int saveId = canvas.save();
        // Translate to the correct position and draw
        canvas.translate(posX + offset.x, posY + offset.y);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}
