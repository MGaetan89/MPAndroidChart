package com.github.mikephil.charting.highlight;

import android.support.annotation.Nullable;

/**
 * @author Philipp Jahoda
 */
public interface IHighlighter {
    /**
     * Returns a Highlight object corresponding to the given x- and y- touch positions in pixels.
     *
     * @param x
     * @param y
     */
    @Nullable
    Highlight getHighlight(float x, float y);
}
