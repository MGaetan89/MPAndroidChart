package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the legend of the chart. The legend will contain one entry per color and
 * DataSet. Multiple colors in one DataSet are grouped together. The legend object is NOT available
 * before setting data to the chart.
 *
 * @author Philipp Jahoda
 */
public class Legend extends ComponentBase {
    public enum LegendForm {
        /**
         * Avoid drawing a form.
         */
        NONE,

        /**
         * Do not draw the a form, but leave space for it.
         */
        EMPTY,

        /**
         * Use default (default dataset's form to the legend's form).
         */
        DEFAULT,

        /**
         * Draw a square.
         */
        SQUARE,

        /**
         * Draw a circle.
         */
        CIRCLE,

        /**
         * Draw a horizontal line.
         */
        LINE
    }

    public enum LegendHorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    public enum LegendVerticalAlignment {
        TOP, CENTER, BOTTOM
    }

    public enum LegendOrientation {
        HORIZONTAL, VERTICAL
    }

    public enum LegendDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    /**
     * The legend entries array
     */
    @NonNull
    private LegendEntry[] mEntries = new LegendEntry[0];

    /**
     * Entries that will be appended to the end of the auto calculated entries after calculating the
     * legend (if the legend has already been calculated, you will need to call
     * notifyDataSetChanged() to let the changes take effect),
     */
    @Nullable
    private LegendEntry[] mExtraEntries;

    /**
     * Are the legend labels/colors a custom value or auto calculated? If false, then it's auto, if
     * true, then custom.
     */
    private boolean mIsLegendCustom = false;

    private LegendHorizontalAlignment mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
    private LegendVerticalAlignment mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
    private LegendOrientation mOrientation = LegendOrientation.HORIZONTAL;
    private boolean mDrawInside = false;

    /**
     * The text direction for the legend.
     */
    private LegendDirection mDirection = LegendDirection.LEFT_TO_RIGHT;

    /**
     * The shape/form the legend colors are drawn in.
     */
    private LegendForm mShape = LegendForm.SQUARE;

    /**
     * The size of the legend forms/shapes.
     */
    private float mFormSize = 8f;

    /**
     * The size of the legend forms/shapes.
     */
    private float mFormLineWidth = 3f;

    /**
     * Line dash path effect used for shapes that consist of lines.
     */
    @Nullable
    private DashPathEffect mFormLineDashEffect;

    /**
     * The space between the legend entries on a horizontal axis.
     */
    private float mXEntrySpace = 6f;

    /**
     * The space between the legend entries on a vertical axis.
     */
    private float mYEntrySpace = 0f;

    /**
     * The space between the form and the actual label/text.
     */
    private float mFormToTextSpace = 5f;

    /**
     * The space that should be left between stacked forms.
     */
    private float mStackSpace = 3f;

    /**
     * The maximum relative size out of the whole chart view in percent.
     */
    private float mMaxSizePercent = 0.95f;

    /**
     * The total width of the legend (needed width space).
     */
    public float mNeededWidth = 0f;

    /**
     * The total height of the legend (needed height space).
     */
    public float mNeededHeight = 0f;

    public float mTextHeightMax = 0f;

    public float mTextWidthMax = 0f;

    /**
     * Flag that indicates if word wrapping is enabled.
     */
    private boolean mWordWrapEnabled = false;

    private List<FSize> mCalculatedLabelSizes = new ArrayList<>(16);
    private List<Boolean> mCalculatedLabelBreakPoints = new ArrayList<>(16);
    private List<FSize> mCalculatedLineSizes = new ArrayList<>(16);

