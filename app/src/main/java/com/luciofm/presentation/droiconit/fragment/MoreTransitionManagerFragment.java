package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;
import com.luciofm.presentation.droiconit.model.Entry;
import com.luciofm.presentation.droiconit.transitions.ChangeImageTransform;
import com.luciofm.presentation.droiconit.transitions.ChangeVideoTransform;
import com.luciofm.presentation.droiconit.util.CircleTransformation;
import com.luciofm.presentation.droiconit.util.IOUtils;
import com.luciofm.presentation.droiconit.util.Utils;
import com.luciofm.presentation.droiconit.widget.AnimatedLayout;
import com.luciofm.presentation.droiconit.widget.CanvasGridLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by luciofm on 27/08/15.
 */
public class MoreTransitionManagerFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup root;
    @InjectView(R.id.grid_container)
    ViewGroup gridContainer;
    @InjectView(R.id.grid)
    CanvasGridLayout grid;

    @InjectView(R.id.thumb)
    ImageView thumb;
    @InjectView(R.id.text1)
    TextView text1;
    @InjectView(R.id.text2)
    TextSwitcher text2;

    TransitionSet transitionSet = new FaultGridTransition();

    private static final int MAX_ANIMATED = 3;
    private int currentAnimated = 0;
    private boolean canAnimate = false;
    private boolean multiple;
    private Random rnd = new Random();

    private static List<Entry> entries;
    static {
        entries = new ArrayList<>();
        entries.add(new Entry(R.drawable.image1));
        entries.add(new Entry(R.drawable.image2));
        entries.add(new Entry(R.drawable.image3));
        entries.add(new Entry(R.drawable.image4));
        entries.add(new Entry(R.drawable.image5));
        entries.add(new Entry(R.drawable.image6));
        entries.add(new Entry(R.drawable.image7));
        entries.add(new Entry(R.drawable.image8));

        entries.add(new Entry(R.drawable.video1, "video1"));
        entries.add(new Entry(R.drawable.video2, "video2"));
        entries.add(new Entry(R.drawable.video3, "video3"));
        entries.add(new Entry(R.drawable.video4, "video4"));
        entries.add(new Entry(R.drawable.video5, "video5"));
        entries.add(new Entry(R.drawable.video6, "video6"));
        entries.add(new Entry(R.drawable.video7, "video7"));
        entries.add(new Entry(R.drawable.video8, "video8"));

        Collections.shuffle(entries);
    }

    private List<AnimatedLayout> imgs;
    private int currentEntryIndex;

    Picasso picasso;

    private Handler handler = new Handler(Looper.getMainLooper());

    Spanned code1;
    Spanned code2;
    Spanned code3;
    Spanned code4;
    Spanned code5;
    Spanned code6;

    @Override
    public int getLayout() {
        return R.layout.fragment_tm2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        picasso = Picasso.with(getActivity());

        code1 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm1.java.html"));
        code2 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm2.java.html"));
        code3 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm3.java.html"));
        code4 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm4.java.html"));
        code5 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm5.java.html"));
        code6 = Html.fromHtml(IOUtils.readFile(getActivity(), "source/tm6.java.html"));

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
                gridContainer.setVisibility(View.VISIBLE);

                picasso.load(R.drawable.lucio_badge).transform(new CircleTransformation())
                    .placeholder(R.drawable.shape_circle_placeholder)
                    .into(thumb);

                shuffleImages();
                break;
            case 3:
            case 5:
            case 7:
                View v1 = imgs.get(0);
                v1.animate().alpha(0f).setDuration(100);
                break;
            case 4:
            case 6:
            case 8:
                final int totalEntries = entries.size();
                if (currentEntryIndex >= totalEntries)
                    currentEntryIndex = 0;

                AnimatedLayout v2 = (AnimatedLayout) grid.getChildAt(0);
                grid.removeView(v2);
                imgs.remove(v2);

                if (v2.getEntryType() == Entry.TYPE_VIDEO)
                    currentAnimated--;

                v2 = new AnimatedLayout(getActivity());
                imgs.add(v2);
                grid.addView(v2);

                final Entry entry = entries.get(currentEntryIndex++);
                loadImage(v2, entry);
                break;
            case 9:
                handler.post(imageChangeRunnable);
                break;
            case 10:
                canAnimate = true;
                break;
            case 11:
                transitionSet = new GridTransition();
                break;
            case 12:
                multiple = true;
                break;
            case 13:
                TransitionManager.beginDelayedTransition(root);
                handler.removeCallbacks(imageChangeRunnable);
                gridContainer.setVisibility(View.GONE);
                clearImages();
                text2.setVisibility(View.VISIBLE);
                break;
            case 14:
                text2.setText(code2);
                break;
            case 15:
                text2.setText(code3);
                break;
            case 16:
                text2.setText(code4);
                break;
            case 17:
                text2.setText(code5);
                break;
            case 18:
                text2.setText(code6);
                break;
            default:
                handler.removeCallbacks(imageChangeRunnable);
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    private void clearImages() {
        for (AnimatedLayout layout : imgs) {
            layout.clear();
            grid.removeView(layout);
        }

        imgs.clear();
    }

    @OnClick(R.id.container)
    public void onClick() {
        onNextPressed();
    }

    private void setupImages() {
        imgs = new ArrayList<>();

        final int totalEntries = entries.size();
        for (int i = 0; i < Math.min(6, totalEntries); i++) {
            AnimatedLayout view = new AnimatedLayout(getActivity());
            imgs.add(view);
            grid.addView(view);
        }
    }

    private void shuffleImages() {
        if (entries == null)
            return;

        if (imgs == null) {
            setupImages();
        }

        final int totalEntries = entries.size();

        for (int i = 0; i < Math.min(imgs.size(), totalEntries); i++) {
            if (currentEntryIndex >= totalEntries)
                currentEntryIndex = 0;
            final AnimatedLayout img = imgs.get(i);
            final Entry entry = entries.get(currentEntryIndex++);

            loadImage(img, entry);
        }

        preloadNextCanvas();
    }

    private void loadImage(final AnimatedLayout img, final Entry entry) {
        if (canAnimate && entry.getType() == Entry.TYPE_VIDEO && currentAnimated < MAX_ANIMATED) {
            currentAnimated++;
            img.loadVideo(entry);
        } else
            img.loadImage(entry);

        img.animate()
                .setDuration(100)
                .alpha(1f);
    }

    private Runnable imageChangeRunnable = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            final int totalEntries = entries.size();

            TransitionManager.beginDelayedTransition(grid, transitionSet);
            Entry entry;

            if (!multiple) {
                if (currentEntryIndex >= totalEntries)
                    currentEntryIndex = 0;
                AnimatedLayout v = (AnimatedLayout) grid.getChildAt(0);
                grid.removeView(v);
                imgs.remove(v);
                if (v.getEntryType() == Entry.TYPE_VIDEO)
                    currentAnimated--;

                v = new AnimatedLayout(getActivity());
                imgs.add(v);
                grid.addView(v);

                entry = entries.get(currentEntryIndex++);
                loadImage(v, entry);
            } else {
                for (int i = 0; i < 2; i++){
                    if (currentEntryIndex >= totalEntries)
                        currentEntryIndex = 0;

                    AnimatedLayout v = (AnimatedLayout) grid.getChildAt(3);
                    grid.removeView(v);
                    imgs.remove(v);
                    if (v.getEntryType() == Entry.TYPE_VIDEO)
                        currentAnimated--;

                    v = new AnimatedLayout(getActivity());
                    imgs.add(i == 0 ? 0 : grid.getChildCount(), v);
                    grid.addView(v, i == 0 ? 0 : grid.getChildCount());
                    entry = entries.get(currentEntryIndex++);
                    loadImage(v, entry);
                }
            }

            handler.postDelayed(imageChangeRunnable, 3000);
        }
    };


    private void preloadNextCanvas() {
        // preload the next page of images
        int j = currentEntryIndex;
        for (int i = imgs.size(); i >= 0; i--) {
            if (j >= entries.size())
                j = 0;

            picasso.load(entries.get(j++).getImageId()).fetch();
        }
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(imageChangeRunnable);
        super.onDestroyView();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static class FaultGridTransition extends TransitionSet {
        public FaultGridTransition() {
            init();
        }

        private void init() {
            TransitionSet set = new TransitionSet();
            set.addTransition(new ChangeBounds())
                    .addTransition(new ChangeImageTransform());
            setOrdering(ORDERING_SEQUENTIAL);
            addTransition(new Fade(Fade.OUT)).
                    addTransition(set).
                    addTransition(new Fade(Fade.IN));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static class GridTransition extends TransitionSet {
        public GridTransition() {
            TransitionSet bounds = new TransitionSet();
            bounds.addTransition(new ChangeBounds())
                    .addTransition(new ChangeImageTransform())
                    .addTransition(new ChangeVideoTransform());
            setOrdering(ORDERING_SEQUENTIAL);
            addTransition(new Fade(Fade.OUT)).
                    addTransition(bounds).
                    addTransition(new Fade(Fade.IN));
        }
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (transit == 0) {
            return null;
        }

        //Target will be filled in by the framework
        return enter ? ObjectAnimator.ofFloat(null, new XFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new YFractionProperty(), 0f, -1f);
    }

}
