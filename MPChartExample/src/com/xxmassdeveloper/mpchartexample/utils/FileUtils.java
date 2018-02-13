package com.xxmassdeveloper.mpchartexample.utils;

import android.content.res.AssetManager;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class for interacting with the assets and the devices storage to
 * load and save DataSet objects from and to .txt files.
 * 
 * @author Philipp Jahoda
 */
public class FileUtils {
    private static final String LOG = "MPChart-FileUtils";

    /**
     * Loads an array of Entries from a textfile from the assets folder.
     * 
     * @param am
     * @param path the name of the file in the assets folder (+ path if needed)
     * @return
     */
    public static List<Entry> loadEntriesFromAssets(AssetManager am, String path) {
        List<Entry> entries = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(am.open(path), "UTF-8"));

            String line = reader.readLine();

            while (line != null) {
                // process line
                String[] split = line.split("#");

                if (split.length <= 2) {
                    entries.add(new Entry(Float.parseFloat(split[1]), Float.parseFloat(split[0])));
                } else {

                    float[] vals = new float[split.length - 1];

                    for (int i = 0; i < vals.length; i++) {
                        vals[i] = Float.parseFloat(split[i]);
                    }

                    entries.add(new BarEntry(Integer.parseInt(split[split.length - 1]), vals));
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG, e.toString());
                }
            }
        }

        return entries;
    }

    public static List<BarEntry> loadBarEntriesFromAssets(AssetManager am, String path) {
        List<BarEntry> entries = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(am.open(path), "UTF-8"));

            String line = reader.readLine();

            while (line != null) {
                // process line
                String[] split = line.split("#");

                entries.add(new BarEntry(Float.parseFloat(split[1]), Float.parseFloat(split[0])));

                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG, e.toString());
                }
            }
        }

        return entries;
    }
}
