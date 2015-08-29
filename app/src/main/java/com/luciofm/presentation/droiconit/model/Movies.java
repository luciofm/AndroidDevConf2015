package com.luciofm.presentation.droiconit.model;

import com.luciofm.presentation.droiconit.R;

import java.util.ArrayList;

/**
 * Created by luciofm on 1/28/15.
 */
public class Movies {

    public static ArrayList<Movie> movies;

    static {
        movies = new ArrayList<>();


        movies.add(new Movie("The Big Lebowski",
                "1998",
                R.drawable.lebowski_ver2,
                R.drawable.lebowski_scene_02));

        movies.add(new Movie("Crazy Heart",
                "2009",
                R.drawable.crazy_heart,
                R.drawable.crazy_heart_scene_01));

        movies.add(new Movie("True Grit",
                "2010",
                R.drawable.true_grit,
                R.drawable.true_grit_scene_01));

        movies.add(new Movie("The Men Who Stare at Goats ",
                "2009",
                R.drawable.goats,
                R.drawable.goats_scene_02));

        movies.add(new Movie("Arlington Road",
                "1999",
                R.drawable.arlington,
                R.drawable.arlington_scene_02));

        movies.add(new Movie("Tron",
                "1982",
                R.drawable.tron,
                R.drawable.tron_screen_01));

        movies.add(new Movie("Tron Legacy",
                "2010",
                R.drawable.tron_legacy,
                R.drawable.tron_legacy_02));

        movies.add(new Movie("Iron Man",
                "2008",
                R.drawable.iron_man,
                R.drawable.iron_man_scene));
        movies.add(new Movie("Surf's up",
                "2007",
                R.drawable.surfs_up,
                R.drawable.surfs_up_scene));
        movies.add(new Movie("Seabiscuit",
                "2003",
                R.drawable.seabiscuit,
                R.drawable.seabiscuit_scene));
    }
}
