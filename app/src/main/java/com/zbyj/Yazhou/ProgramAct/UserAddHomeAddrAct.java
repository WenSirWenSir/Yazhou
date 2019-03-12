package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.ConfigPageValue.MAP;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;
import com.zbyj.Yazhou.config;


/**
 * 用户新增收件地址
 */
public class UserAddHomeAddrAct extends YazhouActivity {
    private ImageView btn_back, btn_openmap;
    private TextView addrString;
    private EditText edit_name, edit_phone, edit_street;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useraddhome_addr);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        hideBottomUIMenu();//隐藏虚拟键盘
        init();
    }

    private void init() {
        btn_back = findViewById(R.id.activity_useraddhome_addr_btnback);
        btn_openmap = findViewById(R.id.activity_useraddhome_addr_btnopenmap);
        addrString = findViewById(R.id.activity_useraddhome_addrString);//用户的大体位置
        edit_name = findViewById(R.id.activity_useraddhome_addr_editName);//用户名
        edit_phone = findViewById(R.id.activity_useraddhome_addr_editPhone);//用户的地址
        edit_street = findViewById(R.id.activity_useraddhome_addr_editStreet);//用户的街道地址
        Listener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Listener() {
        edit_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText et = (EditText) v;
                ((EditText) v).setText("");
                ((EditText) v).setTextColor(Color.BLACK);
                return false;
            }
        });
        edit_street.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText et = (EditText) v;
                ((EditText) v).setText("");
                ((EditText) v).setTextColor(Color.BLACK);
                return false;
            }
        });
        edit_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText et = (EditText) v;
                ((EditText) v).setText("");
                ((EditText) v).setTextColor(Color.BLACK);
                return false;
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YazhouStartActivityForResult(InputAddrAct.class, false, MAP.SET_USERADDR_SUCESS);
            }
        });
    }

    /**
     * 设置地图回调之后的监听
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(config.DEBUG_STR, "调用成功");
        try {
            if (resultCode == MAP.SET_USERADDR_SUCESS) {
                String addr = data.getStringExtra(MAP.GET_USERADDR_ONSUCESS);
                if (addrString != null) {
                    addrString.setText(addr);
                    addrString.setTextColor(Color.BLACK);//设置黑色
                }
            }
        } catch (Exception e) {
            Log.e(config.DEBUG_STR, "没有回传数据信息");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
