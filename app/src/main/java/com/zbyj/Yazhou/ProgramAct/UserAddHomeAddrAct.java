package com.zbyj.Yazhou.ProgramAct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.ConfigPageValue.MAP;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.USER_KEY_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.WindowPage;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.XML_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyTools.Usertools;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyTools.XmlBuilder;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.LeftCompanyProgram.Interface.ProgramInterface;
import com.zbyj.Yazhou.LeftCompanyProgram.JsonEndata;
import com.zbyj.Yazhou.LeftCompanyProgram.Net;
import com.zbyj.Yazhou.LeftCompanyProgram.Tools;
import com.zbyj.Yazhou.LoginAct;
import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.YazhouActivity;
import com.zbyj.Yazhou.config;

import java.util.ArrayList;


/**
 * 用户新增收件地址
 */
public class UserAddHomeAddrAct extends YazhouActivity {
    private ImageView btn_back, btn_openmap;
    private TextView addrString;
    private EditText edit_name, edit_phone, edit_street;
    private int Sex;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useraddhome_addr);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        hideBottomUIMenu();//隐藏虚拟键盘
        init();
    }

    @SuppressLint({"ResourceType", "NewApi"})
    private void init() {
        findViewById(R.id.activity_useraddhome_addr_btnsetSir).setBackground(Tools
                .setBackgroundType(0, getResources().getString(R.color.TextAndBodyColor),
                        getResources().getString(R.color.TextAndBodyColor), 0));
        //判断界面是否传值
        btn_back = findViewById(R.id.activity_useraddhome_addr_btnback);
        btn_openmap = findViewById(R.id.activity_useraddhome_addr_btnopenmap);
        addrString = findViewById(R.id.activity_useraddhome_addrString);//用户的所属位置
        edit_name = findViewById(R.id.activity_useraddhome_addr_editName);//用户名
        edit_phone = findViewById(R.id.activity_useraddhome_addr_editPhone);//用户的手机号码
        edit_street = findViewById(R.id.activity_useraddhome_addr_editStreet);//用户的街道地址
        if (!getBundlerValue(WindowPage.ACTION_USER_NAME).equals("")) {
            //存在界面传值数据
            edit_name.setText(getBundlerValue(WindowPage.ACTION_USER_NAME));
        }
        if (!getBundlerValue(WindowPage.ACTION_USER_TEL).equals("")) {
            edit_phone.setText(getBundlerValue(WindowPage.ACTION_USER_TEL));
        }
        if (!getBundlerValue(WindowPage.ACTION_ADDR).equals("")) {
            edit_street.setText(getBundlerValue(WindowPage.ACTION_ADDR));
        }
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
        /**
         * 选择男士的监听
         */
        findViewById(R.id.activity_useraddhome_addr_btnsetSir).setOnClickListener(new View
                .OnClickListener() {


            @SuppressLint({"ResourceType", "NewApi"})
            @Override
            public void onClick(View v) {
                Sex = USER_KEY_PAGE.KEY_SIR;
                v.setBackground(Tools.setBackgroundType(0, getResources().getString(R.color
                        .TextAndBodyColor), getResources().getString(R.color.TextAndBodyColor), 0));
                findViewById(R.id.activity_useraddhome_addr_btnsetLady).setBackgroundColor(Color
                        .WHITE);

            }
        });
        /**
         * 选择女士的监听
         */
        findViewById(R.id.activity_useraddhome_addr_btnsetLady).setOnClickListener(new View
                .OnClickListener() {
            @SuppressLint({"NewApi", "ResourceType"})
            @Override
            public void onClick(View v) {
                Sex = USER_KEY_PAGE.KEY_LADY;
                v.setBackground(Tools.setBackgroundType(0, getResources().getString(R.color
                        .TextAndBodyColor), getResources().getString(R.color.TextAndBodyColor), 0));
                findViewById(R.id.activity_useraddhome_addr_btnsetSir).setBackgroundColor(Color
                        .WHITE);

            }
        });

        /**
         * 保存数据
         */
        findViewById(R.id.activity_useraddhome_addr_btnSave).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断信息是否为空
                String addr_in = addrString.getText().toString().trim();//获取用户的所属区域
                String user = edit_name.getText().toString().trim();//用户名
                String phone = edit_phone.getText().toString().trim();//用户的电话号码
                String addr = edit_street.getText().toString().trim();//用户的详细地址


                //判断是否为空
                if (!TextUtils.isEmpty(addr_in) && !TextUtils.isEmpty(user) && !TextUtils.isEmpty
                        (phone) && !TextUtils.isEmpty(addr)) {
                    Log.e(Config.DEBUG, "用户名" + user + "电话" + phone + "所属区域" + addr_in + "街道地址" +
                            addr);

                    //数据提交 获取性别物理地址
                    String physics_add = "123,2344";
                    String year = "23";//年龄
                    String _default = "1";//是否默认地址
                    String _phone = Tools.gettoKen(getApplicationContext(), USER_KEY_PAGE
                            .KEY_PHONE);
                    String token = Tools.gettoKen(getApplicationContext(), USER_KEY_PAGE.KEY_TOKEN);
                    if (!TextUtils.isEmpty(_phone) && !TextUtils.isEmpty(token)) {
                        //不为空
                        //构造XML数据信息
                        XmlBuilder xmlBuilder = new XmlBuilder("body");
                        XML_PAGE xml_page = new XML_PAGE("", "", "");
                        xml_page.addGrandsonNode(Config.HttpMethodUserAction.KEY_USER, Tools
                                .getStringMD5(_phone)).addGrandsonNode(Config
                                .HttpMethodUserAction.KEY_TOKEN, token).addGrandsonNode(Config
                                .HttpMethodUserAction.KEY_ADDR_NAME, user).addGrandsonNode(Config
                                .HttpMethodUserAction.KEY_ADDR_TEL, phone).addGrandsonNode(Config
                                .HttpMethodUserAction.KEY_ADDR_ADDR, addr).addGrandsonNode(Config
                                .HttpMethodUserAction.KEY_ADDR_IN, addr_in).addGrandsonNode
                                (Config.HttpMethodUserAction.KEY_ADDR_PHYSICS, physics_add)
                                .addGrandsonNode(Config.HttpMethodUserAction.KEY_ADDR_DEFAULT,
                                        _default).addGrandsonNode(Config.HttpMethodUserAction
                                .KEY_ACTION, Config.HttpMethodUserAction.INSERT_USER_ADDR)
                                .addGrandsonNode(Config.HttpMethodUserAction.KEY_ADDR_USER_SEX,
                                        Sex + "").addGrandsonNode(Config.HttpMethodUserAction
                                .KEY_ADDR_USER_YEAR, "23");
                        ArrayList<XML_PAGE> list = new ArrayList<XML_PAGE>();
                        list.add(xml_page);
                        Net.doPostXml(getApplicationContext(), xmlBuilder.getXmlString(list),
                                Config.HTTP_ADDR.getUser_init(), new ProgramInterface() {
                            @Override
                            public void onSucess(String data, int code) {
                                Log.i(Config.DEBUG, "xml数据返回" + data.toString());
                            }

                            @Override
                            public void onFaile(String data, int code) {

                            }
                        });


                    } else {
                        //没有登录
                        YaZhouStartActivity(LoginAct.class, true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "数据信息填写不完整", Toast.LENGTH_SHORT).show();
                }
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
