package com.felix.storiesview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.image_activity).setOnClickListener(this);
        findViewById(R.id.video_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_activity:
                Intent imageAct = new Intent(this, Image.class);
                startActivity(imageAct);
                break;
            case R.id.video_activity:
                Intent videoAct = new Intent(this, Video.class);
                startActivity(videoAct);
                break;
        }
    }
}
