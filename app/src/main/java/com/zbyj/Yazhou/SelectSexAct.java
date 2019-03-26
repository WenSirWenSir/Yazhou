package com.zbyj.Yazhou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.USER_KEY_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.Net;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;
import com.zbyj.Yazhou.ProgramAct.InputAddrAct;
import com.zbyj.Yazhou.LeftCompanyProgram.JsonEndata;
import com.zbyj.Yazhou.ProgramAct.UserAddHomeAddrAct;

public class SelectSexAct extends YazhouActivity {
    private RelativeLayout activity_select_sex_headbody;
    private TextView activity_select_btnconfirm;
    private LinearLayout btn_sir, btn_lady;//先生或者女士
    private ImageView img_sir, img_lady;
    private GradientDrawable btn_sirDrawable, btn_ladyDrawable = null;
    private int Sex = 0;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sex);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        init();
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    private void init() {
        activity_select_sex_headbody = findViewById(R.id.activity_select_sex_headbody);//头部的ico
        activity_select_btnconfirm = findViewById(R.id.activity_select_btnconfirm);//确定按钮
        btn_sir = findViewById(R.id.activity_select_sex_sir);//先生
        btn_lady = findViewById(R.id.activity_select_sex_lady);//女士
        img_lady = findViewById(R.id.activity_select_sex_ladyimg);
        img_sir = findViewById(R.id.activity_select_sex_sirimg);
        //开始布局
        GradientDrawable sex_headbodyDrawable = (GradientDrawable) activity_select_sex_headbody
                .getBackground();
        sex_headbodyDrawable.setStroke(5, Color.parseColor("#e9e9e9"));
        sex_headbodyDrawable.setColor(Color.parseColor("#e9e9e9"));
        //确定按钮布局
        GradientDrawable btn_confirm = (GradientDrawable) activity_select_btnconfirm
                .getBackground();
        btn_confirm.setStroke(0, R.color.TextAndBodyColor);
        btn_confirm.setColor(Color.parseColor(getResources().getString(R.color.TextAndBodyColor)));
        Listener();
    }

    public void Listener() {
        /**
         * 男士按钮被点击
         */
        img_sir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //被点击了 背景为显示
                Sex = USER_KEY_PAGE.KEY_SIR;
                btn_sir.setVisibility(View.VISIBLE);
                btn_lady.setVisibility(View.GONE);
                if (btn_sirDrawable != null) {
                    btn_sirDrawable.setStroke(5, Color.BLACK);
                    btn_sirDrawable.setColor(Color.parseColor(getResources().getString(R.color
                            .TextAndBodyColor)));
                } else {
                    btn_sirDrawable = (GradientDrawable) btn_sir.getBackground();
                    btn_sirDrawable.setStroke(5, Color.BLACK);
                    btn_sirDrawable.setColor(Color.parseColor(getResources().getString(R.color
                            .TextAndBodyColor)));

                }


            }
        });

        /**
         * 女士按钮被点击
         */
        img_lady.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Sex = USER_KEY_PAGE.KEY_LADY;
                btn_sir.setVisibility(View.GONE);
                btn_lady.setVisibility(View.VISIBLE);
                if (btn_ladyDrawable != null) {
                    btn_ladyDrawable.setStroke(5, Color.BLACK);
                    btn_ladyDrawable.setColor(Color.parseColor(getResources().getString(R.color
                            .TextAndBodyColor)));
                } else {
                    btn_ladyDrawable = (GradientDrawable) btn_lady.getBackground();
                    btn_ladyDrawable.setStroke(5, Color.BLACK);
                    btn_ladyDrawable.setColor(Color.parseColor(getResources().getString(R.color
                            .TextAndBodyColor)));

                }

            }
        });
        activity_select_btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = tools.gettoKen(getApplicationContext(), USER_KEY_PAGE
                        .KEY_PHONE);
                Log.i(Config.DEBUG, phone);
                final String token = tools.gettoKen(getApplicationContext(), USER_KEY_PAGE
                        .KEY_TOKEN);
                //token
                Log.i(Config.DEBUG, "md5加密:" + phone);
                if (Sex == 0) {
                    Toast.makeText(getApplicationContext(), "您还没有选择您喜欢的物品呢", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    //更新用户的性别
                    Net.doGet(getApplicationContext(), Config.HTTP_ADDR.updateSex(),
                            new Net.onVisitInterServiceListener() {
                        @Override
                        public void onSucess(String tOrgin) {
                            JsonEndata jsonEndata = new JsonEndata(tOrgin);
                            Log.i(Config.DEBUG, tOrgin);
                            if (jsonEndata.getJsonKeyValue(Config.HttpMethodUserAction
                                    .KEY_STATUS).equals(Config.HttpMethodUserAction
                                    .STATUS_SELECT_SEX_OK)) {
                                //第一次登录 设置用户的性别成功  去访问是否有默认收件的地址 如果没有 就让用户去添加一个收件地址
                                Net.doGet(getApplicationContext(), Config.HTTP_ADDR
                                        .getUser_init(), new Net.onVisitInterServiceListener() {
                                    @Override
                                    public void onSucess(String tOrgin) {
                                        JsonEndata json = new JsonEndata(tOrgin);

                                    }

                                    @Override
                                    public void onNotConnect() {

                                    }

                                    @Override
                                    public void onFail(String tOrgin) {

                                    }
                                }, Config.HttpMethodUserAction.KEY_ACTION, Config
                                        .HttpMethodUserAction.GET_DEFAULT_ADDR, Config
                                        .HttpMethodUserAction.KEY_USER, Tools.getStringMD5(phone)
                                        , Config.HttpMethodUserAction.KEY_TOKEN, token);
                                Toast.makeText(getApplicationContext(), R.string
                                        .UPDATE_USERSEX_SUCESS, Toast.LENGTH_SHORT).show();
                                //已经设定成功 就提示用户是否去设置地址
                                final AlertDialog alertDialog = new AlertDialog.Builder
                                        (SelectSexAct.this).create();
                                View item = LayoutInflater.from(SelectSexAct.this).inflate(R
                                        .layout.item_trueorfalse_dialog, null);
                                alertDialog.setView(item);
                                TextView title = item.findViewById(R.id
                                        .item_trueorfalse_dialog_title);
                                title.setText("提示");
                                TextView content = item.findViewById(R.id
                                        .item_trueorfalse_dialog_content);
                                content.setText(R.string.MESSAGE_LOGIN_USERFIRST);
                                TextView btn_cancle = item.findViewById(R.id
                                        .item_trueorfalse_dialog_btnCancle);
                                btn_cancle.setText("不了,下一次");
                                btn_cancle.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        finish();
                                    }
                                });
                                TextView btn_confirm = item.findViewById(R.id
                                        .item_trueorfalse_dialog_btnDetermine);
                                if (Sex == 2) {
                                    //是女生
                                    btn_confirm.setText("带本娘娘走");

                                } else {
                                    btn_confirm.setText("带朕走");
                                }
                                btn_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //打开地图模块
                                        YaZhouStartActivity(UserAddHomeAddrAct.class, true);
                                    }
                                });
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onNotConnect() {

                        }

                        @Override
                        public void onFail(String tOrgin) {
                            Toast.makeText(getApplicationContext(), R.string.INTER_ERROR, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }, Config.HttpMethodUserAction.KEY_ACTION, Config.HttpMethodUserAction
                                    .UPDATE_USER_SEX, Config.HttpMethodUserAction.KEY_USER, Tools
                                    .getStringMD5(phone), Config.HttpMethodUserAction.KEY_TOKEN,
                            token, Config.HttpMethodUserAction.KEY_UPDATESEX, String.valueOf(Sex));
                }
            }
        });
    }
}
