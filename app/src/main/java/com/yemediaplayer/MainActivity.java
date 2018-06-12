package com.yemediaplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    MediaView mediaView;
    YeMediaPlayer player;
    private String testUri = "http://funbox-w6.dwstatic.com/8/4/1546/186122-98-1447157302.mp4";

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_main);
        requestPerm();
        Toast.makeText(this,new String("大声道".getBytes(), Charset.forName("utf-8"))
        ,Toast.LENGTH_SHORT).show();
        mediaView = findViewById(R.id.player);
        mediaView.setDefaultView(getDefaultView());
        player = new YeMediaPlayer(this, mediaView,
                getResources().getDisplayMetrics().widthPixels,
                YeMediaPlayer.dp2px(this,210),true);
        player.setMedia(Environment.getExternalStorageDirectory()+"/movie.mp4");
        player.setMediaPlayingListener((currPos, totalLength) ->{

        });
        player.toggleFullScreen();
    }

    private View getDefaultView() {
        View view = new View(this);
        view.setBackgroundColor(Color.BLACK);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    private void requestPerm() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    /**
     * 全屏或非全屏
     * @param view
     */
    public void full(View view) {
        player.toggleFullScreen();
    }

    /**
     * 暂停
     * @param view
     */
    public void pause(View view) {
        player.pause();
    }

    /**
     * 播放
     * @param view
     */
    public void play(View view) {
        mediaView.hideDefaultView();
        player.play();
    }

    /**
     * 停止
     * @param view
     */
    public void stop(View view) {
        mediaView.showDefaultView();
        player.stop();
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

}
