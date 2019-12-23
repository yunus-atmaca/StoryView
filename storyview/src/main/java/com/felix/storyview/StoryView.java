package com.felix.storyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
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

    private StoryViewListener mListener;

    public StoryView(@NonNull Context context) {
        this(context,null);
    }

    public StoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.story_view, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StoryView);
        mStoryCount = typedArray.getInt(R.styleable.StoryView_storyCount, 5);
        mStoryBackgroundColor = typedArray.getColor(R.styleable.StoryView_storyBackgroundColor,
                Color.DKGRAY);
        mStoryFrontColor = typedArray.getColor(R.styleable.StoryView_storyFrontColor,
                Color.WHITE);
        mDuration = typedArray.getInt(R.styleable.StoryView_duration, 3000);
        typedArray.recycle();

        mProgressViewsRoot = findViewById(R.id.progress_views_root);
        mProgressViews = new ArrayList<>();
        mCurrentProgress = 0;

        createProgressViews(context);
    }

    private void createProgressViews(Context context){
        for (int i=0;i<mStoryCount;++i){
            ProgressView progressView = new ProgressView(context);
            progressView.setProperties(mStoryBackgroundColor, mStoryFrontColor, mDuration);
            progressView.setId(i);
            progressView.setProgressViewListener(this);

            mProgressViewsRoot.addView(progressView);
            mProgressViews.add(progressView);
        }
    }

    /**
     * This will set duration for the currentProgress.
     * @param duration
     */
    public void setDuration(int duration){
        mProgressViews.get(mCurrentProgress).calculateLevelIncrement(duration);
    }
    public void addStoryListener(@NonNull StoryViewListener listener){
        mListener = listener;
    }

    public void start(){
        mListener.onCurrentStory(mCurrentProgress);
        mProgressViews.get(mCurrentProgress).start();
    }

    public void stop(){
        mProgressViews.get(mCurrentProgress).stop();
    }

    public void resume(){
        start();
    }

    public void nextStory(){
        if (mCurrentProgress < mStoryCount)
            mProgressViews.get(mCurrentProgress).setMaxLevel();
    }

    public void previousStory(){
        mProgressViews.get(mCurrentProgress).setMinLevel();
        if (mCurrentProgress == 0) {
            start();
        } else{
            --mCurrentProgress;
            mProgressViews.get(mCurrentProgress).setMinLevel();
            start();
        }
    }
    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        ++mCurrentProgress;
        if (mCurrentProgress < mStoryCount){
            start();
        }else{
            --mCurrentProgress;
            mListener.onCompleteStories();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    public interface StoryViewListener{
        void onCurrentStory(int currentStory);
        void onCompleteStories();
    }
}
