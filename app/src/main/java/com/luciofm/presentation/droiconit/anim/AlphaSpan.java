package com.luciofm.presentation.droiconit.anim;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/**
 * Created by luciofm on 5/31/14.
 */
public class AlphaSpan extends CharacterStyle implements UpdateAppearance, Parcelable {

    private int mAlpha = 0;

    public AlphaSpan(int alpha) {
        this.mAlpha = alpha;
    }

    public int getAlpha() {
        return mAlpha;
    }

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setAlpha(mAlpha);
    }

    protected AlphaSpan(Parcel in) {
        mAlpha = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAlpha);
    }

    @SuppressWarnings("unused")
    public static final Creator<AlphaSpan> CREATOR = new Creator<AlphaSpan>() {
        @Override
        public AlphaSpan createFromParcel(Parcel in) {
            return new AlphaSpan(in);
        }

        @Override
        public AlphaSpan[] newArray(int size) {
            return new AlphaSpan[size];
        }
    };
}
