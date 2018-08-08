package com.github.mikephil.charting.charts;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.IHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class of all Chart-Views.
 *
 * @author Philipp Jahoda
 */
public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>>
        extends ViewGroup implements ChartInterface {
    public static final String LOG_TAG = "MPAndroidChart";

    /**
     * Flag that indicates if logging is enabled or not.
     */
    protected boolean mLogEnabled = false;

    /**
     * Object that holds all data that was originally set for the chart, before it was modified or
     * any filtering algorithms had been applied.
     */
    protected T mData = null;

    /**
     * Flag that indicates if highlighting per tap (touch) is enabled.
     */
    protected boolean mHighLightPerTapEnabled = true;

    /**
     * If set to true, chart continues to scroll after touch up.
     */
    private boolean mDragDecelerationEnabled = true;

    /**
     * Deceleration friction coefficient in [0 ; 1] interval, higher values indicate that speed will
     * decrease slowly, for example if it set to 0, it will stop immediately. 1 is an invalid value,
     * and will be converted to 0.999f automatically.
     */
    private float mDragDecelerationFrictionCoef = 0.9f;

    /**
     * Default value-formatter, number of digits depends on provided chart-data.
     */
    protected DefaultValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(0);

    /**
     * Paint object used for drawing the description text in the bottom right corner of the chart.
     */
    protected Paint mDescPaint;

    /**
     * Paint object for drawing the information text when there are no values in the chart.
     */
    protected Paint mInfoPaint;

    /**
     * The object representing the labels on the x-axis.
     */
    protected XAxis mXAxis;

    /**
     * If true, touch gestures are enabled on the chart.
     */
    protected boolean mTouchEnabled = true;

    /**
     * The object responsible for representing the description text.
     */
    protected Description mDescription;

    /**
     * The legend object containing all data associated with the legend.
     */
    protected Legend mLegend;

    /**
     * Listener that is called when a value on the chart is selected.
     */
    protected OnChartValueSelectedListener mSelectionListener;

    protected ChartTouchListener mChartTouchListener;

    /**
     * Text that is displayed when the chart is empty.
     */
    private String mNoDataText = "No chart data available.";

    /**
     * Gesture listener for custom callbacks when making gestures on the chart.
     */
    private OnChartGestureListener mGestureListener;

    protected LegendRenderer mLegendRenderer;

    /**
     * Object responsible for rendering the data.
     */
    protected DataRenderer mRenderer;

    protected IHighlighter mHighlighter;

    /**
     * Object that manages the bounds and drawing constraints of the chart.
     */
    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();

    /**
     * Object responsible for animations.
     */
    protected ChartAnimator mAnimator;

    /**
     * Extra offsets to be appended to the viewport.
     */
    private float mExtraTopOffset = 0f;
    private float mExtraRightOffset = 0f;
    private float mExtraBottomOffset = 0f;
    private float mExtraLeftOffset = 0f;

    /**
     * Default constructor for initialization in code.
     */
    public Chart(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor for initialization in xml.
     */
    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Even more awesome constructor.
     */
    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initialize all paints and stuff.
     */
    protected void init() {
        setWillNotDraw(false);

        mAnimator = new ChartAnimator(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        // Initialize the utils
        Utils.init(getContext());

        mMaxHighlightDistance = Utils.convertDpToPixel(500f);
        mDescription = new Description();
        mLegend = new Legend();
        mLegendRenderer = new LegendRenderer(mViewPortHandler, mLegend);
        mXAxis = new XAxis();
        mDescPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(0xF7BD33); // orange
        mInfoPaint.setTextAlign(Align.CENTER);
        mInfoPaint.setTextSize(Utils.convertDpToPixel(12f));
    }

    /**
     * Sets a new data object for the chart. The data object contains all values and information
     * needed for displaying.
     *
     * @param data
     */
    public void setData(@Nullable T data) {
        mData = data;
        mOffsetsCalculated = false;

        if (data == null) {
            return;
        }

        // Calculate how many digits are needed
        setupDefaultFormatter(data.getYMin(), data.getYMax());

        for (IDataSet set : mData.getDataSets()) {
            if (set.needsFormatter() || set.getValueFormatter() == mDefaultValueFormatter) {
                set.setValueFormatter(mDefaultValueFormatter);
            }
        }

        // Let the chart know there is new data
        notifyDataSetChanged();

        if (mLogEnabled) {
            Log.i(LOG_TAG, "Data is set.");
        }
    }

    /**
     * Clears the chart from all data (sets it to null) and refreshes it (by calling invalidate()).
     */
    public void clear() {
        mData = null;
        mOffsetsCalculated = false;
        mIndicesToHighlight = null;
        mChartTouchListener.setLastHighlighted(null);

        invalidate();
    }

    /**
     * Removes all DataSets (and thereby Entries) from the chart. Does not set the data object to
     * null. Also refreshes the chart by calling invalidate().
     */
    public void clearValues() {
        mData.clearValues();

        invalidate();
    }

    /**
     * Returns true if the chart is empty (meaning it's data object is either null or contains no
     * entries).
     */
    public boolean isEmpty() {
        return mData == null || mData.getEntryCount() <= 0;
    }

    /**
     * Lets the chart know its underlying data has changed and performs all  necessary
     * recalculations. It is crucial that this method is called every time data is changed
     * dynamically. Not calling this method can lead to crashes or unexpected behaviour.
     */
    public abstract void notifyDataSetChanged();

    /**
     * Calculates the offsets of the chart to the border depending on the position of an eventual
     * legend or depending on the length of the y-axis and x-axis labels and their position.
     */
    protected abstract void calculateOffsets();

    /**
     * Calculates the y-min and y-max value and the y-delta and x-delta value.
     */
    protected abstract void calcMinMax();

    /**
     * Calculates the required number of digits for the values that might be drawn in the chart
     * (if enabled), and creates the default-value-formatter.
     */
    protected void setupDefaultFormatter(float min, float max) {
        float reference;
        if (mData == null || mData.getEntryCount() < 2) {
            reference = Math.max(Math.abs(min), Math.abs(max));
        } else {
            reference = Math.abs(max - min);
        }

        int digits = Utils.getDecimals(reference);

        // Setup the formatter with a new number of digits
        mDefaultValueFormatter.setup(digits);
    }

    /**
     * Flag that indicates if offsets calculation has already been done or not.
     */
    private boolean mOffsetsCalculated = false;

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (mData == null) {
            if (!TextUtils.isEmpty(mNoDataText)) {
                MPPointF center = getCenter();
                canvas.drawText(mNoDataText, center.x, center.y, mInfoPaint);
            }
        } else if (!mOffsetsCalculated) {
            calculateOffsets();
            mOffsetsCalculated = true;
        }
    }

    /**
     * Draws the description text in the bottom right corner of the chart (per default).
     */
    protected void drawDescription(@NonNull Canvas canvas) {
        // Check if description should be drawn
        if (mDescription != null && mDescription.isEnabled()) {
            MPPointF position = mDescription.getPosition();

            mDescPaint.setTypeface(mDescription.getTypeface());
            mDescPaint.setTextSize(mDescription.getTextSize());
            mDescPaint.setColor(mDescription.getTextColor());
            mDescPaint.setTextAlign(mDescription.getTextAlign());

            float x;
            float y;

            // If no position specified, draw on default position
            if (position == null) {
                x = getWidth() - mViewPortHandler.offsetRight() - mDescription.getXOffset();
                y = getHeight() - mViewPortHandler.offsetBottom() - mDescription.getYOffset();
            } else {
                x = position.x;
                y = position.y;
            }

            canvas.drawText(mDescription.getText(), x, y, mDescPaint);
        }
    }

    /**
     * Array of Highlight objects that reference the highlighted slices in the chart.
     */
    protected Highlight[] mIndicesToHighlight;

    /**
     * The maximum distance in dp away from an entry causing it to highlight.
     */
    protected float mMaxHighlightDistance = 0f;

    @Override
    public float getMaxHighlightDistance() {
        return mMaxHighlightDistance;
    }

    /**
     * Sets the maximum distance in screen dp a touch can be away from an entry to cause it to get
     * highlighted.
     *
     * @param distDp
     */
    public void setMaxHighlightDistance(float distDp) {
        mMaxHighlightDistance = Utils.convertDpToPixel(distDp);
    }

    /**
     * Returns the array of currently highlighted values. This might a null or empty array if
     * nothing is highlighted.
     */
    public Highlight[] getHighlighted() {
        return mIndicesToHighlight;
    }

    /**
     * Returns true if values can be highlighted via tap gesture, false if not.
     */
    public boolean isHighlightPerTapEnabled() {
        return mHighLightPerTapEnabled;
    }

    /**
     * Set this to false to prevent values from being highlighted by tap gesture. Values can still
     * be highlighted via drag or programmatically.
     *
     * @param enabled
     */
    public void setHighlightPerTapEnabled(boolean enabled) {
        mHighLightPerTapEnabled = enabled;
    }

    /**
     * Returns true if there are values to highlight, false if there are no values to highlight.
     * Checks if the highlight array is null, has a length of zero or if the first object is null.
     */
    public boolean valuesToHighlight() {
        return mIndicesToHighlight != null && mIndicesToHighlight.length > 0 && mIndicesToHighlight[0] != null;
    }

    /**
     * Sets the last highlighted value for the touch listener.
     *
     * @param highlights
     */
    protected void setLastHighlighted(@Nullable Highlight[] highlights) {
        if (highlights == null || highlights.length <= 0 || highlights[0] == null) {
            mChartTouchListener.setLastHighlighted(null);
        } else {
            mChartTouchListener.setLastHighlighted(highlights[0]);
        }
    }

    /**
     * Highlights the values at the given indices in the given DataSets. Provide null or an empty
     * array to undo all highlighting. This should be used to programmatically highlight values.
     * This method *will not* call the listener.
     *
     * @param highlights
     */
    public void highlightValues(Highlight[] highlights) {
        // Set the indices to highlight
        mIndicesToHighlight = highlights;

        setLastHighlighted(highlights);

        // Redraw the chart
        invalidate();
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet. Provide -1 as the
     * dataSetIndex to undo all highlighting. This method will call the listener.
     *
     * @param x            The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     */
    public void highlightValue(float x, int dataSetIndex) {
        highlightValue(x, dataSetIndex, true);
    }

    /**
     * Highlights the value at the given x-value and y-value in the given DataSet. Provide -1 as the
     * dataSetIndex to undo all highlighting. This method will call the listener.
     *
     * @param x            The x-value to highlight
     * @param y            The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     */
    public void highlightValue(float x, float y, int dataSetIndex) {
        highlightValue(x, y, dataSetIndex, true);
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet. Provide -1 as the
     * dataSetIndex to undo all highlighting.
     *
     * @param x            The x-value to highlight
     * @param dataSetIndex The dataset index to search in
     * @param callListener Should the listener be called for this change
     */
    public void highlightValue(float x, int dataSetIndex, boolean callListener) {
        highlightValue(x, Float.NaN, dataSetIndex, callListener);
    }

    /**
     * Highlights any y-value at the given x-value in the given DataSet. Provide -1 as the
     * dataSetIndex to undo all highlighting.
     *
     * @param x            The x-value to highlight
     * @param y            The y-value to highlight. Supply `NaN` for "any"
     * @param dataSetIndex The dataset index to search in
     * @param callListener Should the listener be called for this change
     */
    public void highlightValue(float x, float y, int dataSetIndex, boolean callListener) {
        if (dataSetIndex < 0 || dataSetIndex >= mData.getDataSetCount()) {
            highlightValue(null, callListener);
        } else {
            highlightValue(new Highlight(x, y, dataSetIndex), callListener);
        }
    }

    /**
     * Highlights the values represented by the provided Highlight object This method *will not*
     * call the listener.
     *
     * @param highlight contains information about which entry should be highlighted
     */
    public void highlightValue(@Nullable Highlight highlight) {
        highlightValue(highlight, false);
    }

    /**
     * Highlights the value selected by touch gesture. Unlike highlightValues(...), this generates a
     * callback to the OnChartValueSelectedListener.
     *
     * @param highlight    - the highlight object
     * @param callListener - call the listener
     */
    public void highlightValue(@Nullable Highlight highlight, boolean callListener) {
        Entry entry = null;
        if (highlight == null) {
            mIndicesToHighlight = null;
        } else {
            if (mLogEnabled) {
                Log.i(LOG_TAG, "Highlighted: " + highlight.toString());
            }

            entry = mData.getEntryForHighlight(highlight);
            if (entry == null) {
                mIndicesToHighlight = null;
                highlight = null;
            } else {
                // Set the indices to highlight
                mIndicesToHighlight = new Highlight[]{highlight};
            }
        }

        setLastHighlighted(mIndicesToHighlight);

        if (callListener && mSelectionListener != null) {
            if (!valuesToHighlight())
                mSelectionListener.onNothingSelected();
            else {
                // Notify the listener
                mSelectionListener.onValueSelected(entry, highlight);
            }
        }

        // Redraw the chart
        invalidate();
    }

    /**
     * Returns the Highlight object (contains x-index and DataSet index) of the selected value at
     * the given touch point inside the Line-, Scatter-, or CandleStick-Chart.
     *
     * @param x
     * @param y
     */
    @Nullable
    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else {
            return getHighlighter().getHighlight(x, y);
        }
    }

    /**
     * Set a new (e.g. custom) ChartTouchListener NOTE: make sure to setTouchEnabled(true); if you
     * need touch gestures on the chart.
     *
     * @param listener
     */
    public void setOnTouchListener(ChartTouchListener listener) {
        this.mChartTouchListener = listener;
    }

    /**
     * Returns an instance of the currently active touch listener.
     */
    public ChartTouchListener getOnTouchListener() {
        return mChartTouchListener;
    }

    /**
     * If set to true, the marker view is drawn when a value is clicked.
     */
    protected boolean mDrawMarkers = true;

    /**
     * The view that represents the marker.
     */
    protected IMarker mMarker;

    /**
     * Draws all MarkerViews on the highlighted positions.
     */
    protected void drawMarkers(@NonNull Canvas canvas) {
        // IOf there is no marker view or drawing marker is disabled
        if (mMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight()) {
            return;
        }

        for (Highlight highlight : mIndicesToHighlight) {
            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());
            if (set == null) {
                continue;
            }

            Entry entry = mData.getEntryForHighlight(highlight);
            int entryIndex = set.getEntryIndex(entry);

            // Make sure entry not null
            if (entry == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX()) {
                continue;
            }

            float[] position = getMarkerPosition(highlight);

            // Check bounds
            if (!mViewPortHandler.isInBounds(position[0], position[1])) {
                continue;
            }

            // Callbacks to update the content
            mMarker.refreshContent(entry, highlight);

            // Draw the marker
            mMarker.draw(canvas, position[0], position[1]);
        }
    }

    /**
     * Returns the actual position in pixels of the MarkerView for the given Highlight object.
     *
     * @param highlight
     */
    protected float[] getMarkerPosition(@NonNull Highlight highlight) {
        return new float[]{highlight.getDrawX(), highlight.getDrawY()};
    }

    /**
     * Returns the animator responsible for animating chart values.
     */
    public ChartAnimator getAnimator() {
        return mAnimator;
    }

    /**
     * If set to true, chart continues to scroll after touch up.
     */
    public boolean isDragDecelerationEnabled() {
        return mDragDecelerationEnabled;
    }

    /**
     * If set to true, chart continues to scroll after touch up.
     *
     * @param enabled
     */
    public void setDragDecelerationEnabled(boolean enabled) {
        mDragDecelerationEnabled = enabled;
    }

    /**
     * Returns drag deceleration friction coefficient.
     */
    public float getDragDecelerationFrictionCoef() {
        return mDragDecelerationFrictionCoef;
    }

    /**
     * Deceleration friction coefficient in [0 ; 1] interval, higher values indicate that speed will
     * decrease slowly, for example if it set to 0, it will stop immediately. 1 is an invalid value,
     * and will be converted to 0.999f automatically.
     *
     * @param newValue
     */
    public void setDragDecelerationFrictionCoef(float newValue) {
        if (newValue < 0f) {
            newValue = 0f;
        }

        if (newValue >= 1f) {
            newValue = 0.999f;
        }

        mDragDecelerationFrictionCoef = newValue;
    }

    /**
     * Animates the drawing / rendering of the chart on both x- and y-axis with the specified
     * animation time. If animate(...) is called, no further calling of invalidate() is necessary to
     * refresh the chart.
     *
     * @param durationMillisX
     * @param durationMillisY
     * @param easingX         a custom easing function to be used on the animation phase
     * @param easingY         a custom easing function to be used on the animation phase
     */
    public void animateXY(int durationMillisX, int durationMillisY, TimeInterpolator easingX, TimeInterpolator easingY) {
        mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing         a custom easing function to be used on the animation phase
     */
    public void animateX(int durationMillis, TimeInterpolator easing) {
        mAnimator.animateX(durationMillis, easing);
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     * @param easing         a custom easing function to be used on the animation phase
     */
    public void animateY(int durationMillis, TimeInterpolator easing) {
        mAnimator.animateY(durationMillis, easing);
    }

    /**
     * Animates the rendering of the chart on the x-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateX(int durationMillis) {
        mAnimator.animateX(durationMillis);
    }

    /**
     * Animates the rendering of the chart on the y-axis with the specified animation time. If
     * animate(...) is called, no further calling of invalidate() is necessary to refresh the chart.
     *
     * @param durationMillis
     */
    public void animateY(int durationMillis) {
        mAnimator.animateY(durationMillis);
    }

    /**
     * Animates the drawing / rendering of the chart on both x- and y-axis with the specified
     * animation time. If animate(...) is called, no further calling of invalidate() is necessary to
     * refresh the chart.
     *
     * @param durationMillisX
     * @param durationMillisY
     */
    public void animateXY(int durationMillisX, int durationMillisY) {
        mAnimator.animateXY(durationMillisX, durationMillisY);
    }

    /**
     * Returns the object representing all x-labels, this method can be used to acquire the XAxis
     * object and modify it (e.g. change the position of the labels, styling, etc.)
     */
    public XAxis getXAxis() {
        return mXAxis;
    }

    /**
     * Returns the default IValueFormatter that has been determined by the chart considering the
     * provided minimum and maximum values.
     */
    @NonNull
    public IValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }

    /**
     * Set a selection listener for the chart.
     *
     * @param listener
     */
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener listener) {
        this.mSelectionListener = listener;
    }

    /**
     * Sets a gesture-listener for the chart for custom callbacks when executing gestures on the
     * chart surface.
     *
     * @param listener
     */
    public void setOnChartGestureListener(OnChartGestureListener listener) {
        this.mGestureListener = listener;
    }

    /**
     * Returns the custom gesture listener.
     */
    public OnChartGestureListener getOnChartGestureListener() {
        return mGestureListener;
    }

    /**
     * Returns the current y-max value across all DataSets.
     */
    public float getYMax() {
        return mData.getYMax();
    }

    /**
     * Returns the current y-min value across all DataSets.
     */
    public float getYMin() {
        return mData.getYMin();
    }

    @Override
    public float getXChartMax() {
        return mXAxis.mAxisMaximum;
    }

    @Override
    public float getXChartMin() {
        return mXAxis.mAxisMinimum;
    }

    @Override
    public float getXRange() {
        return mXAxis.mAxisRange;
    }

    /**
     * Returns a recyclable MPPointF instance. Returns the center point of the chart (the whole
     * View) in pixels.
     */
    @NonNull
    public MPPointF getCenter() {
        return MPPointF.getInstance(getWidth() / 2f, getHeight() / 2f);
    }

    /**
     * Returns a recyclable MPPointF instance. Returns the center of the chart taking offsets under
     * consideration. Returns the center of the content rectangle.
     */
    @NonNull
    @Override
    public MPPointF getCenterOffsets() {
        return mViewPortHandler.getContentCenter();
    }

    /**
     * Sets extra offsets (around the chart view) to be appended to the auto-calculated offsets.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setExtraOffsets(float left, float top, float right, float bottom) {
        setExtraLeftOffset(left);
        setExtraTopOffset(top);
        setExtraRightOffset(right);
        setExtraBottomOffset(bottom);
    }

    /**
     * Set an extra offset to be appended to the viewport's top.
     */
    public void setExtraTopOffset(float offset) {
        mExtraTopOffset = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns the extra offset to be appended to the viewport's top.
     */
    public float getExtraTopOffset() {
        return mExtraTopOffset;
    }

    /**
     * Set an extra offset to be appended to the viewport's right.
     */
    public void setExtraRightOffset(float offset) {
        mExtraRightOffset = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns the extra offset to be appended to the viewport's right.
     */
    public float getExtraRightOffset() {
        return mExtraRightOffset;
    }

    /**
     * Set an extra offset to be appended to the viewport's bottom.
     */
    public void setExtraBottomOffset(float offset) {
        mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns the extra offset to be appended to the viewport's bottom.
     */
    public float getExtraBottomOffset() {
        return mExtraBottomOffset;
    }

    /**
     * Set an extra offset to be appended to the viewport's left.
     */
    public void setExtraLeftOffset(float offset) {
        mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns the extra offset to be appended to the viewport's left.
     */
    public float getExtraLeftOffset() {
        return mExtraLeftOffset;
    }

    /**
     * Set this to true to enable logcat outputs for the chart. Beware that logcat output decreases
     * rendering performance. Default: disabled.
     *
     * @param enabled
     */
    public void setLogEnabled(boolean enabled) {
        mLogEnabled = enabled;
    }

    /**
     * Returns true if log-output is enabled for the chart, false if not.
     */
    public boolean isLogEnabled() {
        return mLogEnabled;
    }

    /**
     * Sets the text that informs the user that there is no data available with which to draw the
     * chart.
     *
     * @param text
     */
    public void setNoDataText(String text) {
        mNoDataText = text;
    }

    /**
     * Sets the color of the no data text.
     *
     * @param color
     */
    public void setNoDataTextColor(int color) {
        mInfoPaint.setColor(color);
    }

    /**
     * Sets the typeface to be used for the no data text.
     *
     * @param tf
     */
    public void setNoDataTextTypeface(Typeface tf) {
        mInfoPaint.setTypeface(tf);
    }

    /**
     * Set this to false to disable all gestures and touches on the chart.
     *
     * @param enabled
     */
    public void setTouchEnabled(boolean enabled) {
        this.mTouchEnabled = enabled;
    }

    /**
     * Sets the marker that is displayed when a value is clicked on the chart.
     *
     * @param marker
     */
    public void setMarker(IMarker marker) {
        mMarker = marker;
    }

    /**
     * Returns the marker that is set as a marker view for the chart.
     */
    public IMarker getMarker() {
        return mMarker;
    }

    /**
     * Sets a new Description object for the chart.
     *
     * @param desc
     */
    public void setDescription(Description desc) {
        this.mDescription = desc;
    }

    /**
     * Returns the Description object of the chart that is responsible for holding all information
     * related to the description text that is displayed in the bottom right corner of the chart.
     */
    public Description getDescription() {
        return mDescription;
    }

    /**
     * Returns the Legend object of the chart. This method can be used to get an instance of the
     * legend in order to customize the automatically generated Legend.
     */
    public Legend getLegend() {
        return mLegend;
    }

    /**
     * Returns the renderer object responsible for rendering / drawing the Legend.
     */
    public LegendRenderer getLegendRenderer() {
        return mLegendRenderer;
    }

    /**
     * Returns the rectangle that defines the borders of the chart-value surface (into which the
     * actual values are drawn).
     */
    @NonNull
    @Override
    public RectF getContentRect() {
        return mViewPortHandler.getContentRect();
    }

    /**
     * Disables intercept touch events.
     */
    public void disableScroll() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * Enables intercept touch events.
     */
    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    /**
     * Paint for the grid background (only line and barchart).
     */
    public static final int PAINT_GRID_BACKGROUND = 4;

    /**
     * Paint for the info text that is displayed when there are no values in the chart.
     */
    public static final int PAINT_INFO = 7;

    /**
     * Paint for the description text in the bottom right corner.
     */
    public static final int PAINT_DESCRIPTION = 11;

    /**
     * Paint for the hole in the middle of the pie chart.
     */
    public static final int PAINT_HOLE = 13;

    /**
     * Paint for the text in the middle of the pie chart.
     */
    public static final int PAINT_CENTER_TEXT = 14;

    /**
     * Paint used for the legend.
     */
    public static final int PAINT_LEGEND_LABEL = 18;

    /**
     * Set a new paint object for the specified parameter in the chart e.g. Chart.PAINT_VALUES.
     *
     * @param paint the new paint object
     * @param which Chart.PAINT_VALUES, Chart.PAINT_GRID, Chart.PAINT_VALUES, ...
     */
    public void setPaint(Paint paint, int which) {
        switch (which) {
            case PAINT_INFO:
                mInfoPaint = paint;
                break;
            case PAINT_DESCRIPTION:
                mDescPaint = paint;
                break;
        }
    }

    /**
     * Returns the paint object associated with the provided constant.
     *
     * @param which e.g. Chart.PAINT_LEGEND_LABEL
     */
    @Nullable
    public Paint getPaint(int which) {
        switch (which) {
            case PAINT_INFO:
                return mInfoPaint;
            case PAINT_DESCRIPTION:
                return mDescPaint;
            default:
                return null;
        }
    }

    /**
     * Returns true if drawing the marker is enabled when tapping on values (use the
     * setMarker(IMarker marker) method to specify a marker).
     */
    public boolean isDrawMarkersEnabled() {
        return mDrawMarkers;
    }

    /**
     * Set this to true to draw a user specified marker when tapping on chart values (use the
     * setMarker(IMarker marker) method to specify a marker).
     *
     * @param enabled
     */
    public void setDrawMarkers(boolean enabled) {
        mDrawMarkers = enabled;
    }

    /**
     * Returns the ChartData object that has been set for the chart.
     */
    @Nullable
    public T getData() {
        return mData;
    }

    /**
     * Returns the ViewPortHandler of the chart that is responsible for the content area of the
     * chart and its offsets and dimensions.
     */
    public ViewPortHandler getViewPortHandler() {
        return mViewPortHandler;
    }

    /**
     * Returns the Renderer object the chart uses for drawing data.
     */
    public DataRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * Sets a new DataRenderer object for the chart.
     *
     * @param renderer
     */
    public void setRenderer(@Nullable DataRenderer renderer) {
        if (renderer != null) {
            mRenderer = renderer;
        }
    }

    public IHighlighter getHighlighter() {
        return mHighlighter;
    }

    /**
     * Sets a custom highligher object for the chart that handles / processes all highlight touch
     * events performed on the chart-view.
     *
     * @param highlighter
     */
    public void setHighlighter(ChartHighlighter highlighter) {
        mHighlighter = highlighter;
    }

    /**
     * Returns a recyclable MPPointF instance.
     */
    @NonNull
    @Override
    public MPPointF getCenterOfView() {
        return getCenter();
    }

    /**
     * Returns the bitmap that represents the chart.
     */
    public Bitmap getChartBitmap() {
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable background = getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        draw(canvas);

        return returnedBitmap;
    }

    /**
     * Saves the current chart state with the given name to the given path on the sdcard leaving the
     * path empty "" will put the saved file directly on the SD card chart is saved as a PNG image,
     * example: saveToPath("myfilename", "foldername1/foldername2");
     *
     * @param title
     * @param pathOnSD e.g. "folder1/folder2/folder3"
     * @return returns true on success, false on error
     */
    public boolean saveToPath(String title, String pathOnSD) {
        Bitmap bitmap = getChartBitmap();
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + pathOnSD + "/" + title + ".png");

            // Write bitmap to file using JPEG or PNG and 40% quality hint for JPEG.
            bitmap.compress(CompressFormat.PNG, 40, stream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * Saves the current state of the chart to the gallery as an image type. The compression must be
     * set for JPEG only. 0 == maximum compression, 100 = low compression (high quality).
     * NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName        e.g. "my_image"
     * @param subFolderPath   e.g. "ChartPics"
     * @param fileDescription e.g. "Chart details"
     * @param format          e.g. Bitmap.CompressFormat.PNG
     * @param quality         e.g. 50, min = 0, max = 100
     * @return returns true if saving was successful, false if not
     */
    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, Bitmap.CompressFormat format, @IntRange(from = 0, to = 100) int quality) {
        if (quality < 0 || quality > 100) {
            quality = 50;
        }

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }

        String mimeType;
        switch (format) {
            case PNG:
                mimeType = "image/png";
                if (!fileName.endsWith(".png")) {
                    fileName += ".png";
                }
                break;
            case WEBP:
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp")) {
                    fileName += ".webp";
                }
                break;
            case JPEG:
            default:
                mimeType = "image/jpeg";
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
                    fileName += ".jpg";
                }
                break;
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            Bitmap bitmap = getChartBitmap();
            bitmap.compress(format, quality, out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(8);

        // Store the details
        values.put(Images.Media.TITLE, fileName);
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.DATE_ADDED, currentTime);
        values.put(Images.Media.MIME_TYPE, mimeType);
        values.put(Images.Media.DESCRIPTION, fileDescription);
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values) != null;
    }

    /**
     * Saves the current state of the chart to the gallery as a JPEG image. The filename and
     * compression can be set. 0 == maximum compression, 100 = low compression (high quality).
     * NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     *
     * @param fileName e.g. "my_image"
     * @param quality  e.g. 50, min = 0, max = 100
     * @return returns true if saving was successful, false if not
     */
    public boolean saveToGallery(String fileName, int quality) {
        return saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.JPEG, quality);
    }

    /**
     * Tasks to be done after the view is setup.
     */
    protected List<Runnable> mJobs = new ArrayList<>();

    public void removeViewportJob(Runnable job) {
        mJobs.remove(job);
    }

    public void clearAllViewportJobs() {
        mJobs.clear();
    }

    /**
     * Either posts a job immediately if the chart has already setup it's dimensions or adds the job
     * to the execution queue.
     *
     * @param job
     */
    public void addViewportJob(Runnable job) {
        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    /**
     * Returns all jobs that are scheduled to be executed after onSizeChanged(...).
     */
    public List<Runnable> getJobs() {
        return mJobs;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = (int) Utils.convertDpToPixel(50f);

        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(), resolveSize(size, widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(), resolveSize(size, heightMeasureSpec))
        );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        if (mLogEnabled) {
            Log.i(LOG_TAG, "OnSizeChanged()");
        }

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            if (mLogEnabled) {
                Log.i(LOG_TAG, "Setting chart dimens, width: " + w + ", height: " + h);
            }

            mViewPortHandler.setChartDimens(w, h);
        } else {
            if (mLogEnabled) {
                Log.w(LOG_TAG, "*Avoiding* setting chart dimens! width: " + w + ", height: " + h);
            }
        }

        // This may cause the chart view to mutate properties affecting the view port
        // Lets do this before we try to run any pending jobs on the view port itself
        notifyDataSetChanged();

        for (Runnable r : mJobs) {
            post(r);
        }

        mJobs.clear();

        super.onSizeChanged(w, h, oldWidth, oldHeight);
    }

    /**
     * Setting this to true will set the layer-type HARDWARE for the view, false will set layer-type
     * SOFTWARE.
     *
     * @param enabled
     */
    public void setHardwareAccelerationEnabled(boolean enabled) {
        if (enabled) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mUnbind) {
            unbindDrawables(this);
        }
    }

    /**
     * Unbind flag.
     */
    private boolean mUnbind = false;

    /**
     * Unbind all drawables to avoid memory leaks.
     * Link: http://stackoverflow.com/a/6779164/1590502
     *
     * @param view
     */
    private void unbindDrawables(@NonNull View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                unbindDrawables(group.getChildAt(i));
            }

            group.removeAllViews();
        }
    }

    /**
     * Set this to true to enable "unbinding" of drawables. When a View is detached from a window.
     * This helps avoid memory leaks.
     * Link: http://stackoverflow.com/a/6779164/1590502
     *
     * @param enabled
     */
    public void setUnbindEnabled(boolean enabled) {
        this.mUnbind = enabled;
    }
}
