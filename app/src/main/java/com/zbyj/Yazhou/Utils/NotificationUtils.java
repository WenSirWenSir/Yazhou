package com.zbyj.Yazhou.Utils;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.zbyj.Yazhou.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取通知类的系统权限
 */
public class NotificationUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    @SuppressLint("NewApi")
    public static boolean isNotificationeGet(Context context) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = applicationInfo.uid;
        Class appopsClass = null;
        try {
            appopsClass = Class.forName(AppOpsManager.class.getName());
            Method checMethod = appopsClass.getMethod(CHECK_OP_NO_THROW,
                    Integer.TYPE, Integer.TYPE, String.class);
            Field field = appopsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) field.get(Integer.class);
            return (Integer)checMethod.invoke(manager,value,uid,pkg) == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取系统通知权限
     * @param tcontext
     */
    public static void toGetNotificationService(final Context tcontext){
        if(isNotificationeGet(tcontext)){

        }
        else{
            final AlertDialog dialog = new AlertDialog.Builder(tcontext).create();
            View view = View.inflate(tcontext,R.layout.item_trueorfalse_dialog,null);
            TextView title = view.findViewById(R.id.item_trueorfalse_dialog_title);
            TextView btn_determine = view.findViewById(R.id.item_trueorfalse_dialog_btnDetermine);
            TextView btn_cancle = view.findViewById(R.id.item_trueorfalse_dialog_btnCancle);
            final TextView content = view.findViewById(R.id.item_trueorfalse_dialog_content);
            content.setText("您没有开启通知权限,只有开启了通知权限才能查看订单信息,是否打开?");
            btn_determine.setText("去设置");
            btn_cancle.setText("不用了");
            title.setText("重要提示");
            btn_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_determine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT >= 9){
                        i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        i.setData(Uri.fromParts("package",tcontext.getPackageName(),null));
                    }
                    else if(Build.VERSION.SDK_INT <= 8){
                        i.setAction(Intent.ACTION_VIEW);
                        i.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                        i.putExtra("com.android.settings.ApplicationPkgName",tcontext.getPackageName());
                    }
                    tcontext.startActivity(i);
                }
            });
            dialog.setView(view);
            dialog.show();
        }

    }
}
