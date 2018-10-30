package com.lhy.comm.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.lhy.comm.R;
import com.lhy.comm.ui.view.AudioAttachmentView;

public class DynamicActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        setContentView(R.layout.dynamic_activity_main);
        FrameLayout layout = (FrameLayout) findViewById(R.id.container);

        AudioAttachmentView audio = (AudioAttachmentView)
                LayoutInflater.from(this).inflate(R.layout.hy_message_audio_attachment,
                        null, false);
        audio.bind(Uri.parse("/sdcard/test.m4a"), 7);
        layout.addView(audio);

        audio = (AudioAttachmentView)
                LayoutInflater.from(this).inflate(R.layout.hy_message_audio_attachment,
                        null, false);
        audio.bind(Uri.parse("/sdcard/test.m4a"), 7);
        params.setMargins(200, 0, 0, 0);
        layout.addView(audio, params);

        audio = (AudioAttachmentView)
                LayoutInflater.from(this).inflate(R.layout.hy_message_audio_attachment,
                        null, false);
        audio.bind(Uri.parse("/sdcard/test.m4a"), 7);
        params.setMargins(400, 0, 0, 0);
        layout.addView(audio, params);

        layout.invalidate();

//        audio = (AudioAttachmentView)
//                LayoutInflater.from(this).inflate(R.layout.hy_message_audio_attachment,
//                        null, false);
//        audio.bind(Uri.parse("/sdcard/test.m4a"), 7);
//
//        layout.addView(audio);
    }

    @Override
    public void onClick(View v) {

    }
}
