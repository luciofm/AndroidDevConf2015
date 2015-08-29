package com.luciofm.presentation.droiconit.anim;

import android.util.Property;
import android.view.View;

public class YFractionProperty extends Property<View, Float> {

    public YFractionProperty() {
        super(Float.class, "fractionY");
    }

    @Override
    public Float get(View object) {
        final int viewHeight = object.getHeight();
        if (viewHeight == 0) {
            return 0f;
        }

        return object.getTranslationY() / viewHeight;
    }

    @Override
    public void set(View object, Float value) {
        final int viewHeight = object.getHeight();
        //Avoid flickers on entering views until they are measured
        float translation = viewHeight > 0 ? viewHeight * value : Float.MAX_VALUE;

        object.setTranslationY(translation);
    }
}
