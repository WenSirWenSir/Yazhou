package com.zbyj.Yazhou;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.LeftCompanyProgram.CompanyAct.LeftCompanyAct;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.USER_KEY_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.ConfigPageClass;
import com.zbyj.Yazhou.LeftCompanyProgram.Factory.DialogFactory;
import com.zbyj.Yazhou.LeftCompanyProgram.Net;
import com.zbyj.Yazhou.LeftCompanyProgram.JsonEndata;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends LeftCompanyAct {
    private Handler h;
    private Timer onUseronclick;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        hideBottomUIMenu();
        init();
    }

    /**
     * 处理界面的动画和跳转的事件
     */
    @SuppressLint("HandlerLeak")
    private void init() {
        Log.i(config.DEBUG_STR, "token" + tools.gettoKen(getApplicationContext(), USER_KEY_PAGE
                .KEY_TOKEN));
        if (tools.isIntentConnect(getApplicationContext())) {
            Log.i(config.DEBUG_STR, "网络连接中");
        } else {
            Log.i(config.DEBUG_STR, "网络连接失败");
        }

        if (tools.isIntentConnect(getApplicationContext())) {
            //判断是是否需要更新软件信息  和  是否需要展示图片信息
            Net.doGet(getApplicationContext(), config.getServiceProgramMainConfig(), new Net
                    .onVisitInterServiceListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onSucess(String tOrgin) {
                    JsonEndata jsonEndata = new JsonEndata(tOrgin);
                    if (jsonEndata.getJsonKeyValue(config.WEB_SERVICE_PROGRAM_VERSION).equals
                            ("1.0")) {
                        Toast.makeText(getApplicationContext(), "需要更新", Toast.LENGTH_LONG).show();
                    } else if (!jsonEndata.getJsonKeyValue(config.WEB_SERVICE_PROGRAM_SHOWIMG)
                            .equals("是")) {
                        setStatusBar("#ffffff");
                        Toast.makeText(getApplicationContext(), "要求展示图片,图片地址:" + jsonEndata
                                .getJsonKeyValue(config.WEB_SERVICE_PROGRAM_IMG), Toast
                                .LENGTH_LONG).show();
                        LinearLayout activity_welcome_body = findViewById(R.id
                                .activity_welcome_body);
                        activity_welcome_body.removeAllViews();//移除所有的View
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout
                                .item_welcome_showimg, null);
                        //找到跳过文字 处理监听事件
                        TextView btn_back = view.findViewById(R.id
                                .activity_welcome_showimg_btnback);
                        GradientDrawable btn_backDrawable = (GradientDrawable) btn_back
                                .getBackground();
                        btn_backDrawable.setColor(Color.parseColor(getResources().getString(R
                                .color.TextAndBodyColor)));
                        btn_backDrawable.setStroke(3, Color.parseColor(getResources().getString(R
                                .color.TextAndBodyColor)));
                        btn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //用户主动取消广告
                                if (onUseronclick != null) {
                                    onUseronclick.cancel();
                                    LeftCompanyActStartActivity(MainAct.class, true);
                                }
                            }
                        });
                        onUseronclick = new Timer();
                        onUseronclick.schedule(new TimerTask() {
                            @Override
                            public void run() {


                                LeftCompanyActStartActivity(MainAct.class, true);
                            }
                        }, 3400);
                        activity_welcome_body.addView(view);
                    } else {
                        /**
                         * 用户没有点击取消的时候的的线程监听器
                         */
                        setStatusBar("#ffffff");//设置为白色的状态条
                        onUseronclick = new Timer();
                        onUseronclick.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                LeftCompanyActStartActivity(MainAct.class, true);
                            }
                        }, 3400);
                    }
                }

                @Override
                public void onNotConnect() {
                    Toast.makeText(getApplicationContext(), "没有网络信息:", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFail(String tOrgin) {
                    Toast.makeText(getApplicationContext(), "网络数据访问错误", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "无网络连接,请检查您的手机是否连网", Toast.LENGTH_LONG).show();
        }

    }
}
