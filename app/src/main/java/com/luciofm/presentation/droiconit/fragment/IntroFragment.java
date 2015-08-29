package com.luciofm.presentation.droiconit.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.title2)
    TextView title2;
    @InjectView(R.id.title3)
    TextView title3;
    @InjectView(R.id.subtitle)
    TextView subtitle;

    public IntroFragment() {
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_intro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;
        return v;
    }

    @Override
    public void onNextPressed() {
        switch (++currentStep) {
            case 2:
                TransitionSet set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set.addTransition(new Fade(Fade.OUT)).
                        addTransition(new ChangeBounds()).
                        addTransition(new Fade(Fade.IN));
                TransitionManager.beginDelayedTransition(container, set);
                title.setVisibility(View.GONE);
                title2.setVisibility(View.VISIBLE);
                break;
            case 3:
                TransitionManager.beginDelayedTransition(container,
                        new AutoTransition().setOrdering(TransitionSet.ORDERING_TOGETHER));
                title2.setVisibility(View.GONE);
                title3.setVisibility(View.VISIBLE);
                subtitle.setVisibility(View.VISIBLE);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (transit == 0) {
            return null;
        }

        //Target will be filled in by the framework
        return enter ? ObjectAnimator.ofFloat(null, new YFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new YFractionProperty(), 0f, -1f);
    }
}
