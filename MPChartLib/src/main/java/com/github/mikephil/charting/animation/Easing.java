package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;

/**
 * Easing options.
 *
 * @author Daniel Cohen Gindi
 */
public final class Easing {
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

    private Easing() {
    }

    @NonNull
    public static TimeInterpolator getEasingFunctionFromOption(@NonNull EasingOption easing) {
        switch (easing) {
            default:
            case Linear:
                return Linear;
            case EaseInQuad:
                return EaseInQuad;
            case EaseOutQuad:
                return EaseOutQuad;
            case EaseInOutQuad:
                return EaseInOutQuad;
            case EaseInCubic:
                return EaseInCubic;
            case EaseOutCubic:
                return EaseOutCubic;
            case EaseInOutCubic:
                return EaseInOutCubic;
            case EaseInQuart:
                return EaseInQuart;
            case EaseOutQuart:
                return EaseOutQuart;
            case EaseInOutQuart:
                return EaseInOutQuart;
            case EaseInSine:
                return EaseInSine;
            case EaseOutSine:
                return EaseOutSine;
            case EaseInOutSine:
                return EaseInOutSine;
            case EaseInExpo:
                return EaseInExpo;
            case EaseOutExpo:
                return EaseOutExpo;
            case EaseInOutExpo:
                return EaseInOutExpo;
            case EaseInCirc:
                return EaseInCirc;
            case EaseOutCirc:
                return EaseOutCirc;
            case EaseInOutCirc:
                return EaseInOutCirc;
            case EaseInElastic:
                return EaseInElastic;
            case EaseOutElastic:
                return EaseOutElastic;
            case EaseInOutElastic:
                return EaseInOutElastic;
            case EaseInBack:
                return EaseInBack;
            case EaseOutBack:
                return EaseOutBack;
            case EaseInOutBack:
                return EaseInOutBack;
            case EaseInBounce:
                return EaseInBounce;
            case EaseOutBounce:
                return EaseOutBounce;
            case EaseInOutBounce:
                return EaseInOutBounce;
        }
    }

