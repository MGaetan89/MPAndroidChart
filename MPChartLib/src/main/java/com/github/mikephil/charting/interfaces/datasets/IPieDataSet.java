package com.github.mikephil.charting.interfaces.datasets;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

/**
 * @author Philipp Jahoda
 */
public interface IPieDataSet extends IDataSet<PieEntry> {
    /**
     * Returns the space that is set to be between the pie chart-slices of this DataSet, in pixels.
     */
    float getSliceSpace();

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be smaller than
     * the slice spacing itself.
     *
     * @return
     */
    boolean isAutomaticallyDisableSliceSpacingEnabled();

    /**
     * Returns the distance a highlighted pie chart slice is "shifted" away from the chart-center in
     * dp.
     */
    float getSelectionShift();

    @NonNull
    PieDataSet.ValuePosition getXValuePosition();

    @NonNull
    PieDataSet.ValuePosition getYValuePosition();

    /**
     * When valuePosition is OutsideSlice, use slice colors as line color if true.
     */
    boolean isUsingSliceColorAsValueLineColor();

    /**
     * When valuePosition is OutsideSlice, indicates line color.
     */
    @ColorInt
    int getValueLineColor();

    /**
     * When valuePosition is OutsideSlice, indicates line width.
     */
    float getValueLineWidth();

    /**
     * When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size.
     */
    float getValueLinePart1OffsetPercentage();

    /**
     * When valuePosition is OutsideSlice, indicates length of first half of the line.
     */
    float getValueLinePart1Length();

    /**
     * When valuePosition is OutsideSlice, indicates length of second half of the line.
     */
    float getValueLinePart2Length();

    /**
     * When valuePosition is OutsideSlice, this allows variable line length.
     */
    boolean isValueLineVariableLength();
}
