package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AwesomeFragment extends BaseFragment {

    public AwesomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_awesome;
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
        ((BaseActivity) getActivity()).nextFragment();
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
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