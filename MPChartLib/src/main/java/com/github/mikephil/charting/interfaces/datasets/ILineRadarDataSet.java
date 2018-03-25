package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;

/**
 * @author Philipp Jahoda
 */
public interface ILineRadarDataSet<T extends Entry> extends ILineScatterCandleRadarDataSet<T> {
    /**
     * Returns the color that is used for filling the line surface area.
     */
    int getFillColor();

    /**
     * Returns the drawable used for filling the area below the line.
     */
    @Nullable
    Drawable getFillDrawable();

    /**
     * Returns the alpha value that is used for filling the line surface.
     */
    int getFillAlpha();

    /**
     * Returns the stroke-width of the drawn line.
     */
    float getLineWidth();

    /**
     * Returns true if filled drawing is enabled, false if not.
     */
    boolean isDrawFilledEnabled();

    /**
     * Set to true if the DataSet should be drawn filled (surface), and not just as a line,
     * disabling this will give great performance boost. Please note that this method uses the
     * canvas.clipPath(...) method for drawing the filled area. For devices with API level < 18
     * (Android 4.3), hardware acceleration of the chart should be turned off.
     *
     * @param enabled
     */
    void setDrawFilled(boolean enabled);
}
