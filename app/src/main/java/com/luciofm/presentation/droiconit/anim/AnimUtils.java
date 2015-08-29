package com.luciofm.presentation.droiconit.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by luciofm on 5/25/14.
 */
public class AnimUtils {

    public static final int ANIM_MULTIPLIER = 1;
    public static final int ANIM_DURATION = 300 * ANIM_MULTIPLIER;

    public static Interpolator BOUNCE = new BounceInterpolator();
    public static Interpolator OVERSHOOT = new OvershootInterpolator(2.8f);
    public static Interpolator ANTICIPATE = new AnticipateInterpolator(2.8f);
    public static Interpolator ACCELERATE = new AccelerateInterpolator();
    public static Interpolator DECELERATE = new DecelerateInterpolator();
    public static Interpolator ACCELDECEL = new AccelerateDecelerateInterpolator();

    private AnimUtils() {
    }

    public static void shakeView(final View view) {
        ObjectAnimator physX = ObjectAnimator.ofFloat(view, "translationX", -12f, 12f);
        physX.setDuration(50);
        physX.setRepeatCount(10);
        physX.setRepeatMode(ObjectAnimator.RESTART);
        physX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().translationX(0f).setDuration(10);
            }
        });
        physX.start();
    }

    public static void beginDelayedTransition(ViewGroup container) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            TransitionManager.beginDelayedTransition(container);
    }

    public static void beginDelayedTransition(ViewGroup container, Transition set) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            TransitionManager.beginDelayedTransition(container, set);
    }


    public static Animator createXTransition(View view, boolean enter) {
        ObjectAnimator x;
        ObjectAnimator y;
        if (enter) {
            x = ObjectAnimator.ofFloat(view, "translationX", 1f, 0f);
            y = ObjectAnimator.ofFloat(view, "translationY", 1f, 0f);
        } else {
            x = ObjectAnimator.ofFloat(view, "translationX", 0f, -1f);
            y = ObjectAnimator.ofFloat(view, "translationY", 0f, -1f);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(x, y);
        return set;
    }

    /**
     * This method of image pixelization utilizes the bitmap scaling operations built
     * into the framework. By downscaling the bitmap and upscaling it back to its
     * original size (while setting the filter flag to false), the same effect can be
     * achieved with much better performance.
     */
    public static BitmapDrawable builtInPixelization(Context context, float pixelizationFactor,
                                                     Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int downScaleFactorWidth = (int) (pixelizationFactor * width);
        downScaleFactorWidth = downScaleFactorWidth > 0 ? downScaleFactorWidth : 1;
        int downScaleFactorHeight = (int) (pixelizationFactor * height);
        downScaleFactorHeight = downScaleFactorHeight > 0 ? downScaleFactorHeight : 1;

        int downScaledWidth = width / downScaleFactorWidth;
        int downScaledHeight = height / downScaleFactorHeight;

        Bitmap pixelatedBitmap = Bitmap.createScaledBitmap(bitmap, downScaledWidth,
                                                           downScaledHeight, false);

        /* Bitmap's createScaledBitmap method has a filter parameter that can be set to either
         * true or false in order to specify either bilinear filtering or point sampling
         * respectively when the bitmap is scaled up or now.
         *
         * Similarly, a BitmapDrawable also has a flag to specify the same thing. When the
         * BitmapDrawable is applied to an ImageView that has some scaleType, the filtering
         * flag is taken into consideration. However, for optimization purposes, this flag was
         * ignored in BitmapDrawables before Jelly Bean MR1.
         *
         * Here, it is important to note that prior to JBMR1, two bitmap scaling operations
         * are required to achieve the pixelization effect. Otherwise, a BitmapDrawable
         * can be created corresponding to the downscaled bitmap such that when it is
         * upscaled to fit the ImageView, the upscaling operation is a lot faster since
         * it uses internal optimizations to fit the ImageView.
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(),
                                                               pixelatedBitmap);
            bitmapDrawable.setFilterBitmap(false);
            return bitmapDrawable;
        } else {
            Bitmap upscaled = Bitmap.createScaledBitmap(pixelatedBitmap, width, height, false);
            return new BitmapDrawable(context.getResources(), upscaled);
        }
    }

    public static final float DEFAULT_BLUR_RADIUS = 12f;

    public static Bitmap fastblur(Context context, Bitmap sentBitmap) {
        return fastblur(context, sentBitmap, DEFAULT_BLUR_RADIUS);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, float radius) {

        if (Build.VERSION.SDK_INT > 16 &&
                    (!Build.MODEL.contains("google_sdk")
                             && !Build.MODEL.contains("Emulator")
                             && !Build.MODEL.contains("Android SDK")
                             && !Build.FINGERPRINT.contains("vbox86p"))) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                                                                 Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        radius *= 0.1;
        return blurfast(sentBitmap, (int) radius);
    }

    static Bitmap blurfast(Bitmap bmp, int radius) {
        try {
            Bitmap bitmap = bmp.copy(bmp.getConfig(), true);
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            int[] pix = new int[w * h];
            bmp.getPixels(pix, 0, w, 0, 0, w, h);

            for(int r = radius; r >= 1; r /= 2) {
                for(int i = r; i < h - r; i++) {
                    for(int j = r; j < w - r; j++) {
                        int tl = pix[(i - r) * w + j - r];
                        int tr = pix[(i - r) * w + j + r];
                        int tc = pix[(i - r) * w + j];
                        int bl = pix[(i + r) * w + j - r];
                        int br = pix[(i + r) * w + j + r];
                        int bc = pix[(i + r) * w + j];
                        int cl = pix[i * w + j - r];
                        int cr = pix[i * w + j + r];

                        pix[(i * w) + j] = 0xFF000000 |
                                                   (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) + (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
                                                   (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00) + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
                                                   (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) + (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) + (cr & 0xFF0000)) >> 3) & 0xFF0000;
                    }
                }
            }
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        }catch (Exception e) {
            return bmp;
        }
    }

    public static void popOutViewDelayed(final View view, long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view == null)
                    return;

                view.setVisibility(View.VISIBLE);
                view.setScaleY(0f);
                view.setScaleX(0f);

                view.animate().scaleX(1f).scaleY(1f).setInterpolator(OVERSHOOT).setDuration(ANIM_DURATION);
            }
        }, delay);
    }

    public static void animateHeartBeat(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f);
        scaleX.setInterpolator(ANTICIPATE);
        scaleX.setRepeatCount(1);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setDuration((long) (200));
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f);
        scaleY.setInterpolator(ANTICIPATE);
        scaleY.setRepeatCount(1);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setDuration((long) (200));

        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX, scaleY);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleX, scaleY);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(set1, set2);
        animSet.start();
    }

    public static View.OnTouchListener setupResizeTouchListener(View v) {
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.animate().setInterpolator(OVERSHOOT)
                            .scaleX(.85f).scaleY(.85f)
                            .setStartDelay(0).setDuration(ANIM_DURATION)
                            .setListener(null);
                } else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL ||
                        event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    v.animate().setInterpolator(OVERSHOOT)
                            .scaleX(1f).scaleY(1f)
                            .setStartDelay(0).setDuration(ANIM_DURATION)
                            .setListener(null);
                }
                return false;
            }
        };
        v.setOnTouchListener(touchListener);
        return touchListener;
    }
}
