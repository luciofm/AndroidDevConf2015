package com.luciofm.presentation.droiconit.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WorkFragment extends BaseFragment {

    @InjectView(R.id.imageWhi1)
    ImageView imageWhi1;
    @InjectView(R.id.imageWhi2)
    ImageView imageWhi2;

    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_work;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);

        imageWhi1.animate().alpha(1f).setDuration(200).start();

        v.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isAdded())
                    return;
                imageWhi1.animate().alpha(0f).setDuration(1000).start();
                imageWhi2.animate().alpha(1f).setDuration(1000).start();
            }
        }, 200);

        currentStep = 1;
        return v;
    }

    @Override
    public void onNextPressed() {
        super.onNextPressed();
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
        return enter ? null :
                ObjectAnimator.ofFloat(null, new YFractionProperty(), 0f, -1f);
    }
}
