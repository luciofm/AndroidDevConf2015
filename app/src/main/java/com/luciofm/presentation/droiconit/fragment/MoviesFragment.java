package com.luciofm.presentation.droiconit.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.Spanned;
import android.transition.CircularPropagation;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.activity.BaseActivity;
import com.luciofm.presentation.droiconit.activity.MovieDetailsActivity;
import com.luciofm.presentation.droiconit.anim.XFractionProperty;
import com.luciofm.presentation.droiconit.model.Movie;
import com.luciofm.presentation.droiconit.model.Movies;
import com.luciofm.presentation.droiconit.transitions.Pop;
import com.luciofm.presentation.droiconit.util.IOUtils;
import com.luciofm.presentation.droiconit.util.PaletteTransformation;
import com.luciofm.presentation.droiconit.util.Utils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

public class MoviesFragment extends BaseFragment {

    @InjectView(R.id.grid)
    GridView grid;
    @InjectView(R.id.textCode)
    TextSwitcher text;
    @InjectView(R.id.textReveal)
    TextView textReveal;

    TransitionInflater inflater;
    Transition defaultTransition;

    Spanned slide_left;
    Spanned at2_calling;
    Spanned at2_called;
    Spanned at3_calling;
    Spanned at3_called;
    Spanned at4_calling;
    Spanned at4_called;
    Spanned at5_calling;
    Spanned at5_called;
    Spanned at6_calling;

    private boolean loaded;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = TransitionInflater.from(getActivity());
        defaultTransition = inflater.inflateTransition(R.transition.fade);

        new LoadCodeTask().execute();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_movies;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        ButterKnife.inject(this, v);
        currentStep = 1;

        grid.setAdapter(new MoviesAdapter(getActivity(), Movies.movies));

        text.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        text.setOutAnimation(getActivity(), android.R.anim.slide_out_right);

