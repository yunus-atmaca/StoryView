package com.felix.storyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StoryView extends FrameLayout implements ProgressView.ProgressViewListener {
    private static final String TAG = "StoryView";

    private LinearLayout mProgressViewsRoot;
    private ArrayList<ProgressView> mProgressViews;
    private int mCurrentProgress;

    private int mStoryCount;
    private int mStoryBackgroundColor;
    private int mStoryFrontColor;
    private int mDuration;
    private int mStoryHeight;
    private boolean mAutoNextStory;

    private boolean isComplete;

    private StoryViewListener mListener;

    Context mContext;

    public StoryView(@NonNull Context context) {
        this(context,null);
    }

    public StoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.story_view, this);

        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoryView);
        mStoryCount = typedArray.getInt(R.styleable.StoryView_storyCount, 5);
        mStoryBackgroundColor = typedArray.getColor(R.styleable.StoryView_storyBackgroundColor,
                Color.DKGRAY);
        mStoryFrontColor = typedArray.getColor(R.styleable.StoryView_storyFrontColor,
                Color.WHITE);
        mDuration = typedArray.getInt(R.styleable.StoryView_duration, 3000);
        mStoryHeight = typedArray.getDimensionPixelOffset(R.styleable.StoryView_storyHeight, 5);
        mAutoNextStory = typedArray.getBoolean(R.styleable.StoryView_autoNextStory, true);
        typedArray.recycle();

        mProgressViewsRoot = findViewById(R.id.progress_views_root);
        mProgressViews = new ArrayList<>();
        mCurrentProgress = 0;
        isComplete = false;

        createProgressViews();
    }

    private void createProgressViews(){
        for (int i=0;i<mStoryCount;++i){
            ProgressView progressView = new ProgressView(mContext);
            progressView.setViewParams(mContext, mStoryHeight, mStoryBackgroundColor, mStoryFrontColor, mDuration);
            progressView.setId(i);
            progressView.setProgressViewListener(this);

            mProgressViewsRoot.addView(progressView);
            mProgressViews.add(progressView);
        }
    }

    /**
     * This will set duration for the currentProgress.
     * @param duration the duration
     */
    public void setDuration(long duration){
        mProgressViews.get(mCurrentProgress).calculateLevelIncrement(duration);
    }
    public void setStoryCount(int storyCount){
        mStoryCount = storyCount;
        mProgressViewsRoot.removeAllViews();
        mProgressViews.clear();

        createProgressViews();
    }
    public void setAllStoriesMin(){
        for (int i=0;i<mStoryCount;++i)
            mProgressViews.get(i).setMinLevel();
        mCurrentProgress = 0;
    }
    public void addStoryListener(@NonNull StoryViewListener listener){
        mListener = listener;
    }

    public void start(){
        if (mListener != null)
            mListener.onCurrentStory(mCurrentProgress);

        mProgressViews.get(mCurrentProgress).start();
    }

    public void stop(){
        mProgressViews.get(mCurrentProgress).stop();
    }

    public void nextStory(){
        if (isComplete)
            return;

        if (mCurrentProgress < mStoryCount)
            mProgressViews.get(mCurrentProgress).setMaxLevel();
    }

    public void previousStory(){
        if (isComplete)
            return;

        mProgressViews.get(mCurrentProgress).setMinLevel();
        if (mCurrentProgress == 0) {
            start();
        } else{
            --mCurrentProgress;
            mProgressViews.get(mCurrentProgress).setMinLevel();
            if (mAutoNextStory)
                start();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "[onStart]current progress:" +mCurrentProgress);
    }

    @Override
    public void onEnd() {
        ++mCurrentProgress;
        if (mCurrentProgress < mStoryCount){
            if (mAutoNextStory)
                start();
        }else{
            --mCurrentProgress;
            isComplete = true;
            if (mListener != null)
                mListener.onCompleteStories();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "[onStop]current progress:" +mCurrentProgress);
    }

    public interface StoryViewListener{
        void onCurrentStory(int currentStory);
        void onCompleteStories();
    }
}