package com.luciofm.presentation.droiconit.transitions;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.luciofm.presentation.droiconit.widget.TextureVideoView;

import java.util.Map;

/**
 * This Transition captures an ImageView's matrix before and after the
 * scene change and animates it during the transition.
 *
 * <p>In combination with ChangeBounds, ChangeVideoTransform allows ImageViews
 * that change size, shape, or {@link android.widget.ImageView.ScaleType} to animate contents
 * smoothly.</p>
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ChangeVideoTransform extends Transition {

    private static final String TAG = "ChangeVideoTransform";

    private static final String PROPNAME_MATRIX = "android:changeVideoTransform:matrix";
    private static final String PROPNAME_BOUNDS = "android:changeVideoTransform:bounds";

    private static final String[] sTransitionProperties = {
            PROPNAME_MATRIX,
            PROPNAME_BOUNDS,
    };

    private static TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() {
        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            return null;
        }
    };

    private static Property<TextureVideoView, Matrix> ANIMATED_TRANSFORM_PROPERTY
            = new Property<TextureVideoView, Matrix>(Matrix.class, "animatedTransform") {
        @Override
        public void set(TextureVideoView object, Matrix value) {
            object.setTransform(value);
            object.invalidate();
        }

        @Override
        public Matrix get(TextureVideoView object) {
            return null;
        }
    };

    public ChangeVideoTransform() {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChangeVideoTransform(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (!(view instanceof TextureVideoView)) {
            return;
        }
        TextureVideoView videoView = (TextureVideoView) view;
        Map<String, Object> values = transitionValues.values;

        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);
        values.put(PROPNAME_BOUNDS, bounds);
        Matrix matrix;
        matrix = new Matrix(videoView.getTransformMatrix());
        values.put(PROPNAME_MATRIX, matrix);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        View v = transitionValues.view;
        Matrix m = null;
        if (v instanceof TextureVideoView) {
            m = ((TextureVideoView) v).getTransformMatrix();
            if (m != null)
                ((TextureVideoView) v).updateTextureViewSize();
        }
        captureValues(transitionValues);

        if (v instanceof TextureVideoView)
            ((TextureVideoView) v).setTransform(m);
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    /**
     * Creates an Animator for ImageViews moving, changing dimensions, and/or changing
     * {@link android.widget.ImageView.ScaleType}.
     *
     * @param sceneRoot   The root of the transition hierarchy.
     * @param startValues The values for a specific target in the start scene.
     * @param endValues   The values for the target in the end scene.
     * @return An Animator to move an ImageView or null if the View is not an ImageView,
     * the Drawable changed, the View is not VISIBLE, or there was no change.
     */
    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }
        Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);
        if (startBounds == null || endBounds == null) {
            return null;
        }

        Matrix startMatrix = (Matrix) startValues.values.get(PROPNAME_MATRIX);
        Matrix endMatrix = (Matrix) endValues.values.get(PROPNAME_MATRIX);

        boolean matricesEqual = (startMatrix == null && endMatrix == null) ||
                (startMatrix != null && startMatrix.equals(endMatrix));

        if (startBounds.equals(endBounds) && matricesEqual) {
            return null;
        }

        TextureVideoView videoView = (TextureVideoView) endValues.view;

        ObjectAnimator animator;
        if (startMatrix == null) {
            startMatrix = IDENTITY_MATRIX;
        }
        if (endMatrix == null) {
            endMatrix = IDENTITY_MATRIX;
        }
        ANIMATED_TRANSFORM_PROPERTY.set(videoView, startMatrix);
        animator = createMatrixAnimator(videoView, startMatrix, endMatrix);
        return animator;
    }

    private ObjectAnimator createNullAnimator(TextureVideoView videoView) {
        return ObjectAnimator.ofObject(videoView, ANIMATED_TRANSFORM_PROPERTY,
                NULL_MATRIX_EVALUATOR, null, null);
    }

    private ObjectAnimator createMatrixAnimator(final TextureVideoView videoView, Matrix startMatrix,
                                                final Matrix endMatrix) {
        return ObjectAnimator.ofObject(videoView, ANIMATED_TRANSFORM_PROPERTY,
                new MatrixEvaluator(), startMatrix, endMatrix);
    }

    public static Matrix IDENTITY_MATRIX = new Matrix() {
        void oops() {
            throw new IllegalStateException("Matrix can not be modified");
        }

        @Override
        public void set(Matrix src) {
            oops();
        }

        @Override
        public void reset() {
            oops();
        }

        @Override
        public void setTranslate(float dx, float dy) {
            oops();
        }

        @Override
        public void setScale(float sx, float sy, float px, float py) {
            oops();
        }

        @Override
        public void setScale(float sx, float sy) {
            oops();
        }

        @Override
        public void setRotate(float degrees, float px, float py) {
            oops();
        }

        @Override
        public void setRotate(float degrees) {
            oops();
        }

        @Override
        public void setSinCos(float sinValue, float cosValue, float px, float py) {
            oops();
        }

        @Override
        public void setSinCos(float sinValue, float cosValue) {
            oops();
        }

        @Override
        public void setSkew(float kx, float ky, float px, float py) {
            oops();
        }

        @Override
        public void setSkew(float kx, float ky) {
            oops();
        }

        @Override
        public boolean setConcat(Matrix a, Matrix b) {
            oops();
            return false;
        }

        @Override
        public boolean preTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @Override
        public boolean preScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preScale(float sx, float sy) {
            oops();
            return false;
        }

        @Override
        public boolean preRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preRotate(float degrees) {
            oops();
            return false;
        }

        @Override
        public boolean preSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean preSkew(float kx, float ky) {
            oops();
            return false;
        }

        @Override
        public boolean preConcat(Matrix other) {
            oops();
            return false;
        }

        @Override
        public boolean postTranslate(float dx, float dy) {
            oops();
            return false;
        }

        @Override
        public boolean postScale(float sx, float sy, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postScale(float sx, float sy) {
            oops();
            return false;
        }

        @Override
        public boolean postRotate(float degrees, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postRotate(float degrees) {
            oops();
            return false;
        }

        @Override
        public boolean postSkew(float kx, float ky, float px, float py) {
            oops();
            return false;
        }

        @Override
        public boolean postSkew(float kx, float ky) {
            oops();
            return false;
        }

        @Override
        public boolean postConcat(Matrix other) {
            oops();
            return false;
        }

        @Override
        public boolean setRectToRect(RectF src, RectF dst, ScaleToFit stf) {
            oops();
            return false;
        }

        @Override
        public boolean setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex,
                                     int pointCount) {
            oops();
            return false;
        }

        @Override
        public void setValues(float[] values) {
            oops();
        }
    };

    public static class MatrixEvaluator implements TypeEvaluator<Matrix> {

        float[] mTempStartValues = new float[9];

        float[] mTempEndValues = new float[9];

        Matrix mTempMatrix = new Matrix();

        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            startValue.getValues(mTempStartValues);
            endValue.getValues(mTempEndValues);
            for (int i = 0; i < 9; i++) {
                float diff = mTempEndValues[i] - mTempStartValues[i];
                mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
            }
            mTempMatrix.setValues(mTempEndValues);
            return mTempMatrix;
        }
    }

}