    private static final TimeInterpolator Linear = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return input;
        }
    };

    private static final TimeInterpolator EaseInQuad = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return input * input;
        }
    };

    private static final TimeInterpolator EaseOutQuad = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return -input * (input - 2f);
        }
    };

    private static final TimeInterpolator EaseInOutQuad = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float position = input / 0.5f;
            if (position < 1f) {
                return 0.5f * position * position;
            }

            return -0.5f * ((--position) * (position - 2f) - 1f);
        }
    };

    private static final TimeInterpolator EaseInCubic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return input * input * input;
        }
    };

    private static final TimeInterpolator EaseOutCubic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            input--;
            return input * input * input + 1f;
        }
    };

    private static final TimeInterpolator EaseInOutCubic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float position = input / 0.5f;
            if (position < 1f) {
                return 0.5f * position * position * position;
            }

            position -= 2f;
            return 0.5f * (position * position * position + 2f);
        }
    };

    private static final TimeInterpolator EaseInQuart = new TimeInterpolator() {
        public float getInterpolation(float input) {
            return input * input * input * input;
        }
    };

    private static final TimeInterpolator EaseOutQuart = new TimeInterpolator() {
        public float getInterpolation(float input) {
            input--;
            return -(input * input * input * input - 1f);
        }
    };

    private static final TimeInterpolator EaseInOutQuart = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float position = input / 0.5f;
            if (position < 1f) {
                return 0.5f * position * position * position * position;
            }

            position -= 2f;
            return -0.5f * (position * position * position * position - 2f);
        }
    };

    private static final TimeInterpolator EaseInSine = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return -(float) Math.cos(input * Math.PI / 2f) + 1f;
        }
    };

    private static final TimeInterpolator EaseOutSine = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return (float) Math.sin(input * Math.PI / 2f);
        }
    };

    private static final TimeInterpolator EaseInOutSine = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return -0.5f * ((float) Math.cos(Math.PI * input) - 1f);
        }
    };

    private static final TimeInterpolator EaseInExpo = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return input == 0f ? 0f : (float) Math.pow(2.f, 10f * (input - 1f));
        }
    };

    private static final TimeInterpolator EaseOutExpo = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return input == 1f ? 1f : -(float) Math.pow(2f, -10f * (input + 1f));
        }
    };

    private static final TimeInterpolator EaseInOutExpo = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input == 0f || input == 1f) {
                return input;
            }

            float position = input / 0.5f;
            if (position < 1f) {
                return 0.5f * (float) Math.pow(2f, 10f * (position - 1f));
            }

            return 0.5f * (-(float) Math.pow(2f, -10f * --position) + 2f);
        }
    };

    private static final TimeInterpolator EaseInCirc = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return -((float) Math.sqrt(1f - input * input) - 1f);
        }
    };

    private static final TimeInterpolator EaseOutCirc = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            input--;
            return (float) Math.sqrt(1f - input * input);
        }
    };

    private static final TimeInterpolator EaseInOutCirc = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float position = input / 0.5f;
            if (position < 1f) {
                return -0.5f * ((float) Math.sqrt(1f - position * position) - 1f);
            }

            return 0.5f * ((float) Math.sqrt(1f - (position -= 2f) * position) + 1f);
        }
    };

    private static final TimeInterpolator EaseInElastic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input == 0f || input == 1f) {
                return input;
            }

            float p = 0.3f;
            float position = input;
            float s = p / (2f * (float) Math.PI) * (float) Math.asin(1f);
            return -((float) Math.pow(2f, 10f * (position -= 1f)) * (float) Math.sin((position - s) * (2f * Math.PI) / p));
        }
    };

    private static final TimeInterpolator EaseOutElastic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input == 0f || input == 1f) {
                return input;
            }

            float p = 0.3f;
            float s = p / (2f * (float) Math.PI) * (float) Math.asin(1f);
            return (float) Math.pow(2f, -10f * input) * (float) Math.sin((input - s) * (2f * Math.PI) / p) + 1f;
        }
    };

    private static final TimeInterpolator EaseInOutElastic = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input == 0f) {
                return 0f;
            }

            float position = input / 0.5f;
            if (position == 2f) {
                return 1f;
            }

            float p = (0.3f * 1.5f);
            float s = p / (2f * (float) Math.PI) * (float) Math.asin(1f);
            if (position < 1f) {
                return -0.5f * ((float) Math.pow(2f, 10f * (position -= 1f)) * (float) Math.sin((position * 1f - s) * (2f * Math.PI) / p));
            }

            return (float) Math.pow(2f, -10f * (position -= 1f)) * (float) Math.sin((position * 1f - s) * (2f * Math.PI) / p) * 0.5f + 1f;
        }
    };

    private static final TimeInterpolator EaseInBack = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            final float s = 1.70158f;
            return input * input * ((s + 1f) * input - s);
        }
    };

    private static final TimeInterpolator EaseOutBack = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            final float s = 1.70158f;
            float position = input;
            position--;
            return (position * position * ((s + 1f) * position + s) + 1f);
        }
    };

    private static final TimeInterpolator EaseInOutBack = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float s = 1.70158f;
            float position = input / 0.5f;
            if (position < 1f) {
                return 0.5f * (position * position * (((s *= (1.525f)) + 1f) * position - s));
            }

            return 0.5f * ((position -= 2f) * position * (((s *= (1.525f)) + 1f) * position + s) + 2f);
        }
    };

    private static final TimeInterpolator EaseInBounce = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return 1f - EaseOutBounce.getInterpolation(1f - input);
        }
    };

    private static final TimeInterpolator EaseOutBounce = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            float position = input;
            if (position < 1f / 2.75f) {
                return 7.5625f * position * position;
            } else if (position < 2f / 2.75f) {
                return 7.5625f * (position -= (1.5f / 2.75f)) * position + 0.75f;
            } else if (position < 2.5f / 2.75f) {
                return 7.5625f * (position -= (2.25f / 2.75f)) * position + 0.9375f;
            } else {
                return 7.5625f * (position -= (2.625f / 2.75f)) * position + 0.984375f;
            }
        }
    };

    private static final TimeInterpolator EaseInOutBounce = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            if (input < 0.5f) {
                return EaseInBounce.getInterpolation(input * 2f) * 0.5f;
            }

            return EaseOutBounce.getInterpolation(input * 2f - 1f) * 0.5f + 0.5f;
        }
    };
}
