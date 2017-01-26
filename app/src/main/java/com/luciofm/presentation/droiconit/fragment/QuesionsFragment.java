package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.transition.CircularPropagation;
import android.transition.Transition;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.ThanksActivity;
import com.luciofm.presentation.droiconit.anim.AlphaSpan;
import com.luciofm.presentation.droiconit.anim.LayerEnablingAnimatorListener;
import com.luciofm.presentation.droiconit.anim.TextSizeSpan;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.transitions.Recolor;
import com.luciofm.presentation.droiconit.transitions.ZoomOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class QuesionsFragment extends BaseFragment {

    @InjectView(R.id.text1)
    TextView text1;
    @InjectView(R.id.text2)
    TextView text2;
    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.container2)
    ViewGroup container2;

    int blue;
    int white;

    private SpannableString title = new SpannableString("QUESTIONS??");
    AnimatorSet spanSet;

    public QuesionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blue = getResources().getColor(R.color.vibrant_rgb);
        white = getResources().getColor(R.color.white_opaque);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_questions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        container2.setVisibility(View.GONE);

        v.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                animateTitle();
            }
        }, 800);

        return v;
    }

    @Override
    public void onNextPressed() {
        switch (++currentStep) {
            case 2:
                if (spanSet != null)
                    spanSet.cancel();
                animateOut();
                break;
            default:
                super.onNextPressed();
        }
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    private void animateTitle() {
        buildSpansAnimation(0, title.length() - 1);
    }

    private static final int SPAN_ANIM_DURATION = 1200;
    private static final int SPAN_ANIM_DELAY = 120;
    private int currentDelay = 0;

    private void buildSpansAnimation(int start, int end) {

        float textSize = text1.getTextSize();

        ArrayList<TextSizeSpan> tmp = new ArrayList<>();
        for(int index = start ; index <= end ; index++) {
            TextSizeSpan span = new TextSizeSpan(0);
            tmp.add(span);
            title.setSpan(span, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        ArrayList<TextSizeSpan> spans = new ArrayList<>();
        int middle = end / 2;
        spans.add(tmp.get(middle));
        for (int i = 1; i <= middle; i++) {
            spans.add(tmp.get(middle - i));
            spans.add(tmp.get(middle + i));
        }

        currentDelay = 0;
        List<Animator> animators = new ArrayList<>();
        for (TextSizeSpan span : spans) {
            ObjectAnimator anim = ObjectAnimator.ofInt(span, SIZE_SPAN_PROPERTY, 0, (int) textSize);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(SPAN_ANIM_DURATION);
            anim.setStartDelay(currentDelay);
            animators.add(anim);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (isResumed())
                        text1.setText(title);
                }
            });
            currentDelay += SPAN_ANIM_DELAY;
        }

        spanSet = new AnimatorSet();
        spanSet.playTogether(animators);
        spanSet.start();
    }

    private static final Property<TextSizeSpan, Integer> SIZE_SPAN_PROPERTY =
            new Property<TextSizeSpan, Integer>(Integer.class, "SIZE_SPAN_PROPERTY") {
                @Override
                public void set(TextSizeSpan span, Integer value) {
                    span.setSize(value);
                }

                @Override
                public Integer get(TextSizeSpan span) {
                    return span.getSize();
                }
            };

    private static final Property<AlphaSpan, Integer> ALPHA_SPAN_PROPERTY =
            new Property<AlphaSpan, Integer>(Integer.class, "ALPHA_SPAN_PROPERTY") {

                @Override
                public void set(AlphaSpan span, Integer value) {
                    span.setAlpha(value);
                }

                @Override
                public Integer get(AlphaSpan span) {
                    return span.getAlpha();
                }
            };

    private void animateOut() {
        text1.setVisibility(View.GONE);
        container2.setVisibility(View.VISIBLE);

        container2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                container2.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewPropertyAnimator animator = animateViewsOut();
                animator.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        container2.setVisibility(View.GONE);
                    }
                });
                animator.start();

                text2.setVisibility(View.VISIBLE);
                text2.setAlpha(0f);
                text2.animate().alpha(1f).setDuration(300).setStartDelay(600).start();

                ObjectAnimator background = ObjectAnimator.ofObject(container, "backgroundColor",
                        new ArgbEvaluator(), blue,
                        white);
                background.setDuration(300);
                background.setStartDelay(600);
                background.start();

                return false;
            }
        });
    }

    public static final int ANIM_DELAY = 30;
    public static final int MULTIPLIER = 1;
    public static final int ANIM_DURATION = 900;
    AccelerateInterpolator accelerate = new AccelerateInterpolator();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public ViewPropertyAnimator animateViewsOut() {
        ArrayList<View> views = new ArrayList<>();
        ViewPropertyAnimator anim = null;
        for (int i = 0; i < container2.getChildCount(); i++) {
            View v = container2.getChildAt(i);
            views.add(v);
        }

        Collections.shuffle(views);

        for (int i = 0; i < views.size(); i++) {
            View v = views.get(i);
            v.setPivotY(v.getHeight() / 2);
            v.setPivotX(v.getWidth() / 2);
            anim = v.animate();
            anim.alpha(0f).scaleY(12f).scaleX(12f).setInterpolator(accelerate)
                    .setStartDelay(i * ANIM_DELAY * MULTIPLIER)
                    .setDuration((ANIM_DURATION - (i * ANIM_DELAY)) * MULTIPLIER)
                    .setListener(new LayerEnablingAnimatorListener(v));

            container.getOverlay().add(v);
        }
        return anim;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (transit == 0 | enter) {
            return null;
        }

        //Target will be filled in by the framework
        return enter ? ObjectAnimator.ofFloat(null, new XFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new XFractionProperty(), 0f, -1f);
    }

}
