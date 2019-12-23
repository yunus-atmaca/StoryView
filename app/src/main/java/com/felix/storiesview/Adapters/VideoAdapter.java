package com.felix.storiesview.Adapters;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.felix.storiesview.VideoViews;

import java.util.HashMap;

public class VideoAdapter extends FragmentStatePagerAdapter {

    HashMap<Integer, VideoViews> fragments = new HashMap<>();

    public VideoAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        VideoViews videoViews = new VideoViews();

        Bundle bundle = new Bundle();
        bundle.putString("videoUri", getVideoUriByPosition(position));
        videoViews.setArguments(bundle);

        fragments.put(position, videoViews);
        return videoViews;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public VideoViews getFragmentByPosition(int position){
        return fragments.get(position);
    }

    private String getVideoUriByPosition(int position) {
        if (position == 0){
            return "file:///android_asset/video_1.mp4";
        }else if (position == 1){
            return "file:///android_asset/video_2.mp4";
        }else {
            return "file:///android_asset/video_3.mp4";
        }
    }
}
