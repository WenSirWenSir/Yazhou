package com.zbyj.Yazhou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.LeftCompanyProgram.Factory.DialogFactory;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;

public class LoginAct extends YazhouActivity {
    private TextView btn_reg;
    private RelativeLayout headIco;
    private EditText activity_login_inputPhone;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        init();
    }

    private void init() {
        btn_reg = findViewById(R.id.activity_login_btnReg);
        headIco = findViewById(R.id.activity_login_headIco);
        activity_login_inputPhone = findViewById(R.id.activity_login_inputPhone);
        ViewHandle();
        Listener();
    }

    /**
     * 获取到的View的系统监听事件
     */
    private void Listener() {
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "font/main_number" + ".ttf");
        activity_login_inputPhone.setTypeface(typeface);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YaZhouStartActivityWithBundler(EndlogCode.class, true, config
                        .ACTIVITY_ACTION_PHONE, activity_login_inputPhone.getText().toString());
            }
        });
    }

    /**
     * 控件改变边框和颜色
     */
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    private void ViewHandle() {
        //注册登录边框
        GradientDrawable btn_regBackground = (GradientDrawable) btn_reg.getBackground();
        btn_regBackground.setStroke(0, R.color.TextAndBodyColor);
        btn_regBackground.setColor(Color.parseColor(getResources().getString(R.color
                .TextAndBodyColor)));

        //头部图标
        GradientDrawable headicoBackground = (GradientDrawable) headIco.getBackground();
        headicoBackground.setStroke(5, Color.parseColor("#e9e9e9"));
        headicoBackground.setColor(Color.parseColor("#e9e9e9"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
