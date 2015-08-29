package com.luciofm.presentation.droiconit.model;

/**
 * Created by luciofm on 27/08/15.
 */
public class Entry {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private int type;
    private int imageId;
    private String videoUrl;

    public Entry(int imageId) {
        type = TYPE_IMAGE;
        this.imageId = imageId;
    }

    public Entry(int imageId, String videoUrl) {
        type = TYPE_VIDEO;
        this.imageId = imageId;
        this.videoUrl = "android.resource://com.luciofm.presentation.droiconit/raw/" + videoUrl;
    }

    public int getType() {
        return type;
    }

    public int getImageId() {
        return imageId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
