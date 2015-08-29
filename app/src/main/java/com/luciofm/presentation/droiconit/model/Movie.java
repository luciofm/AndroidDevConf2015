package com.luciofm.presentation.droiconit.model;

import org.parceler.Parcel;

@Parcel
public class Movie {
    public String title;
    public String year;
    public int resId;
    public int sceneId;

    public Movie() {
    }

    public Movie(String title, String year, int resId, int sceneId) {
        this.title = title;
        this.year = year;
        this.resId = resId;
        this.sceneId = sceneId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public int getResId() {
        return resId;
    }

    public int getSceneId() {
        return sceneId;
    }
}
