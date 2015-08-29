package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.CircularPropagation;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;
import com.luciofm.presentation.droiconit.transitions.NoTransition;
import com.luciofm.presentation.droiconit.util.IOUtils;
import com.luciofm.presentation.droiconit.util.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SceneFragment extends BaseFragment {

    private static final int ANIM_DURATION = 600;

    @InjectView(R.id.container)
    ViewGroup container;
    @InjectView(R.id.titleContainer)
    View titleContainer;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.checkMark)
    View checkMark;

    @InjectView(R.id.container2)
    ViewGroup container2;
    @InjectView(R.id.root)
    ViewGroup root;
    @InjectView(R.id.root2)
    ViewGroup root2;

    @InjectView(R.id.textSwitcher)
    TextSwitcher textSwitcher;
    @InjectView(R.id.textCode)
    TextSwitcher codeSwitcher;

    TransitionManager tm;
    Scene scene1;
    Scene scene2;

    Spanned code1;
    Spanned code2;
    Spanned code3;
    Spanned code4;
    Spanned code5;
    Spanned code6;
    Spanned code7;
    Spanned code8;


    public SceneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_scene;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        textSwitcher.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        textSwitcher.setOutAnimation(getActivity(), android.R.anim.slide_out_right);

        codeSwitcher.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        codeSwitcher.setOutAnimation(getActivity(), android.R.anim.slide_out_right);

        code1 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scenes1.xml.html"));
        code2 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scenes2.xml.html"));
        code3 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scenes3.xml.html"));
        code4 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scene_transition.xml.html"));
        code5 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/to_details.xml.html"));
        code6 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/from_details.xml.html"));
        code7 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scene_code.java.html"));
        code8 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/scene_code2.java.html"));

        tm = TransitionInflater.from(getActivity()).inflateTransitionManager(R.transition.scene_transition, root);
        scene1 = Scene.getSceneForLayout(root, R.layout.scene_scene1, getActivity());
        scene2 = Scene.getSceneForLayout(root, R.layout.scene_scene2, getActivity());
        scene2.setEnterAction(new Runnable() {
            @Override
            public void run() {
                ((ImageView) scene2.getSceneRoot().findViewById(R.id.image)).setImageResource(R.drawable.ic_image);
            }
        });

        return v;
    }

    @Override
    public void onNextPressed() {
        TransitionSet image;
        TransitionSet set;
        switch (++currentStep) {
            case 2:
                container.setLayoutTransition(new LayoutTransition());
                titleContainer.animate().scaleY(0.5f).scaleX(0.5f);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
                params.topMargin = Utils.dpToPx(getActivity(), 40) * -1;
                titleContainer.setLayoutParams(params);

                container2.setVisibility(View.VISIBLE);
                textSwitcher.setVisibility(View.VISIBLE);
                textSwitcher.setText("Layouts 1 & 2");
                break;
            case 3:
                container.setLayoutTransition(null);
                TransitionManager.beginDelayedTransition(container);
                root2.setVisibility(View.GONE);
                textSwitcher.setText("Scene 1");
                break;
            case 4:
                tm.transitionTo(scene2);
                textSwitcher.setText("Scene 2");
                break;
            case 5:
                checkMark.setAlpha(0);
                tm.transitionTo(scene1);
                textSwitcher.setText("Scene 1");
                break;
            case 6:
                checkMark.animate().alpha(0f);
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());;
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(image).addTransition(new Slide(Gravity.BOTTOM));
                set.excludeTarget(R.id.checkMark, true);
                set.setDuration(ANIM_DURATION);
                TransitionManager.go(scene2, set);
                textSwitcher.setText("Scene 2 - sliding");
                break;
            case 7:
                checkMark.setAlpha(0f);
                tm.transitionTo(scene1);
                textSwitcher.setText("Scene 1");
                break;
            case 8:
                checkMark.animate().alpha(0f);
                Slide slide = new Slide();
                slide.setSlideEdge(Gravity.BOTTOM);
                slide.setPropagation(new CircularPropagation() {
                    @Override
                    public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
                        long delay = super.getStartDelay(sceneRoot, transition, startValues, endValues);
                        return delay * 14;
                    }
                });
                slide.setEpicenterCallback(new Transition.EpicenterCallback() {
                    @Override
                    public Rect onGetEpicenter(Transition transition) {
                        int[] loc = new int[2];
                        root.getLocationOnScreen(loc);

                        return new Rect((root.getWidth() / 2) - 40, loc[1], (root.getWidth() / 2) + 40, loc[1] + 40);
                    }
                });
                image = new TransitionSet();
                image.setOrdering(TransitionSet.ORDERING_TOGETHER);
                image.addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());;
                set = new TransitionSet();
                set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
                set.addTransition(image).addTransition(slide);
                set.excludeTarget(R.id.checkMark, true);
                set.setDuration(ANIM_DURATION + (ANIM_DURATION / 2));
                TransitionManager.go(scene2, set);
                textSwitcher.setText("Scene 2 - sliding delayed");
                break;
            case 9:
                container.setLayoutTransition(new LayoutTransition());
                container2.setVisibility(View.GONE);
                textSwitcher.setVisibility(View.GONE);
                codeSwitcher.setVisibility(View.VISIBLE);
                codeSwitcher.setText(code1);
                break;
            case 10:
                container.setLayoutTransition(null);
                codeSwitcher.setText(code2);
                break;
            case 11:
                codeSwitcher.setText(code3);
                break;
            case 12:
                codeSwitcher.setText(code4);
                break;
            case 13:
                codeSwitcher.setText(code5);
                break;
            case 14:
                codeSwitcher.setText(code6);
                break;
            case 15:
                codeSwitcher.setText(code7);
                break;
            case 16:
                codeSwitcher.setText(code8);
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
    public void onPrevPressed() {
        if (--currentStep >= 3) {
            container.setLayoutTransition(null);
            TransitionManager.beginDelayedTransition(container);
            container2.setVisibility(View.VISIBLE);
            codeSwitcher.setVisibility(View.GONE);
            root2.setVisibility(View.GONE);
            textSwitcher.setText("Scene 1");
            TransitionManager.go(scene1, new NoTransition());
            currentStep = 3;
            return;
        }

        super.onPrevPressed();
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