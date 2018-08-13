package com.github.mikephil.charting.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.MPPointF;

import java.lang.ref.WeakReference;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * View that can be displayed when selecting values in the chart. Extend this class to provide
 * custom layouts for your markers.
 *
 * @author Philipp Jahoda
 */
public class MarkerImage implements IMarker {
    @Nullable
    private final Drawable mDrawable;

    @NonNull
    private MPPointF mOffset = MPPointF.getInstance();

    @NonNull
    private final MPPointF mOffset2 = MPPointF.getInstance();

    @Nullable
    private WeakReference<Chart> mWeakChart;

    @NonNull
    private FSize mSize = FSize.getInstance(0f, 0f);

    @NonNull
    private final Rect mDrawableBoundsCache = new Rect();

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param drawableResourceId the drawable resource to render
     */
    public MarkerImage(@NonNull Context context, @DrawableRes int drawableResourceId) {
        if (drawableResourceId != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDrawable = context.getResources().getDrawable(drawableResourceId, null);
            } else {
                mDrawable = context.getResources().getDrawable(drawableResourceId);
            }
        } else {
            mDrawable = null;
        }
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

    public void setSize(@Nullable FSize size) {
        if (size == null) {
            mSize = FSize.getInstance(0f, 0f);
        } else {
            mSize = size;
        }
    }

    @NonNull
    public FSize getSize() {
        return mSize;
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
        MPPointF offset = mOffset;
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = mSize.width;
        float height = mSize.height;

        if (width == 0f && mDrawable != null) {
            width = mDrawable.getIntrinsicWidth();
        }

        if (height == 0f && mDrawable != null) {
            height = mDrawable.getIntrinsicHeight();
        }

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
        // Unused
    }

    @Override
    public void draw(@NonNull Canvas canvas, float posX, float posY) {
        if (mDrawable == null) {
            return;
        }

        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        float width = mSize.width;
        float height = mSize.height;

        if (width == 0f) {
            width = mDrawable.getIntrinsicWidth();
        }

        if (height == 0f) {
            height = mDrawable.getIntrinsicHeight();
        }

        mDrawable.copyBounds(mDrawableBoundsCache);
        mDrawable.setBounds(
                mDrawableBoundsCache.left, mDrawableBoundsCache.top,
                mDrawableBoundsCache.left + (int) width, mDrawableBoundsCache.top + (int) height
        );

        int saveId = canvas.save();
        // Translate to the correct position and draw
        canvas.translate(posX + offset.x, posY + offset.y);
        mDrawable.draw(canvas);
        canvas.restoreToCount(saveId);

        mDrawable.setBounds(mDrawableBoundsCache);
    }
}
