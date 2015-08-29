package com.luciofm.presentation.droiconit.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luciofm on 5/25/14.
 */
public class IOUtils {

    private IOUtils() {
    }

    public static InputStream openAssetFile(Context context, String file) throws IOException {
        AssetManager assetManager = context.getAssets();

        return assetManager.open(file);

    }

    public static String readFile(Context context, String file) {
        InputStream is;

        try {
            is = openAssetFile(context, file);
            return org.apache.commons.io.IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
