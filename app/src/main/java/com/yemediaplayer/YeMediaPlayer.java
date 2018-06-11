package com.yemediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.FileDescriptor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class YeMediaPlayer{

    private MediaPlayer mediaPlayer;//媒体播放器
    private LibVLC libVLC = null;//VLC库
    private SurfaceView surfaceView;//视频界面
    private Context context;
    private Media media;
    private int width, height;
    private boolean isScreenPortrait;
    private Handler handler;
    private OnPlayListener playListener;

    public YeMediaPlayer(Context ctx, SurfaceView surfaceView, int width, int height,
                         boolean isScreenPortrait){
        this.surfaceView = surfaceView;
        context = ctx;
        this.width = width;
        this.height = height;
        this.isScreenPortrait = isScreenPortrait;
        launcher();
    }

    /**
     * 初始化播放器
     */
    private void launcher() {
        handler = new Handler();
        //在Activity中可以为按钮增加事件
        ArrayList<String> options = new ArrayList<>();
        //创建VLC库
        libVLC = new LibVLC(context, options);
        try {
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                mediaPlayer.stop();
////                mediaPlayer.release();
////                mediaPlayer = null;
////            }
            //创建媒体播放器
            mediaPlayer = new MediaPlayer(libVLC);
            //设置视频界面
            mediaPlayer.getVLCVout().setVideoSurface(surfaceView.getHolder().getSurface(), surfaceView.getHolder());
            //将SurfaceView贴到MediaPlayer上
            mediaPlayer.getVLCVout().attachViews();
            //设置播放窗口的尺寸
            mediaPlayer.getVLCVout().setWindowSize(surfaceView.getWidth(), surfaceView.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.getVLCVout().setWindowSize(width, height);
    }

    /**
     * 获取媒体文件时长
     * @return
     */
    public long getMediaLength(){
        return  mediaPlayer.getLength();
    }

    /**
     * 设置当前的播放时间
     * @param position
     */
    public void setCurrentPosition(long position){
        mediaPlayer.setTime(position);
    }

    /**
     * 获取媒体文件当前播放位置
     * @return
     */
    public long getCurrentPosition(){
        return mediaPlayer.getTime();
    }

    /**
     * 播放
     */
    public void play(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.play();
        }
    }

    /**
     * 将文件时长转换为歌曲时间
     * @param length
     * @return
     */
    public String msToSongTime(long length){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(new Date(length));
    }


    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void onResume(){
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void onDestroy(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            handler.getLooper().quit();
            handler.removeCallbacksAndMessages(null);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 设置全屏或取消全屏
     */
    public void toggleFullScreen() {
        if(isScreenPortrait){
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isScreenPortrait = false;
        }else {
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isScreenPortrait = true;
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying()){
                playListener.play(getCurrentPosition(), getMediaLength());
                handler.postDelayed(this, 100);
            }
        }
    };


    public void setMediaPlayListener(OnPlayListener playListener){
        this.playListener = playListener;
        mediaPlayer.setEventListener(event -> {
            switch (event.type){
                case MediaPlayer.Event.Playing:
                    handler.postDelayed(runnable, 100);
                    break;
            }
        });


    }

    public interface OnPlayListener{
        void play(long currPos, long totalLength);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            surfaceView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mediaPlayer.getVLCVout().setWindowSize(context.getResources().getDisplayMetrics().widthPixels,
                    context.getResources().getDisplayMetrics().heightPixels);
        } else {
            surfaceView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            mediaPlayer.getVLCVout().setWindowSize(width, height);
        }

    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(String mediaPath) {
        this.media = new Media(libVLC, mediaPath);
        mediaPlayer.setMedia(media);
    }

    public void setMedia(Uri mediaUri) {
        this.media = new Media(libVLC, mediaUri);
        mediaPlayer.setMedia(media);
    }

    public void setMedia(FileDescriptor mediaFD) {
        this.media = new Media(libVLC, mediaFD);
        mediaPlayer.setMedia(media);
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
