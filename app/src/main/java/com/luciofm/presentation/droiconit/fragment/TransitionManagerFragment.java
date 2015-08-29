package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.transition.ChangeBounds;
import android.transition.CircularPropagation;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.transitions.Pop;
import com.luciofm.presentation.droiconit.util.IOUtils;
import com.luciofm.presentation.droiconit.util.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class TransitionManagerFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup root;
    @InjectView(R.id.container2)
    ViewGroup container2;
    @InjectView(R.id.container3)
    ViewGroup container3;

    @InjectView(R.id.reg_container)
    View reg_container;
    @InjectView(R.id.login_container)
    View login_container;
    @InjectView(R.id.buttonReg1)
    Button buttonReg;
    @InjectView(R.id.buttonLog1)
    Button buttonLog;

    @InjectViews({R.id.editReg1, R.id.editReg2, R.id.editReg3, R.id.buttonReg1})
    List<View> register;

    @InjectViews({R.id.editLog1, R.id.editLog2, R.id.buttonLog1})
    List<View> login;

    @InjectView(R.id.text1)
    TextView text1;
    @InjectView(R.id.text2)
    TextSwitcher text2;

    @InjectView(R.id.meme)
    ImageView meme;

    Spanned code1;
    Spanned code2;
    Spanned codeAuto;
    Spanned codeTransitions;
    Spanned code3;
    Spanned code4;
    Spanned code5;
    Spanned code6;

    private static final int DEFAULT = 0;
    private static final int CUSTOM = 1;
    private int transitionType = DEFAULT;

    private boolean registerOpened = false;
    private boolean loginOpened = false;
    private boolean memeShowed = false;

    public TransitionManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_tm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        code1 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/morph.xml.html"));
        code2 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/delayed.java.html"));
        codeAuto = Html.fromHtml(IOUtils.readFile(getActivity(), "source/delayed2.java.html"));
        codeTransitions = Html.fromHtml(IOUtils.readFile(getActivity(), "source/transitions.java.html"));

        code3 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/custom2.java.html"));
        code4 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/custom3.java.html"));
        code5 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/custom4.java.html"));
        code6 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/custom1.java.html"));

        text2.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        text2.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
        text2.setText(code1);

        return v;
    }

    @Override
    public void onNextPressed() {
        switch (++currentStep) {
            case 2:
                text1.animate().scaleX(0.6f).scaleY(0.6f);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) text1.getLayoutParams();
                params.topMargin = Utils.dpToPx(getActivity(), 40) * -1;
                text1.setLayoutParams(params);

                TransitionManager.beginDelayedTransition(root);
                container2.setVisibility(View.VISIBLE);
                break;
            case 3:
                Utils.dispatchTouch(reg_container);
                break;
            case 4:
                Utils.dispatchTouch(buttonReg);
                break;
            case 5:
                Utils.dispatchTouch(login_container);
                break;
            case 6:
                Utils.dispatchTouch(buttonLog);
                break;
            case 7:
                Utils.dispatchTouch(reg_container);
                break;
            case 8:
                if (!memeShowed) {
                    memeShowed = true;
                    TransitionManager.beginDelayedTransition(root);
                    container2.setVisibility(View.GONE);
                    text1.setVisibility(View.GONE);
                    meme.setVisibility(View.VISIBLE);
                    break;
                } else {
                    TransitionManager.beginDelayedTransition(root);
                    container2.setVisibility(View.GONE);
                    currentStep = 9;
                }
            case 9:
                TransitionManager.beginDelayedTransition(root);
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                meme.setVisibility(View.GONE);
                break;
            case 10:
                text2.setText(code2);
                break;
            case 11:
                text2.setText(codeAuto);
                break;
            case 12:
                text2.setText(codeTransitions);
                break;
            case 13:
                transitionType = CUSTOM;
                ButterKnife.apply(register, new ButterKnife.Action<View>() {
                    @Override
                    public void apply(View view, int i) {
                        view.setVisibility(View.GONE);
                    }
                });
                ButterKnife.apply(login, new ButterKnife.Action<View>() {
                    @Override
                    public void apply(View view, int i) {
                        view.setVisibility(View.GONE);
                    }
                });
                TransitionManager.beginDelayedTransition(root);
                text2.setVisibility(View.GONE);
                container2.setVisibility(View.VISIBLE);
                break;
            case 14:
                Utils.dispatchTouch(reg_container);
                break;
            case 15:
                Utils.dispatchTouch(buttonReg);
                break;
            case 16:
                Utils.dispatchTouch(login_container);
                break;
            case 17:
                Utils.dispatchTouch(buttonLog);
                break;
            case 18:
                Utils.dispatchTouch(reg_container);
                break;
            case 19:
                TransitionManager.beginDelayedTransition(root);
                container2.setVisibility(View.GONE);
                text2.setVisibility(View.VISIBLE);
                text2.setText(code3);
                break;
            case 20:
                text2.setText(code4);
                break;
            case 21:
                text2.setText(code5);
                break;
            case 22:
                text2.setText(code6);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    @Override
    public void onPrevPressed() {
        if (--currentStep == 8) {
            currentStep = 7;
            text2.setVisibility(View.GONE);
            container2.setVisibility(View.VISIBLE);
            return;
        }

        super.onPrevPressed();
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.textRegister)
    public void onRegClick(final View v) {
        if (loginOpened)
            onButtonLogClick();

        registerOpened = true;
        reg_container.setClickable(false);

        showDelayedTransition();
        ButterKnife.apply(register, new ButterKnife.Action<View>() {
            @Override
            public void apply(View view, int i) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.buttonReg1)
    public void onButtonRegClick() {
        registerOpened = false;
        reg_container.setClickable(true);

        hideDelayedTransition();
        ButterKnife.apply(register, new ButterKnife.Action<View>() {
            @Override
            public void apply(View view, int i) {
                view.setVisibility(View.GONE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.textLogin)
    public void onLogClick() {
        if (registerOpened)
            onButtonRegClick();
        login_container.setClickable(false);
        loginOpened = true;
        showDelayedTransition();
        ButterKnife.apply(login, new ButterKnife.Action<View>() {
            @Override
            public void apply(View view, int i) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.buttonLog1)
    public void onButtonLogClick() {
        loginOpened = false;
        login_container.setClickable(true);
        hideDelayedTransition();
        ButterKnife.apply(login, new ButterKnife.Action<View>() {
            @Override
            public void apply(View view, int i) {
                view.setVisibility(View.GONE);
            }
        });
    }

    public void showDelayedTransition() {
        if (transitionType == DEFAULT) {
            TransitionManager.beginDelayedTransition(container3);
        } else {
            TransitionSet set = new TransitionSet();
            set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
            Pop pop = new Pop();
            pop.setPropagation(new CircularPropagation() {
                @Override
                public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
                    long delay = super.getStartDelay(sceneRoot, transition, startValues, endValues) * 6;
                    return delay;
                }
            });
            pop.setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition transition) {
                    int[] loc = new int[2];
                    container3.getLocationOnScreen(loc);
                    return new Rect(loc[0], loc[1], loc[0] + container3.getWidth(), loc[1] + 40);
                }
            });

            set.addTransition(new ChangeBounds()).addTransition(pop);
            TransitionManager.beginDelayedTransition(container3, set);
        }
    }

    public void hideDelayedTransition() {
        if (transitionType == DEFAULT) {
            TransitionManager.beginDelayedTransition(container3);
        } else {
            TransitionSet set = new TransitionSet();
            set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
            Pop pop = new Pop();
            pop.setPropagation(new CircularPropagation() {
                @Override
                public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
                    long delay = super.getStartDelay(sceneRoot, transition, startValues, endValues) * 6;
                    return delay;
                }
            });
            pop.setEpicenterCallback(new Transition.EpicenterCallback() {
                @Override
                public Rect onGetEpicenter(Transition transition) {
                    int[] loc = new int[2];
                    container3.getLocationOnScreen(loc);
                    return new Rect(loc[0], loc[1], loc[0] + container3.getWidth(), loc[1] + 40);
                }
            });

            set.addTransition(pop).addTransition(new ChangeBounds());
            TransitionManager.beginDelayedTransition(container3, set);
        }
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