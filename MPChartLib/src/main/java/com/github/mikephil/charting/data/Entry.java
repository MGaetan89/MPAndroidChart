package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.utils.Utils;

/**
 * Class representing one entry in the chart. Might contain multiple values. Might only contain a
 * single value depending on the used constructor.
 *
 * @author Philipp Jahoda
 */
public class Entry extends BaseEntry implements Parcelable {
    /**
     * The x value.
     */
    private float x = 0f;

    public Entry() {
        super();
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    public Entry(float x, float y) {
        super(y);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x    the x value
     * @param y    the y value (the actual value of the entry)
     * @param data Spot for additional data this Entry represents.
     */
    public Entry(float x, float y, @Nullable Object data) {
        super(y, data);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x    the x value
     * @param y    the y value (the actual value of the entry)
     * @param icon icon image
     */
    public Entry(float x, float y, @Nullable Drawable icon) {
        super(y, icon);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x    the x value
     * @param y    the y value (the actual value of the entry)
     * @param icon icon image
     * @param data Spot for additional data this Entry represents.
     */
    public Entry(float x, float y, @Nullable Drawable icon, @Nullable Object data) {
        super(y, icon, data);
        this.x = x;
    }

    /**
     * Returns the x-value of this Entry object.
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this Entry object.
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Returns an exact copy of the entry.
     */
    @NonNull
    public Entry copy() {
        return new Entry(x, getY(), getData());
    }

    /**
     * Compares value, xIndex and data of the entries. Returns true if entries are equal in those
     * points, false if not. Does not check by hash-code like it's done by the "equals" method.
     *
     * @param entry
     */
    public boolean equalTo(@Nullable Entry entry) {
        if (entry == null) {
            return false;
        }

        if (entry.getData() != this.getData()) {
            return false;
        }

        if (Math.abs(entry.x - this.x) > Utils.FLOAT_EPSILON) {
            return false;
        }

        return Math.abs(entry.getY() - this.getY()) <= Utils.FLOAT_EPSILON;
    }

    /**
     * Returns a string representation of the entry containing x-index and value.
     */
    @NonNull
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        if (getData() != null) {
            if (getData() instanceof Parcelable) {
                dest.writeInt(1);
                dest.writeParcelable((Parcelable) this.getData(), flags);
            } else {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }
        } else {
            dest.writeInt(0);
        }
    }

    protected Entry(@NonNull Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
        if (in.readInt() == 1) {
            this.setData(in.readParcelable(this.getClass().getClassLoader()));
        }
    }

    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
