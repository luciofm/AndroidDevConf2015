package com.luciofm.presentation.droiconit.model;

import org.parceler.Parcel;

@Parcel
public class Item {
    String title;
    int color;

    public Item() {
    }

    public Item(String title, int color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}