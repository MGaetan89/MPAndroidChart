package com.github.mikephil.charting.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

/**
 * Utilities class that has some helper methods. Needs to be initialized by calling
 * Utils.init(Context) before usage.
 *
 * @author Philipp Jahoda
 */
public final class Utils {
    public static final double DEG2RAD = Math.PI / 180.0;
    public static final float FDEG2RAD = (float) Math.PI / 180f;
    public static final float FLOAT_EPSILON = Float.intBitsToFloat(1);

    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;
    private static int mMaximumFlingVelocity = 8000;

    @NonNull
    private static final Rect mCalcTextHeightRect = new Rect();

    @NonNull
    private static final Rect mCalcTextSizeRect = new Rect();

    @NonNull
    private static final Rect mDrawableBoundsCache = new Rect();

    @NonNull
    private static final Rect mDrawTextRectBuffer = new Rect();

    @NonNull
    private static final Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    @NonNull
    private static final IValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(1);

    /**
     * Initialize method.
     *
     * @param context
     */
    public static void init(@Nullable Context context) {
        if (context == null) {
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            mMetrics = null;

            Log.e("MPChartLib-Utils", "Utils.init(Context) PROVIDED CONTEXT OBJECT IS NULL");
        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            mMetrics = context.getResources().getDisplayMetrics();
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     * NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp) {
        if (mMetrics == null) {
            Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call " +
                    "Utils.init(Context) before calling Utils.convertDpToPixel(float). " +
                    "Otherwise conversion does not take place.");

            return dp;
        }

        return dp * mMetrics.density;
    }

    /**
     * Calculates the approximate width of a text, based on a sample text.
     * Avoid repeated calls (e.g. inside drawing methods).
     *
     * @param paint
     * @param text
     * @return
     */
    public static int calcTextWidth(@NonNull Paint paint, @NonNull String text) {
        return (int) paint.measureText(text);
    }

    /**
     * Calculates the approximate height of a text, based on a sample text.
     * Avoid repeated calls (e.g. inside drawing methods).
     *
     * @param paint
     * @param text
     * @return
     */
    public static int calcTextHeight(@NonNull Paint paint, @NonNull String text) {
        Rect rect = mCalcTextHeightRect;
        rect.setEmpty();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public static float getLineHeight(@NonNull Paint paint) {
        return getLineHeight(paint, mFontMetrics);
    }

    public static float getLineHeight(@NonNull Paint paint, @NonNull Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static float getLineSpacing(@NonNull Paint paint) {
        return getLineSpacing(paint, mFontMetrics);
    }

    public static float getLineSpacing(@NonNull Paint paint, @NonNull Paint.FontMetrics fontMetrics) {
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.ascent - fontMetrics.top + fontMetrics.bottom;
    }

    /**
     * Calculates the approximate size of a text, based on a sample text.
     * Avoid repeated calls (e.g. inside drawing methods).
     *
     * @param paint
     * @param text
     * @return A Recyclable FSize instance
     */
    @NonNull
    public static FSize calcTextSize(@NonNull Paint paint, @NonNull String text) {
        FSize result = FSize.getInstance(0, 0);
        calcTextSize(paint, text, result);
        return result;
    }

    /**
     * Calculates the approximate size of a text, based on a sample text.
     * Avoid repeated calls (e.g. inside drawing methods).
     *
     * @param paint
     * @param text
     * @param outputSize An output variable, modified by the function.
     */
    private static void calcTextSize(@NonNull Paint paint, @NonNull String text, @NonNull FSize outputSize) {
        Rect rect = mCalcTextSizeRect;
        rect.setEmpty();
        paint.getTextBounds(text, 0, text.length(), rect);

        outputSize.width = rect.width();
        outputSize.height = rect.height();
    }

    /**
     * @return The default value formatter used for all chart components that needs a default
     */
    @NonNull
    public static IValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }

    /**
     * Rounds the given number to the next significant number.
     *
     * @param number
     * @return
     */
    public static float roundToNextSignificant(double number) {
        if (Double.isInfinite(number) || Double.isNaN(number) || number == 0.0) {
            return 0;
        }

        final float d = (float) Math.ceil((float) Math.log10(Math.abs(number)));
        final int pow = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pow);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }

    /**
     * Returns the appropriate number of decimals to be used for the provided number.
     *
     * @param number
     */
    public static int getDecimals(float number) {
        float nextSignificant = roundToNextSignificant(number);
        if (Float.isInfinite(nextSignificant)) {
            return 0;
        }

        return (int) Math.ceil(-Math.log10(nextSignificant)) + 2;
    }

    /**
     * Calculates the position around a center point, depending on the distance from the center, and
     * the angle of the position around the center.
     *
     * @param center
     * @param distance
     * @param angle    in degrees, converted to radians internally
     * @return A recyclable MPPointF instance
     */
    @NonNull
    public static MPPointF getPosition(@NonNull MPPointF center, float distance, float angle) {
        MPPointF point = MPPointF.getInstance(0f, 0f);
        getPosition(center, distance, angle, point);
        return point;
    }

