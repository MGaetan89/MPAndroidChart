package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Typeface;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class LegendRenderer extends Renderer {
    /**
     * Paint for the legend labels.
     */
    protected Paint mLegendLabelPaint;

    /**
     * Paint used for the legend forms.
     */
    protected Paint mLegendFormPaint;

    /**
     * The legend object this renderer renders.
     */
    protected Legend mLegend;

    @NonNull
    protected Paint.FontMetrics legendFontMetrics = new Paint.FontMetrics();

    @NonNull
    private final Path mLineFormPath = new Path();

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);

        this.mLegend = legend;

        mLegendLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9f));
        mLegendLabelPaint.setTextAlign(Align.LEFT);

        mLegendFormPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLegendFormPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Returns the Paint object used for drawing the Legend labels.
     */
    public Paint getLabelPaint() {
        return mLegendLabelPaint;
    }

    /**
     * Returns the Paint object used for drawing the Legend forms.
     */
    public Paint getFormPaint() {
        return mLegendFormPaint;
    }

    @NonNull
    protected List<LegendEntry> computedEntries = new ArrayList<>(16);

    /**
     * Prepares the legend and calculates all needed forms, labels and colors.
     *
     * @param data
     */
    public void computeLegend(@NonNull ChartData<?> data) {
        if (!mLegend.isLegendCustom()) {
            computedEntries.clear();

            // Loop for building up the colors and labels used in the legend
            for (int i = 0; i < data.getDataSetCount(); i++) {
                IDataSet dataSet = data.getDataSetByIndex(i);
                List<Integer> colors = dataSet.getColors();
                int entryCount = dataSet.getEntryCount();

                // If we have a bar chart with stacked bars
                if (dataSet instanceof IBarDataSet && ((IBarDataSet) dataSet).isStacked()) {
                    IBarDataSet barDataSet = (IBarDataSet) dataSet;
                    String[] labels = barDataSet.getStackLabels();
                    for (int j = 0; j < colors.size() && j < barDataSet.getStackSize(); j++) {
                        computedEntries.add(new LegendEntry(
                                labels[j % labels.length],
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                colors.get(j)
                        ));
                    }

                    if (!barDataSet.getLabel().isEmpty()) {
                        // Add the legend description label
                        computedEntries.add(new LegendEntry(
                                dataSet.getLabel(),
                                Legend.LegendForm.NONE,
                                Float.NaN,
                                Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                        ));
                    }
                } else if (dataSet instanceof IPieDataSet) {
                    IPieDataSet pieDataSet = (IPieDataSet) dataSet;
                    for (int j = 0; j < colors.size() && j < entryCount; j++) {
                        computedEntries.add(new LegendEntry(
                                pieDataSet.getEntryForIndex(j).getLabel(),
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                colors.get(j)
                        ));
                    }

                    if (!pieDataSet.getLabel().isEmpty()) {
                        // Add the legend description label
                        computedEntries.add(new LegendEntry(
                                dataSet.getLabel(),
                                Legend.LegendForm.NONE,
                                Float.NaN,
                                Float.NaN,
                                null,
                                ColorTemplate.COLOR_NONE
                        ));
                    }
                } else if (dataSet instanceof ICandleDataSet && ((ICandleDataSet) dataSet).getDecreasingColor() != ColorTemplate.COLOR_NONE) {
                    int decreasingColor = ((ICandleDataSet) dataSet).getDecreasingColor();
                    int increasingColor = ((ICandleDataSet) dataSet).getIncreasingColor();

                    computedEntries.add(new LegendEntry(
                            null,
                            dataSet.getForm(),
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            decreasingColor
                    ));

                    computedEntries.add(new LegendEntry(
                            dataSet.getLabel(),
                            dataSet.getForm(),
                            dataSet.getFormSize(),
                            dataSet.getFormLineWidth(),
                            dataSet.getFormLineDashEffect(),
                            increasingColor
                    ));
                } else { // All others
                    for (int j = 0; j < colors.size() && j < entryCount; j++) {
                        String label;
                        // If multiple colors are set for a DataSet, group them
                        if (j < colors.size() - 1 && j < entryCount - 1) {
                            label = null;
                        } else { // add label to the last entry
                            label = data.getDataSetByIndex(i).getLabel();
                        }

                        computedEntries.add(new LegendEntry(
                                label,
                                dataSet.getForm(),
                                dataSet.getFormSize(),
                                dataSet.getFormLineWidth(),
                                dataSet.getFormLineDashEffect(),
                                colors.get(j)
                        ));
                    }
                }
            }

            if (mLegend.getExtraEntries() != null) {
                Collections.addAll(computedEntries, mLegend.getExtraEntries());
            }

            mLegend.setEntries(computedEntries);
        }

        Typeface typeface = mLegend.getTypeface();
        if (typeface != null) {
            mLegendLabelPaint.setTypeface(typeface);
        }

        mLegendLabelPaint.setTextSize(mLegend.getTextSize());
        mLegendLabelPaint.setColor(mLegend.getTextColor());

        // Calculate all dimensions of the mLegend
        mLegend.calculateDimensions(mLegendLabelPaint, mViewPortHandler);
    }

    public void renderLegend(Canvas canvas) {
        if (!mLegend.isEnabled()) {
            return;
        }

        Typeface typeface = mLegend.getTypeface();
        if (typeface != null) {
            mLegendLabelPaint.setTypeface(typeface);
        }

        mLegendLabelPaint.setTextSize(mLegend.getTextSize());
        mLegendLabelPaint.setColor(mLegend.getTextColor());

        float labelLineHeight = Utils.getLineHeight(mLegendLabelPaint, legendFontMetrics);
        float labelLineSpacing = Utils.getLineSpacing(mLegendLabelPaint, legendFontMetrics) + Utils.convertDpToPixel(mLegend.getYEntrySpace());
        float formYOffset = labelLineHeight - Utils.calcTextHeight(mLegendLabelPaint, "ABC") / 2f;

        LegendEntry[] entries = mLegend.getEntries();

        float formToTextSpace = Utils.convertDpToPixel(mLegend.getFormToTextSpace());
        float xEntrySpace = Utils.convertDpToPixel(mLegend.getXEntrySpace());
        Legend.LegendOrientation orientation = mLegend.getOrientation();
        Legend.LegendHorizontalAlignment horizontalAlignment = mLegend.getHorizontalAlignment();
        Legend.LegendVerticalAlignment verticalAlignment = mLegend.getVerticalAlignment();
        Legend.LegendDirection direction = mLegend.getDirection();
        float defaultFormSize = Utils.convertDpToPixel(mLegend.getFormSize());

        // Space between the entries
        float stackSpace = Utils.convertDpToPixel(mLegend.getStackSpace());

        float yOffset = mLegend.getYOffset();
        float xOffset = mLegend.getXOffset();
        float originPosX = 0f;

        switch (horizontalAlignment) {
            case LEFT:
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    originPosX = xOffset;
                } else {
                    originPosX = mViewPortHandler.contentLeft() + xOffset;
                }

                if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                    originPosX += mLegend.mNeededWidth;
                }
                break;

            case RIGHT:
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    originPosX = mViewPortHandler.getChartWidth() - xOffset;
                } else {
                    originPosX = mViewPortHandler.contentRight() - xOffset;
                }

                if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                    originPosX -= mLegend.mNeededWidth;
                }
                break;

            case CENTER:
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    originPosX = mViewPortHandler.getChartWidth() / 2f;
                } else {
                    originPosX = mViewPortHandler.contentLeft() + mViewPortHandler.contentWidth() / 2f;
                }

                originPosX += (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? +xOffset : -xOffset);

                // Horizontally laid out legends do the center offset on a line basis, so here we
                // offset the vertical ones only.
                if (orientation == Legend.LegendOrientation.VERTICAL) {
                    originPosX += (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? -mLegend.mNeededWidth / 2f + xOffset : mLegend.mNeededWidth / 2f - xOffset);
                }
                break;
        }

        switch (orientation) {
            case HORIZONTAL: {
                List<FSize> calculatedLineSizes = mLegend.getCalculatedLineSizes();
                List<FSize> calculatedLabelSizes = mLegend.getCalculatedLabelSizes();
                List<Boolean> calculatedLabelBreakPoints = mLegend.getCalculatedLabelBreakPoints();

                float posX = originPosX;
                float posY = 0f;

                switch (verticalAlignment) {
                    case TOP:
                        posY = yOffset;
                        break;

                    case BOTTOM:
                        posY = mViewPortHandler.getChartHeight() - yOffset - mLegend.mNeededHeight;
                        break;

                    case CENTER:
                        posY = (mViewPortHandler.getChartHeight() - mLegend.mNeededHeight) / 2.f + yOffset;
                        break;
                }

                int lineIndex = 0;
                for (int i = 0, count = entries.length; i < count; i++) {
                    LegendEntry entry = entries[i];
                    boolean drawingForm = entry.form != Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(entry.formSize) ? defaultFormSize : Utils.convertDpToPixel(entry.formSize);

                    if (i < calculatedLabelBreakPoints.size() && calculatedLabelBreakPoints.get(i)) {
                        posX = originPosX;
                        posY += labelLineHeight + labelLineSpacing;
                    }

                    if (posX == originPosX && horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && lineIndex < calculatedLineSizes.size()) {
                        posX += (direction == Legend.LegendDirection.RIGHT_TO_LEFT
                                ? calculatedLineSizes.get(lineIndex).width
                                : -calculatedLineSizes.get(lineIndex).width) / 2f;
                        lineIndex++;
                    }

                    // Grouped forms have null labels
                    boolean isStacked = entry.label == null;

                    if (drawingForm) {
                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX -= formSize;
                        }

                        drawForm(canvas, posX, posY + formYOffset, entry, mLegend);

                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += formSize;
                        }
                    }

                    if (!isStacked) {
                        if (drawingForm) {
                            posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -formToTextSpace : formToTextSpace;
                        }

                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX -= calculatedLabelSizes.get(i).width;
                        }

                        drawLabel(canvas, posX, posY + labelLineHeight, entry.label);

                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += calculatedLabelSizes.get(i).width;
                        }

                        posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -xEntrySpace : xEntrySpace;
                    } else
                        posX += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -stackSpace : stackSpace;
                }
                break;
            }

            case VERTICAL: {
                // Contains the stacked legend size in pixels
                float stack = 0f;
                boolean wasStacked = false;
                float posY = 0f;

                switch (verticalAlignment) {
                    case TOP:
                        posY = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? 0f : mViewPortHandler.contentTop());
                        posY += yOffset;
                        break;

                    case BOTTOM:
                        posY = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? mViewPortHandler.getChartHeight() : mViewPortHandler.contentBottom());
                        posY -= mLegend.mNeededHeight + yOffset;
                        break;

                    case CENTER:
                        posY = mViewPortHandler.getChartHeight() / 2f - mLegend.mNeededHeight / 2f + mLegend.getYOffset();
                        break;
                }

                for (LegendEntry entry : entries) {
                    boolean drawingForm = entry.form != Legend.LegendForm.NONE;
                    float formSize = Float.isNaN(entry.formSize) ? defaultFormSize : Utils.convertDpToPixel(entry.formSize);

                    float posX = originPosX;

                    if (drawingForm) {
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += stack;
                        } else {
                            posX -= formSize - stack;
                        }

                        drawForm(canvas, posX, posY + formYOffset, entry, mLegend);

                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            posX += formSize;
                        }
                    }

                    if (entry.label != null) {
                        if (drawingForm && !wasStacked) {
                            posX += direction == Legend.LegendDirection.LEFT_TO_RIGHT ? formToTextSpace : -formToTextSpace;
                        } else if (wasStacked) {
                            posX = originPosX;
                        }

                        if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            posX -= Utils.calcTextWidth(mLegendLabelPaint, entry.label);
                        }

                        if (!wasStacked) {
                            drawLabel(canvas, posX, posY + labelLineHeight, entry.label);
                        } else {
                            posY += labelLineHeight + labelLineSpacing;
                            drawLabel(canvas, posX, posY + labelLineHeight, entry.label);
                        }

                        // Make a step down
                        posY += labelLineHeight + labelLineSpacing;
                        stack = 0f;
                    } else {
                        stack += formSize + stackSpace;
                        wasStacked = true;
                    }
                }
                break;

            }
        }
    }

    /**
     * Draws the Legend-form at the given position with the color at the given index.
     *
     * @param canvas canvas to draw with
     * @param x      position
     * @param y      position
     * @param entry  the entry to render
     * @param legend the legend context
     */
    protected void drawForm(
            @NonNull Canvas canvas, float x, float y, @NonNull LegendEntry entry, @NonNull Legend legend
    ) {
        if (entry.formColor == ColorTemplate.COLOR_SKIP || entry.formColor == ColorTemplate.COLOR_NONE || entry.formColor == 0) {
            return;
        }

        int restoreCount = canvas.save();

        Legend.LegendForm form = entry.form;
        if (form == Legend.LegendForm.DEFAULT) {
            form = legend.getForm();
        }

        mLegendFormPaint.setColor(entry.formColor);

        final float formSize = Utils.convertDpToPixel(Float.isNaN(entry.formSize) ? legend.getFormSize() : entry.formSize);
        final float half = formSize / 2f;

        switch (form) {
            case NONE:
                // Do nothing
                break;

            case EMPTY:
                // Do not draw, but keep space for the form
                break;

            case DEFAULT:
            case CIRCLE:
                mLegendFormPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(x + half, y, half, mLegendFormPaint);
                break;

            case SQUARE:
                mLegendFormPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(x, y - half, x + formSize, y + half, mLegendFormPaint);
                break;

            case LINE: {
                final float formLineWidth = Utils.convertDpToPixel(Float.isNaN(entry.formLineWidth) ? legend.getFormLineWidth() : entry.formLineWidth);
                final DashPathEffect formLineDashEffect = entry.formLineDashEffect == null ? legend.getFormLineDashEffect() : entry.formLineDashEffect;
                mLegendFormPaint.setStyle(Paint.Style.STROKE);
                mLegendFormPaint.setStrokeWidth(formLineWidth);
                mLegendFormPaint.setPathEffect(formLineDashEffect);

                mLineFormPath.reset();
                mLineFormPath.moveTo(x, y);
                mLineFormPath.lineTo(x + formSize, y);
                canvas.drawPath(mLineFormPath, mLegendFormPaint);
            }
            break;
        }

        canvas.restoreToCount(restoreCount);
    }

    /**
     * Draws the provided label at the given position.
     *
     * @param canvas canvas to draw with
     * @param x
     * @param y
     * @param label  the label to draw
     */
    protected void drawLabel(@NonNull Canvas canvas, float x, float y, String label) {
        canvas.drawText(label, x, y, mLegendLabelPaint);
    }
}
