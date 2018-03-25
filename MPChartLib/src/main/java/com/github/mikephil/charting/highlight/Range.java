package com.github.mikephil.charting.highlight;

/**
 * Class that represents the range of one value in a stacked bar entry.
 * For example, if stack values are -10, 5, 20, then ranges are (-10 - 0, 0 - 5, 5 - 25).
 *
 * @author Philipp Jahoda
 */
public final class Range {
    public float from;
    public float to;

    public Range(float from, float to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Returns true if {@code value} is greater than {@code from} and smaller or equal to
     * {@code to}, false if not.
     *
     * @param value
     */
    public boolean contains(float value) {
        return value > from && value <= to;
    }

    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    public boolean isLarger(float value) {
        return value > to;
    }

    /**
     * @deprecated since version 3.1.0. Will be removed in version 3.2.0.
     */
    @Deprecated
    public boolean isSmaller(float value) {
        return value < from;
    }
}
