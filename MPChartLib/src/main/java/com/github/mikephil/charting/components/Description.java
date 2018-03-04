package com.github.mikephil.charting.components;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.utils.MPPointF;

/**
 * @author Philipp Jahoda
 */
public class Description extends ComponentBase {
    /**
     * The text used in the description.
     */
    @NonNull
    private String text = "Description Label";

    /**
     * The custom position of the description text.
     */
    @Nullable
    private MPPointF mPosition;

    /**
     * The alignment of the description text.
     */
    private Paint.Align mTextAlign = Paint.Align.RIGHT;

    public Description() {
        super();

        // Default size
        setTextSize(8f);
    }

    /**
     * Sets the text to be shown as the description.
     *
     * @param text
     */
    public void setText(@Nullable String text) {
        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }
    }

    /**
     * Returns the description text.
     */
    @NonNull
    public String getText() {
        return text;
    }

    /**
     * Sets a custom position for the description text in pixels on the screen.
     *
     * @param x - x coordinate
     * @param y - y coordinate
     */
    public void setPosition(float x, float y) {
        if (mPosition == null) {
            mPosition = MPPointF.getInstance(x, y);
        } else {
            mPosition.x = x;
            mPosition.y = y;
        }
    }

    /**
     * Returns the customized position of the description, or null if none set.
     */
    @Nullable
    public MPPointF getPosition() {
        return mPosition;
    }

    /**
     * Sets the text alignment of the description text.
     *
     * @param align
     */
    public void setTextAlign(Paint.Align align) {
        this.mTextAlign = align;
    }

    /**
     * Returns the text alignment of the description.
     */
    public Paint.Align getTextAlign() {
        return mTextAlign;
    }
}
