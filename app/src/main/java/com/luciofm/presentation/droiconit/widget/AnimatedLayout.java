package com.luciofm.presentation.droiconit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.model.Entry;
import com.luciofm.presentation.droiconit.util.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AnimatedLayout extends FrameLayout {

    Picasso picasso;

    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.video)
    TextureVideoView video;

    private Entry entry;

    private int videoDelay = 0;

    private int type = Entry.TYPE_IMAGE;

    public AnimatedLayout(Context context) {
        super(context);
        init();
    }

    public AnimatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimatedLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        picasso = Picasso.with(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.canvas_grid_entry, this, true);
        ButterKnife.inject(this);
    }

    public void clear(){
        picasso.cancelRequest(image);
        image.setImageBitmap(null);
        //WhiViewUtils.unbindDrawables(image);
        image.setVisibility(INVISIBLE);

        video.stopPlayback();
        video.setVisibility(INVISIBLE);
    }

    public void loadImage(Entry entry) {
        loadImage(entry, null);
    }

    public void loadImage(Entry entry, Transformation transformation) {
        type = Entry.TYPE_IMAGE;
        this.entry = entry;
        image.setVisibility(VISIBLE);
        video.setVisibility(INVISIBLE);
        video.stopPlayback();

        RequestCreator request = picasso.load(entry.getImageId());

        if (transformation != null)
            request.transform(transformation);
        request.into(image);
    }

    public void loadVideo(final Entry entry) {
        loadVideo(entry, TextureVideoView.ScaleType.CENTER_CROP);
    }

    public void loadVideo(final Entry entry, TextureVideoView.ScaleType scaleType) {
        loadVideo(entry, scaleType, null);
    }

    public void loadVideo(final Entry entry, TextureVideoView.ScaleType scaleType, final Transformation transformation) {
        this.entry = entry;
        type = Entry.TYPE_VIDEO;
        video.setVisibility(VISIBLE);
        video.setAlpha(0f);
        image.setVisibility(INVISIBLE);
        if (scaleType != null)
            video.setScaleType(scaleType);
        video.setVideoURI(Uri.parse(entry.getVideoUrl()));
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == null) {
                    return;
                }
                Utils.fadeIn(video, videoDelay);
                mp.setLooping(true);
                mp.start();
            }
        });

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (video == null || mp == null) {
                        return;
                    }
                    video.setVideoURI(Uri.parse(entry.getVideoUrl()));
                    video.start();
                }
            });
        }

        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                loadImage(entry, transformation);
                return true;
            }
        });
    }

    public void stopVideo() {
        video.stopPlayback();
    }

    public int getVideoDelay() {
        return videoDelay;
    }

    public void setVideoDelay(int videoDelay) {
        this.videoDelay = videoDelay;
    }

    public Entry getEntry() {
        return entry;
    }

    public int getEntryType() {
        return type;
    }
}
