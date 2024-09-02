package com.kerolsmm.incognito.Utilts;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeWidthAnimation extends Animation {
    private int mStartWidth;
    private View mView;
    
    public ResizeWidthAnimation(View view) {
        mView = view;
        mStartWidth = view.getWidth();
    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        mView.requestLayout();
    }
    
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
    
    @Override
    public boolean willChangeBounds() {
        return true;
    }
}