package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;

/**
 * Easing options.
 * 
 * @author Daniel Cohen Gindi
 */
public class Easing {
    public enum EasingOption {
        Linear,
        EaseInQuad,
        EaseOutQuad,
        EaseInOutQuad,
        EaseInCubic,
        EaseOutCubic,
        EaseInOutCubic,
        EaseInQuart,
        EaseOutQuart,
        EaseInOutQuart,
        EaseInSine,
        EaseOutSine,
        EaseInOutSine,
        EaseInExpo,
        EaseOutExpo,
        EaseInOutExpo,
        EaseInCirc,
        EaseOutCirc,
        EaseInOutCirc,
        EaseInElastic,
        EaseOutElastic,
        EaseInOutElastic,
        EaseInBack,
        EaseOutBack,
        EaseInOutBack,
        EaseInBounce,
        EaseOutBounce,
        EaseInOutBounce,
    }

    @NonNull
    public static TimeInterpolator getEasingFunctionFromOption(@NonNull EasingOption easing) {
        switch (easing) {
            default:
            case Linear:
                return Easing.EasingFunctions.Linear;
            case EaseInQuad:
                return Easing.EasingFunctions.EaseInQuad;
            case EaseOutQuad:
                return Easing.EasingFunctions.EaseOutQuad;
            case EaseInOutQuad:
                return Easing.EasingFunctions.EaseInOutQuad;
            case EaseInCubic:
                return Easing.EasingFunctions.EaseInCubic;
            case EaseOutCubic:
                return Easing.EasingFunctions.EaseOutCubic;
            case EaseInOutCubic:
                return Easing.EasingFunctions.EaseInOutCubic;
            case EaseInQuart:
                return Easing.EasingFunctions.EaseInQuart;
            case EaseOutQuart:
                return Easing.EasingFunctions.EaseOutQuart;
            case EaseInOutQuart:
                return Easing.EasingFunctions.EaseInOutQuart;
            case EaseInSine:
                return Easing.EasingFunctions.EaseInSine;
            case EaseOutSine:
                return Easing.EasingFunctions.EaseOutSine;
            case EaseInOutSine:
                return Easing.EasingFunctions.EaseInOutSine;
            case EaseInExpo:
                return Easing.EasingFunctions.EaseInExpo;
            case EaseOutExpo:
                return Easing.EasingFunctions.EaseOutExpo;
            case EaseInOutExpo:
                return Easing.EasingFunctions.EaseInOutExpo;
            case EaseInCirc:
                return Easing.EasingFunctions.EaseInCirc;
            case EaseOutCirc:
                return Easing.EasingFunctions.EaseOutCirc;
            case EaseInOutCirc:
                return Easing.EasingFunctions.EaseInOutCirc;
            case EaseInElastic:
                return Easing.EasingFunctions.EaseInElastic;
            case EaseOutElastic:
                return Easing.EasingFunctions.EaseOutElastic;
            case EaseInOutElastic:
                return Easing.EasingFunctions.EaseInOutElastic;
            case EaseInBack:
                return Easing.EasingFunctions.EaseInBack;
            case EaseOutBack:
                return Easing.EasingFunctions.EaseOutBack;
            case EaseInOutBack:
                return Easing.EasingFunctions.EaseInOutBack;
            case EaseInBounce:
                return Easing.EasingFunctions.EaseInBounce;
            case EaseOutBounce:
                return Easing.EasingFunctions.EaseOutBounce;
            case EaseInOutBounce:
                return Easing.EasingFunctions.EaseInOutBounce;
        }
    }
    
    private static class EasingFunctions {
        
        /**
         * ########## ########## ########## ########## ########## ##########
         * PREDEFINED EASING FUNCTIONS BELOW THIS
         */

        public static final TimeInterpolator Linear = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        };

