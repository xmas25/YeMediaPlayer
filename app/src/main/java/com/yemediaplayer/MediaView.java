package com.yemediaplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MediaView extends FrameLayout {

    private SurfaceView surfaceView;
    private View defaultView;
    private View controlView;

    public void showDefaultView(){
        if(defaultView!=null) defaultView.setVisibility(View.VISIBLE);
    }

    public void hideDefaultView(){
        if(defaultView!=null) defaultView.setVisibility(View.GONE);
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public View getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(View defaultView) {
        if(this.defaultView!=null) removeView(this.defaultView);
        this.defaultView = defaultView;
        this.defaultView.setVisibility(View.GONE);
        addView(defaultView);
    }

    public View getControlView() {
        return controlView;
    }

    public void setControllView(View controllView) {
        if(this.controlView!=null) removeView(this.controlView);
        this.controlView = controllView;
        addView(controllView);
    }

    public MediaView(@NonNull Context context) {
        this(context,null);
    }

    public MediaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MediaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceView = new SurfaceView(context);
        surfaceView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(surfaceView);
        initControlView(context);
    }

    private void initControlView(Context context) {
        controlView = LayoutInflater.from(context).inflate(R.layout.controlview, null);
    }
}
