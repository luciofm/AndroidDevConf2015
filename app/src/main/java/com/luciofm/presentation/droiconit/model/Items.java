package com.luciofm.presentation.droiconit.model;

import com.luciofm.presentation.droiconit.R;

import java.util.ArrayList;

/**
 * Created by luciofm on 3/31/15.
 */
public class Items {
    public static ArrayList<Item> items;

    static {
        items = new ArrayList<>();

        items.add(new Item("Shared Element", R.color.vibrant_rgb));
        items.add(new Item("Exit transition", R.color.dark_muted_rgb));
        items.add(new Item("Exit+Enter", R.color.dark_vibrant_rgb));
        items.add(new Item("Return", R.color.light_vibrant_rgb));
        items.add(new Item("Reenter", R.color.light_muted_rgb));
        items.add(new Item("Everything", R.color.muted_rgb));
    }
}
