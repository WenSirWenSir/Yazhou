package com.zbyj.Yazhou;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.zbyj.Yazhou.ConNet.SystemVisitInterService;
import com.zbyj.Yazhou.ConfigPageValue.MAP;
import com.zbyj.Yazhou.ConfigPageValue.USER_KEY_PAGE;
import com.zbyj.Yazhou.ProgramAct.InputAddrAct;
import com.zbyj.Yazhou.ProgramAct.UserAddHomeAddr;
import com.zbyj.Yazhou.ProgramAct.UserComment;
import com.zbyj.Yazhou.Utils.JsonEndata;
import com.zbyj.Yazhou.Utils.NotificationUtils;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import localInterface.ScrollViewListener;
import localViews.RefreshScrollView;

public class MainAct extends YazhouActivity implements ScrollViewListener {
    private VideoView videoView;
    private RefreshScrollView scrollView;
    private LinearLayout headView, mainShoplistBody, activity_mainloginstatusBody;
    private NotificationManager notificationManager;
    private RelativeLayout btn_order;
    private Handler handler;
    private TextView headStatue, btn_orderTitle, activity_main_reservetime, activity_main_price;
    private Timer Reversetimer, PriceAni;
    private String price;

    @SuppressLint({"ResourceType", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setStatusBar(getResources().getString(R.color.TextAndBodyColor));
        setBackStatic(true);
        //hideBottomUIMenu();

        /**
         * 检查是否有虚拟键盘
         */
        init();
        checkGetPermission();//判断是否授权
        /**
         * 主线程 用来倒计时等操作
         */
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case config.CALC_RESERVE_TIME:
                        //倒计时操作
                        int m = Integer.parseInt(activity_main_reservetime.getText().toString().split("\\:")[2]);
                        int s = Integer.parseInt(activity_main_reservetime.getText().toString().split("\\:")[1]);
                        int h = Integer.parseInt(activity_main_reservetime.getText().toString().split("\\:")[0]);
                        if (m == 0) {
                            Log.i(config.DEBUG_STR, "现在秒:" + m);
                            //这边日期是0的话  就要判断分钟是不是为0  如果分钟为0  小时要减1
                            if (s == 0) {
                                s = 59;//变成59分钟
                                m = 59;
                                if (h == 0) {
                                    //小时如果为0  就不要改变了
                                } else {
                                    h -= 1;//小时去减1
                                }
                            } else {
                                //如果分钟不是为0的话
                                m = 59;//秒变为59
                                s -= 1;//分钟减去1
                            }


                        } else {
                            //如果秒不为0  那么 秒就减去1
                            m -= 1;
                        }
                        activity_main_reservetime.setText(h + ":" + s + ":" + m);
                        break;
                    case config.CALC_PRICE_TIME:
                        //$4.0
                        try {
                            double Viewprice = Double.parseDouble(activity_main_price.getText().toString());
                            double position = Double.parseDouble(msg.obj.toString());
                            BigDecimal db = new BigDecimal(Viewprice + 0.1);
                            if (Viewprice < position) {
                                activity_main_price.setText(String.valueOf(db.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                            } else {
                                PriceAni.cancel();//线程停止
                                PriceAni = null;
                                System.gc();//系统进行回收
                                activity_main_price.setText("$" + position);
                            }

                        } catch (Exception e) {
                            Log.e(config.DEBUG_STR, "速度太快");
                        }

                        break;
                }
                super.handleMessage(msg);
            }
        };
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    private void init() {
        //判断用户是否登录了
        activity_main_price = findViewById(R.id.activity_main_price);
        activity_main_reservetime = findViewById(R.id.activity_main_reservetime);
        activity_mainloginstatusBody = findViewById(R.id.activity_mainloginstatusBody);//用来标识用户是否登录的父布局
        scrollView = findViewById(R.id.main_RefreshScrollView);//自定义滑动组件
        headView = findViewById(R.id.main_RefreshHeadView);//头部控件
        headStatue = findViewById(R.id.main_RefreshStatue);//状态标题
        btn_order = findViewById(R.id.btn_order);//订购的边框
        btn_orderTitle = findViewById(R.id.btn_orderTitle);//订购的标题
        mainShoplistBody = findViewById(R.id.main_shoplistBody);//显示商品的详情列表
        //设置边框和填充颜色
        GradientDrawable btn_orderDrawable = (GradientDrawable) btn_order.getBackground();
        btn_orderDrawable.setColor(Color.parseColor("#ffffff"));
        btn_orderDrawable.setStroke(10, Color.BLACK);
        //设置边框和填充颜色
        GradientDrawable btn_titleDrawable = (GradientDrawable) btn_orderTitle.getBackground();
        btn_titleDrawable.setColor(Color.parseColor(this.getResources().getString(R.color.TextAndBodyColor)));
        btn_titleDrawable.setStroke(1, Color.parseColor(this.getResources().getString(R.color.TextAndBodyColor)));
        //设置边框和填充颜色
        GradientDrawable mainShoplistDrawable = (GradientDrawable) mainShoplistBody.getBackground();
        mainShoplistDrawable.setColor(Color.parseColor("#ffffff"));
        mainShoplistDrawable.setStroke(2, Color.parseColor("#ffffff"));
        scrollView.setListener(this);
        scrollView.setHeadView(headView);
        if (tools.gettoKen(getApplicationContext(), USER_KEY_PAGE.KEY_TOKEN).equals("")) {
            Toast.makeText(getApplicationContext(), "您还没有登录哦,请你登录之后使用", Toast.LENGTH_LONG).show();
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_main_nologin, null);
            activity_mainloginstatusBody.removeAllViews();
            activity_mainloginstatusBody.addView(view);
            //设置预定为登录按钮
            btn_orderTitle.setText("登录");
        } else {
            //登录之后判断是否已经有了订单记录
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_main_showuaddr, null);
            activity_mainloginstatusBody.removeAllViews();
            activity_mainloginstatusBody.addView(view);
            //设置按钮为预定
            btn_orderTitle.setText("预定");

            /**
             * 开始登陆成功的按钮监听
             */
            ImageView check_homeaddr = view.findViewById(R.id.item_main_showuaddr_img);
            RelativeLayout btn_userexit = view.findViewById(R.id.item_main_showuaddr_userexit);
            RelativeLayout btn_comment = view.findViewById(R.id.item_main_showuaddr_comment);

            /**
             * 用户查看早早点评
             */
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YaZhouStartActivity(UserComment.class,false);
                }
            });
            /**
             * 用户选择退出  就提示他是否真的要退出
             */
            btn_userexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog builder = new AlertDialog.Builder(MainAct.this).create();
                    View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_trueorfalse_dialog,null);
                    builder.setView(item);
                    TextView btn_confirm = item.findViewById(R.id.item_trueorfalse_dialog_btnDetermine);
                    btn_confirm.setText("我要退出");
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onUserExit();
                        }
                    });
                    TextView btn_cancle = item.findViewById(R.id.item_trueorfalse_dialog_btnCancle);
                    btn_cancle.setText("怎么可能");
                    btn_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OutputFlower();
                            builder.dismiss();//任何的dialog弹出都会让虚拟键盘重新载入 一旦销毁 必须调用hide隐藏
                            hideBottomUIMenu();//隐藏虚拟键盘
                        }
                    });
                    TextView dialog_text = item.findViewById(R.id.item_trueorfalse_dialog_content);
                    dialog_text.setText("您确定要退出左边早早嘛");
                    TextView dialog_title = item.findViewById(R.id.item_trueorfalse_dialog_title);
                    dialog_title.setText("程序猿的含泪提示");
                    builder.setCancelable(false);
                    builder.show();
                }
            });

            /**
             * 新增用户的收件地址
             */
            check_homeaddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YazhouStartActivityForResult(UserAddHomeAddr.class, false, MAP.SET_USERADDR_SUCESS);
                }
            });
            showNotification(LoginAct.class, R.drawable.ico_freightcart, getResources().getString(R.string.no_orderpage));
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
        SystemVisitInterService.InterServiceGet(getApplicationContext(), config.getReserveTime(), new SystemVisitInterService.onVisitInterServiceListener() {
            @Override
            public void onSucess(String tOrgin) {
                JsonEndata jsonEndata = new JsonEndata(tOrgin);
                String h = jsonEndata.getJsonKeyValue("h");
                String s = jsonEndata.getJsonKeyValue("s");
                String m = jsonEndata.getJsonKeyValue("m");
                price = jsonEndata.getJsonKeyValue("price");
                activity_main_reservetime.setText(h + ":" + s + ":" + m);
                //启动一个时间线程  用来倒计时用
                Reversetimer = new Timer();
                Reversetimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = config.CALC_RESERVE_TIME;
                        handler.sendMessage(msg);
                    }
                }, 100, 1000);
                //执行一个价格动画
                PriceAni = new Timer();
                PriceAni.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = config.CALC_PRICE_TIME;
                        msg.obj = price;
                        handler.sendMessage(msg);
                    }
                }, 30, 30);

            }

            @Override
            public void onNotConnect() {

            }

            @Override
            public void onFail(String tOrgin) {
                Toast.makeText(getApplicationContext(), "调用失败", Toast.LENGTH_LONG).show();

            }
        }, "action", config.WEB_SERVICE_ACTION_GETRESERVETIME);
    }

    /**
     * 发送一个状态栏通知 用来供客户查看交易订单和物流信息
     */
    private void showNotification(Class<?> into, int showImg, String showText) {
        if (NotificationUtils.isNotificationeGet(getApplicationContext())) {
            //已经获取到权限 就发送一个状态栏通知信息
            notificationManager = tools.showStatueBarNotify(getApplicationContext(), into, showImg, showText);
        } else {
            NotificationUtils.toGetNotificationService(MainAct.this);//没有获取到通知的权限就让模块提示用户必须先获取到权限
        }
    }

    private void Listener() {
        /**
         * 订购监听
         */
        btn_orderTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                if (tv.getText().toString().equals("登录")) {
                    //YazhouStartActivityForResult(InputAddrAct.class, false, MAP.SET_USERADDR_SUCESS);
                    //YaZhouStartActivity(UserAddHomeAddr.class,false);
                    YaZhouStartActivity(LoginAct.class, false);

                } else {
                    //YaZhouStartActivity(LoginAct.class,false);
                    //测试百度地图
                    //YazhouStartActivityForResult(InputAddrAct.class, false, MAP.SET_USERADDR_SUCESS);
                    //YaZhouStartActivity(UserAddHomeAddr.class,false);
                    Toast.makeText(getApplicationContext(), "开始预定", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Timer timer = new Timer();
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        scrollView.stopRefresh();
                        break;
                }
            }
        };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 2000);
    }

    @Override
    public void onRefreshdone() {

    }

    @Override
    public void onStopRefresh() {

    }

    @Override
    public void onState(String statue) {
        headStatue.setText(statue);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onRestart() {
        Log.i("capitalist", "onRestart");
        showNotification(LoginAct.class, R.drawable.ico_ok, "您的订单已经成功送达哦,您可以点击评价");
        //重新获取焦点的话 要把倒计时的线程重新启动
        activity_main_price.setText("0");
        PriceAni = new Timer();
        PriceAni.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = config.CALC_PRICE_TIME;
                msg.obj = price;
                handler.sendMessage(msg);
            }
        }, 30, 30);
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
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "授权", Toast.LENGTH_SHORT).show();//应用授权
        } else {
            Toast.makeText(getApplicationContext(), "没有授权", Toast.LENGTH_SHORT).show();
            //没有授权 弹出对话框 交代授权的
            final AlertDialog builder = new AlertDialog.Builder(MainAct.this).create();
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_trueorfalse_dialog, null);
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
