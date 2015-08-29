package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.activity.DetailsActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.util.IOUtils;
import com.luciofm.presentation.droiconit.util.Utils;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ActivityTransitionFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.container2)
    ViewGroup container2;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.thumb)
    ImageView image;
    @InjectView(R.id.text2)
    TextSwitcher text2;

    Spanned code1;
    Spanned code2;
    Spanned code3;

    public ActivityTransitionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        code1 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/activity_transitions.xml.html"));
        code2 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/activity_transitions.java.html"));
        code3 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/activity_transitions_methods.java.html"));
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_activitytransition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        text2.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        text2.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
        text2.setText(code1);

        return v;
    }

    @Override
    public void onNextPressed() {
        switch (++currentStep) {
            case 2:
                title.animate().scaleX(0.6f).scaleY(0.6f);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) title.getLayoutParams();
                params.topMargin = Utils.dpToPx(getActivity(), 40) * -1;
                title.setLayoutParams(params);

                TransitionManager.beginDelayedTransition(container);
                container2.setVisibility(View.VISIBLE);
                break;
            case 3:
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                setupTransition();
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), image, "thumb");
                startActivity(intent, options.toBundle());
                break;
            case 4:
                container2.setVisibility(View.GONE);
                text2.setVisibility(View.VISIBLE);
                break;
            case 5:
                text2.setText(code2);
                break;
            case 6:
                text2.setText(code3);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    private void setupTransition() {
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    @Override
    public void onPrevPressed() {
        if (currentStep >= 3)
            currentStep = 2;
        else
            super.onPrevPressed();
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