package com.luciofm.presentation.droiconit.anim;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by luciofm on 10/17/14.
 */
public class TextTransform extends Transition {

    private static final String PROPNAME_TEXTSIZE = "android:textTransform:textSize";
    private static final String PROPNAME_TEXTCOLOR = "android:textTransform:textColor";
    private static final String PROPNAME_PARENT = "android:textTransform:parent";
    private static final String PROPNAME_WINDOW_X = "android:textTransform:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:textTransform:windowY";

    int[] tempLocation = new int[2];

    private Method suppressLayout;

    private void captureValues(TransitionValues values) {
        View view = values.view;
        if (!(view instanceof TextView))
            return;

        TextView text = (TextView) view;
        values.values.put(PROPNAME_TEXTSIZE, pixelsToSp(view.getContext(), text.getTextSize()));
        values.values.put(PROPNAME_TEXTCOLOR, ((TextView) view).getTextColors().getDefaultColor());
        values.values.put(PROPNAME_PARENT, view.getParent());
        values.view.getLocationInWindow(tempLocation);
        values.values.put(PROPNAME_WINDOW_X, tempLocation[0]);
        values.values.put(PROPNAME_WINDOW_Y, tempLocation[1]);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Map<String, Object> startParentVals = startValues.values;
        Map<String, Object> endParentVals = endValues.values;
        ViewGroup startParent = (ViewGroup) startParentVals.get(PROPNAME_PARENT);
        ViewGroup endParent = (ViewGroup) endParentVals.get(PROPNAME_PARENT);
        if (startParent == null || endParent == null) {
            return null;
        }
        final View view = endValues.view;
        boolean parentsEqual = (startParent == endParent) ||
                (startParent.getId() == endParent.getId());

        if (!parentsEqual || !(view instanceof TextView))
            return null;

        int numChanges = 0;
        float startSize = (float) startValues.values.get(PROPNAME_TEXTSIZE);
        float endSize = (float) endValues.values.get(PROPNAME_TEXTSIZE);
        int startX = (int) startValues.values.get(PROPNAME_WINDOW_X);
        int endX = (int) endValues.values.get(PROPNAME_WINDOW_X);
        int startY = (int) startValues.values.get(PROPNAME_WINDOW_Y);
        int endY = (int) endValues.values.get(PROPNAME_WINDOW_Y);
        int startColor = (int) startValues.values.get(PROPNAME_TEXTCOLOR);
        int endColor = (int) endValues.values.get(PROPNAME_TEXTCOLOR);

        if (startSize != endSize) numChanges++;
        if (startX != endX) numChanges++;
        if (startY != endY) numChanges++;
        if (startColor != endColor) numChanges++;

        if (numChanges > 0) {
            PropertyValuesHolder pvh[] = new PropertyValuesHolder[numChanges];
            int pvhIndex = 0;

            if (startX != endX) {
                float delta = startX - endX;
                view.setTranslationX(delta);
                pvh[pvhIndex++] = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0);
            }

            if (startY != endY) {
                float delta = startY - endY;
                view.setTranslationY(delta);
                pvh[pvhIndex++] = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);
            }

            if (startSize != endSize) {
                ((TextView) view).setTextSize(startSize);
                pvh[pvhIndex++] = PropertyValuesHolder.ofFloat("textSize", startSize - 1, endSize);
            }

            if (startColor != endColor) {
                ((TextView) view).setTextColor(startColor);
                pvh[pvhIndex++] = PropertyValuesHolder.ofObject("textColor",
                        new ArgbEvaluator(), startColor, endColor);
            }

            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
            /*anim.addListener(new LayerEnablingAnimatorListener(view));
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                int frame = 0;
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Log.d("Droidcon", "Frame: " + frame);
                    frame++;
                }
            });*/
            return anim;
        }

        return null;
    }

    float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    void callSuppressLayout(ViewGroup viewGroup, boolean value) {
        if (suppressLayout != null) {
            try {
                suppressLayout.invoke(viewGroup, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            suppressLayout = viewGroup.getClass().getMethod("suppressLayout", Boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
