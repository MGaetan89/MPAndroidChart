package com.github.mikephil.charting.model;

import android.support.annotation.ColorInt;

public class GradientColor {
    @ColorInt
    private int startColor;
    @ColorInt
    private int endColor;

    public GradientColor(@ColorInt int startColor, @ColorInt int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @ColorInt
    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(@ColorInt int startColor) {
        this.startColor = startColor;
    }

    @ColorInt
    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(@ColorInt int endColor) {
        this.endColor = endColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GradientColor)) {
            return false;
        }

        GradientColor other = (GradientColor) obj;
        return this.startColor == other.startColor && this.endColor == other.endColor;
    }
}
