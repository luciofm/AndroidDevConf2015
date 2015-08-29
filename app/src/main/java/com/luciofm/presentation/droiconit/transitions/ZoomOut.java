package com.luciofm.presentation.droiconit.transitions;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

public class ZoomOut extends Visibility {
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        view.setScaleX(12f);
        view.setScaleY(12f);
        view.setAlpha(0f);

        PropertyValuesHolder[] pvh = new PropertyValuesHolder[3];
        pvh[0] = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        pvh[1] = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
        pvh[2] = PropertyValuesHolder.ofFloat(View.ALPHA, 1f);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
        anim.setInterpolator(new OvershootInterpolator());

        return anim;
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        PropertyValuesHolder[] pvh = new PropertyValuesHolder[3];
        pvh[0] = PropertyValuesHolder.ofFloat(View.SCALE_X, 12f);
        pvh[1] = PropertyValuesHolder.ofFloat(View.SCALE_Y, 12f);
        pvh[2] = PropertyValuesHolder.ofFloat(View.ALPHA, 0f);

        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
        anim.setInterpolator(new AnticipateInterpolator());

        return anim;
    }
}
