package com.luciofm.presentation.droiconit.anim;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by luciofm on 9/23/14.
 */
public class TextSizeSpan extends MetricAffectingSpan implements ParcelableSpan {

    private int mSize;
    private boolean mDip;

    /**
     * Set the text size to <code>size</code> physical pixels.
     */
    public TextSizeSpan(int size) {
        mSize = size;
    }

    /**
     * Set the text size to <code>size</code> physical pixels,
     * or to <code>size</code> device-independent pixels if
     * <code>dip</code> is true.
     */
    public TextSizeSpan(int size, boolean dip) {
        mSize = size;
        mDip = dip;
    }

    public TextSizeSpan(Parcel src) {
        mSize = src.readInt();
        mDip = src.readInt() != 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSize);
        dest.writeInt(mDip ? 1 : 0);
    }

    public int getSize() {
        return mSize;
    }

    public boolean getDip() {
        return mDip;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public void setDip(boolean mDip) {
        this.mDip = mDip;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (mDip) {
            ds.setTextSize(mSize * ds.density);
        } else {
            ds.setTextSize(mSize);
        }
    }

    @Override
    public void updateMeasureState(TextPaint ds) {
        if (mDip) {
            ds.setTextSize(mSize * ds.density);
        } else {
            ds.setTextSize(mSize);
        }
    }

    @Override
    public int getSpanTypeId() {
        return 0;
    }
}
