package com.luciofm.presentation.droiconit.activity;

import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.transition.CircularPropagation;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.fragment.BaseFragment;
import com.luciofm.presentation.droiconit.model.Movie;
import com.luciofm.presentation.droiconit.transitions.CircleTransition;
import com.luciofm.presentation.droiconit.transitions.TransitionListenerAdapter;
import com.luciofm.presentation.droiconit.util.PaletteTransformation;
import com.luciofm.presentation.droiconit.util.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MovieDetailsActivity extends BaseActivity {

    @InjectView(R.id.mainHeader)
    ViewGroup mainHeader;
    @InjectView(R.id.imagesHeader)
    ViewGroup imagesHeader;
    @InjectView(R.id.content)
    ViewGroup content;
    @InjectView(R.id.moreContent)
    ViewGroup moreContent;
    @InjectView(R.id.contentHeader)
    View contentHeader;
    @InjectView(R.id.thumb)
    ImageView thumb;
    @InjectView(R.id.scene)
    ImageView scene;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.year)
    TextView year;

    private int position;

    @Override
    ArrayList<Class<? extends BaseFragment>> getFragmentsList() {
        return null;
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_movie_details;
    }

    @Override
    int getFragmentContainerId() {
        return 0;
    }

    @Override
    public boolean shouldSetSystemUiVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("MOVIE"));
        position = getIntent().getIntExtra("CURRENT_POSITION", 0);

        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().getEnterTransition().addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                setupPostTransition();
            }
        });

        setupTransition();

        // Postpone the transition until the window's decor view has
        // finished its layout.
        postponeEnterTransition();

        Picasso.with(this).load(movie.resId).transform(PaletteTransformation.instance())
                .into(thumb, new PaletteTransformation.PaletteCallback(thumb) {
                    @Override
                    protected void onSuccess(Palette palette) {
                        Palette.Swatch dark = Utils.getBackgroundSwatch(palette);
                        Palette.Swatch light = Utils.getTitleSwatch(palette);

                        imagesHeader.setBackgroundColor(dark.getRgb());
                        contentHeader.setBackgroundColor(light.getRgb());

                        scene.setImageResource(movie.getSceneId());
                        title.setTextColor(light.getTitleTextColor());
                        title.setText(movie.getTitle());
                        year.setTextColor(light.getBodyTextColor());
                        year.setText(movie.getYear());

                        final ViewTreeObserver observer = getWindow().getDecorView().getViewTreeObserver();
                        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                                startPostponedEnterTransition();
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    protected void setupPostTransition() {
    }

    protected void setupTransition() {
        TransitionSet set;
        CircularPropagation propagation;
        Explode explode;
        Slide slide;
        Slide slide2;
        Fade fade;

        switch (position) {
            case 2:
                slide = new Slide(Gravity.RIGHT);
                fade = new Fade();
                slide.addTarget(R.id.imagesHeader);
                set = new TransitionSet();
                set.addTransition(slide).addTransition(fade);
                getWindow().setEnterTransition(set);
                getWindow().setAllowEnterTransitionOverlap(true);
                getWindow().setAllowReturnTransitionOverlap(true);
                break;
            case 3:
                getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.slide_bottom));
                getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.explode));
                getWindow().setAllowEnterTransitionOverlap(false);
                break;
            case 4:
                slide = new Slide(Gravity.RIGHT);
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                getWindow().setEnterTransition(slide);
                getWindow().setReturnTransition(slide);
                getWindow().setAllowEnterTransitionOverlap(false);
                getWindow().setAllowReturnTransitionOverlap(false);
                break;
            case 5:
                slide = new Slide(Gravity.BOTTOM);
                slide2 = new Slide(Gravity.TOP);
                getWindow().setEnterTransition(slide);
                getWindow().setReturnTransition(slide2);
                getWindow().setAllowEnterTransitionOverlap(false);
                break;
            case 7:
                setupRevealTransition();
                break;
            case 8:
                getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.move_arc));
                break;
            case 9:
                getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.move_path));
                break;
        }
    }

    private void setupRevealTransition() {
        CircleTransition transition = new CircleTransition();
        transition.setColor(getIntent().getIntExtra("COLOR", Color.GRAY));
        getWindow().setSharedElementEnterTransition(transition);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                View view = new View(context);
                view.setBackground(new BitmapDrawable((Bitmap) snapshot));
                return view;
            }

            @Override
            public void onSharedElementStart(List<String> sharedElementNames,
                                             List<View> sharedElements,
                                             List<View> sharedElementSnapshots) {
                ImageView sharedElement = (ImageView) findViewById(R.id.thumb);
                for (int i = 0; i < sharedElements.size(); i++) {
                    if (sharedElements.get(i) == sharedElement) {
                        View snapshot = sharedElementSnapshots.get(i);
                        Drawable snapshotDrawable = snapshot.getBackground();
                        sharedElement.setBackground(snapshotDrawable);
                        sharedElement.setImageAlpha(0);
                        forceSharedElementLayout();
                        break;
                    }
                }
            }

            private void forceSharedElementLayout() {
                ImageView sharedElement = (ImageView) findViewById(R.id.thumb);
                int widthSpec = View.MeasureSpec.makeMeasureSpec(sharedElement.getWidth(),
                        View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(sharedElement.getHeight(),
                        View.MeasureSpec.EXACTLY);
                int left = sharedElement.getLeft();
                int top = sharedElement.getTop();
                int right = sharedElement.getRight();
                int bottom = sharedElement.getBottom();
                sharedElement.measure(widthSpec, heightSpec);
                sharedElement.layout(left, top, right, bottom);
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {
                ImageView sharedElement = (ImageView) findViewById(R.id.thumb);
                sharedElement.setBackground(null);
                sharedElement.setImageAlpha(255);
            }
        });
    }
}
