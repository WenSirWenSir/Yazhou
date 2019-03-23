package com.zbyj.Yazhou.LeftCompanyProgram;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.zbyj.Yazhou.LeftCompanyProgram.Interface.ProgramInterface;
import com.zbyj.Yazhou.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络访问模块  有关于网络访问的监听和提交事件都在这里
 * <p>
 * 该模块属于左边远景软件开发工作室公司服务器内模块 任何开发个体的android project都可以使用
 * 该套模块
 */
public class Net {
    private String tUrl = "";
    private StringBuffer mKvsBuffer = new StringBuffer();
    private Net.onVisitInterServiceListener mOnVisitInterServiceListener;
    private int VisitInterMethod;

    /**
     * 用GET方式获取数据信息
     *
     * @param tUrl                         地址
     * @param mOnVisitInterServiceListener 监听回调
     * @param kvs                          参数对,没有就直接用NULL
     */
    public static void InterServiceGet(Context context, String tUrl, final Net
            .onVisitInterServiceListener mOnVisitInterServiceListener, String... kvs) {
        if (!Tools.isIntentConnect(context)) {
            //网络无连接 就不做什么操作了
            if (mOnVisitInterServiceListener != null) {
                mOnVisitInterServiceListener.onNotConnect();
            }
            return;
        } else {
            final StringBuffer kvsBuffer = new StringBuffer();
            if (kvs != null && kvs.length > 1) {
                try {
                    for (int i = 0; i < kvs.length; i += 2) {
                        kvsBuffer.append(kvs[i] + "=" + kvs[i + 1] + "&");
                    }
                } catch (Exception e) {
                    Log.e(Config.DEBUG, "SystemVisitInerService.java[+]:" + e.getMessage());
                }
            }
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... urls) {
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL(urls[0].trim()
                                .toString() + "?" + kvsBuffer.toString()).openConnection();
                        Log.i(Config.DEBUG, "访问网络地址:" + urls[0].trim().toString() + "?" +
                                kvsBuffer.toString());
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(5000);
                        con.setReadTimeout(5000);
                        con.connect();
                        if (con != null) {
                            InputStream is = con.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new
                                    InputStreamReader(is));
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
                        Log.e(Config.DEBUG, "SystemVisitInerService.java[+]:" + e.getMessage());

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    if (s != null) {
                        if (mOnVisitInterServiceListener != null) {
                            mOnVisitInterServiceListener.onSucess(s);
                        }
                    } else {
                        if (mOnVisitInterServiceListener != null) {
                            mOnVisitInterServiceListener.onFail("");
                        }
                    }
                    super.onPostExecute(s);
                }
            }.execute(tUrl);
        }
    }

    public interface onVisitInterServiceListener {

        void onSucess(String tOrgin);//成功的监听

        void onNotConnect();//网络断开连接

        void onFail(String tOrgin);//失败的监听
    }


    /**
     * 获取XML文件的网络访问
     */
    @SuppressLint("StaticFieldLeak")
    public static void getXMLInterGet(Context mContext, String url, final ProgramInterface
            .XMLDomServiceInterface xmlDomServiceInterface, String... kvs) {


        /**
         * 判断是否没有网络访问
         */
        if (!Tools.isIntentConnect(mContext)) {
            if (xmlDomServiceInterface != null) {
                xmlDomServiceInterface.onNotService();
            }
            return;
        } else {
            final StringBuffer kvsBuffer = new StringBuffer();
            if (kvs != null && kvs.length > 1) {
                try {
                    for (int i = 0; i < kvs.length; i += 2) {
                        kvsBuffer.append(kvs[i] + "=" + kvs[i + 1] + "&");
                    }
                } catch (Exception e) {
                    Log.e(Config.DEBUG, "Net.java[+]:" + e.getMessage());
                }
            }
            /**
             * 返回XMLinputStream
             */
            new AsyncTask<String, Void, InputStream>() {
                @Override
                protected InputStream doInBackground(String... urls) {
                    InputStream in = null;
                    try {
                        URL url = new URL(urls[0].trim().toString() + "?" + kvsBuffer.toString());
                        Log.i(Config.DEBUG, "网络访问的地址" + urls[0].trim().toString() + "?" +
                                kvsBuffer.toString());
                        if (url != null) {
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(2000);
                            con.setDoInput(true);
                            con.setRequestMethod("GET");
                            if (200 == con.getResponseCode()) {
                                //成功
                                in = con.getInputStream();
                            }
                        }
                    } catch (Exception e) {
                        Log.e(Config.DEBUG, "getXMLInterGet[+]" + e.getMessage());
                    }
                    return in;
                }

                @Override
                protected void onPostExecute(InputStream inputStream) {
                    if (inputStream != null) {
                        //成功
                        if (xmlDomServiceInterface != null) {
                            xmlDomServiceInterface.onSucess(inputStream);
                        }
                    } else {
                        //失败
                        if (xmlDomServiceInterface != null) {
                            xmlDomServiceInterface.onFain();
                        }
                    }
                    super.onPostExecute(inputStream);
                }
            }.execute(url);
        }
    }


    public static void interServicePost(){

    }

}
