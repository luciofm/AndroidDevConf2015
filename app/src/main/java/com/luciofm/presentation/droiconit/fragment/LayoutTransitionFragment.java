package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.util.Utils;
import com.luciofm.presentation.droiconit.widget.SquareGridLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LayoutTransitionFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.container2)
    ViewGroup container2;
    @InjectView(R.id.container3)
    ViewGroup container3;
    @InjectView(R.id.container4)
    ViewGroup container4;
    @InjectView(R.id.textSwitcher)
    TextSwitcher textSwitcher;

    public LayoutTransitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_layouttransition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        textSwitcher.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(getActivity(), android.R.anim.slide_out_right);

        return v;
    }

    @Override
    public void onNextPressed() {
        TextView textView;
        TextView textView2;
        int size = Utils.dpToPx(getActivity(), 120);
        SquareGridLayout.LayoutParams params = new SquareGridLayout.LayoutParams(size, size);
        switch (++currentStep) {
            case 2:
                TransitionManager.beginDelayedTransition(container);
                title.setVisibility(View.GONE);
                textSwitcher.setVisibility(View.VISIBLE);
                container2.setVisibility(View.VISIBLE);
                textSwitcher.setText("LayoutTransition");
                break;
            case 3:
                textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView.setText("Item C");
                container3.addView(textView, 1);

                textView2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView2.setText("Item E");
                container4.addView(textView2, 2, params);
                break;
            case 4:
                container3.removeViewAt(1);
                container4.removeViewAt(2);
                break;
            case 5:
                textSwitcher.setText("android:animateLayoutChanges=\"true\"");
                break;
            case 6:
                container3.setLayoutTransition(new LayoutTransition());
                container4.setLayoutTransition(new LayoutTransition());
                textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView.setText("Item C");
                container3.addView(textView, 1);

                textView2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                params = new SquareGridLayout.LayoutParams(size, size);
                textView2.setText("Item E");
                container4.addView(textView2, 2, params);
                break;
            case 7:
                container3.removeViewAt(1);
                container4.removeViewAt(2);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    @Override
    public void onPrevPressed() {
        TextView textView;
        TextView textView2;
        int size = Utils.dpToPx(getActivity(), 120);
        SquareGridLayout.LayoutParams params = new SquareGridLayout.LayoutParams(size, size);
        switch (--currentStep) {
            case 6:
                textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView.setText("Item C");
                container3.addView(textView, 1);

                textView2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                params = new SquareGridLayout.LayoutParams(size, size);
                textView2.setText("Item E");
                container4.addView(textView2, 2, params);
                break;
            case 5:
                container3.removeViewAt(1);
                container4.removeViewAt(2);
                break;
            case 4:
                textSwitcher.setText("LayoutTransition");
                break;
            case 3:
                container3.setLayoutTransition(null);
                container4.setLayoutTransition(null);
                textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView.setText("Item C");
                container3.addView(textView, 1);

                textView2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.text_item, container3, false);
                textView2.setText("Item E");
                container4.addView(textView2, 2, params);
                break;
            case 2:
                container3.removeViewAt(1);
                container4.removeViewAt(2);
                break;
            case 1:
                TransitionManager.beginDelayedTransition(container);
                title.setVisibility(View.VISIBLE);
                textSwitcher.setVisibility(View.GONE);
                container2.setVisibility(View.GONE);
                break;
            case 0:
                super.onPrevPressed();
        }
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