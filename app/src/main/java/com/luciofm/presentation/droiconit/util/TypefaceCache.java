package com.luciofm.presentation.droiconit.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by luciofm on 3/24/15.
 */
public class TypefaceCache {
    private static HashMap<String, Typeface> typefaces = new HashMap<>();

    public static Typeface getTypeface(Context context, String typeface) {
        Typeface tf = typefaces.get(typeface);
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + typeface);

            if (tf == null)
                tf = Typeface.DEFAULT;

            typefaces.put(typeface, tf);
        }

        return tf;
    }
}
