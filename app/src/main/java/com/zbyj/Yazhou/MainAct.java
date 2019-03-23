package com.zbyj.Yazhou;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baidu.mapapi.SDKInitializer;
import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.USER_KEY_PAGE;
import com.zbyj.Yazhou.LeftCompanyProgram.Config;
import com.zbyj.Yazhou.ProgramFrame.MainFrame;
import com.zbyj.Yazhou.ProgramFrame.OrderListFrame;
import com.zbyj.Yazhou.ProgramFrame.UserPageFrame;
import com.zbyj.Yazhou.Utils.NotificationUtils;

import java.util.ArrayList;
import java.util.Timer;

public class MainAct extends YazhouActivity {
    private NotificationManager notificationManager;
    private FragmentManager fragmentTransaction;
    private Timer Reversetimer, PriceAni;
    private ImageView btn_Order, btn_Orderlist, btn_OrderUserpage;
    private TextView btn_OrderTitle,btn_OrderlistTitle,btn_OrderUserpageTitle;
    private ArrayList<TextView> btn_title = new ArrayList<TextView>();
    private MainFrame mainFrame = null;
    private OrderListFrame orderListFrame = null;
    private UserPageFrame userPageFrame = null;
    private String price;

    @SuppressLint({"ResourceType", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        setBackStatic(true);
        SDKInitializer.initialize(getApplicationContext());
        Toast.makeText(getApplicationContext(), Config.JSON_USERPAGE.USER_LEFTCOMPANY_ID, Toast
                .LENGTH_SHORT).show();
        hideBottomUIMenu();
        /**
         * 检查是否有虚拟键盘
         */
        init();
        checkGetPermission();//判断是否授权
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    private void init() {
        fragmentTransaction = getFragmentManager();
        btn_Order = findViewById(R.id.activity_main_btnOrder);
        btn_OrderTitle = findViewById(R.id.activity_main_btnOrder_Title);//用户的主界面标题
        btn_title.add(btn_OrderTitle);
        btn_Orderlist = findViewById(R.id.activity_main_btnOrderlist);
        btn_OrderlistTitle = findViewById(R.id.activity_main_btnOrderlist_Title);//用户的订单界面标题
        btn_title.add(btn_OrderlistTitle);
        btn_OrderUserpage = findViewById(R.id.activity_main_btnUserpage);
        btn_OrderUserpageTitle = findViewById(R.id.activity_main_btnUserpage_Title);//用户的界面标题
        btn_title.add(btn_OrderUserpageTitle);
        //用来标识用户是否登录的父布局
        if (tools.gettoKen(getApplicationContext(), USER_KEY_PAGE.KEY_TOKEN).equals("")) {
            Toast.makeText(getApplicationContext(), "您还没有登录哦,请你登录之后使用", Toast.LENGTH_LONG).show();
            //设置预定为登录按钮
        } else {
            //已经登录  就去获取用户的地址首页地址信息
        }
        Listener();
        Visit();

    }

    /**
     * 不选择退出 给你朵小发发
     */
    private void OutputFlower() {
    }

    /**
     * 用户请求退出
     */
    private void onUserExit() {
    }

    private void Visit() {
        //访问服务器获取需要的数据信息
    }

    /**
     * 发送一个状态栏通知 用来供客户查看交易订单和物流信息
     */
    private void showNotification(Class<?> into, int showImg, String showText) {
        if (NotificationUtils.isNotificationeGet(getApplicationContext())) {
            //已经获取到权限 就发送一个状态栏通知信息
            notificationManager = tools.showStatueBarNotify(getApplicationContext(), into,
                    showImg, showText);
        } else {
            NotificationUtils.toGetNotificationService(MainAct.this);//没有获取到通知的权限就让模块提示用户必须先获取到权限
        }
    }

    private void Listener() {
        /**
         * 界面切换监听
         */
        selectFrame(config.FRAMELAYOUT_ORDER);

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim
                .android_anim_big_to_smail);
        btn_Order.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //开始动画
                btn_Order.startAnimation(animation);
                selectFrame(config.FRAMELAYOUT_ORDER);
                //更新标题颜色
                for (int i = 0;i < btn_title.size();i++){
                    btn_title.get(i).setTextColor(Color.BLACK);
                }
                btn_OrderTitle.setTextColor(Color.parseColor(getResources().getString(R.color.btn_titlecolor)));
            }
        });
        btn_Orderlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                selectFrame(config.FRAMELAYOUT_ORDERLIST);
                btn_Orderlist.startAnimation(animation);
                //更新标题颜色
                for (int i = 0;i < btn_title.size();i++){
                    btn_title.get(i).setTextColor(Color.BLACK);
                }
                btn_OrderlistTitle.setTextColor(Color.parseColor(getResources().getString(R.color.btn_titlecolor)));

            }
        });

        btn_OrderUserpage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                selectFrame(config.FRAMELAYOUT_USERPAGE);
                btn_OrderUserpage.startAnimation(animation);
                //更新标题颜色
                for (int i = 0;i < btn_title.size();i++){
                    btn_title.get(i).setTextColor(Color.BLACK);
                }
                btn_OrderUserpageTitle.setTextColor(Color.parseColor(getResources().getString(R.color.btn_titlecolor)));

            }
        });
    }

    private void selectFrame(int position) {
        FragmentTransaction ft = fragmentTransaction.beginTransaction();
        hideFrame(ft);//先隐藏全部的Frament
        switch (position) {
            case config.FRAMELAYOUT_ORDER:
                if (mainFrame != null) {
                    ft.show(mainFrame);
                } else {
                    mainFrame = new MainFrame();
                    ft.add(R.id.activity_main_Frame, mainFrame);
                }
                break;
            case config.FRAMELAYOUT_ORDERLIST:
                if (orderListFrame != null) {
                    ft.show(orderListFrame);
                } else {
                    orderListFrame = new OrderListFrame();
                    ft.add(R.id.activity_main_Frame, orderListFrame);
                }
                break;
            case config.FRAMELAYOUT_USERPAGE:
                if (userPageFrame != null) {
                    ft.show(userPageFrame);
                } else {
                    userPageFrame = new UserPageFrame();
                    ft.add(R.id.activity_main_Frame, userPageFrame);
                }
                break;
        }
        ft.commit();
    }

    private void hideFrame(FragmentTransaction ft) {
        if (mainFrame != null) {
            ft.hide(mainFrame);
        }
        if (orderListFrame != null) {
            ft.hide(orderListFrame);
        }
        if (userPageFrame != null) {
            ft.hide(userPageFrame);
        }
    }


    @Override
    protected void onRestart() {
        Log.i("capitalist", "onRestart");
        showNotification(LoginAct.class, R.drawable.ico_ok, "您的订单已经成功送达哦,您可以点击评价");
        //重新获取焦点的话 要把倒计时的线程重新启动
        hideBottomUIMenu();//隐藏导航
        super.onRestart();
    }

    @Override
    protected void onResume() {
        hideBottomUIMenu();//隐藏导航
        Log.i("capitalist", "onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        //   Reversetimer.cancel();//倒计时停止
        //   Reversetimer = null;
        //   System.gc();//调用系统进行回收
        if (PriceAni != null) PriceAni.cancel();
        PriceAni = null;
        System.gc();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (notificationManager != null) notificationManager.cancel(0);
        if (Reversetimer != null) Reversetimer.cancel();//倒计时停止
        if (PriceAni != null) PriceAni.cancel();
        PriceAni = null;
        Reversetimer = null;
        System.gc();//调用系统进行回收
        super.onDestroy();
    }


    /**
     * 判断应用是否获取了权限
     *
     * @return
     */
    @SuppressLint("NewApi")
    public void checkGetPermission() {
        //判断是否有读取手机状态码权限
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager
                .PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "授权", Toast.LENGTH_SHORT).show();//应用授权
        } else {
            Toast.makeText(getApplicationContext(), "没有授权", Toast.LENGTH_SHORT).show();
            //没有授权 弹出对话框 交代授权的
            final AlertDialog builder = new AlertDialog.Builder(MainAct.this).create();
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout
                    .item_trueorfalse_dialog, null);
            builder.setView(view);
            TextView title = view.findViewById(R.id.item_trueorfalse_dialog_title);
            title.setText("重要提示");
            TextView context = view.findViewById(R.id.item_trueorfalse_dialog_content);
            context.setText("我们需要获取您的手机唯一识别号用来标识您的注册/登录信息,如果您没有授权.您的登录可能会有问题,请你点击授权给予押粥权限.");
            TextView btn_cancle = view.findViewById(R.id.item_trueorfalse_dialog_btnCancle);
            btn_cancle.setText("残忍拒绝");
            builder.setCancelable(true);
            btn_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();//销毁界面
                }
            });
            TextView btn_determine = view.findViewById(R.id.item_trueorfalse_dialog_btnDetermine);
            btn_determine.setText("去授权");
            btn_determine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用户同意授权 就提示授权窗口

                }
            });
            builder.show();
        }
    }

}
