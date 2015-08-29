package com.luciofm.presentation.droiconit.transitions;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.ViewGroup;

/**
 * Created by luciofm on 3/27/15.
 */
public class NoTransition extends Transition {
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return super.createAnimator(sceneRoot, startValues, endValues);
    }
}
