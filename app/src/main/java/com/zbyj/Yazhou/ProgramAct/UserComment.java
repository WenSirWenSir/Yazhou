package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.zbyj.Yazhou.ConfigPageValue.CAMER;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;

public class UserComment extends YazhouActivity {
    private ImageView btn_camera;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(this.getResources().getString(R.color.TextAndBodyColor));
        setContentView(R.layout.activity_usercomment);
        init();
    }

    private void init() {
        btn_camera = findViewById(R.id.activity_usercomment_icoCamera);
        Listener();
    }

    private void Listener() {

        /**
         * 调用拍摄图片
         */
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenCamera();
            }
        });
    }

    /**
     * 打开照相机
     */
    public void OpenCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开照相机关键
        startActivityForResult(i, CAMER.GET_ONSHOOT_DONE);
    }

}
