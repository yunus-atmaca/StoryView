package com.felix.storiesview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.felix.storiesview.Adapters.ImageAdapter;
import com.felix.storiesview.ViewPager.NonSwipeableViewPager;
import com.felix.storyview.StoryView;

public class Image extends AppCompatActivity implements
        StoryView.StoryViewListener, View.OnClickListener {

    private NonSwipeableViewPager viewPager;
    private ImageAdapter adapter;

    private int currentPage = 0;

    private StoryView storyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initUI();
    }

    private void initUI() {
        viewPager = findViewById(R.id.view_pager);
        adapter = new ImageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        storyView = findViewById(R.id.story_view);
        storyView.addStoryListener(this);
        storyView.start();

        findViewById(R.id.left_of_screen).setOnClickListener(this);
        findViewById(R.id.right_of_screen).setOnClickListener(this);
    }

    @Override
    public void onCurrentStory(int currentStory) {
        viewPager.setCurrentItem(currentStory);
    }

    @Override
    public void onCompleteStories() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_of_screen:
                storyView.previousStory();
                break;
            case R.id.right_of_screen:
                storyView.nextStory();
                break;
        }
    }
}
