package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

/**
 * Comparator for comparing {@link Entry Entries} by their x-value.
 *
 * @author Philipp Jahoda
 */
public class EntryXComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry entry1, Entry entry2) {
        return Float.compare(entry1.getX(), entry2.getX());
    }
}
