package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.renderer.scatter.IShapeRenderer;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * @author Philipp Jahoda
 */
public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {
    /**
     * Returns the currently set scatter shape size.
     */
    float getScatterShapeSize();

    /**
     * Returns radius of the hole in the shape.
     */
    float getScatterShapeHoleRadius();

    /**
     * Returns the color for the hole in the shape.
     */
    @ColorInt
    int getScatterShapeHoleColor();

    /**
     * Returns the IShapeRenderer responsible for rendering this DataSet.
     */
    @NonNull
    IShapeRenderer getShapeRenderer();
}
