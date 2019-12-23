package com.felix.storyview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

class ProgressView extends AppCompatImageView {
    private static final String TAG = "ProgressView";

    private final static int MAX_LEVEL = 10000;
    private final static int MIN_LEVEL = 0;
    private final static int POST_DELAY = 50;

    private int mDuration = 3000;
    private double levelIncrement;

    private ClipDrawable mImageDrawable;
    private Handler mHandler;

    private int mCurrentLevel;

    private ProgressViewListener mListener;

    private Runnable levelSetter = new Runnable() {
        @Override
        public void run() {
            setLevel();
        }
    };

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setViewParams(context);

        mImageDrawable = (ClipDrawable) getDrawable();
        mHandler = new Handler();

        mCurrentLevel = MIN_LEVEL;
    }

    private void setViewParams(Context context){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,4,1);
        params.leftMargin = 8;
        params.rightMargin = 8;
        setLayoutParams(params);

        setBackground(ContextCompat.getDrawable(context, R.color.dark_grey));
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.clip));
    }

    /**
     * Progress will be complete MAX_LEVEL(10000) with 50 levelIncrement and 50 delay.
     * This is meaning 10 second. --> MAX_LEVEL  * 50 == duration * x --> it will calculate
     * levelIncrement for the time duration.
     *
     * @param duration time duration
     */
    public void calculateLevelIncrement(int duration){
        //MAX_LEVEL  * 50(levelIncrement for the 10 second) == 500000,
        levelIncrement = 500000 / duration;
    }
    public void setProperties(int backgroundColor, int frontColor, int duration){
        setBackgroundColor(backgroundColor);
        mImageDrawable.setColorFilter(frontColor, PorterDuff.Mode.SRC_ATOP);
        mDuration = duration;

        calculateLevelIncrement(mDuration);
    }

    public void setProgressViewListener(@NonNull ProgressViewListener listener){
        mListener = listener;
    }

    private void setLevel() {
        mCurrentLevel += levelIncrement;
        mImageDrawable.setLevel(mCurrentLevel);
        if (mCurrentLevel <= MAX_LEVEL) {
            mHandler.postDelayed(levelSetter, POST_DELAY);
        } else {
            mListener.onEnd();
            mHandler.removeCallbacks(levelSetter);
        }
    }

    public void start(){
        mListener.onStart();
        levelSetter.run();
    }

    public void stop(){
        mListener.onStop();
        mHandler.removeCallbacks(levelSetter);
    }

    public void resume(){
        mListener.onResume();
        start();
    }

    public void setMaxLevel(){
        mCurrentLevel = MAX_LEVEL;
    }

    public void setMinLevel(){
        mCurrentLevel = MIN_LEVEL;
        mHandler.removeCallbacks(levelSetter);
        mImageDrawable.setLevel(MIN_LEVEL);
    }
    public interface ProgressViewListener{
        void onStart();
        void onEnd();
        void onStop();
        void onResume();
    }
}