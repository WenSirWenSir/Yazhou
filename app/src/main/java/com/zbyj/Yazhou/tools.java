package com.zbyj.Yazhou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zbyj.Yazhou.ConNet.SystemVisitInterService;
import com.zbyj.Yazhou.ConfigPageValue.ONREFUSEPHONE;
import com.zbyj.Yazhou.Utils.JsonEndata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class tools {

    /**
     * 拒绝下面的号码进行使用该服务
     *
     * @param tPhone 要查询的电话号码
     */
    public static Boolean IsOnRefusePhone(String tPhone) {
        for (int i = 0; i < ONREFUSEPHONE.ON_REFUSEPHONE_NUMBERS.split("\\;").length; i++) {
            if (ONREFUSEPHONE.ON_REFUSEPHONE_NUMBERS.split("\\;")[i].equals(tPhone)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int GetWindowW_H(Context tContext, int tModule) {
        int value = 0;
        try {
            WindowManager wm = (WindowManager) tContext.getSystemService(Context.WINDOW_SERVICE);
            switch (tModule) {
                case config.GET_WINDOW_HEIGHT:
                    value = wm.getDefaultDisplay().getHeight();
                    break;
                case config.GET_WINDOW_WIDTH:
                    value = wm.getDefaultDisplay().getWidth();
                    break;
            }
            return value;
        } catch (Exception e) {

        }
        return value;
    }

    public void DownUrlFile(String tUrlAddr, String tFilename) {
        try {
            URL url = new URL(tUrlAddr);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            int length = con.getContentLength();
            String path = Environment.getExternalStorageDirectory() + "/" + tFilename;
            File f = new File(path);
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(path);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            Log.i(config.DEBUG_STR, "下载完成");
            os.close();
            is.close();
        } catch (Exception e) {
            Log.i(config.DEBUG_STR, "下载失败 e" + e.getMessage());
        }
    }

    public boolean FileIn(String tPath) {
        try {
            File f = new File(tPath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Bitmap getSvgPhoto() {
        return null;
    }

    public Bitmap CutSvgShape() {
        return null;
    }

    public static int dip2px(Context context
            , int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取用户在本地保存的Token或者是手机号码
     *
     * @param key
     * @return
     */
    public static String gettoKen(Context tContext, String key) {
        SharedPreferences sharedPreferences = tContext.getSharedPreferences("YazhouUser", 0);
        try {
            return sharedPreferences.getString(key, "");//如果不存在  就返回一个空字符
        } catch (Exception e) {
            Log.e(config.DEBUG_STR, "tools.java[+]" + e.getMessage());
            return "";
        }
    }

    /**
     * 存储用户本地的钥匙
     *
     * @param tContext 上下文
     * @param tdata    字符对 user,1520343232,code,22s
     */
    @SuppressLint("ApplySharedPref")
    public static void settoKen(Context tContext, String... tdata) {
        SharedPreferences sharedPreferences = tContext.getSharedPreferences("YazhouUser", 0);
        try {
            sharedPreferences.edit().putString(tdata[0], tdata[1]).commit();
            Log.i(config.DEBUG_STR, "保存用户数据成功");
        } catch (Exception e) {
            Log.e(config.DEBUG_STR, "tools.java[+]" + e.getMessage());
        }

    }

    /**
     * 显示一个通知消息 默认的
     *
     * @param context
     * @param act     对应要打开的窗口的地址
     */
    @SuppressLint("NewApi")
    public static NotificationManager showStatueBarNotify(Context context, Class<?> act, int showImg, String showText) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent i = new Intent();
        i.setClass(context, act);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ico_bell);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ico_bell));
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_notify);
        remoteViews.setImageViewResource(R.id.item_notify_img, showImg);
        remoteViews.setTextViewText(R.id.item_notify_text, showText);
        try {
            builder.setContent(remoteViews);
        } catch (Exception e) {
            Toast.makeText(context, "您使用的Android不支持该软件哦", Toast.LENGTH_LONG).show();
        }
        notificationManager.notify(1, builder.build());
        return notificationManager;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isIntentConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            @SuppressLint("MissingPermission")
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) return true;
                }
            }
            return false;

        }
    }

    /**
     * 发送短信验证码
     * <p>
     * 短信的验证码由服务器进行设置
     *
     * @param tPhone 电话号码
     */
    public static void sendVerificationCodeSMS(final Context tContext, String tPhone) {

        SystemVisitInterService.InterServiceGet(tContext, config.getSendVerificationAddr(), new SystemVisitInterService.onVisitInterServiceListener() {
            @Override
            public void onSucess(String tOrgin) {
                JsonEndata jsonEndata = new JsonEndata(tOrgin);
                if (jsonEndata.getJsonKeyValue(config.WEB_SERVICE_SEND_MESSAGESTATUS).equals(config.SEND_MESSAGE_OK)) {
                    Toast.makeText(tContext, "短信发送成功,请注意查收", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(tContext, "短信发送失败,请检查您的网络是否连接", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNotConnect() {

            }

            @Override
            public void onFail(String tOrgin) {

            }
        }, "phone", tPhone);

    }

    /**
     * 发送短信接口
     *
     * @param tPhone
     * @param tText
     */
    public static void sendSMS(String tPhone, String tText) {

    }

    /**
     * 获取手机的最大的可用内存
     *
     * @param m 指定返回的数据的标注格式大小
     */

    public static int getApplicationMemorySize(int m) {
        int MemorySize = 0;
        if (m == config.GET_SYSTEM_MEMORYSIZE_KB) {
            MemorySize = (int) Runtime.getRuntime().maxMemory();
        } else if (m == config.GET_SYSTEM_MEMORYSIZE_MB) {
            MemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        }
        return MemorySize;
    }


    /**
     * 简单的MD5加密
     *
     * @param str 要加密的字符集合
     * @return
     */
    public static String getStringMD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            StringBuffer strBuf = new StringBuffer();
            byte[] encryption = md5.digest();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (Exception e) {
            Log.e(config.DEBUG_STR, "tools.java:" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取设备唯一标识
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getsystemDevicdeId(Context tContext) {
        TelephonyManager telephonyManager = (TelephonyManager) tContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return telephonyManager.getImei();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 计算两个地图点p1点 和 p2点之间的距离
     * 失败返回一个null
     * @param p1
     * @param p2
     *
     */
    public static Double CalcMapdistance(LatLng p1, LatLng p2) {
        try {
            return DistanceUtil.getDistance(p1, p2);
        } catch (Exception e) {
            Log.e(config.DEBUG_STR, e.getMessage());
            return null;
        }
    }

}
