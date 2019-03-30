package com.zbyj.Yazhou;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.LeftCompanyProgram.CompanyAct.LeftCompanyAct;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.USER_KEY_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyTools.Usertools;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.Factory.DialogFactory;
import com.zbyj.Yazhou.LeftCompanyProgram.Interface.ProgramInterface;
import com.zbyj.Yazhou.LeftCompanyProgram.JsonEndata;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 输入手机验证码界面
 */
public class EndlogCode extends LeftCompanyAct {
    private TextView btn_login, tv_sendStatus;
    private Handler handler;
    private int position = 60;
    private GradientDrawable btn_encodeDrawable;
    private LinearLayout inCodeBody;
    private Timer t;
    private boolean send = true;
    private int subi = 0;//用来添加输入框的监听position
    private ArrayList<EditText> inputCodeET = new ArrayList<EditText>();
    private String verificationCode = "";
    private String phone;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        setContentView(R.layout.activity_entlogincode);
        init();
        Listener();
    }

    private void Listener() {
        inputCodeET.clear();//清空原先有的数据信息

        /**
         * 执行一个动画
         */
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        ImageView ico_img = findViewById(R.id.activity_entlogincode_icoImg);
        tv_sendStatus = findViewById(R.id.activity_entlogincode_sendSmsStatus);
        rotateAnimation.setDuration(1500);
        ico_img.setAnimation(rotateAnimation);
        /**
         * 循环取出EditText添加监听
         *
         */
        for (int i = 0; i < 4; i++) {
            //取出EditText的父布局
            LinearLayout body = (LinearLayout) inCodeBody.getChildAt(i);
            EditText et = (EditText) body.getChildAt(0);
            inputCodeET.add(et);
        }
        inputETaddListener();
        btn_login.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                if (tv.getTag().equals("1")) {
                    //tv正在倒计时,不理会
                } else {
                    tv.setTag("1");
                    if (t != null) {

                    } else {
                        startCountDown();
                    }
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void init() {
        //获取界面传值
        phone = getBundlerValue(config.ACTIVITY_ACTION_PHONE);
        Tools.sendVerificationCodeSMS(getApplicationContext(), phone, new ProgramInterface
                .SMSInterface() {
            @Override
            public void onSendOk() {
                //发送短信验证码完成
                tv_sendStatus.setText("验证码已经发送至您的手机,请检查您的短信");
                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendError() {
                //发送短信验证码失败
                Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                tv_sendStatus.setText("网络异常,点击重新发送短信");

            }
        });
        //调用发送短信模块
        inCodeBody = findViewById(R.id.activity_entlogincode_inCodeBody);
        handler = new Handler() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        position -= 1;
                        if (position == 0) {
                            //线程停止
                            t.cancel();
                            //标题重写
                            btn_login.setText("再次发送");
                            btn_encodeDrawable.setColor(Color.parseColor(getResources().getString
                                    (R.color.TextAndBodyColor)));
                            t = null;
                            System.gc();
                            btn_login.setTag("0");//表示没有在获取验证码操作
                            position = 60;
                            //调用发送短信模块
                        } else {
                            btn_login.setText(position + "秒之后可以再次获取");
                            btn_encodeDrawable.setColor(Color.parseColor("#efefef"));
                        }

                        break;
                }
            }
        };
        btn_login = findViewById(R.id.activity_entlogincode_btnlogin);
        btn_encodeDrawable = (GradientDrawable) btn_login.getBackground();
        btn_login.setTag("0");//表示没有开始倒计时操作
        startCountDown();
    }

    /**
     * 倒计时
     */
    public void startCountDown() {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 500, 1200);
    }


    public void inputETaddListener() {
        //判断是不是第一个输入框
        Log.i(config.DEBUG_STR, "第" + subi + "添加焦点");
        inputCodeET.get(0).requestFocus();//第一个获取焦点
        inputCodeET.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    //为空  不操作
                } else {
                    //不为空 下一个监听获取焦点
                    inputCodeET.get(subi + 1).requestFocus();
                }

            }
        });
        //是不是最后一个输入框
        inputCodeET.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    //不为空 输入验证码
                    verificationCode = "";
                    for (int y = 0; y < inputCodeET.size(); y++) {
                        verificationCode += inputCodeET.get(y).getText().toString().trim();
                    }
                    //开始验证验证码
                    Tools.checkVerficationCode(getApplicationContext(), phone, verificationCode,
                            new ProgramInterface() {
                        @Override
                        public void onSucess(String data, int code) {
                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT)
                                    .show();
                            JsonEndata jsonEndata = new JsonEndata(data);
                            if (jsonEndata.getJsonKeyValue(Config.HttpMethodUserAction
                                    .KEY_STATUS).equals(Config.HttpMethodUserAction
                                    .STATUS_LOGINOK)) {
                                //登录成功  提示信息框  拉取用户的信息  获取token
                                final DialogFactory.RefreshDialog refreshDialog = new DialogFactory()
                                        .new RefreshDialog(EndlogCode.this);
                                refreshDialog.setDialogView(R.layout.item_wait);
                                refreshDialog.intenstDialogView(R.id.item_wait_img, R.id
                                        .item_wait_msg, "加载中", true);
                                //保存TOKEN
                                Tools.settoKen(getApplicationContext(), USER_KEY_PAGE.KEY_TOKEN,
                                        jsonEndata.getJsonKeyValue(USER_KEY_PAGE.KEY_TOKEN));
                                //保存用户的手机号
                                Tools.settoKen(getApplicationContext(), USER_KEY_PAGE
                                        .KEY_USERPHONE, phone);
                                Usertools.getUservalues(getApplicationContext(), new
                                        ProgramInterface() {
                                    @Override
                                    public void onSucess(String data, int code) {
                                        Log.i(Config.DEBUG, "" + data);
                                        JsonEndata json = new JsonEndata(data);
                                        if (json.getJsonKeyValue(Config.HttpMethodUserAction
                                                .KEY_STATUS).equals(Config.HttpMethodUserAction
                                                .STATUS_GETVALUES_OK)) {
                                            //登录获取数据成功 判断性别是否没有设置
                                            if(json.getJsonKeyValue(Config.JSON_USERPAGE.USER_SEX).equals("0")){
                                                //没有设置性别
                                                LeftCompanyActStartActivity(SelectSexAct.class,true);
                                            }
                                            else{
                                                //判断是否有地址 没有的话 强制性用户要添加一个地址信息
                                                Usertools.getUserdefaultaddr();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "获取数据失败",
                                                    Toast.LENGTH_SHORT).show();
                                            refreshDialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onFaile(String data, int code) {
                                        Toast.makeText(getApplicationContext(), "获取数据失败", Toast
                                                .LENGTH_SHORT).show();

                                    }
                                });


                            } else if (jsonEndata.getJsonKeyValue(Config.HttpMethodUserAction
                                    .KEY_STATUS).equals(Config.HttpMethodUserAction
                                    .STATUS_LOGCODE_TOMUCH)) {
                                //错误次数太多
                                Toast.makeText(getApplicationContext(), "错误次数太多,限制登录", Toast
                                        .LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "验证码错误", Toast
                                        .LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFaile(String data, int code) {

                        }
                    });
                } else {
                    inputCodeET.get(2).requestFocus();
                }
            }
        });

        inputCodeET.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    //不为空 下一个获取焦点
                    inputCodeET.get(3).requestFocus();
                } else {
                    inputCodeET.get(1).requestFocus();//不然就上一个编辑框获取焦点
                }
            }
        });
        inputCodeET.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    //不为空 下一个获取焦点
                    inputCodeET.get(2).requestFocus();
                } else {
                    inputCodeET.get(0).requestFocus();//不然就上一个编辑框获取焦点
                }
            }
        });

    }
}
