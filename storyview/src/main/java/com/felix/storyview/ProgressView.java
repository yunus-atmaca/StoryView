package com.felix.storyview;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

class ProgressView extends AppCompatImageView implements TimeAnimator.TimeListener {
    private static final String TAG = "ProgressView";

    private final static int MAX_LEVEL = 10000;
    private final static int MIN_LEVEL = 0;

    private final static int MAX_LEVEL_IN_TERM_OF_TIME = 10; // 10 second
    private final static int ONE_SECOND_IN_TERM_OF_LEVEL = 1000;

    private ClipDrawable mImageDrawable;

    private int mCurrentLevel;
    private double levelIncrement;

    private ProgressViewListener mListener;

    private TimeAnimator mAnimator;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAnimator = new TimeAnimator();
        mAnimator.setTimeListener(this);

        mCurrentLevel = MIN_LEVEL;
    }

    public void setViewParams(Context context, int height, int backgroundColor, int frontColor, int duration){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,height,1);
        params.leftMargin = 8;
        params.rightMargin = 8;
        setLayoutParams(params);

        setBackgroundColor(backgroundColor);
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.clip));

        mImageDrawable = (ClipDrawable) getDrawable();
        mImageDrawable.setColorFilter(frontColor, PorterDuff.Mode.SRC_ATOP);

        calculateLevelIncrement(duration);
    }

    public void calculateLevelIncrement(long duration){
        double durationInSecond = (double)duration/ ONE_SECOND_IN_TERM_OF_LEVEL;
        levelIncrement = MAX_LEVEL_IN_TERM_OF_TIME / durationInSecond;
    }

    public void setProgressViewListener(@NonNull ProgressViewListener listener){
        mListener = listener;
    }

    public void start(){
        mListener.onStart();
        mAnimator.start();
    }

    public void stop(){
        mListener.onStop();
        mAnimator.cancel();
    }

    public void setMaxLevel(){
        mCurrentLevel = MAX_LEVEL;
    }

    public void setMinLevel(){
        mCurrentLevel = MIN_LEVEL;
        mAnimator.cancel();
        mImageDrawable.setLevel(MIN_LEVEL);
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        mImageDrawable.setLevel(mCurrentLevel);
        if (mCurrentLevel >= MAX_LEVEL) {
            mAnimator.cancel();
            mListener.onEnd();
        } else {
            mCurrentLevel = (int)(totalTime*levelIncrement);
        }
    }
    public interface ProgressViewListener{
        void onStart();
        void onEnd();
        void onStop();
    }
}