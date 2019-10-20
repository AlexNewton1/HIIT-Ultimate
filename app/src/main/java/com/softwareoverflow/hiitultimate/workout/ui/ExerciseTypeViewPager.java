package com.softwareoverflow.hiitultimate.workout.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

public class ExerciseTypeViewPager extends ViewPager implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_SCALE = 0.75f;
    private static final float MIN_FADE = 0.4f;


    public ExerciseTypeViewPager(@NonNull Context context) {
        super(context);
    }

    public ExerciseTypeViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void setup(int margin) {
        setPageMargin(margin);
        setPageTransformer(false, this);
        setOffscreenPageLimit(2);
        setClipToPadding(false);
        setClipChildren(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup(-w / 3);
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        float alpha, scaleX = MAX_SCALE, scaleY = MAX_SCALE;
        float translationX = 0;

        if (position == 0) { // view is centered, show it fully
            alpha = 1f;
            scaleX = scaleY = MAX_SCALE;
        } else if (Math.abs(position) > 1) { // View is way off the screen
            alpha = 1f;
        } else {
            alpha = 1 - ((1 - MIN_FADE) * Math.abs(position));
            scaleX = scaleY = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * (1 - Math.abs(position));
            translationX = (pageWidth * scaleX / 2.0f) * -position;
        }

        view.setAlpha(alpha);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setTranslationX(translationX);
        ViewCompat.setTranslationZ(view, alpha); // Views further off to the side are always behind ones in the center
    }
}
