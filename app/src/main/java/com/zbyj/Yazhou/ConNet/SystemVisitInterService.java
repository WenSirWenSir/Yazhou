/*
 * Copyright (c) 2019.
 * 该代码由上杭左边远景软件开发工作室编写维护
 * 任何非本公司的个人或者企业无权进行
 * 修改/重建/编译/使用/拷贝/复制/发送 该程序代码.
 * 一经查证由个人或者公司发布/使用/编译/重写/拷贝/复制
 * 该软件源代码.本公司有权使用法律进行维权。
 * 负责人：WenSir@(翁启鑫)
 *
 */

/*
 * Copyright (c) 2019.
 * 该代码由上杭左边远景软件开发工作室编写维护
 * 任何非本公司的个人或者企业无权进行
 * 修改/重建/编译/使用/拷贝/复制/发送 该程序代码.
 * 一经查证由个人或者公司发布/使用/编译/重写/拷贝/复制
 * 该软件源代码.本公司有权使用法律进行维权。
 * 负责人：WenSir@(翁启鑫)
 *
 */
package com.zbyj.Yazhou.ConNet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zbyj.Yazhou.R;
import com.zbyj.Yazhou.config;
import com.zbyj.Yazhou.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 访问互联网的工厂
 */
public class SystemVisitInterService {
    private String tUrl = "";
    private StringBuffer mKvsBuffer = new StringBuffer();
    private onVisitInterServiceListener mOnVisitInterServiceListener;
    private int VisitInterMethod;

    /**
     * 用GET方式获取数据信息
     *
     * @param tUrl                         地址
     * @param mOnVisitInterServiceListener 监听回调
     * @param kvs                          参数对,没有就直接用NULL
     */
    public static void InterServiceGet(Context context, String tUrl, final onVisitInterServiceListener mOnVisitInterServiceListener, String... kvs) {
        if (!tools.isIntentConnect(context)) {
            //网络无连接 就不做什么操作了
            if(mOnVisitInterServiceListener != null){
                mOnVisitInterServiceListener.onNotConnect();
            }
            return ;
        } else {
            final StringBuffer kvsBuffer = new StringBuffer();
            if (kvs != null && kvs.length > 1) {
                try {
                    for (int i = 0; i < kvs.length; i += 2) {
                        kvsBuffer.append(kvs[i] + "=" + kvs[i + 1] + "&");
                    }
                } catch (Exception e) {
                    Log.e(config.DEBUG_STR,"SystemVisitInerService.java[+]:"+e.getMessage());
                }
            }
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... urls) {
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL(urls[0].trim().toString() +"?"+ kvsBuffer.toString()).openConnection();
                        Log.i(config.DEBUG_STR,"访问网络地址:" + urls[0].trim().toString() +"?"+ kvsBuffer.toString());
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(5000);
                        con.setReadTimeout(5000);
                        con.connect();
                        if (con != null) {
                            InputStream is = con.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                            StringBuffer sb = new StringBuffer();
                            String ReadLine = "";
                            while ((ReadLine = bufferedReader.readLine()) != null) {
                                sb.append(ReadLine);
                            }
                            is.close();
                            con.disconnect();
                            bufferedReader.close();
                            return sb.toString();
                        } else {
                            return null;
                        }
                    } catch (Exception e) {
                        Log.e(config.DEBUG_STR,"SystemVisitInerService.java[+]:"+e.getMessage());

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    if(s != null){
                        if(mOnVisitInterServiceListener != null){
                            mOnVisitInterServiceListener.onSucess(s);
                        }
                    }
                    else{
                        if(mOnVisitInterServiceListener != null){
                            mOnVisitInterServiceListener.onFail("");
                        }
                    }
                    super.onPostExecute(s);
                }
            }.execute(tUrl);
        }
    }


    public interface onVisitInterServiceListener {
        Thread s = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        void onSucess(String tOrgin);//成功的监听
        void onNotConnect();//网络断开连接
        void onFail(String tOrgin);//失败的监听
    }
}
