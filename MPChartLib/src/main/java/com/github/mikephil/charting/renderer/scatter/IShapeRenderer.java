package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 */
public interface IShapeRenderer {
    /**
     * Renders the provided ScatterDataSet with a shape.
     *
     * @param canvas          Canvas object for drawing the shape
     * @param dataSet         The DataSet to be drawn
     * @param viewPortHandler Contains information about the current state of the view
     * @param posX            Position to draw the shape at
     * @param posY            Position to draw the shape at
     * @param renderPaint     Paint object used for styling and drawing
     */
    void renderShape(
            Canvas canvas, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
            float posX, float posY, Paint renderPaint
    );
}