    /**
     * Default constructor.
     */
    public Legend() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(3f);
    }

    /**
     * Constructor. Provide entries for the legend.
     *
     * @param entries
     */
    public Legend(@NonNull LegendEntry[] entries) {
        this();

        this.mEntries = entries;
    }

    /**
     * This method sets the automatically computed colors for the legend. Use setCustom(...) to set
     * custom colors.
     *
     * @param entries
     */
    public void setEntries(@NonNull List<LegendEntry> entries) {
        mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    @NonNull
    public LegendEntry[] getEntries() {
        return mEntries;
    }

    /**
     * Returns the maximum length in pixels across all legend labels + mFormSize + mFormToTextSpace.
     *
     * @param paint the paint object used for rendering the text
     */
    public float getMaximumEntryWidth(@NonNull Paint paint) {
        float max = 0f;
        float maxFormSize = 0f;
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);
        for (LegendEntry entry : mEntries) {
            final float formSize = Utils.convertDpToPixel(Float.isNaN(entry.formSize) ? mFormSize : entry.formSize);
            if (formSize > maxFormSize) {
                maxFormSize = formSize;
            }

            String label = entry.label;
            if (label == null) {
                continue;
            }

            float length = Utils.calcTextWidth(paint, label);
            if (length > max) {
                max = length;
            }
        }

        return max + maxFormSize + formToTextSpace;
    }

    /**
     * Returns the maximum height in pixels across all legend labels.
     *
     * @param paint the paint object used for rendering the text
     */
    public float getMaximumEntryHeight(@NonNull Paint paint) {
        float max = 0f;
        for (LegendEntry entry : mEntries) {
            String label = entry.label;
            if (label != null) {
                float length = Utils.calcTextHeight(paint, label);
                if (length > max) {
                    max = length;
                }
            }
        }

        return max;
    }

    @Nullable
    public LegendEntry[] getExtraEntries() {
        return mExtraEntries;
    }

    public void setExtra(@NonNull List<LegendEntry> entries) {
        mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setExtra(@Nullable LegendEntry[] entries) {
        mExtraEntries = entries;
    }

    /**
     * Entries that will be appended to the end of the auto calculated entries after calculating the
     * legend (if the legend has already been calculated, you will need to call
     * notifyDataSetChanged() to let the changes take effect).
     */
    public void setExtra(@ColorInt int[] colors, @NonNull String[] labels) {
        int count = Math.min(colors.length, labels.length);
        LegendEntry[] entries = new LegendEntry[count];
        for (int i = 0; i < count; i++) {
            final LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];

            if (entry.formColor == ColorTemplate.COLOR_SKIP || entry.formColor == 0) {
                entry.form = LegendForm.NONE;
            } else if (entry.formColor == ColorTemplate.COLOR_NONE) {
                entry.form = LegendForm.EMPTY;
            }

            entries[i] = entry;
        }

        setExtra(entries);
    }

    /**
     * Sets a custom legend's entries array. A null label will start a group. This will disable the
     * feature that automatically calculates the legend entries from the datasets. Call
     * resetCustom() to re-enable automatic calculation (and then notifyDataSetChanged() is needed
     * to auto-calculate the legend again).
     */
    public void setCustom(@NonNull LegendEntry[] entries) {
        mEntries = entries;
        mIsLegendCustom = true;
    }

    /**
     * Sets a custom legend's entries array. A null label will start a group. This will disable the
     * feature that automatically calculates the legend entries from the datasets. Call
     * resetCustom() to re-enable automatic calculation (and then notifyDataSetChanged() is needed
     * to auto-calculate the legend again).
     */
    public void setCustom(@NonNull List<LegendEntry> entries) {
        mEntries = entries.toArray(new LegendEntry[entries.size()]);
        mIsLegendCustom = true;
    }

    /**
     * Calling this will disable the custom legend entries (set by setCustom(...)). Instead, the
     * entries will again be calculated automatically (after notifyDataSetChanged() is called).
     */
    public void resetCustom() {
        mIsLegendCustom = false;
    }

    /**
     * Returns true if a custom legend entries has been set.
     */
    public boolean isLegendCustom() {
        return mIsLegendCustom;
    }

    /**
     * Returns the horizontal alignment of the legend.
     */
    public LegendHorizontalAlignment getHorizontalAlignment() {
        return mHorizontalAlignment;
    }

    /**
     * Sets the horizontal alignment of the legend.
     *
     * @param alignment
     */
    public void setHorizontalAlignment(LegendHorizontalAlignment alignment) {
        mHorizontalAlignment = alignment;
    }

    /**
     * Returns the vertical alignment of the legend.
     */
    public LegendVerticalAlignment getVerticalAlignment() {
        return mVerticalAlignment;
    }

    /**
     * Sets the vertical alignment of the legend.
     *
     * @param alignment
     */
    public void setVerticalAlignment(LegendVerticalAlignment alignment) {
        mVerticalAlignment = alignment;
    }

    /**
     * Returns the orientation of the legend.
     */
    public LegendOrientation getOrientation() {
        return mOrientation;
    }

    /**
     * Sets the orientation of the legend.
     *
     * @param orientation
     */
    public void setOrientation(LegendOrientation orientation) {
        mOrientation = orientation;
    }

    /**
     * Returns whether the legend will draw inside the chart or outside.
     */
    public boolean isDrawInsideEnabled() {
        return mDrawInside;
    }

    /**
     * Sets whether the legend will draw inside the chart or outside.
     *
     * @param drawInside
     */
    public void setDrawInside(boolean drawInside) {
        mDrawInside = drawInside;
    }

    /**
     * Returns the text direction of the legend.
     */
    public LegendDirection getDirection() {
        return mDirection;
    }

    /**
     * Sets the text direction of the legend.
     *
     * @param direction
     */
    public void setDirection(LegendDirection direction) {
        mDirection = direction;
    }

    /**
     * Returns the current form/shape that is set for the legend.
     */
    public LegendForm getForm() {
        return mShape;
    }

    /**
     * Sets the form/shape of the legend forms.
     *
     * @param shape
     */
    public void setForm(LegendForm shape) {
        mShape = shape;
    }

    /**
     * Sets the size in dp of the legend forms.
     *
     * @param size
     */
    public void setFormSize(float size) {
        mFormSize = size;
    }

    /**
     * Returns the size in dp of the legend forms.
     */
    public float getFormSize() {
        return mFormSize;
    }

    /**
     * Sets the line width in dp for forms that consist of lines.
     *
     * @param size
     */
    public void setFormLineWidth(float size) {
        mFormLineWidth = size;
    }

    /**
     * Returns the line width in dp for drawing forms that consist of lines.
     */
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    /**
     * Sets the line dash path effect used for shapes that consist of lines.
     *
     * @param dashPathEffect
     */
    public void setFormLineDashEffect(@Nullable DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    /**
     * Returns the line dash path effect used for shapes that consist of lines.
     */
    @Nullable
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    /**
     * Returns the space between the legend entries on a horizontal axis in pixels.
     */
    public float getXEntrySpace() {
        return mXEntrySpace;
    }

    /**
     * Sets the space between the legend entries on a horizontal axis in pixels, converts to dp
     * internally.
     *
     * @param space
     */
    public void setXEntrySpace(float space) {
        mXEntrySpace = space;
    }

    /**
     * Returns the space between the legend entries on a vertical axis in pixels.
     */
    public float getYEntrySpace() {
        return mYEntrySpace;
    }

    /**
     * Sets the space between the legend entries on a vertical axis in pixels, converts to dp
     * internally.
     *
     * @param space
     */
    public void setYEntrySpace(float space) {
        mYEntrySpace = space;
    }

    /**
     * Returns the space between the form and the actual label/text.
     */
    public float getFormToTextSpace() {
        return mFormToTextSpace;
    }

    /**
     * Sets the space between the form and the actual label/text, converts to dp internally.
     *
     * @param space
     */
    public void setFormToTextSpace(float space) {
        this.mFormToTextSpace = space;
    }

    /**
     * Returns the space that is left out between stacked forms (with no label).
     */
    public float getStackSpace() {
        return mStackSpace;
    }

    /**
     * Sets the space that is left out between stacked forms (with no label).
     *
     * @param space
     */
    public void setStackSpace(float space) {
        mStackSpace = space;
    }

    /**
     * Should the legend word wrap?
     * This is currently supported only for: BelowChartLeft, BelowChartRight, BelowChartCenter.
     * Note that word wrapping a legend takes a toll on performance. You may want to set
     * maxSizePercent when word wrapping, to set the point where the text wraps.
     *
     * @param enabled
     */
    public void setWordWrapEnabled(boolean enabled) {
        mWordWrapEnabled = enabled;
    }

    /**
     * If this is set, then word wrapping the legend is enabled. This means the legend will not be
     * cut off if too long.
     *
     * @return
     */
    public boolean isWordWrapEnabled() {
        return mWordWrapEnabled;
    }

    /**
     * The maximum relative size out of the whole chart view. If the legend is to the right/left of
     * the chart, then this affects the width of the legend. If the legend is on the top/bottom of
     * the chart, then this affects the height of the legend. If the legend is in the center of the
     * piechart, then this defines the size of the rectangular bounds out of the size of the "hole".
     *
     * @return
     */
    public float getMaxSizePercent() {
        return mMaxSizePercent;
    }

    /**
     * The maximum relative size out of the whole chart view. If the legend is to the right/left of
     * the chart, then this affects the width of the legend. If the legend is on the top/bottom of
     * the chart, then this affects the height of the legend.
     *
     * @param maxSize
     */
    public void setMaxSizePercent(float maxSize) {
        mMaxSizePercent = maxSize;
    }

    public List<FSize> getCalculatedLabelSizes() {
        return mCalculatedLabelSizes;
    }

    public List<Boolean> getCalculatedLabelBreakPoints() {
        return mCalculatedLabelBreakPoints;
    }

    public List<FSize> getCalculatedLineSizes() {
        return mCalculatedLineSizes;
    }

    /**
     * Calculates the dimensions of the Legend. This includes the maximum width and height of a
     * single entry, as well as the total width and height of the Legend.
     *
     * @param labelPaint
     */
    public void calculateDimensions(@NonNull Paint labelPaint, @NonNull ViewPortHandler viewPortHandler) {
        float defaultFormSize = Utils.convertDpToPixel(mFormSize);
        float stackSpace = Utils.convertDpToPixel(mStackSpace);
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);
        float xEntrySpace = Utils.convertDpToPixel(mXEntrySpace);
        float yEntrySpace = Utils.convertDpToPixel(mYEntrySpace);
        boolean wordWrapEnabled = mWordWrapEnabled;
        LegendEntry[] entries = mEntries;
        int entryCount = entries.length;

        mTextWidthMax = getMaximumEntryWidth(labelPaint);
        mTextHeightMax = getMaximumEntryHeight(labelPaint);

        switch (mOrientation) {
            case VERTICAL: {
                float maxWidth = 0f;
                float maxHeight = 0f;
                float width = 0f;
                float labelLineHeight = Utils.getLineHeight(labelPaint);
                boolean wasStacked = false;

                for (int i = 0; i < entryCount; i++) {
                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;

                    if (!wasStacked) {
                        width = 0f;
                    }

                    if (drawingForm) {
                        if (wasStacked) {
                            width += stackSpace;
                        }
                        width += formSize;
                    }

                    // Grouped forms have null labels
                    if (label != null) {
                        // Make a step to the left
                        if (drawingForm && !wasStacked) {
                            width += formToTextSpace;
                        } else if (wasStacked) {
                            maxWidth = Math.max(maxWidth, width);
                            maxHeight += labelLineHeight + yEntrySpace;
                            width = 0f;
                            wasStacked = false;
                        }

                        width += Utils.calcTextWidth(labelPaint, label);

                        if (i < entryCount - 1) {
                            maxHeight += labelLineHeight + yEntrySpace;
                        }
                    } else {
                        wasStacked = true;
                        width += formSize;
                        if (i < entryCount - 1) {
                            width += stackSpace;
                        }
                    }

                    maxWidth = Math.max(maxWidth, width);
                }

                mNeededWidth = maxWidth;
                mNeededHeight = maxHeight;

                break;
            }

            case HORIZONTAL: {
                float labelLineHeight = Utils.getLineHeight(labelPaint);
                float labelLineSpacing = Utils.getLineSpacing(labelPaint) + yEntrySpace;
                float contentWidth = viewPortHandler.contentWidth() * mMaxSizePercent;

                // Start calculating layout
                float maxLineWidth = 0f;
                float currentLineWidth = 0f;
                float requiredWidth = 0f;
                int stackedStartIndex = -1;

                mCalculatedLabelBreakPoints.clear();
                mCalculatedLabelSizes.clear();
                mCalculatedLineSizes.clear();

                for (int i = 0; i < entryCount; i++) {
                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize) ? defaultFormSize : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;

                    mCalculatedLabelBreakPoints.add(false);

                    if (stackedStartIndex == -1) {
                        // We are not stacking, so required width is for this label only
                        requiredWidth = 0f;
                    } else {
                        // Add the spacing appropriate for stacked labels/forms
                        requiredWidth += stackSpace;
                    }

                    // Grouped forms have null labels
                    if (label != null) {
                        mCalculatedLabelSizes.add(Utils.calcTextSize(labelPaint, label));
                        requiredWidth += drawingForm ? formToTextSpace + formSize : 0f;
                        requiredWidth += mCalculatedLabelSizes.get(i).width;
                    } else {
                        mCalculatedLabelSizes.add(FSize.getInstance(0f, 0f));
                        requiredWidth += drawingForm ? formSize : 0f;

                        if (stackedStartIndex == -1) {
                            // Mark this index as we might want to break here later
                            stackedStartIndex = i;
                        }
                    }

                    if (label != null || i == entryCount - 1) {
                        float requiredSpacing = currentLineWidth == 0f ? 0f : xEntrySpace;

                        if (!wordWrapEnabled // No word wrapping, it must fit.
                                || currentLineWidth == 0f // The line is empty, it must fit
                                || (contentWidth - currentLineWidth >= requiredSpacing + requiredWidth)) { // It simply fits
                            // Expand current line
                            currentLineWidth += requiredSpacing + requiredWidth;
                        } else { // It doesn't fit, we need to wrap a line
                            // Add current line size to array
                            mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);

                            // Start a new line
                            mCalculatedLabelBreakPoints.set(stackedStartIndex > -1 ? stackedStartIndex : i, true);
                            currentLineWidth = requiredWidth;
                        }

                        if (i == entryCount - 1) {
                            // Add last line size to array
                            mCalculatedLineSizes.add(FSize.getInstance(currentLineWidth, labelLineHeight));
                            maxLineWidth = Math.max(maxLineWidth, currentLineWidth);
                        }
                    }

                    stackedStartIndex = label != null ? -1 : stackedStartIndex;
                }

                mNeededWidth = maxLineWidth;
                mNeededHeight = labelLineHeight * (float) mCalculatedLineSizes.size()
                        + labelLineSpacing * (float) (mCalculatedLineSizes.isEmpty() ? 0 : (mCalculatedLineSizes.size() - 1));

                break;
            }
        }

        mNeededHeight += mYOffset;
        mNeededWidth += mXOffset;
    }
}
