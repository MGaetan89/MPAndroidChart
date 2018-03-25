package com.github.mikephil.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;

import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LineDataSet extends LineRadarDataSet<Entry> implements ILineDataSet {
    /**
     * Drawing mode for this line data set.
     **/
    private LineDataSet.Mode mMode = Mode.LINEAR;

    /**
     * List representing all colors that are used for the circles.
     */
    @ColorInt
    @NonNull
    private List<Integer> mCircleColors = Collections.emptyList();

    /**
     * The color of the inner circles.
     */
    @ColorInt
    private int mCircleHoleColor = Color.WHITE;

    /**
     * The radius of the circle-shaped value indicators.
     */
    private float mCircleRadius = 8f;

    /**
     * The hole radius of the circle-shaped value indicators.
     */
    private float mCircleHoleRadius = 4f;

    /**
     * sets the intensity of the cubic lines.
     */
    private float mCubicIntensity = 0.2f;

    /**
     * The path effect of this DataSet that makes dashed lines possible.
     */
    @Nullable
    private DashPathEffect mDashPathEffect = null;

    /**
     * Formatter for customizing the position of the fill-line.
     */
    @NonNull
    private IFillFormatter mFillFormatter = new DefaultFillFormatter();

    /**
     * If true, drawing circles is enabled.
     */
    private boolean mDrawCircles = true;

    private boolean mDrawCircleHole = true;

    public LineDataSet(List<Entry> yValues, String label) {
        super(yValues, label);

        // Default colors
        mCircleColors.add(Color.rgb(140, 234, 255));
    }

    @NonNull
    @Override
    public DataSet<Entry> copy() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        LineDataSet copied = new LineDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(LineDataSet lineDataSet) {
        super.copy(lineDataSet);
        lineDataSet.mCircleColors = mCircleColors;
        lineDataSet.mCircleHoleColor = mCircleHoleColor;
        lineDataSet.mCircleHoleRadius = mCircleHoleRadius;
        lineDataSet.mCircleRadius = mCircleRadius;
        lineDataSet.mCubicIntensity = mCubicIntensity;
        lineDataSet.mDashPathEffect = mDashPathEffect;
        lineDataSet.mDrawCircleHole = mDrawCircleHole;
        lineDataSet.mDrawCircles = mDrawCircleHole;
        lineDataSet.mFillFormatter = mFillFormatter;
        lineDataSet.mMode = mMode;
    }

    /**
     * Returns the drawing mode for this line data set.
     */
    @Override
    public LineDataSet.Mode getMode() {
        return mMode;
    }

    /**
     * Returns the drawing mode for this LineDataSet.
     */
    public void setMode(LineDataSet.Mode mode) {
        mMode = mode;
    }

    /**
     * Sets the intensity for cubic lines (if enabled). Max 1f = very cubic, Min 0.05f = low cubic
     * effect.
     *
     * @param intensity
     */
    public void setCubicIntensity(float intensity) {
        if (intensity > 1f) {
            intensity = 1f;
        }

        if (intensity < 0.05f) {
            intensity = 0.05f;
        }

        mCubicIntensity = intensity;
    }

    @Override
    public float getCubicIntensity() {
        return mCubicIntensity;
    }

    /**
     * Sets the radius of the drawn circles, min = 1f.
     *
     * @param radius
     */
    public void setCircleRadius(float radius) {
        if (radius >= 1f) {
            mCircleRadius = Utils.convertDpToPixel(radius);
        } else {
            Log.e("LineDataSet", "Circle radius cannot be < 1");
        }
    }

    @Override
    public float getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * Sets the hole radius of the drawn circles, min = 0.5f.
     *
     * @param holeRadius
     */
    public void setCircleHoleRadius(float holeRadius) {
        if (holeRadius >= 0.5f) {
            mCircleHoleRadius = Utils.convertDpToPixel(holeRadius);
        } else {
            Log.e("LineDataSet", "Circle radius cannot be < 0.5");
        }
    }

    @Override
    public float getCircleHoleRadius() {
        return mCircleHoleRadius;
    }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this "- - - - - -". THIS ONLY WORKS IF
     * HARDWARE-ACCELERATION IS TURNED OFF. Keep in mind that hardware acceleration boosts
     * performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    @Override
    public boolean isDashedLineEnabled() {
        return mDashPathEffect != null;
    }

    @Nullable
    @Override
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    /**
     * Set this to true to enable the drawing of circle indicators for this DataSet.
     *
     * @param enabled
     */
    public void setDrawCircles(boolean enabled) {
        this.mDrawCircles = enabled;
    }

    @Override
    public boolean isDrawCirclesEnabled() {
        return mDrawCircles;
    }

    /**
     * Returns all colors specified for the circles.
     */
    public List<Integer> getCircleColors() {
        return mCircleColors;
    }

    @ColorInt
    @Override
    public int getCircleColor(int index) {
        return mCircleColors.get(index);
    }

    @Override
    public int getCircleColorCount() {
        return mCircleColors.size();
    }

    /**
     * Sets the colors that should be used for the circles of this DataSet. Colors are reused as
     * soon as the number of Entries the DataSet represents is higher than the size of the colors
     * array.
     *
     * @param colors
     */
    public void setCircleColors(@ColorInt @NonNull List<Integer> colors) {
        mCircleColors = colors;
    }

    /**
     * Sets the colors that should be used for the circles of this DataSet. Colors are reused as
     * soon as the number of Entries the DataSet represents is higher than the size of the colors
     * array.
     *
     * @param colors
     */
    public void setCircleColors(@ColorInt @NonNull int... colors) {
        this.mCircleColors = ColorTemplate.createColors(colors);
    }

    /**
     * Sets the colors that should be used for the circles of this DataSet. Colors are reused as
     * soon as the number of Entries the DataSet represents is higher than the size of the colors
     * array. You can use "new String[] { R.color.red, R.color.green, ... }" to provide colors for
     * this method. Internally, the colors are resolved using getResources().getColor(...).
     *
     * @param colors
     */
    public void setCircleColors(@ColorRes int[] colors, @NonNull Context context) {
        List<Integer> newColors = new ArrayList<>();
        for (int color : colors) {
            newColors.add(context.getResources().getColor(color));
        }

        mCircleColors = newColors;
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet. Internally, this recreates
     * the colors array and adds the specified color.
     *
     * @param color
     */
    public void setCircleColor(int color) {
        resetCircleColors();
        mCircleColors.add(color);
    }

    /**
     * Resets the circle-colors array and creates a new one.
     */
    public void resetCircleColors() {
        mCircleColors.clear();
    }

    /**
     * Sets the color of the inner circle of the line-circles.
     *
     * @param color
     */
    public void setCircleHoleColor(int color) {
        mCircleHoleColor = color;
    }

    @Override
    public int getCircleHoleColor() {
        return mCircleHoleColor;
    }

    /**
     * Set this to true to allow drawing a hole in each data circle.
     *
     * @param enabled
     */
    public void setDrawCircleHole(boolean enabled) {
        mDrawCircleHole = enabled;
    }

    @Override
    public boolean isDrawCircleHoleEnabled() {
        return mDrawCircleHole;
    }

    /**
     * Sets a custom IFillFormatter to the chart that handles the position of the filled-line for
     * each DataSet. Set this to null to use the default logic.
     *
     * @param formatter
     */
    public void setFillFormatter(@Nullable IFillFormatter formatter) {
        if (formatter == null) {
            mFillFormatter = new DefaultFillFormatter();
        } else {
            mFillFormatter = formatter;
        }
    }

    @NonNull
    @Override
    public IFillFormatter getFillFormatter() {
        return mFillFormatter;
    }

    public enum Mode {
        LINEAR,
        STEPPED,
        CUBIC_BEZIER,
        HORIZONTAL_BEZIER
    }
}
