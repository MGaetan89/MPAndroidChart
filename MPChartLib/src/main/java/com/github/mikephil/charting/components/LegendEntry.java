package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.utils.ColorTemplate;

public class LegendEntry {
    public LegendEntry() {
    }

    /**
     * @param label              The legend entry text. A `null` label will start a group.
     * @param form               The form to draw for this entry.
     * @param formSize           Set to NaN to use the legend's default.
     * @param formLineWidth      Set to NaN to use the legend's default.
     * @param formLineDashEffect Set to nil to use the legend's default.
     * @param formColor          The color for drawing the form.
     */
    public LegendEntry(@Nullable String label, @NonNull Legend.LegendForm form, float formSize, float formLineWidth, @Nullable DashPathEffect formLineDashEffect, int formColor) {
        this.label = label;
        this.form = form;
        this.formSize = formSize;
        this.formLineWidth = formLineWidth;
        this.formLineDashEffect = formLineDashEffect;
        this.formColor = formColor;
    }

    /**
     * The legend entry text.
     * A {@code null} label will start a group.
     */
    @Nullable
    public String label;

    /**
     * The form to draw for this entry.
     * <p>
     * {@code LegendForm.NONE} will avoid drawing a form, and any related space.
     * {@code LegendForm.EMPTY} will avoid drawing a form, but keep its space.
     * {@code LegendForm.DEFAULT} will use the Legend's default.
     */
    @NonNull
    public Legend.LegendForm form = Legend.LegendForm.DEFAULT;

    /**
     * Form size will be considered except for when {@code LegendForm.NONE} is used.
     * <p>
     * Set as {@code Float.NaN} to use the legend's default.
     */
    public float formSize = Float.NaN;

    /**
     * Line width used for shapes that consist of lines.
     * <p>
     * Set to {@code Float.NaN} to use the legend's default.
     */
    public float formLineWidth = Float.NaN;

    /**
     * Line dash path effect used for shapes that consist of lines.
     * <p>
     * Set to {@code null} to use the legend's default.
     */
    @Nullable
    public DashPathEffect formLineDashEffect;

    /**
     * The color for drawing the form.
     */
    @ColorInt
    public int formColor = ColorTemplate.COLOR_NONE;
}
