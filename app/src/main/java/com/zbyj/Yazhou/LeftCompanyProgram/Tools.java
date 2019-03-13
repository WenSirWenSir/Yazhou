package com.zbyj.Yazhou.LeftCompanyProgram;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.zbyj.Yazhou.LeftCompanyProgram.Interface.ProgramInterface;
import com.zbyj.Yazhou.R;

import java.io.File;
import java.security.MessageDigest;

public class Tools {

    /**
     * Whether the network is Connected
     *
     * @param context
     * @return
     */
    public static boolean isIntentConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            @SuppressLint("MissingPermission") NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) return true;
                }
            }
            return false;

        }
    }


    /**
     * gets the screen height or width
     */
    public static int GetWindowScreen(Context tContext, int tModule) {
        int value = 0;
        try {
            WindowManager wm = (WindowManager) tContext.getSystemService(Context.WINDOW_SERVICE);
            switch (tModule) {
                case Config.Windows.GET_WINDOW_HEIGHT:
                    value = wm.getDefaultDisplay().getHeight();
                    break;
                case Config.Windows.GET_WINDOW_WIDHT:
                    value = wm.getDefaultDisplay().getWidth();
                    break;
            }
            return value;
        } catch (Exception e) {

        }
        return value;
    }

    /**
     * Determines whether the file exists
     *
     * @param tPath file addrs
     * @return
     */
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


    /**
     * get the Token data saved by the User
     *
     * @param key Want key
     * @return
     */
    public static String gettoKen(Context tContext, String key) {
        SharedPreferences sharedPreferences = tContext.getSharedPreferences("YazhouUser", 0);
        try {
            return sharedPreferences.getString(key, "");//如果不存在  就返回一个空字符
        } catch (Exception e) {
            Log.i(Config.DEBUG, "LeftCompanyProgram Tools.java[+]" + e.getMessage());
            return "";
        }
    }

    /**
     * saved user data
     *
     * @param tContext
     * @param tdata    Characters of name,key
     */
    @SuppressLint("ApplySharedPref")
    public static void settoKen(Context tContext, String... tdata) {
        SharedPreferences sharedPreferences = tContext.getSharedPreferences("YazhouUser", 0);
        try {
            sharedPreferences.edit().putString(tdata[0], tdata[1]).commit();
            Log.i(Config.DEBUG, "LeftCompanyProgarm Tools.java[+]保存用户数据成功");
        } catch (Exception e) {
            Log.e(Config.DEBUG, "LeftCompnayProgram Tools.java[+]" + e.getMessage());
        }

    }

    /**
     * 发送短信验证码
     * <p>
     * 短信的验证码由服务器进行设置
     *
     * @param tPhone 电话号码
     */
    public static void sendVerificationCodeSMS(final Context tContext, String tPhone, final
    ProgramInterface.SMSInterface listener) {


        Net.InterServiceGet(tContext, Config.HTTP_ADDR.SendVerificationCodeAddr(), new Net
                .onVisitInterServiceListener() {
            @Override
            public void onSucess(String tOrgin) {
                JsonEndata jsonEndata = new JsonEndata(tOrgin);
                if (jsonEndata.getJsonKeyValue(Config.HttpMethodUserAction.KEY_STATUS).equals
                        (Config.HttpMethodUserAction.STATUS_SENDOK)) {
                    listener.onSendOk();
                } else {
                    listener.onSendError();
                }
            }

            @Override
            public void onNotConnect() {

            }

            @Override
            public void onFail(String tOrgin) {

            }
        }, Config.HttpMethodUserAction.KEY_ACTION, "" + Config.HttpMethodUserAction
                .SEND_VERIFICATION, Config.HttpMethodUserAction.KEY_USER, Tools.getStringMD5
                (tPhone), Config.HttpMethodUserAction.KEY_PHONE, tPhone);

    }


    /**
     * get the maximum memory of the phone
     * return values tyeps is kb
     */

    public static int getApplicationMemorySize() {
        return (int) Runtime.getRuntime().maxMemory();
    }


    /**
     * md5     encryption
     *
     * @param str want encryption values
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
            Log.e(Config.DEBUG, "LeftCompanyProgram Tools.java[+]:" + e.getMessage());
            return "";
        }
    }

    /**
     * gets the device unique identifier
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getsystemDevicdeId(Context tContext) {
        TelephonyManager telephonyManager = (TelephonyManager) tContext.getSystemService(Context
                .TELEPHONY_SERVICE);
        try {
            return telephonyManager.getImei();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * change the background lines and background color
     */
    public static void setBackgroundValues(View view, int StorkeWith, String StorkeColor, String
            BackgroundColor) {
        GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();//get view
        // background
        if (StorkeWith != 0) {
            gradientDrawable.setStroke(StorkeWith, Color.parseColor(StorkeColor));//Set
            // background line Color and width
        }

        //update background color
        gradientDrawable.setColor(Color.parseColor(BackgroundColor));
    }


    /**
     * Create a AlertDilg box,
     *
     * @param view            view
     * @param mConext         ApplictionContext
     * @param title           titleStr
     * @param context         contextStr
     * @param cancleStr       cancleStr
     * @param confirmStr      confirmStr
     * @param alertDilgClick  interface Cancle and Confirm
     * @param alertViewIDpage Calss for ConfigPageClass.AlertViewIDpage(Instance) you can call
     *                        getAlertViewIDpageInstance to get
     */

    public static void showAlertDilg(View view, Context mConext, String title, String context,
                                     String cancleStr, String confirmStr, final AlertDilgClick
                                             alertDilgClick, ConfigPageClass.AlertViewIDpage
                                             alertViewIDpage) {
        if (alertViewIDpage != null) {
            final AlertDialog alertDialog = new AlertDialog.Builder(mConext).create();
            alertViewIDpage.getTitle().setText(title);
            alertViewIDpage.getContext().setText(context);
            alertViewIDpage.getCancle().setText(cancleStr);
            alertViewIDpage.getConfirm().setText(confirmStr);
            // cancle
            alertViewIDpage.getCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDilgClick != null) {

                        alertDilgClick.onCancle(alertDialog);
                    }
                }
            });
            //btn confirm
            alertViewIDpage.getConfirm().setText(confirmStr);
            alertViewIDpage.getConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDilgClick != null) {
                        alertDilgClick.onConfirm(alertDialog);
                    }
                }
            });
            alertDialog.setView(view);
            alertDialog.show();

        } else {

        }

    }

    public interface AlertDilgClick {
        void onConfirm(AlertDialog alertDialog);

        void onCancle(AlertDialog alertDialog);
    }


    /**
     * 检查验证码是否正确
     */
    public static void checkVerficationCode(Context tContext, String phone, String code, final
    ProgramInterface programInterface) {
        Net.InterServiceGet(tContext, Config.HTTP_ADDR.CheckVerificationAddr(), new Net
                .onVisitInterServiceListener() {
            @Override
            public void onSucess(String tOrgin) {
                if (programInterface != null) {
                    programInterface.onSucess(tOrgin, 0);

                }
            }

            @Override
            public void onNotConnect() {
                if (programInterface != null) {
                    programInterface.onFaile("", 0);
                }


            }

            @Override
            public void onFail(String tOrgin) {
                if (programInterface != null) {
                    programInterface.onFaile("", 0);
                }

            }
        }, Config.HttpMethodUserAction.KEY_ACTION, Config.HttpMethodUserAction
                .CHECK_VERIFICATION, Config.HttpMethodUserAction.KEY_USER, Tools.getStringMD5
                (phone), Config.HttpMethodUserAction.KEY_CODE, code);

    }

}
