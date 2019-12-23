package com.felix.storiesview.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.felix.storiesview.ImageViews;
import com.felix.storiesview.R;

public class ImageAdapter extends FragmentStatePagerAdapter {

    public ImageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ImageViews imageViews = new ImageViews();

        Bundle bundle = new Bundle();
        bundle.putInt("imageID", getImageByPosition(position));
        imageViews.setArguments(bundle);

        return imageViews;
    }

    @Override
    public int getCount() {
        return 5;
    }

    private int getImageByPosition(int position) {
        if (position == 0){
            return R.mipmap.liv_tyler_1;
        }else if (position == 1){
            return R.mipmap.liv_tyler_2;
        }else if (position == 2){
            return R.mipmap.liv_tyler_3;
        }else if (position == 3){
            return R.mipmap.liv_tyler_4;
        }else{
            return R.mipmap.liv_tyler_5;
        }
    }
}
