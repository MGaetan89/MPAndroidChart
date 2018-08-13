package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.utils.ViewPortHandler;

import androidx.annotation.NonNull;

/**
 * @author Philipp Jahoda
 */
public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {
    public LineRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws the provided path in filled mode with the provided drawable.
     *
     * @param canvas
     * @param filledPath
     * @param drawable
     */
    protected void drawFilledPath(@NonNull Canvas canvas, Path filledPath, @NonNull Drawable drawable) {
        if (clipPathSupported()) {
            int save = canvas.save();
            canvas.clipPath(filledPath);

            drawable.setBounds(
                    (int) mViewPortHandler.contentLeft(),
                    (int) mViewPortHandler.contentTop(),
                    (int) mViewPortHandler.contentRight(),
                    (int) mViewPortHandler.contentBottom()
            );
            drawable.draw(canvas);

            canvas.restoreToCount(save);
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18.");
        }
    }

    /**
     * Draws the provided path in filled mode with the provided color and alpha. Special thanks to
     * Angelo Suzuki (https://github.com/tinsukE) for this.
     *
     * @param canvas
     * @param filledPath
     * @param fillColor
     * @param fillAlpha
     */
    protected void drawFilledPath(@NonNull Canvas canvas, Path filledPath, int fillColor, int fillAlpha) {
        int color = (fillAlpha << 24) | (fillColor & 0xffffff);
        if (clipPathSupported()) {
            int save = canvas.save();
            canvas.clipPath(filledPath);
            canvas.drawColor(color);
            canvas.restoreToCount(save);
        } else {
            // Save
            Paint.Style previous = mRenderPaint.getStyle();
            int previousColor = mRenderPaint.getColor();

            // Set
            mRenderPaint.setStyle(Paint.Style.FILL);
            mRenderPaint.setColor(color);

            canvas.drawPath(filledPath, mRenderPaint);

            // Restore
            mRenderPaint.setColor(previousColor);
            mRenderPaint.setStyle(previous);
        }
    }

    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     */
    private boolean clipPathSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}
