package com.luciofm.presentation.droiconit.anim;

import android.util.Property;
import android.view.View;

public class XFractionProperty extends Property<View, Float> {

    public XFractionProperty() {
        super(Float.class, "fractionX");
    }

    @Override
    public Float get(View object) {
        final int viewWidth = object.getWidth();
        if (viewWidth == 0) {
            return 0f;
        }

        return object.getTranslationX() / viewWidth;
    }

    @Override
    public void set(View object, Float value) {
        final int viewWidth = object.getWidth();
        //Avoid flickers on entering views until they are measured
        float translation = viewWidth > 0 ? viewWidth * value : Float.MAX_VALUE;

        object.setTranslationX(translation);
    }
}