        public static final TimeInterpolator EaseInQuad = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input * input;
            }
        };

        public static final TimeInterpolator EaseOutQuad = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return -input * (input - 2f);
            }
        };

        public static final TimeInterpolator EaseInOutQuad = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {

                float position = input / 0.5f;

                if (position < 1.f) {
                    return 0.5f * position * position;
                }

                return -0.5f * ((--position) * (position - 2.f) - 1.f);
            }
        };

        public static final TimeInterpolator EaseInCubic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input * input * input;
            }
        };

        public static final TimeInterpolator EaseOutCubic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                input--;
                return (input * input * input + 1.f);
            }
        };

        public static final TimeInterpolator EaseInOutCubic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {

                float position = input / 0.5f;
                if (position < 1.f) {
                    return 0.5f * position * position * position;
                }
                position -= 2.f;
                return 0.5f * (position * position * position + 2.f);
            }
        };

        public static final TimeInterpolator EaseInQuart = new TimeInterpolator() {

            public float getInterpolation(float input) {
                return input * input * input * input;
            }
        };

        public static final TimeInterpolator EaseOutQuart = new TimeInterpolator() {

            public float getInterpolation(float input) {
                input--;
                return -(input * input * input * input - 1f);
            }
        };

        public static final TimeInterpolator EaseInOutQuart = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.f) {
                    return 0.5f * position * position * position * position;
                }
                position -= 2.f;
                return -0.5f * (position * position * position * position - 2.f);
            }
        };

        public static final TimeInterpolator EaseInSine = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return -(float) Math.cos(input * (Math.PI / 2.f)) + 1.f;
            }
        };

        public static final TimeInterpolator EaseOutSine = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) Math.sin(input * (Math.PI / 2.f));
            }
        };

        public static final TimeInterpolator EaseInOutSine = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return -0.5f * ((float) Math.cos(Math.PI * input) - 1.f);
            }
        };

        public static final TimeInterpolator EaseInExpo = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (input == 0) ? 0.f : (float) Math.pow(2.f, 10.f * (input - 1.f));
            }
        };

        public static final TimeInterpolator EaseOutExpo = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (input == 1f) ? 1.f : (-(float) Math.pow(2.f, -10.f * (input + 1.f)));
            }
        };

        public static final TimeInterpolator EaseInOutExpo = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input == 0)
                {
                    return 0.f;
                }
                if (input == 1f)
                {
                    return 1.f;
                }

                float position = input / 0.5f;
                if (position < 1.f)
                {
                    return 0.5f * (float) Math.pow(2.f, 10.f * (position - 1.f));
                }
                return 0.5f * (-(float) Math.pow(2.f, -10.f * --position) + 2.f);
            }
        };

        public static final TimeInterpolator EaseInCirc = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return -((float) Math.sqrt(1.f - input * input) - 1.f);
            }
        };

        public static final TimeInterpolator EaseOutCirc = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                input--;
                return (float) Math.sqrt(1.f - input * input);
            }
        };

        public static final TimeInterpolator EaseInOutCirc = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                float position = input / 0.5f;
                if (position < 1.f)
                {
                    return -0.5f * ((float) Math.sqrt(1.f - position * position) - 1.f);
                }
                return 0.5f * ((float) Math.sqrt(1.f - (position -= 2.f) * position)
                + 1.f);
            }
        };

        public static final TimeInterpolator EaseInElastic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input == 0)
                {
                    return 0.f;
                }

                float position = input;
                if (position == 1)
                {
                    return 1.f;
                }

                float p = .3f;
                float s = p / (2.f * (float) Math.PI) * (float) Math.asin(1.f);
                return -((float) Math.pow(2.f, 10.f * (position -= 1.f)) * (float)
                Math
                        .sin((position - s) * (2.f * Math.PI) / p));
            }
        };

        public static final TimeInterpolator EaseOutElastic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input == 0)
                {
                    return 0.f;
                }

                float position = input;
                if (position == 1)
                {
                    return 1.f;
                }

                float p = .3f;
                float s = p / (2 * (float) Math.PI) * (float) Math.asin(1.f);
                return (float) Math.pow(2, -10 * position)
                        * (float) Math.sin((position - s) * (2.f * Math.PI) / p) +
                        1.f;
            }
        };

        public static final TimeInterpolator EaseInOutElastic = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input == 0)
                {
                    return 0.f;
                }

                float position = input / 0.5f;
                if (position == 2)
                {
                    return 1.f;
                }

                float p = (.3f * 1.5f);
                float s = p / (2.f * (float) Math.PI) * (float) Math.asin(1.f);
                if (position < 1.f)
                {
                    return -.5f
                            * ((float) Math.pow(2.f, 10.f * (position -= 1.f)) * (float) Math
                                    .sin((position * 1f - s) * (2.f * Math.PI) / p));
                }
                return (float) Math.pow(2.f, -10.f * (position -= 1.f))
                        * (float) Math.sin((position * 1f - s) * (2.f * Math.PI) / p) *
                        .5f
                        + 1.f;
            }
        };

        public static final TimeInterpolator EaseInBack = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                final float s = 1.70158f;
                float position = input;
                return position * position * ((s + 1.f) * position - s);
            }
        };

        public static final TimeInterpolator EaseOutBack = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                final float s = 1.70158f;
                float position = input;
                position--;
                return (position * position * ((s + 1.f) * position + s) + 1.f);
            }
        };

        public static final TimeInterpolator EaseInOutBack = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                float s = 1.70158f;
                float position = input / 0.5f;
                if (position < 1.f)
                {
                    return 0.5f * (position * position * (((s *= (1.525f)) + 1.f) *
                            position - s));
                }
                return 0.5f * ((position -= 2.f) * position
                        * (((s *= (1.525f)) + 1.f) * position + s) + 2.f);
            }
        };

        public static final TimeInterpolator EaseInBounce = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return 1.f - EaseOutBounce.getInterpolation(1f - input);
            }
        };

        public static final TimeInterpolator EaseOutBounce = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                float position = input;
                if (position < (1.f / 2.75f))
                {
                    return (7.5625f * position * position);
                }
                else if (position < (2.f / 2.75f))
                {
                    return (7.5625f * (position -= (1.5f / 2.75f)) * position + .75f);
                }
                else if (position < (2.5f / 2.75f))
                {
                    return (7.5625f * (position -= (2.25f / 2.75f)) * position + .9375f);
                }
                else
                {
                    return (7.5625f * (position -= (2.625f / 2.75f)) * position +
                    .984375f);
                }
            }
        };

        public static final TimeInterpolator EaseInOutBounce = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input < 0.5f)
                {
                    return EaseInBounce.getInterpolation(input * 2) * .5f;
                }
                return EaseOutBounce.getInterpolation(input * 2 - 1f) * .5f + .5f;
            }
        };
    }
}
