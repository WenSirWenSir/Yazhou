package com.zbyj.Yazhou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * 押粥官方Activity
 */
public class YazhouActivity extends Activity {
    private Boolean isBackTwo = false;//退出再按一次
    private Boolean isBackOk = false;
    /**
     * 打开一个窗口
     *
     * @param Bclass Class的名称
     */
    protected void YaZhouStartActivity(Class<?> Bclass, Boolean ColseF) {
        Intent i = new Intent();
        i.setClass(this, Bclass);
        this.startActivity(i);
        //界面动画
        this.overridePendingTransition(R.anim.android_anim_activity_in, R.anim.android_anim_activity_out);
        if (ColseF) {
            this.finish();
        }
    }
    /**
     *
     */
    protected void YazhouStartActivityForResult(Class<?> mClass,Boolean ColseF,int requestCode){
        Intent i = new Intent();
        i.setClass(this,mClass);
        startActivityForResult(i,requestCode);
        if(ColseF){
            this.finish();
        }
    }
    /**
     * 打开一个窗口 并且传入值
     */
    protected void YaZhouStartActivityWithBundler(Class<?> Bclass,Boolean ColoseF,String... values){
        Intent intent = new Intent();
        for(int i = 0;i < values.length;i+= 2){
            intent.putExtra(values[i],values[i + 1]);
        }
        intent.setClass(this,Bclass);
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.android_anim_activity_in,R.anim.android_anim_activity_out);
        if(ColoseF){
            this.finish();
        }
    }


    /**
     * 获取传入窗口的值
     * @param key
     * @return
     */
    protected String getBundlerValue(String key){
        try {
            Intent intent = this.getIntent();
            return intent.getStringExtra(key);
        }catch (Exception e){
            return "";
        }
    }
    /**
     * 设置状态栏
     */
    protected void setStatusBar(String tColor) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //5.O版本及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.parseColor(tColor));
        }
    }

    /**
     * 设置透明状态栏
     */
    protected void setTransparentBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


    }

    /**
     * 设置是否要进行退出的监听  默认不监听
     *
     * @param i false 不监听 true 监听
     */
    protected void setBackStatic(Boolean i) {
        if (i) {
            isBackTwo = true;
            isBackOk = false;
        } else {
            isBackTwo = false;
        }

    }

    @Override
    public void onBackPressed() {
        if (isBackTwo) {
            if (isBackOk) {
                super.onBackPressed();
            } else {
                isBackOk = true;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
            }

        } else {
            super.onBackPressed();
        }
    }

    public void hideBottomUIMenu(){
        if(Build.VERSION.SDK_INT > 11 &&Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >= 19){
            View decorView = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(options);
        }
    }

}
