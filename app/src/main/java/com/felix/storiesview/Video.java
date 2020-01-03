package com.felix.storiesview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.felix.storiesview.Adapters.VideoAdapter;
import com.felix.storiesview.ViewPager.NonSwipeableViewPager;
import com.felix.storyview.StoryView;

public class Video extends AppCompatActivity implements
        StoryView.StoryViewListener,
        View.OnClickListener,
        VideoViews.VideoListener {
    private static final String TAG = "Video";
    private NonSwipeableViewPager viewPager;
    private VideoAdapter adapter;

    private StoryView storyView;

    private int previous = -1;
    private int currentVideo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initUI();
    }

    private void initUI() {
        storyView = findViewById(R.id.story_view);
        storyView.addStoryListener(this);

        findViewById(R.id.left_of_screen).setOnClickListener(this);
        findViewById(R.id.right_of_screen).setOnClickListener(this);

        viewPager = findViewById(R.id.view_pager);
        adapter = new VideoAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        final ViewPager.OnPageChangeListener listener = viewPagerPageListener();
        viewPager.addOnPageChangeListener(listener);
        viewPager.post(() -> listener.onPageSelected(0));
    }

    private ViewPager.OnPageChangeListener viewPagerPageListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (previous != -1)
                    adapter.getFragmentByPosition(previous).releasePlayer();

                adapter.getFragmentByPosition(position).initializePlayer();

                previous = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        };
    }

    @Override
    public void onCurrentStory(int currentStory) {
        //Calls before current progress start
        Log.d(TAG,"onCurrentStory");
    }

    @Override
    public void onCompleteStories() {
        //Calls when all progress has done
        Log.d(TAG,"onCompleteStories");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_of_screen:
                if (previous == 0)
                    return;
                storyView.previousStory();
                goPrevious();
                break;
            case R.id.right_of_screen:
                storyView.nextStory();
                goNext();
                break;
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof VideoViews){
            VideoViews videoViews = (VideoViews) fragment;
            videoViews.setVideoListener(this);
        }
    }
    private void goPrevious(){
        --currentVideo;
        if (currentVideo >= 0)
            viewPager.setCurrentItem(currentVideo);
    }
    private void goNext(){
        ++currentVideo;
        if (currentVideo < adapter.getCount())
            viewPager.setCurrentItem(currentVideo);
    }
    @Override
    public void onEnd() {
        goNext();
    }

    @Override
    public void onReady(long duration) {
        storyView.setDuration(duration);
        storyView.start();
    }
}
