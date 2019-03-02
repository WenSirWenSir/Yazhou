package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbyj.Yazhou.LeftCompanyProgram.CAMER;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.ConfigPageClass;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;
import com.zbyj.Yazhou.LeftCompanyProgram.UserMsg;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;


public class UserComment extends YazhouActivity {
    private ImageView btn_camera;
    private EditText et_comment;
    private TextView open_cameraTitle, btn_orderComment;
    private Bitmap bitmap = null;
    private boolean isCameraHaveing = false;

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
        et_comment = findViewById(R.id.activity_usercommend_etComment);//btn edit comment
        et_comment.setText(UserMsg.COMMENT_MSG.trim());
        btn_orderComment = findViewById(R.id.activity_usercomment_btnOrderComment);//btn user edit comment done,order
        open_cameraTitle = findViewById(R.id.activity_usercommend_opencameratitle);//as open camera message
        Listener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Listener() {
        btn_orderComment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isCameraHaveing) {
                    //no phone data,Prompt the user no phone msg
                }

                return false;
            }
        });
        /**
         * Call open Camera API
         */
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCameraHaveing) {
                    //if the isCameraHaveing values is true
                    //that means we have data,Prompt the user for data
                    View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout
                            .item_trueorfalse_dialog, null);
                    ConfigPageClass configPageClass = new ConfigPageClass();
                    ConfigPageClass.AlertViewIDpage alertViewIDpage = configPageClass.getAlertViewIDpageInstance();
                    TextView item_title = item.findViewById(R.id.item_trueorfalse_dialog_title);
                    alertViewIDpage.setTitle(item_title);
                    TextView item_context = item.findViewById(R.id.item_trueorfalse_dialog_content);//text
                    alertViewIDpage.setContext(item_context);
                    TextView item_cancle = item.findViewById(R.id.item_trueorfalse_dialog_btnCancle);//btn
                    alertViewIDpage.setCancle(item_cancle);
                    TextView item_confirm = item.findViewById(R.id.item_trueorfalse_dialog_btnDetermine);
                    alertViewIDpage.setConfirm(item_confirm);
                    Tools.showAlertDilg(item, UserComment.this, "程序猿的温馨提示", "您已经拍摄了一张照片,一次评论只能上传一张照片.你确定取消刚刚拍摄的照片吗?",
                            "取消", "没拍好,确定", new Tools.AlertDilgClick() {
                        @Override
                        public void onConfirm(AlertDialog alertDialog) {
                            isCameraHaveing = false;
                            btn_camera.setImageBitmap(null);
                            //The user decide to re-take the photo
                            OpenCamera();
                            alertDialog.dismiss();//destruction
                        }

                        @Override
                        public void onCancle(AlertDialog alertDialog) {
                            alertDialog.dismiss();

                        }
                    },alertViewIDpage);
                } else {
                    //if the isCameraHaveing values is false
                    //open device camera
                    OpenCamera();
                }
            }
        });

        /**
         * User editing Comment
         */
        et_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (et_comment.getText().toString().trim().equals(UserMsg.COMMENT_MSG.trim())) {
                    //User can't update Comment
                    et_comment.setText("");
                    et_comment.setTextColor(Color.parseColor("#000000"));
                } else {
                    //User have commnet data,not check
                }

                return false;
            }
        });
    }

    /**
     * Open Device Camera
     */
    public void OpenCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开照相机关键
        startActivityForResult(i, CAMER.GET_ONSHOOT_DONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMER.GET_ONSHOOT_DONE:
                if (resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    btn_camera.setImageBitmap(bitmap);

                    //if set ImageView bitmap,Prompt the user to open Camera
                    isCameraHaveing = true;
                    open_cameraTitle.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (btn_camera != null) {
            btn_camera = null;
            System.gc();//提示系统回收该控件中的BITMAP
        }
        if (bitmap != null) {
            bitmap = null;
            System.gc();
        }
        super.onDestroy();
    }
}
