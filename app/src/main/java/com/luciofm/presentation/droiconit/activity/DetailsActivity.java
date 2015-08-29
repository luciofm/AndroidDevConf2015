package com.luciofm.presentation.droiconit.activity;

import android.os.Bundle;
import android.transition.CircularPropagation;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.fragment.BaseFragment;
import com.luciofm.presentation.droiconit.transitions.TransitionListenerAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsActivity extends BaseActivity {

    @InjectView(R.id.thumb)
    ImageView image;
    @InjectView(R.id.title)
    TextView title;

    int currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        //setupTransition();
        /*getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);*/

        getWindow().getEnterTransition().addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                setupPostTransition();
            }
        });

        postponeEnterTransition();
        final ViewTreeObserver observer = getWindow().getDecorView().getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    private void setupTransition() {
        TransitionSet set;
        CircularPropagation propagation;
        Explode explode;
        Slide slide;
        Slide slide2;
        Fade fade;


        slide = new Slide(Gravity.LEFT);
        slide2 = new Slide(Gravity.BOTTOM);
        fade = new Fade(Fade.IN);
        slide.addTarget(R.id.title);
        slide2.addTarget(R.id.content);
        set = new TransitionSet();
        set.addTransition(slide).addTransition(slide2).addTransition(fade);
        //set.setDuration(900);
        getWindow().setEnterTransition(set);

    }

    private void setupPostTransition() {
        switch (currentStep) {

        }
    }

    @Override
    public boolean shouldSetSystemUiVisibility() {
        return false;
    }

    @Override
    ArrayList<Class<? extends BaseFragment>> getFragmentsList() {
        return null;
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_details;
    }

    @Override
    int getFragmentContainerId() {
        return 0;
    }
}
