package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base class of PieChart and RadarChart.
 *
 * @author Philipp Jahoda
 */
public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>>
        extends Chart<T> {
    /**
     * Holds the normalized version of the current rotation angle of the chart.
     */
    private float mRotationAngle = 270f;

    /**
     * Holds the raw version of the current rotation angle of the chart.
     */
    private float mRawRotationAngle = 270f;

    /**
     * Flag that indicates if rotation is enabled or not.
     */
    protected boolean mRotateEnabled = true;

    /**
     * Sets the minimum offset (padding) around the chart.
     */
    protected float mMinOffset = 0.f;

    public PieRadarChartBase(Context context) {
        super(context);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    @Override
    public int getMaxVisibleCount() {
        return mData.getEntryCount();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Use the pie- and radar chart listener own listener
        if (mTouchEnabled && mChartTouchListener != null) {
            return mChartTouchListener.onTouch(this, event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void computeScroll() {
        if (mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener) mChartTouchListener).computeScroll();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (mData == null) {
            return;
        }

        calcMinMax();

        if (mLegend != null) {
            mLegendRenderer.computeLegend(mData);
        }

        calculateOffsets();
    }

    @Override
    public void calculateOffsets() {
        float legendLeft = 0f;
        float legendRight = 0f;
        float legendBottom = 0f;
        float legendTop = 0f;

        if (mLegend != null && mLegend.isEnabled() && !mLegend.isDrawInsideEnabled()) {
            float fullLegendWidth = Math.min(mLegend.mNeededWidth, mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent());

            switch (mLegend.getOrientation()) {
                case VERTICAL: {
                    float xLegendOffset = 0f;

                    if (mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.LEFT || mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                        if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                            // This is the space between the legend and the chart
                            final float spacing = Utils.convertDpToPixel(13f);

                            xLegendOffset = fullLegendWidth + spacing;
                        } else {
                            // This is the space between the legend and the chart
                            float spacing = Utils.convertDpToPixel(8f);
                            float legendWidth = fullLegendWidth + spacing;
                            float legendHeight = mLegend.mNeededHeight + mLegend.mTextHeightMax;

                            MPPointF center = getCenter();

                            float bottomX = mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT ? getWidth() - legendWidth + 15f : legendWidth - 15f;
                            float bottomY = legendHeight + 15f;
                            float distLegend = distanceToCenter(bottomX, bottomY);

                            MPPointF reference = getPosition(center, getRadius(), getAngleForPoint(bottomX, bottomY));

                            float distReference = distanceToCenter(reference.x, reference.y);
                            float minOffset = Utils.convertDpToPixel(5f);

                            if (bottomY >= center.y && getHeight() - legendWidth > getWidth()) {
                                xLegendOffset = legendWidth;
                            } else if (distLegend < distReference) {
                                float diff = distReference - distLegend;
                                xLegendOffset = minOffset + diff;
                            }

                            MPPointF.recycleInstance(center);
                            MPPointF.recycleInstance(reference);
                        }
                    }

                    switch (mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            legendLeft = xLegendOffset;
                            break;

                        case RIGHT:
                            legendRight = xLegendOffset;
                            break;

                        case CENTER:
                            switch (mLegend.getVerticalAlignment()) {
                                case TOP:
                                    legendTop = Math.min(mLegend.mNeededHeight, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;

                                case BOTTOM:
                                    legendBottom = Math.min(mLegend.mNeededHeight, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;
                            }
                            break;
                    }
                }
                break;

                case HORIZONTAL:
                    float yLegendOffset;

                    if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.TOP || mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.BOTTOM) {

                        // It's possible that we do not need this offset anymore as it is available
                        // through the extraOffsets, but changing it can mean changing default
                        // visibility for existing apps.
                        float yOffset = getRequiredLegendOffset();

                        yLegendOffset = Math.min(mLegend.mNeededHeight + yOffset, mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                        switch (mLegend.getVerticalAlignment()) {
                            case TOP:
                                legendTop = yLegendOffset;
                                break;

                            case BOTTOM:
                                legendBottom = yLegendOffset;
                                break;
                        }
                    }
                    break;
            }

            legendLeft += getRequiredBaseOffset();
            legendRight += getRequiredBaseOffset();
            legendTop += getRequiredBaseOffset();
            legendBottom += getRequiredBaseOffset();
        }

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        if (this instanceof RadarChart) {
            XAxis x = this.getXAxis();

            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, x.mLabelRotatedWidth);
            }
        }

        legendTop += getExtraTopOffset();
        legendRight += getExtraRightOffset();
        legendBottom += getExtraBottomOffset();
        legendLeft += getExtraLeftOffset();

        float offsetLeft = Math.max(minOffset, legendLeft);
        float offsetTop = Math.max(minOffset, legendTop);
        float offsetRight = Math.max(minOffset, legendRight);
        float offsetBottom = Math.max(minOffset, Math.max(getRequiredBaseOffset(), legendBottom));

        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);

        if (mLogEnabled) {
            Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
        }
    }

    /**
     * Returns the angle relative to the chart center for the given point on the chart in degrees.
     * The angle is always between 0 and 360°, 0° is NORTH, 90° is EAST, ...
     *
     * @param x
     * @param y
     */
    public float getAngleForPoint(float x, float y) {
        MPPointF centerOffsets = getCenterOffsets();

        double tx = x - centerOffsets.x, ty = y - centerOffsets.y;
        double length = Math.sqrt(tx * tx + ty * ty);
        double r = Math.acos(ty / length);

        float angle = (float) Math.toDegrees(r);

        if (x > centerOffsets.x) {
            angle = 360f - angle;
        }

        MPPointF.recycleInstance(centerOffsets);

        // Add 90° because chart starts EAST
        angle = angle + 90f;

        // Neutralize overflow
        if (angle > 360f) {
            angle = angle - 360f;
        }


        return angle;
    }

    /**
     * Returns a recyclable MPPointF instance. Calculates the position around a center point,
     * depending on the distance from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     */
    @Nullable
    public MPPointF getPosition(MPPointF center, float dist, float angle) {
        MPPointF p = MPPointF.getInstance(0, 0);
        getPosition(center, dist, angle, p);
        return p;
    }

    public void getPosition(@NonNull MPPointF center, float dist, float angle, @NonNull MPPointF outputPoint) {
        outputPoint.x = (float) (center.x + dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float) (center.y + dist * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Returns the distance of a certain point on the chart to the center of the chart.
     *
     * @param x
     * @param y
     */
    public float distanceToCenter(float x, float y) {
        MPPointF center = getCenterOffsets();

        float xDist;
        if (x > center.x) {
            xDist = x - center.x;
        } else {
            xDist = center.x - x;
        }

        float yDist;
        if (y > center.y) {
            yDist = y - center.y;
        } else {
            yDist = center.y - y;
        }

        MPPointF.recycleInstance(center);

        // Pythagoras
        return (float) Math.sqrt(Math.pow(xDist, 2f) + Math.pow(yDist, 2f));
    }

    /**
     * Returns the xIndex for the given angle around the center of the chart. Returns -1 if not
     * found.
     *
     * @param angle
     */
    public abstract int getIndexForAngle(float angle);

    /**
     * Set an offset for the rotation of the RadarChart in degrees.
     *
     * @param angle
     */
    public void setRotationAngle(float angle) {
        mRawRotationAngle = angle;
        mRotationAngle = Utils.getNormalizedAngle(mRawRotationAngle);
    }

    /**
     * Returns the raw version of the current rotation angle of the pie chart the returned value
     * could be any value, negative or positive, outside of the 360 degrees. this is used when
     * working with rotation direction, mainly by gestures and animations.
     */
    public float getRawRotationAngle() {
        return mRawRotationAngle;
    }

    /**
     * Returns a normalized version of the current rotation angle of the pie chart, which will
     * always be between 0.0 < 360.0.
     */
    public float getRotationAngle() {
        return mRotationAngle;
    }

    /**
     * Set this to true to enable the rotation / spinning of the chart by touch. Set it to false to
     * disable it. Default: true
     *
     * @param enabled
     */
    public void setRotationEnabled(boolean enabled) {
        mRotateEnabled = enabled;
    }

    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     */
    public boolean isRotationEnabled() {
        return mRotateEnabled;
    }

    /**
     * Returns the minimum offset (padding) around the chart.
     */
    public float getMinOffset() {
        return mMinOffset;
    }

    /**
     * Sets the minimum offset (padding) around the chart.
     */
    public void setMinOffset(float minOffset) {
        mMinOffset = minOffset;
    }

    /**
     * Returns the diameter of the pie- or radar-chart.
     */
    public float getDiameter() {
        RectF content = mViewPortHandler.getContentRect();
        content.left += getExtraLeftOffset();
        content.top += getExtraTopOffset();
        content.right -= getExtraRightOffset();
        content.bottom -= getExtraBottomOffset();
        return Math.min(content.width(), content.height());
    }

    /**
     * Returns the radius of the chart in pixels.
     */
    public abstract float getRadius();

    /**
     * Returns the required offset for the chart legend.
     */
    protected abstract float getRequiredLegendOffset();

    /**
     * Returns the base offset needed for the chart without calculating the legend size.
     */
    protected abstract float getRequiredBaseOffset();

    @Override
    public float getYChartMax() {
        return 0;
    }

    @Override
    public float getYChartMin() {
        return 0;
    }

    /**
     * Applies a spin animation to the Chart.
     *
     * @param durationMillis
     * @param fromAngle
     * @param toAngle
     */
    public void spin(int durationMillis, float fromAngle, float toAngle, Easing.EasingOption easing) {
        setRotationAngle(fromAngle);

        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(this, "rotationAngle", fromAngle, toAngle);
        spinAnimator.setDuration(durationMillis);
        spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(easing));
        spinAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
        spinAnimator.start();
    }
}