    public static void getPosition(@NonNull MPPointF center, float distance, float angle, @NonNull MPPointF outputPoint) {
        outputPoint.x = (float) (center.x + distance * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float) (center.y + distance * Math.sin(Math.toRadians(angle)));
    }

    public static void velocityTrackerPointerUpCleanUpIfNecessary(@NonNull MotionEvent event, @NonNull VelocityTracker tracker) {
        // Check the dot product of current velocities.
        // If the pointer that left was opposing another velocity vector, clear.
        tracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        final int upIndex = event.getActionIndex();
        final int id1 = event.getPointerId(upIndex);
        final float x1 = tracker.getXVelocity(id1);
        final float y1 = tracker.getYVelocity(id1);
        for (int i = 0, count = event.getPointerCount(); i < count; i++) {
            if (i != upIndex) {
                final int id2 = event.getPointerId(i);
                final float x = x1 * tracker.getXVelocity(id2);
                final float y = y1 * tracker.getYVelocity(id2);
                final float dot = x + y;

                if (dot < 0) {
                    tracker.clear();
                    break;
                }
            }
        }
    }

    /**
     * Original method View#postInvalidateOnAnimation() is only supported in API >= 16.
     * This is a replica of the code from ViewCompat.
     *
     * @param view
     */
    public static void postInvalidateOnAnimation(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidateDelayed(10);
        }
    }

    public static int getMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    public static int getMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    /**
     * Returns an angle between 0 and 360 (excluded).
     */
    @FloatRange(from = 0f, to = 360f, toInclusive = false)
    public static float getNormalizedAngle(float angle) {
        while (angle < 0f) {
            angle += 360f;
        }

        return angle % 360f;
    }

    public static void drawImage(@NonNull Canvas canvas, @NonNull Drawable drawable, int x, int y, int width, int height) {
        MPPointF drawOffset = MPPointF.getInstance();
        drawOffset.x = x - (width / 2f);
        drawOffset.y = y - (height / 2f);

        drawable.copyBounds(mDrawableBoundsCache);
        drawable.setBounds(
                mDrawableBoundsCache.left, mDrawableBoundsCache.top,
                mDrawableBoundsCache.left + width, mDrawableBoundsCache.top + width
        );

        int saveId = canvas.save();

        // Translate to the correct position and draw
        canvas.translate(drawOffset.x, drawOffset.y);
        drawable.draw(canvas);
        canvas.restoreToCount(saveId);
    }

    public static void drawXAxisValue(
            @NonNull Canvas canvas, @NonNull String text, float x, float y,
            @NonNull Paint paint, @NonNull MPPointF anchor, float angleDegrees
    ) {
        float drawOffsetX = 0f;
        float drawOffsetY = 0f;

        final float lineHeight = paint.getFontMetrics(mFontMetrics);
        paint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);

        // Android sometimes has pre-padding
        drawOffsetX -= mDrawTextRectBuffer.left;

        // Android does not snap the bounds to line boundaries, and draws from bottom to top.
        // And we want to normalize it.
        drawOffsetY += -mFontMetrics.ascent;

        // To have a consistent point of reference, we always draw left-aligned
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);

        if (angleDegrees != 0f) {
            // Move the text drawing rect in a way that it always rotates around its center
            drawOffsetX -= mDrawTextRectBuffer.width() * 0.5f;
            drawOffsetY -= lineHeight * 0.5f;

            float translateX = x;
            float translateY = y;

            // Move the "outer" rect relative to the anchor, assuming its centered
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                final FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(
                        mDrawTextRectBuffer.width(), lineHeight, angleDegrees
                );

                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
                FSize.recycleInstance(rotatedSize);
            }

            canvas.save();
            canvas.translate(translateX, translateY);
            canvas.rotate(angleDegrees);

            canvas.drawText(text, drawOffsetX, drawOffsetY, paint);
            canvas.restore();
        } else {
            if (anchor.x != 0f || anchor.y != 0f) {
                drawOffsetX -= mDrawTextRectBuffer.width() * anchor.x;
                drawOffsetY -= lineHeight * anchor.y;
            }

            drawOffsetX += x;
            drawOffsetY += y;

            canvas.drawText(text, drawOffsetX, drawOffsetY, paint);
        }

        paint.setTextAlign(originalTextAlign);
    }

    /**
     * Represents size of a rotated rectangle by degrees.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param degrees
     * @return A Recyclable FSize instance
     */
    @NonNull
    public static FSize getSizeOfRotatedRectangleByDegrees(float rectangleWidth, float rectangleHeight, float degrees) {
        final float radians = degrees * FDEG2RAD;
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, radians);
    }

    /**
     * Represents size of a rotated rectangle by radians.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param radians
     * @return A Recyclable FSize instance
     */
    @NonNull
    private static FSize getSizeOfRotatedRectangleByRadians(float rectangleWidth, float rectangleHeight, float radians) {
        return FSize.getInstance(
                Math.abs(rectangleWidth * (float) Math.cos(radians)) + Math.abs(rectangleHeight * (float) Math.sin(radians)),
                Math.abs(rectangleWidth * (float) Math.sin(radians)) + Math.abs(rectangleHeight * (float) Math.cos(radians))
        );
    }
}
