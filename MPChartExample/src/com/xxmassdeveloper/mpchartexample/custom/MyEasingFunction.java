package com.xxmassdeveloper.mpchartexample.custom;

import android.animation.TimeInterpolator;

/**
 * Example of a custom made animation TimeInterpolator.
 * 
 * @author Philipp Jahoda
 */
public class MyEasingFunction implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        // do awesome stuff here, this is just linear easing
        return input;
    }
}
