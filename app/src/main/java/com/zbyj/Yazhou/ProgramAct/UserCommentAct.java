package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbyj.Yazhou.LeftCompanyProgram.CAMER;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.ConfigPageClass;
import com.zbyj.Yazhou.LeftCompanyProgram.Factory.FileoperationFactory;
import com.zbyj.Yazhou.LeftCompanyProgram.Factory.ImageCacheFactory;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;
import com.zbyj.Yazhou.LeftCompanyProgram.UserMsg;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;


public class UserCommentAct extends YazhouActivity {
    private ImageView btn_camera, btn_back;
    private EditText et_comment;
    private TextView btn_orderComment;
    private Bitmap bitmap = null;
    private LinearLayout activity_usercomment_setboyLayout;
    private boolean isCameraHaveing = false;
    private ImageCacheFactory imageCacheFactory = new ImageCacheFactory();

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
        btn_orderComment = findViewById(R.id.activity_usercomment_btnOrderComment);//btn user
        activity_usercomment_setboyLayout = findViewById(R.id.activity_usercomment_setboyLayout);
        btn_back = findViewById(R.id.activity_usercomment_btnback);//btn back imagebtn
        //set boy
        // edit comment done,order
        // camera message
        try {
            btn_camera.setImageBitmap(FileoperationFactory.getImageFile(getApplicationContext(),
                    "123", false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Listener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Listener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserwantExit();
            }
        });
        for (int i = 0; i < 5; i++) {
            ImageView img = (ImageView) activity_usercomment_setboyLayout.getChildAt(i);
            img.setTag("0");//no set
            img.setAlpha(50);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView iv = (ImageView) v;
                    if (iv.getTag().toString().equals("0")) {
                        //set alpha
                        iv.setTag("1");
                        iv.setAlpha(255);
                    } else {
                        iv.setTag("0");
                        iv.setAlpha(50);
                    }
                }
            });
        }
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
                    ConfigPageClass.AlertViewIDpage alertViewIDpage = configPageClass
                            .getAlertViewIDpageInstance();
                    TextView item_title = item.findViewById(R.id.item_trueorfalse_dialog_title);
                    alertViewIDpage.setTitle(item_title);
                    TextView item_context = item.findViewById(R.id
                            .item_trueorfalse_dialog_content);//text
                    alertViewIDpage.setContext(item_context);
                    TextView item_cancle = item.findViewById(R.id
                            .item_trueorfalse_dialog_btnCancle);//btn
                    alertViewIDpage.setCancle(item_cancle);
                    TextView item_confirm = item.findViewById(R.id
                            .item_trueorfalse_dialog_btnDetermine);
                    alertViewIDpage.setConfirm(item_confirm);
                    Tools.showAlertDilg(item, UserCommentAct.this, "程序猿的温馨提示", "您已经拍摄了一张照片," +
                            "一次评论只能上传一张照片.你确定取消刚刚拍摄的照片吗?", "取消", "没拍好,确定", new Tools
                            .AlertDilgClick() {
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
                    }, alertViewIDpage);
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
                    if (bitmap != null) {
                        //save it
                        imageCacheFactory.saveImage("woailiuyu", bitmap);
                    }

                    btn_camera.setImageBitmap(imageCacheFactory.getImage("woailiuyu"));
                    //if set ImageView bitmap,Prompt the user to open Camera
                    isCameraHaveing = true;
                } else if (resultCode == RESULT_CANCELED) {
                    isCameraHaveing = false;
                    //not ImageBitmap,set default img
                    btn_camera.setImageResource(R.drawable.ico_addphoto);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(Config.DEBUG, "Call onDestroy");
        if (btn_camera != null) {
            btn_camera = null;
        }
        if (bitmap != null) {
            bitmap = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //key is back
            onUserwantExit();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * check if User updete this page,
     * must remind User make true to Exit
     */
    private void onUserwantExit(){
        if (isCameraHaveing) {
            //open the Camera and haveing data
            View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout
                    .item_trueorfalse_dialog, null);
            ConfigPageClass configPageClass = new ConfigPageClass();
            ConfigPageClass.AlertViewIDpage alertViewIDpage = configPageClass
                    .getAlertViewIDpageInstance();
            TextView title = item.findViewById(R.id.item_trueorfalse_dialog_title);
            alertViewIDpage.setTitle(title);
            TextView content = item.findViewById(R.id.item_trueorfalse_dialog_content);
            alertViewIDpage.setContext(content);
            TextView btn_cancle = item.findViewById(R.id.item_trueorfalse_dialog_btnCancle);
            alertViewIDpage.setCancle(btn_cancle);
            TextView btn_confirm = item.findViewById(R.id
                    .item_trueorfalse_dialog_btnDetermine);
            alertViewIDpage.setConfirm(btn_confirm);
            Tools.showAlertDilg(item, UserCommentAct.this, "是否确定退出", "您已经有修改评论," +
                    "是否不保存该评论直接退出?", "确定退出", "我在看看", new Tools.AlertDilgClick() {

                @Override
                public void onConfirm(AlertDialog alertDialog) {
                    //don't do nothing
                    alertDialog.dismiss();
                }

                @Override
                public void onCancle(AlertDialog alertDialog) {
                    alertDialog.dismiss();
                    finish();
                }
            }, alertViewIDpage);
        }
        else{
            finish();
        }
    }
}
