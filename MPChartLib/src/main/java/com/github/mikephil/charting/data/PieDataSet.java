package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class PieDataSet extends DataSet<PieEntry> implements IPieDataSet {

    /**
     * The space in pixels between the chart-slices.
     */
    private float mSliceSpace = 0f;
    private boolean mAutomaticallyDisableSliceSpacing;

    /**
     * Indicates the selection distance of a pie slice.
     */
    private float mShift = 18f;

    @NonNull
    private ValuePosition mXValuePosition = ValuePosition.INSIDE_SLICE;
    @NonNull
    private ValuePosition mYValuePosition = ValuePosition.INSIDE_SLICE;
    private boolean mUsingSliceColorAsValueLineColor = false;
    @ColorInt
    private int mValueLineColor = 0xff000000;
    private float mValueLineWidth = 1f;
    private float mValueLinePart1OffsetPercentage = 75f;
    private float mValueLinePart1Length = 0.3f;
    private float mValueLinePart2Length = 0.4f;
    private boolean mValueLineVariableLength = true;

    public PieDataSet(List<PieEntry> yValues, String label) {
        super(yValues, label);
    }

    @NonNull
    @Override
    public DataSet<PieEntry> copy() {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < mValues.size(); i++) {
            entries.add(mValues.get(i).copy());
        }
        PieDataSet copied = new PieDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    @Override
    protected void calcMinMax(@NonNull PieEntry entry) {
        calcMinMaxY(entry);
    }

    /**
     * Sets the space that is left out between the pie chart-slices in dp. Min 0f no space,
     * maximum 20f.
     *
     * @param spaceDp
     */
    public void setSliceSpace(float spaceDp) {
        if (spaceDp > 20f) {
            spaceDp = 20f;
        }

        if (spaceDp < 0f) {
            spaceDp = 0f;
        }

        mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    @Override
    public float getSliceSpace() {
        return mSliceSpace;
    }

    /**
     * When enabled, slice spacing will be 0f when the smallest value is going to be smaller than
     * the slice spacing itself.
     *
     * @param autoDisable
     */
    public void setAutomaticallyDisableSliceSpacing(boolean autoDisable) {
        mAutomaticallyDisableSliceSpacing = autoDisable;
    }

    /**
     * When enabled, slice spacing will be 0f when the smallest value is going to be smaller than
     * the slice spacing itself.
     *
     * @return
     */
    @Override
    public boolean isAutomaticallyDisableSliceSpacingEnabled() {
        return mAutomaticallyDisableSliceSpacing;
    }

    /**
     * Sets the distance the highlighted pie chart-slice of this DataSet is "shifted" away from the
     * center of the chart.
     *
     * @param shift
     */
    public void setSelectionShift(float shift) {
        mShift = Utils.convertDpToPixel(shift);
    }

    @Override
    public float getSelectionShift() {
        return mShift;
    }

    @NonNull
    @Override
    public ValuePosition getXValuePosition() {
        return mXValuePosition;
    }

    public void setXValuePosition(@NonNull ValuePosition xValuePosition) {
        this.mXValuePosition = xValuePosition;
    }

    @NonNull
    @Override
    public ValuePosition getYValuePosition() {
        return mYValuePosition;
    }

    public void setYValuePosition(@NonNull ValuePosition yValuePosition) {
        this.mYValuePosition = yValuePosition;
    }

    /**
     * When valuePosition is OutsideSlice, indicates line color.
     */
    @Override
    public boolean isUsingSliceColorAsValueLineColor() {
        return mUsingSliceColorAsValueLineColor;
    }

    public void setUsingSliceColorAsValueLineColor(boolean usingSliceColorAsValueLineColor) {
        this.mUsingSliceColorAsValueLineColor = usingSliceColorAsValueLineColor;
    }

    /**
     * When valuePosition is OutsideSlice, indicates line color
     */
    @ColorInt
    @Override
    public int getValueLineColor() {
        return mValueLineColor;
    }

    public void setValueLineColor(@ColorInt int valueLineColor) {
        this.mValueLineColor = valueLineColor;
    }

    /**
     * When valuePosition is OutsideSlice, indicates line width.
     */
    @Override
    public float getValueLineWidth() {
        return mValueLineWidth;
    }

    public void setValueLineWidth(float valueLineWidth) {
        this.mValueLineWidth = valueLineWidth;
    }

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size.
     */
    @Override
    public float getValueLinePart1OffsetPercentage() {
        return mValueLinePart1OffsetPercentage;
    }

    public void setValueLinePart1OffsetPercentage(float valueLinePart1OffsetPercentage) {
        this.mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage;
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line.
     */
    @Override
    public float getValueLinePart1Length() {
        return mValueLinePart1Length;
    }

    public void setValueLinePart1Length(float valueLinePart1Length) {
        this.mValueLinePart1Length = valueLinePart1Length;
    }

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line.
     */
    @Override
    public float getValueLinePart2Length() {
        return mValueLinePart2Length;
    }

    public void setValueLinePart2Length(float valueLinePart2Length) {
        this.mValueLinePart2Length = valueLinePart2Length;
    }

    /**
     * When valuePosition is OutsideSlice, this allows variable line length.
     */
    @Override
    public boolean isValueLineVariableLength() {
        return mValueLineVariableLength;
    }

    public void setValueLineVariableLength(boolean valueLineVariableLength) {
        this.mValueLineVariableLength = valueLineVariableLength;
    }

    public enum ValuePosition {
        INSIDE_SLICE,
        OUTSIDE_SLICE
    }
}