        return v;
    }

    public void onMovieClick(Movie movie, MoviesAdapter.ViewHolder holder, int position) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE", Parcels.wrap(movie));

        holder.thumb.setTransitionName("thumb_" + position);
        grid.setClipChildren(false);

        setupTransition(position, holder);
        intent.putExtra("CURRENT_POSITION", position);
        intent.putExtra("COLOR", holder.bgColor);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                holder.thumb, "thumb");

        startActivity(intent, options.toBundle());
    }

    public void setupTransition(int position, MoviesAdapter.ViewHolder holder) {
        TransitionSet set;
        CircularPropagation propagation;
        Explode explode;
        Slide slide;
        Fade fade;
        getActivity().getWindow().setReenterTransition(defaultTransition);
        getActivity().getWindow().setExitTransition(defaultTransition);
        getActivity().getWindow().setAllowEnterTransitionOverlap(true);
        getActivity().getWindow().setAllowReturnTransitionOverlap(true);
        getActivity().setExitSharedElementCallback(null);

        switch (position) {
            case 1:
                getActivity().getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_left));
                break;
            case 2:
                set = new TransitionSet();
                fade = new Fade();
                slide = new Slide(Gravity.BOTTOM);
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                set.addTransition(fade).addTransition(slide);
                set.setPropagation(propagation);
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                getActivity().getWindow().setExitTransition(set);
                break;
            case 3:
                explode = new Explode();
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                explode.setPropagation(propagation);
                getActivity().getWindow().setReenterTransition(explode);
                getActivity().getWindow().setExitTransition(explode);
                break;
            case 4:
                slide = new Slide(Gravity.LEFT);
                propagation = new CircularPropagation();
                propagation.setPropagationSpeed(1f);
                slide.setPropagation(propagation);
                getActivity().getWindow().setExitTransition(slide);
                getActivity().getWindow().setReenterTransition(slide);
                getActivity().getWindow().setAllowEnterTransitionOverlap(false);
                getActivity().getWindow().setAllowReturnTransitionOverlap(false);
                break;
            case 5:
                getActivity().getWindow().setExitTransition(inflater.inflateTransition(R.transition.slide_top));
                getActivity().getWindow().setReenterTransition(inflater.inflateTransition(R.transition.slide_bottom));
                break;
            case 6:
                Pop pop = new Pop(false);
                getActivity().getWindow().setReenterTransition(pop);
                break;
            case 7:
                getActivity().setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                        int bitmapWidth = Math.round(screenBounds.width());
                        int bitmapHeight = Math.round(screenBounds.height());
                        Bitmap bitmap = null;
                        if (bitmapWidth > 0 && bitmapHeight > 0) {
                            Matrix matrix = new Matrix();
                            matrix.set(viewToGlobalMatrix);
                            matrix.postTranslate(-screenBounds.left, -screenBounds.top);
                            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            canvas.concat(matrix);
                            sharedElement.draw(canvas);
                        }
                        return bitmap;
                    }
                });
                break;
            case 8:
                break;
        }
    }

    @Override
    public void onNextPressed() {
        if (!loaded)
            return;
        View view;
        switch (++currentStep) {
            case 2:
                view = grid.getChildAt(0);
                Utils.dispatchTouch(view, 300);
                break;
            case 3:
                view = grid.getChildAt(1);
                Utils.dispatchTouch(view, 300);
                break;
            case 4:
            case 5:
                view = grid.getChildAt(1);
                text.setCurrentText(slide_left);
                toggleCodeView(view, text);
                break;
            case 6:
                view = grid.getChildAt(1);
                Utils.dispatchTouch(view, 300);
                break;
            case 7:
                view = grid.getChildAt(2);
                Utils.dispatchTouch(view, 300);
                break;
            case 8:
                view = grid.getChildAt(2);
                text.setCurrentText(at2_calling);
                toggleCodeView(view, text);
                break;
            case 9:
                text.setText(at2_called);
                break;
            case 10:
                view = grid.getChildAt(2);
                toggleCodeView(view, text);
                break;
            case 11:
                view = grid.getChildAt(2);
                Utils.dispatchTouch(view, 300);
                break;
            case 12:
                view = grid.getChildAt(3);
                Utils.dispatchTouch(view, 300);
                break;
            case 13:
                view = grid.getChildAt(3);
                text.setCurrentText(at3_calling);
                toggleCodeView(view, text);
                break;
            case 14:
                text.setText(at3_called);
                break;
            case 15:
                view = grid.getChildAt(3);
                toggleCodeView(view, text);
                break;
            case 16:
                view = grid.getChildAt(3);
                Utils.dispatchTouch(view, 300);
                break;
            case 17:
                view = grid.getChildAt(4);
                Utils.dispatchTouch(view, 300);
                break;
            case 18:
                view = grid.getChildAt(4);
                text.setCurrentText(at4_calling);
                toggleCodeView(view, text);
                break;
            case 19:
                text.setText(at4_called);
                break;
            case 20:
                view = grid.getChildAt(4);
                toggleCodeView(view, text);
                break;
            case 21:
                view = grid.getChildAt(4);
                Utils.dispatchTouch(view, 300);
                break;
            case 22:
                view = grid.getChildAt(5);
                Utils.dispatchTouch(view, 300);
                break;
            case 23:
                view = grid.getChildAt(5);
                text.setCurrentText(at5_calling);
                toggleCodeView(view, text);
                break;
            case 24:
                text.setText(at5_called);
                break;
            case 25:
                view = grid.getChildAt(5);
                toggleCodeView(view, text);
                break;
            case 26:
                view = grid.getChildAt(5);
                Utils.dispatchTouch(view, 300);
                break;
            case 27:
            case 30:
                view = grid.getChildAt(6);
                Utils.dispatchTouch(view, 300);
                break;
            case 28:
            case 29:
                view = grid.getChildAt(6);
                text.setCurrentText(at6_calling);
                toggleCodeView(view, text);
                break;
            case 31:
            case 32:
                view = grid.getChildAt(7);
                Utils.dispatchTouch(view, 300);
                break;
            case 33:
            case 34:
                view = grid.getChildAt(7);
                toggleCodeView(view, textReveal);
                break;
            case 35:
            case 36:
                view = grid.getChildAt(8);
                Utils.dispatchTouch(view, 300);
                break;
            case 37:
            case 38:
                view = grid.getChildAt(9);
                Utils.dispatchTouch(view, 300);
                break;
            default:
                ((BaseActivity) getActivity()).nextFragment();
        }
    }

    private void toggleCodeView(View origin, final View view) {
        int cx = (int) origin.getX();
        int cy = (int) origin.getY();
        int width = view.getWidth();
        int height = view.getHeight();
        int radius = Math.max(width, height);

        if (view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius).start();
        } else {
            radius = view.getHeight();
            Animator reveal = ViewAnimationUtils.createCircularReveal(
                    view, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
    }

    @Override
    public void onPrevPressed() {
        if (--currentStep < 1)
            super.onPrevPressed();
    }

    @Optional
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

    public class MoviesAdapter extends ArrayAdapter<Movie> {

        LayoutInflater inflater;

        public MoviesAdapter(Context context, List<Movie> objects) {
            super(context, R.layout.movie_item, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;

            if (v == null) {
                v = inflater.inflate(R.layout.movie_item, parent, false);
                holder = new ViewHolder(v);
            } else
                holder = (ViewHolder) v.getTag();

            holder.setMovie(getItem(position), position);

            return v;
        }

        public class ViewHolder {
            @InjectView(R.id.movie)
            FrameLayout root;
            @InjectView(R.id.content)
            ViewGroup content;
            @InjectView(R.id.thumb)
            ImageView thumb;
            @InjectView(R.id.title)
            TextView title;
            @InjectView(R.id.year)
            TextView year;

            Movie movie;
            int bgColor;
            int position;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
                view.setTag(this);
                view.setOnClickListener(clickListener);
            }

            public void setMovie(Movie movie, int position) {
                this.movie = movie;
                this.position = position;
                title.setText(movie.getTitle());
                year.setText(movie.getYear());

                Picasso.with(getContext()).load(movie.resId)
                        .transform(PaletteTransformation.instance())
                        .into(thumb, new PaletteTransformation.PaletteCallback(thumb) {
                            @Override
                            protected void onSuccess(Palette palette) {
                                Palette.Swatch bgSwatch = Utils.getBackgroundSwatch(palette);
                                Palette.Swatch titleSwatch = Utils.getTitleSwatch(palette);

                                title.setTextColor(titleSwatch.getTitleTextColor());
                                year.setTextColor(titleSwatch.getBodyTextColor());
                                content.setBackgroundColor(titleSwatch.getRgb());

                                bgColor = titleSwatch.getRgb();
                                Utils.colorRipple(root, bgSwatch.getRgb(), titleSwatch.getRgb());
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMovieClick(movie, (ViewHolder) v.getTag(), position);
                }
            };
        }
    }

    public class LoadCodeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            slide_left = Html.fromHtml(IOUtils.readFile(getActivity(), "source/slide_left.xml.html"));
            at2_calling = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at2_calling.java.html"));
            at2_called = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at2_called.java.html"));
            at3_calling = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at3_calling.java.html"));
            at3_called = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at3_called.java.html"));
            at4_calling = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at4_calling.java.html"));
            at4_called = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at4_called.java.html"));
            at5_calling = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at5_calling.java.html"));
            at5_called = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at5_called.java.html"));
            at6_calling = Html.fromHtml(IOUtils.readFile(getActivity(), "source/at6_calling.java.html"));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loaded = true;
        }
    }
}