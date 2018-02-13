package com.github.mikephil.charting.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
 * Utilities class that has some helper methods. Needs to be initialized by
 * calling Utils.init(...) before usage. Inside the Chart.init() method, this is
 * done, if the Utils are used before that, Utils.init(...) needs to be called
 * manually.
 *
 * @author Philipp Jahoda
 */
public abstract class Utils {

    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;
    private static int mMaximumFlingVelocity = 8000;
    public final static double DEG2RAD = (Math.PI / 180.0);
    public final static float FDEG2RAD = ((float) Math.PI / 180.f);
    public final static float FLOAT_EPSILON = Float.intBitsToFloat(1);

    /**
     * initialize method, called inside the Chart.init() method.
     *
     * @param context
     */
    public static void init(@Nullable Context context) {

        if (context == null) {
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();

            Log.e("MPChartLib-Utils"
                    , "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");

        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

            Resources res = context.getResources();
            mMetrics = res.getDisplayMetrics();
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need
     *           to convert into pixels
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp) {

        if (mMetrics == null) {

            Log.e("MPChartLib-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                            " calling Utils.convertDpToPixel(...). Otherwise conversion does not " +
                            "take place.");
            return dp;
        }

        return dp * mMetrics.density;
    }

    /**
     * calculates the approximate width of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return
     */
    public static int calcTextWidth(@NonNull Paint paint, String demoText) {
        return (int) paint.measureText(demoText);
    }

    private static Rect mCalcTextHeightRect = new Rect();
    /**
     * calculates the approximate height of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return
     */
    public static int calcTextHeight(@NonNull Paint paint, @NonNull String demoText) {

        Rect r = mCalcTextHeightRect;
        r.set(0,0,0,0);
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }

    private static Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();

    public static float getLineHeight(@NonNull Paint paint) {
        return getLineHeight(paint, mFontMetrics);
    }

    public static float getLineHeight(@NonNull Paint paint, @NonNull Paint.FontMetrics fontMetrics){
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static float getLineSpacing(@NonNull Paint paint) {
        return getLineSpacing(paint, mFontMetrics);
    }

    public static float getLineSpacing(@NonNull Paint paint, @NonNull Paint.FontMetrics fontMetrics){
        paint.getFontMetrics(fontMetrics);
        return fontMetrics.ascent - fontMetrics.top + fontMetrics.bottom;
    }

    /**
     * Returns a recyclable FSize instance.
     * calculates the approximate size of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @return A Recyclable FSize instance
     */
    @NonNull
    public static FSize calcTextSize(@NonNull Paint paint, @NonNull String demoText) {

        FSize result = FSize.getInstance(0,0);
        calcTextSize(paint, demoText, result);
        return result;
    }

    private static Rect mCalcTextSizeRect = new Rect();
    /**
     * calculates the approximate size of a text, depending on a demo text
     * avoid repeated calls (e.g. inside drawing methods)
     *
     * @param paint
     * @param demoText
     * @param outputFSize An output variable, modified by the function.
     */
    private static void calcTextSize(@NonNull Paint paint, @NonNull String demoText, @NonNull FSize outputFSize) {

        Rect r = mCalcTextSizeRect;
        r.set(0,0,0,0);
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        outputFSize.width = r.width();
        outputFSize.height = r.height();

    }


    /**
     * Math.pow(...) is very expensive, so avoid calling it and create it
     * yourself.
     */
    private static final int POW_10[] = {
            1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
    };

    private static IValueFormatter mDefaultValueFormatter = generateDefaultValueFormatter();

    @NonNull
    private static IValueFormatter generateDefaultValueFormatter() {
		return new DefaultValueFormatter(1);
    }

    /// - returns: The default value formatter used for all chart components that needs a default
    @NonNull
    public static IValueFormatter getDefaultValueFormatter()
    {
        return mDefaultValueFormatter;
    }

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters. If thousands are separated, the separating
     * character is a dot (".").
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @return
     */
    public static String formatNumber(float number, int digitCount, boolean separateThousands) {
        return formatNumber(number, digitCount, separateThousands, '.');
    }

    /**
     * Formats the given number to the given number of decimals, and returns the
     * number as a string, maximum 35 characters.
     *
     * @param number
     * @param digitCount
     * @param separateThousands set this to true to separate thousands values
     * @param separateChar      a caracter to be paced between the "thousands"
     * @return
     */
    private static String formatNumber(float number, int digitCount, boolean separateThousands, char separateChar) {

        char[] out = new char[35];

        boolean neg = false;
        if (number == 0) {
            return "0";
        }

        boolean zero = false;
        if (number < 1 && number > -1) {
            zero = true;
        }

        if (number < 0) {
            neg = true;
            number = -number;
        }

        if (digitCount > POW_10.length) {
            digitCount = POW_10.length - 1;
        }

        number *= POW_10[digitCount];
        long lval = Math.round(number);
        int ind = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;

        while (lval != 0 || charCount < (digitCount + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[ind--] = (char) (digit + '0');
            charCount++;

            // add decimal point
            if (charCount == digitCount) {
                out[ind--] = ',';
                charCount++;
                decimalPointAdded = true;

                // add thousand separators
            } else if (separateThousands && lval != 0 && charCount > digitCount) {

                if (decimalPointAdded) {

                    if ((charCount - digitCount) % 4 == 0) {
                        out[ind--] = separateChar;
                        charCount++;
                    }

                } else {

                    if ((charCount - digitCount) % 4 == 3) {
                        out[ind--] = separateChar;
                        charCount++;
                    }
                }
            }
        }

        // if number around zero (between 1 and -1)
        if (zero) {
            out[ind--] = '0';
            charCount += 1;
        }

        // if the number is negative
        if (neg) {
            out[ind--] = '-';
            charCount += 1;
        }

        int start = out.length - charCount;

        // use this instead of "new String(...)" because of issue < Android 4.0
        return String.valueOf(out, start, out.length - start);
    }

    /**
     * rounds the given number to the next significant number
     *
     * @param number
     * @return
     */
    public static float roundToNextSignificant(double number) {
        if (Double.isInfinite(number) || 
            Double.isNaN(number) || 
            number == 0.0)
            return 0;
        
        final float d = (float) Math.ceil((float) Math.log10(number < 0 ? -number : number));
        final int pw = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }

    /**
     * Returns the appropriate number of decimals to be used for the provided
     * number.
     *
     * @param number
     * @return
     */
    public static int getDecimals(float number) {

        float i = roundToNextSignificant(number);
        
        if (Float.isInfinite(i))
            return 0;
        
        return (int) Math.ceil(-Math.log10(i)) + 2;
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    @NonNull
    public static MPPointF getPosition(@NonNull MPPointF center, float dist, float angle) {

        MPPointF p = MPPointF.getInstance(0,0);
        getPosition(center, dist, angle, p);
        return p;
    }

    public static void getPosition(@NonNull MPPointF center, float dist, float angle, @NonNull MPPointF outputPoint){
        outputPoint.x = (float) (center.x + dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y = (float) (center.y + dist * Math.sin(Math.toRadians(angle)));
    }

    public static void velocityTrackerPointerUpCleanUpIfNecessary(@NonNull MotionEvent ev,
                                                                  @NonNull VelocityTracker tracker) {

        // Check the dot product of current velocities.
        // If the pointer that left was opposing another velocity vector, clear.
        tracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        final int upIndex = ev.getActionIndex();
        final int id1 = ev.getPointerId(upIndex);
        final float x1 = tracker.getXVelocity(id1);
        final float y1 = tracker.getYVelocity(id1);
        for (int i = 0, count = ev.getPointerCount(); i < count; i++) {
            if (i == upIndex)
                continue;

            final int id2 = ev.getPointerId(i);
            final float x = x1 * tracker.getXVelocity(id2);
            final float y = y1 * tracker.getYVelocity(id2);

            final float dot = x + y;
            if (dot < 0) {
                tracker.clear();
                break;
            }
        }
    }

    /**
     * Original method view.postInvalidateOnAnimation() only supportd in API >=
     * 16, This is a replica of the code from ViewCompat.
     *
     * @param view
     */
    public static void postInvalidateOnAnimation(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.postInvalidateOnAnimation();
        else
            view.postInvalidateDelayed(10);
    }

    public static int getMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    public static int getMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    /**
     * returns an angle between 0.f < 360.f (not less than zero, less than 360)
     */
    public static float getNormalizedAngle(float angle) {
        while (angle < 0.f)
            angle += 360.f;

        return angle % 360.f;
    }

    private static Rect mDrawableBoundsCache = new Rect();

    public static void drawImage(@NonNull Canvas canvas,
                                 @NonNull Drawable drawable,
                                 int x, int y,
                                 int width, int height) {

        MPPointF drawOffset = MPPointF.getInstance();
        drawOffset.x = x - (width / 2);
        drawOffset.y = y - (height / 2);

        drawable.copyBounds(mDrawableBoundsCache);
        drawable.setBounds(
                mDrawableBoundsCache.left,
                mDrawableBoundsCache.top,
                mDrawableBoundsCache.left + width,
                mDrawableBoundsCache.top + width);

        int saveId = canvas.save();
        // translate to the correct position and draw
        canvas.translate(drawOffset.x, drawOffset.y);
        drawable.draw(canvas);
        canvas.restoreToCount(saveId);
    }

    private static Rect mDrawTextRectBuffer = new Rect();
    private static Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();

    public static void drawXAxisValue(@NonNull Canvas c, @NonNull String text, float x, float y,
                                      @NonNull Paint paint,
                                      @NonNull MPPointF anchor, float angleDegrees) {

        float drawOffsetX = 0.f;
        float drawOffsetY = 0.f;

        final float lineHeight = paint.getFontMetrics(mFontMetricsBuffer);
        paint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);

        // Android sometimes has pre-padding
        drawOffsetX -= mDrawTextRectBuffer.left;

        // Android does not snap the bounds to line boundaries,
        //  and draws from bottom to top.
        // And we want to normalize it.
        drawOffsetY += -mFontMetricsBuffer.ascent;

        // To have a consistent point of reference, we always draw left-aligned
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);

        if (angleDegrees != 0.f) {

            // Move the text drawing rect in a way that it always rotates around its center
            drawOffsetX -= mDrawTextRectBuffer.width() * 0.5f;
            drawOffsetY -= lineHeight * 0.5f;

            float translateX = x;
            float translateY = y;

            // Move the "outer" rect relative to the anchor, assuming its centered
            if (anchor.x != 0.5f || anchor.y != 0.5f) {
                final FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(
                        mDrawTextRectBuffer.width(),
                        lineHeight,
                        angleDegrees);

                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
                FSize.recycleInstance(rotatedSize);
            }

            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);

            c.drawText(text, drawOffsetX, drawOffsetY, paint);

            c.restore();
        } else {
            if (anchor.x != 0.f || anchor.y != 0.f) {

                drawOffsetX -= mDrawTextRectBuffer.width() * anchor.x;
                drawOffsetY -= lineHeight * anchor.y;
            }

            drawOffsetX += x;
            drawOffsetY += y;

            c.drawText(text, drawOffsetX, drawOffsetY, paint);
        }

        paint.setTextAlign(originalTextAlign);
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by degrees.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param degrees
     * @return A Recyclable FSize instance
     */
    @NonNull
    public static FSize getSizeOfRotatedRectangleByDegrees(float rectangleWidth, float
            rectangleHeight, float degrees) {
        final float radians = degrees * FDEG2RAD;
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, radians);
    }

    /**
     * Returns a recyclable FSize instance.
     * Represents size of a rotated rectangle by radians.
     *
     * @param rectangleWidth
     * @param rectangleHeight
     * @param radians
     * @return A Recyclable FSize instance
     */
    @NonNull
    private static FSize getSizeOfRotatedRectangleByRadians(float rectangleWidth, float
			rectangleHeight, float radians) {
        return FSize.getInstance(
                Math.abs(rectangleWidth * (float) Math.cos(radians)) + Math.abs(rectangleHeight *
                        (float) Math.sin(radians)),
                Math.abs(rectangleWidth * (float) Math.sin(radians)) + Math.abs(rectangleHeight *
                        (float) Math.cos(radians))
        );
    }
}
