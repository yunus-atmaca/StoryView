package com.felix.storiesview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageViews extends Fragment {
    private static final String TAG = "ImageViews";
    private View view;

    private int imageID;

    public ImageViews() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageID = getArguments().getInt("imageID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_views, container, false);

        view.findViewById(R.id.image).setBackgroundResource(imageID);

        return view;
    }
}
