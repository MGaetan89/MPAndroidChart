package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 */
public class ChevronDownShapeRenderer implements IShapeRenderer {
    @Override
    public void renderShape(
            Canvas canvas, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
            float posX, float posY, Paint renderPaint
    ) {
        final float shapeHalf = dataSet.getScatterShapeSize() / 2f;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        canvas.drawLine(posX, posY + (2f * shapeHalf), posX + (2f * shapeHalf), posY, renderPaint);
        canvas.drawLine(posX, posY + (2f * shapeHalf), posX - (2f * shapeHalf), posY, renderPaint);
    }
}
