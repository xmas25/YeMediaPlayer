package com.yemediaplayer;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    YeMediaPlayer player;
    View blackView;
    private String testUri = "http://funbox-w6.dwstatic.com/8/4/1546/186122-98-1447157302.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blackView = findViewById(R.id.blackView);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        player = new YeMediaPlayer(this, findViewById(R.id.player),
                getResources().getDisplayMetrics().widthPixels, YeMediaPlayer.dp2px(this,210),true);
        player.setMedia(Environment.getExternalStorageDirectory()+"/movie.mp4");
        player.setMediaPlayListener((currPos, totalLength) ->
                Log.d("Test", "======="+player.getCurrentPosition()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        player.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
    }

    public void full(View view) {
        player.toggleFullScreen();
    }


    public void pause(View view) {
        player.pause();
    }

    public void play(View view) {
        player.play();
        if(blackView.getVisibility()==View.VISIBLE){
            blackView.setVisibility(View.GONE);
        }
    }

    public void stop(View view) {
        player.stop();
        blackView.setVisibility(View.VISIBLE);
    }
}
