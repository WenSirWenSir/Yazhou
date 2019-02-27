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
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.ConfigPageValue.USER_KEY_PAGE;
import com.zbyj.Yazhou.Factory.LoginActFactory;
import com.zbyj.Yazhou.Interface.UserInterface;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.Utils.JsonEndata;
import com.zbyj.Yazhou.YazhouActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 输入手机验证码界面
 */
public class EndlogCode extends YazhouActivity {
    private TextView btn_login;
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
         * 循环取出EditText添加监听
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
        tools.sendVerificationCodeSMS(getApplicationContext(), phone);
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
                            btn_encodeDrawable.setColor(Color.parseColor(getResources().getString(R.color.TextAndBodyColor)));
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
                    LoginActFactory.isCodecorrect(getApplicationContext(), tools.getStringMD5(phone), verificationCode, new UserInterface.userCheckVerificationOndone() {
                        /**
                         * 系统处理完用户提交的验证码和手机信息
                         *
                         * @param jsonEndata
                         */
                        @Override
                        public void ondone(JsonEndata jsonEndata) {
                            String status = jsonEndata.getJsonKeyValue(config.USER_LOGIN_CHECK_STATUS);//获取登录状态
                            if (status.equals(config.USER_LOGIN_SUCESS)) {
                                //保存用户的数据信息
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.MESSAGE_LOGIN_SUCESS), Toast.LENGTH_SHORT).show();
                                tools.settoKen(getApplicationContext(), config.SAVE_LOCAL_USERPHONE, phone);//设置手机号
                                tools.settoKen(getApplicationContext(), USER_KEY_PAGE.KEY_TOKEN, jsonEndata.getJsonKeyValue(USER_KEY_PAGE.KEY_TOKEN));//设置token
                                tools.settoKen(getApplicationContext(),USER_KEY_PAGE.KEY_SEX,jsonEndata.getJsonKeyValue(USER_KEY_PAGE.KEY_SEX));//设置性别

                                //判断用户的性别是不是为空 如果为空就要跳出设置窗口设置用户的性别
                                if (TextUtils.isEmpty(jsonEndata.getJsonKeyValue(USER_KEY_PAGE.KEY_SEX))) {
                                    //是空为第一次注册
                                    YaZhouStartActivity(SelectSexAct.class, true);
                                } else {
                                    //用户不是第一次登陆就直接跳转到主界面
                                    YaZhouStartActivity(MainAct.class,true);

                                }
                            } else if (status.equals(config.USER_LOGIN_FAIL)) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.MESSAGE_USER_LOGIN_FAIL), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.MESSAGE_LOGIN_VERIFICATION_ERROR), Toast.LENGTH_SHORT).show();
                            }
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
