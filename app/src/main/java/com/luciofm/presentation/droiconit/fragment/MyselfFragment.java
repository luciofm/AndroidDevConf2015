package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.CircularPropagation;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.anim.AnimUtils;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.anim.YFractionProperty;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MyselfFragment extends BaseFragment {

    @InjectView(R.id.container)
    ViewGroup container;

    ImageView image;

    Bitmap originalBitmap;

    Scene scene1;
    Scene scene2;
    TransitionSet set;

    BitmapDrawable drawable;

    public MyselfFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lucio_badge);

        set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        slide.setPropagation(new CircularPropagation() {
            @Override
            public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
                long delay = super.getStartDelay(sceneRoot, transition, startValues, endValues);
                return delay * 8;
            }
        });
        slide.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(Transition transition) {
                int[] loc = new int[2];
                container.getLocationOnScreen(loc);

                return new Rect((container.getWidth() / 2) - 40, loc[1], (container.getWidth() / 2) + 40, loc[1] + 40);
            }
        });

        TransitionSet imageSet = new TransitionSet();
        imageSet.setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeBounds());

        set.addTransition(imageSet).addTransition(slide);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_myself;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        scene1 = Scene.getSceneForLayout(container, R.layout.scene_myself_1, getActivity());
        scene2 = Scene.getSceneForLayout(container, R.layout.scene_myself_2, getActivity());

        scene1.setEnterAction(new Runnable() {
            @Override
            public void run() {
                image = (ImageView) container.findViewById(R.id.image);
                container.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isResumed())
                            return;

                        image = (ImageView) container.findViewById(R.id.image);
                        image.animate().alpha(1f).setDuration(300);
                        ObjectAnimator pixelate = ObjectAnimator.ofInt(MyselfFragment.this, "pixelateFactor", 100, 0);
                        pixelate.setDuration(1200);
                        pixelate.setInterpolator(new DecelerateInterpolator());
                        pixelate.start();
                    }
                }, 600);
            }
        });
        scene2.setEnterAction(new Runnable() {
            @Override
            public void run() {
                image = (ImageView) container.findViewById(R.id.image);
                image.setImageDrawable(drawable);
            }
        });
        scene1.enter();

        return v;
    }

    @Override
    public void onNextPressed() {
        switch (++currentStep) {
            case 2:
                TransitionManager.go(scene2, set);
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
        return enter ? ObjectAnimator.ofFloat(null, new XFractionProperty(), 1f, 0f) :
                ObjectAnimator.ofFloat(null, new YFractionProperty(), 0f, -1f);
    }

    final private static float PROGRESS_TO_PIXELIZATION_FACTOR = 1000.0f;

    public void setPixelateFactor(int number) {
        float factor = number / PROGRESS_TO_PIXELIZATION_FACTOR;

        PixelizeImageAsyncTask asyncPixelateTask = new PixelizeImageAsyncTask();
        asyncPixelateTask.execute(factor, originalBitmap);
    }

    /**
     * Implementation of the AsyncTask class showing how to run the
     * pixelization algorithm in the background, and retrieving the
     * pixelated image from the resulting operation.
     */
    private class PixelizeImageAsyncTask extends AsyncTask<Object, Void, BitmapDrawable> {

        @Override
        protected BitmapDrawable doInBackground(Object... params) {
            if (!isResumed())
                return null;

            float pixelizationFactor = (Float)params[0];
            Bitmap originalBitmap = (Bitmap)params[1];
            return AnimUtils.builtInPixelization(getActivity(), pixelizationFactor, originalBitmap);
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            if (isResumed() && result != null)
                image.setImageDrawable(result);
            drawable = result;
        }
    }
}
